package nl.topicus.eduarte.entities.productregel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.Fase;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.entities.opleiding.Opleidingsvariant;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat.Resultaatsoort;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.providers.ProductregelProvider;

import org.apache.wicket.RequestCycle;
import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Productregel representeert zowel landelijke als lokale productregels. Een productregel
 * geeft aan welke onderwijsproducten een deelnemer af moet nemen voor het behalen van
 * zijn/haar diploma. Voor elke productregel mag of moet een deelnemer een
 * onderwijsproduct selecteren. De mogelijke onderwijsproducten van een productregel
 * worden gedefinieerd als een lijst van deelgebieden (landelijke productregels) of een
 * lijst van onderwijsproducten (lokale productregels).
 * 
 * @author loite
 */
@Exportable()
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {"afkorting", "opleiding", "verbintenisgebied", "cohort"}),
	@UniqueConstraint(columnNames = {"volgnummer", "soortProductregel", "opleiding",
		"verbintenisgebied", "cohort"})})
public class Productregel extends LandelijkOfInstellingEntiteit implements
		Comparable<Productregel>, ProductregelProvider
{
	private static final long serialVersionUID = 1L;

	/**
	 * Een productregel kan een normale productregel zijn, waar een deelnemer een keuze
	 * voor maakt, of het kan een afgeleide productregel zijn waarbij de deelnemer geen
	 * keuze maakt, maar waarbij het eindresultaat van de productregel het gemiddelde is
	 * van de onderwijsproducten die de deelnemer bij andere productregels gekozen heeft.
	 * Als een afgeleide productregel bijvoorbeeld de onderwijsproducten ma, ckv en anw
	 * bevat, en de deelnemer heeft ma en ckv bij andere productregels gekozen, is het
	 * eindresultaat van de afgeleide productregel het gemiddelde van de eindresultaten
	 * van ma en ckv.
	 * 
	 * @author loite
	 * 
	 */
	public static enum TypeProductregel
	{
		Productregel,
		AfgeleideProductregel;

		@Override
		public String toString()
		{
			return StringUtil.convertCamelCase(name());
		}
	}

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private TypeProductregel typeProductregel;

	/**
	 * Unieke afkorting per verbintenisgebied/opleiding.
	 */
	@Column(nullable = false, length = 20)
	private String afkorting;

	@Column(nullable = false, length = 255)
	private String naam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "soortProductregel")
	@Index(name = "idx_Productregel_soortProduct")
	private SoortProductregel soortProductregel;

	/**
	 * Niet nullable, oftewel ook invullen bij lokale productregels. Dit is dan wel
	 * redundante informatie, maar maakt het mogelijk om een unique constraint te plaatsen
	 * op de afkorting per verbintenisgebied/opleiding.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "verbintenisgebied")
	@Index(name = "idx_Productregel_verbintenisg")
	private Verbintenisgebied verbintenisgebied;

	/**
	 * Nullable voor landelijke productregels, verplicht voor lokale productregels.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "opleiding")
	@Index(name = "idx_Productregel_opleiding")
	private Opleiding opleiding;

	@Column(nullable = false)
	private int volgnummer;

	/**
	 * Is dit een verplichte productregel?
	 */
	@Column(nullable = false)
	private boolean verplicht;

	/**
	 * Aantal decimalen voor een afgeleide productregel.
	 */
	@Column(nullable = false)
	private int aantalDecimalen;

	/**
	 * De minimale waarde voor deze productregel. Deze waarde kan gebruikt worden in de
	 * formules van de criteriumbank, of kan afgedrukt worden op een cijferlijst.
	 */
	@Column(nullable = true, scale = 10, precision = 20)
	private BigDecimal minimaleWaarde;

	@Column(nullable = true, length = 10)
	private String minimaleWaardeTekst;

	/**
	 * Productregels zijn altijd cohortspecifiek
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "cohort")
	@Index(name = "idx_Productregel_cohort")
	private Cohort cohort;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productregel")
	@Cache(region = "Instelling", usage = CacheConcurrencyStrategy.READ_WRITE)
	@BatchSize(size = 100)
	private List<ToegestaanOnderwijsproduct> toegestaneOnderwijsproducten;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productregel")
	@Cache(region = "Landelijk", usage = CacheConcurrencyStrategy.READ_WRITE)
	@BatchSize(size = 100)
	private List<ToegestaanDeelgebied> toegestaneDeelgebieden;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "fase")
	@Index(name = "idx_Productregel_fase")
	private Fase fase;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "alleOnderwProdToestaanVan")
	@Index(name = "idx_ProdReg_OPToestaanVan")
	private OrganisatieEenheid alleOnderwijsproductenToestaanVan;

	public Productregel()
	{
	}

	public Productregel(EntiteitContext context)
	{
		super(context);
	}

	@Exportable
	public String getAfkorting()
	{
		return afkorting;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Exportable
	public SoortProductregel getSoortProductregel()
	{
		return soortProductregel;
	}

	public void setSoortProductregel(SoortProductregel soortProductregel)
	{
		this.soortProductregel = soortProductregel;
	}

	@Exportable
	public Verbintenisgebied getVerbintenisgebied()
	{
		return verbintenisgebied;
	}

	public void setVerbintenisgebied(Verbintenisgebied verbintenisgebied)
	{
		this.verbintenisgebied = verbintenisgebied;
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	@Exportable
	public int getVolgnummer()
	{
		return volgnummer;
	}

	public void setVolgnummer(int volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	public boolean isVerplicht()
	{
		return verplicht;
	}

	public void setVerplicht(boolean verplicht)
	{
		this.verplicht = verplicht;
	}

	public String getVerplichtOmschrijving()
	{
		return isVerplicht() ? "Ja" : "Nee";
	}

	@Override
	public int compareTo(Productregel o)
	{
		int res = getSoortProductregel().compareTo(o.getSoortProductregel());
		if (res == 0)
		{
			res = getVolgnummer() - o.getVolgnummer();
		}
		return res;
	}

	@Exportable
	public Cohort getCohort()
	{
		return cohort;
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort;
	}

	@Exportable
	public String getOnderwijsproductCodes()
	{
		if (RequestCycle.get() != null && EduArteSession.get() != null)
			return getOnderwijsproductCodes(EduArteSession.get().getSelectedOpleiding());

		return null;
	}

	/**
	 * @see Productregel#getOnderwijsproducten(Opleiding)
	 * @param getVoorOpleiding
	 * @return String met de codes van de toegestane onderwijsproducten.
	 */
	@Exportable
	public String getOnderwijsproductCodes(Opleiding getVoorOpleiding)
	{
		return StringUtil.toString(getOnderwijsproducten(getVoorOpleiding), null,
			new StringUtil.StringConverter<Onderwijsproduct>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(Onderwijsproduct object, int listIndex)
				{
					return object.getCode();
				}

			});
	}

	/**
	 * @param getVoorOpleiding
	 *            De opleiding waarvoor de onderwijsproducten getoond moeten worden.
	 */
	public SortedSet<Onderwijsproduct> getOnderwijsproducten(Opleiding getVoorOpleiding)
	{
		return getOnderwijsproducten(getVoorOpleiding, true, false);
	}

	public SortedSet<Onderwijsproduct> getOnderwijsproductenMetCache(Opleiding getVoorOpleiding)
	{
		return getOnderwijsproducten(getVoorOpleiding, true, true);
	}

	private transient Map<Opleiding, SortedSet<Onderwijsproduct>> onderwijsproductenCache =
		new HashMap<Opleiding, SortedSet<Onderwijsproduct>>();

	/**
	 * @param getVoorOpleiding
	 *            De opleiding waarvoor de onderwijsproducten getoond moeten worden.
	 * @return Een lijst met de toegestane onderwijsproducten voor deze productregel bij
	 *         de gegeven instelling. Bij een landelijke productregel wordt een vertaling
	 *         gedaan van toegestane deelgebieden naar onderwijsproducten. Bij
	 *         instellingsspecifieke productregels worden de toegestane onderwijsproducten
	 *         teruggegeven.
	 */
	public SortedSet<Onderwijsproduct> getOnderwijsproducten(Opleiding getVoorOpleiding,
			boolean ookHogerNiveau, boolean useCache)
	{
		if (useCache)
		{
			SortedSet<Onderwijsproduct> res = onderwijsproductenCache.get(getVoorOpleiding);
			if (res != null)
			{
				return res;
			}
		}
		if (getOpleiding() != null)
		{
			Asserts
				.assertTrue(
					"opleiding niet gelijk aan " + getVoorOpleiding.toString(),
					getVoorOpleiding.equals(getOpleiding())
						|| ((getVoorOpleiding instanceof Opleidingsvariant) && ((Opleidingsvariant) getVoorOpleiding)
							.getParent().equals(getOpleiding())));
			return getOnderwijsproductenLokaal(useCache);
		}
		Asserts.assertEquals("opleiding.verbintenisgebied",
			getVoorOpleiding.getVerbintenisgebied(), getVerbintenisgebied());
		return getOnderwijsproductenLandelijk(getVoorOpleiding, ookHogerNiveau, useCache);
	}

	private SortedSet<Onderwijsproduct> getOnderwijsproductenLokaal(boolean useCache)
	{
		SortedSet<Onderwijsproduct> res = new TreeSet<Onderwijsproduct>();
		for (ToegestaanOnderwijsproduct prod : getToegestaneOnderwijsproducten())
		{
			res.add(prod.getOnderwijsproduct());
		}
		if (useCache)
		{
			onderwijsproductenCache.put(getOpleiding(), res);
		}
		return res;
	}

	private SortedSet<Onderwijsproduct> getOnderwijsproductenLandelijk(Opleiding getVoorOpleiding,
			boolean ookHogerNiveau, boolean useCache)
	{
		OnderwijsproductDataAccessHelper helper =
			DataAccessRegistry.getHelper(OnderwijsproductDataAccessHelper.class);
		List<OpleidingAanbod> aanbod = getVoorOpleiding.getAanbod();
		SortedSet<Onderwijsproduct> res = new TreeSet<Onderwijsproduct>();
		for (ToegestaanDeelgebied deel : getToegestaneDeelgebieden())
		{
			// Vertaal deelgebied naar beschikbare onderwijsproducten.
			List<Onderwijsproduct> list =
				helper.getGekoppeldeOnderwijsproducten(deel.getDeelgebied(), aanbod);
			if (ookHogerNiveau)
			{
				res.addAll(list);
			}
			else
			{
				for (Onderwijsproduct product : list)
				{
					for (OnderwijsproductTaxonomie ot : product.getOnderwijsproductTaxonomieList())
					{
						Class< ? > clazz = Hibernate.getClass(ot.getTaxonomieElement());
						if (Deelgebied.class.isAssignableFrom(clazz))
						{
							String code =
								((Deelgebied) ot.getTaxonomieElement().doUnproxy())
									.getTaxonomiecodeZonderDeelgebied();
							if (getVoorOpleiding.getVerbintenisgebied().getTaxonomiecode()
								.startsWith(code))
							{
								res.add(product);
								break;
							}
						}
					}
				}
			}
		}
		if (useCache)
		{
			onderwijsproductenCache.put(getVoorOpleiding, res);
		}
		return res;
	}

	public List<ToegestaanOnderwijsproduct> getToegestaneOnderwijsproducten()
	{
		if (toegestaneOnderwijsproducten == null)
			toegestaneOnderwijsproducten = new ArrayList<ToegestaanOnderwijsproduct>();
		return toegestaneOnderwijsproducten;
	}

	public void setToegestaneOnderwijsproducten(
			List<ToegestaanOnderwijsproduct> toegestaneOnderwijsproducten)
	{
		this.toegestaneOnderwijsproducten = toegestaneOnderwijsproducten;
	}

	public List<Onderwijsproduct> getToegestaneOnderwijsproductenAlsProducten()
	{
		List<Onderwijsproduct> res =
			new ArrayList<Onderwijsproduct>(getToegestaneOnderwijsproducten().size());
		for (ToegestaanOnderwijsproduct to : getToegestaneOnderwijsproducten())
		{
			res.add(to.getOnderwijsproduct());
		}
		return res;
	}

	public List<ToegestaanDeelgebied> getToegestaneDeelgebieden()
	{
		if (toegestaneDeelgebieden == null)
			toegestaneDeelgebieden = new ArrayList<ToegestaanDeelgebied>();
		return toegestaneDeelgebieden;
	}

	public void setToegestaneDeelgebieden(List<ToegestaanDeelgebied> toegestaneDeelgebieden)
	{
		this.toegestaneDeelgebieden = toegestaneDeelgebieden;
	}

	public boolean isGekoppeldMetDeelgebied(Deelgebied deelgebied)
	{
		for (ToegestaanDeelgebied toegestaanDeelgebied : getToegestaneDeelgebieden())
		{
			if (toegestaanDeelgebied.getDeelgebied().equals(deelgebied))
				return true;
		}
		return false;
	}

	/**
	 * @return Comma-separated string met de afkortingen van de deelgebieden die gekoppeld
	 *         zijn aan deze productregel. Werkt alleen voor landelijke productregels.
	 */
	public String getDeelgebiedAfkortingen()
	{
		if (!isLandelijk())
		{
			throw new IllegalArgumentException(
				"Deze methode mag alleen aangeroepen worden op landelijke productregels");
		}
		return StringUtil.toString(getToegestaneDeelgebieden(), null,
			new StringUtil.StringConverter<ToegestaanDeelgebied>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(ToegestaanDeelgebied object, int listIndex)
				{
					return object.getDeelgebied().getAfkorting();
				}

			});
	}

	/**
	 * @return Comma-separated string met de externe codes van de deelgebieden die
	 *         gekoppeld zijn aan deze productregel. Werkt alleen voor landelijke
	 *         productregels.
	 */
	public String getDeelgebiedExterneCodes()
	{
		if (!isLandelijk())
		{
			throw new IllegalArgumentException(
				"Deze methode mag alleen aangeroepen worden op landelijke productregels");
		}
		return StringUtil.toString(getToegestaneDeelgebieden(), null,
			new StringUtil.StringConverter<ToegestaanDeelgebied>()
			{

				@Override
				public String getSeparator(int listIndex)
				{
					return ",";
				}

				@Override
				public String toString(ToegestaanDeelgebied object, int listIndex)
				{
					return object.getDeelgebied().getExterneCode();
				}

			});
	}

	@Override
	public String toString()
	{
		return getAfkorting() + " - " + getNaam();
	}

	public TypeProductregel getTypeProductregel()
	{
		return typeProductregel;
	}

	public void setTypeProductregel(TypeProductregel typeProductregel)
	{
		this.typeProductregel = typeProductregel;
	}

	/**
	 * @param verbintenis
	 * @return Het eindresultaat van deze productregel voor de gegeven verbintenis
	 */
	public Resultaat getEindresultaat(Verbintenis verbintenis)
	{
		// Haal alle keuzes op voor de gegeven verbintenis. Hierdoor worden alle keuzes in
		// de cache geladen zodat deze methode meerdere keren achter elkaar aanroepen
		// versneld wordt.
		Map<Productregel, OnderwijsproductAfnameContext> keuzes =
			DataAccessRegistry.getHelper(OnderwijsproductAfnameContextDataAccessHelper.class).list(
				verbintenis);
		return getEindresultaat(verbintenis.getDeelnemer(), verbintenis.getOpleiding(), keuzes);
	}

	/**
	 * 
	 * @param getVoorOpleiding
	 * @param deelnemer
	 * @param keuzes
	 *            De keuzes van de deelnemer voor de gegeven verbintenis. Deze keuzes
	 *            worden gebruikt om de resultaten op te halen.
	 * @return Het eindresultaat van deze productregel voor de gegeven verbintenis op de
	 *         gegeven peildatum.
	 */
	public Resultaat getEindresultaat(Deelnemer deelnemer, Opleiding getVoorOpleiding,
			Map<Productregel, OnderwijsproductAfnameContext> keuzes)
	{
		if (getTypeProductregel() == TypeProductregel.AfgeleideProductregel)
			return getEindresultaatVanAfgeleideProductregel(deelnemer, getVoorOpleiding, keuzes);
		return getEindresultaatVanNormaleProductregel(deelnemer, keuzes);
	}

	private Resultaat getEindresultaatVanNormaleProductregel(Deelnemer deelnemer,
			Map<Productregel, OnderwijsproductAfnameContext> keuzes)
	{
		// Haal alle eindresultaten van de deelnemer op zodat het aanroepen van de methode
		// meerdere keren achter elkaar sneller gaat.
		List<OnderwijsproductAfname> afnames = new ArrayList<OnderwijsproductAfname>(keuzes.size());
		for (OnderwijsproductAfnameContext keuze : keuzes.values())
		{
			if (keuze != null)
			{
				afnames.add(keuze.getOnderwijsproductAfname());
			}
		}
		Map<OnderwijsproductAfname, Resultaat> tempResultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).getEindresultaten(
				deelnemer, afnames);
		OnderwijsproductAfnameContext keuze = keuzes.get(this);
		if (keuze != null)
		{
			return tempResultaten.get(keuze.getOnderwijsproductAfname());
		}
		return null;
	}

	private Resultaat getEindresultaatVanAfgeleideProductregel(Deelnemer deelnemer,
			Opleiding getVoorOpleiding, Map<Productregel, OnderwijsproductAfnameContext> keuzes)
	{
		if (getVoorOpleiding == null)
			return null;
		// Bepaal welke van de producten van deze productregel de deelnemer heeft gekozen.
		List<OnderwijsproductAfname> afnames =
			getAfgeleideProductregelKeuzes(getVoorOpleiding, keuzes.values());
		Map<OnderwijsproductAfname, Resultaat> tempResultaten =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).getEindresultaten(
				deelnemer, afnames);
		// Bepaal het gemiddelde van de resultaten van de gekozen onderwijsproducten.
		BigDecimal gemiddeld = BigDecimal.ZERO;
		Resultaatsoort soort = Resultaatsoort.Berekend;
		int cnt = 0;
		for (Resultaat res : tempResultaten.values())
		{
			if (res.getCijfer() != null)
			{
				gemiddeld = gemiddeld.add(res.getCijfer());
				// Als een van de cijfers waaruit de afgeleide productregel bestaat
				// tijdelijk
				// is, is ook het eindresultaat van de afgeleide productregel tijdelijk.
				if (res.getSoort() == Resultaatsoort.Tijdelijk)
				{
					soort = Resultaatsoort.Tijdelijk;
				}
				cnt++;
			}
		}
		if (cnt > 0)
		{
			gemiddeld =
				gemiddeld.divide(BigDecimal.valueOf(cnt), getAantalDecimalen(),
					RoundingMode.HALF_UP);
			Resultaat resultaat = new Resultaat();
			resultaat.setActueel(true);
			resultaat.setCijfer(gemiddeld);
			resultaat.setDeelnemer(deelnemer);
			resultaat.setGeldend(true);
			resultaat.setSoort(soort);

			return resultaat;
		}
		return null;
	}

	/**
	 * 
	 * @param getVoorOpleiding
	 * @param keuzes
	 * @return Een lijst met de onderwijsproductafnames die gelden voor deze afgeleide
	 *         productregel.
	 * @throws IllegalArgumentException
	 *             Als deze methode aangeroepen wordt voor iets anders dan een afgeleide
	 *             productregel.
	 */
	public List<OnderwijsproductAfname> getAfgeleideProductregelKeuzes(Opleiding getVoorOpleiding,
			Collection<OnderwijsproductAfnameContext> keuzes)
	{
		if (getTypeProductregel() != TypeProductregel.AfgeleideProductregel)
			throw new IllegalArgumentException(
				"Deze methode kan alleen aangeroepen worden voor afgeleide productregels");
		Set<Onderwijsproduct> mogelijkeProducten = getOnderwijsproducten(getVoorOpleiding);
		List<OnderwijsproductAfname> afnames = new ArrayList<OnderwijsproductAfname>();
		for (Onderwijsproduct onderwijsproduct : mogelijkeProducten)
		{
			for (OnderwijsproductAfnameContext context : keuzes)
			{
				if (context != null
					&& context.getOnderwijsproductAfname().getOnderwijsproduct().equals(
						onderwijsproduct))
				{
					afnames.add(context.getOnderwijsproductAfname());
				}
			}
		}
		return afnames;
	}

	public boolean isOnderdeelVanAfgeleideProductregel(Opleiding voorOpleiding,
			OnderwijsproductAfname afname, Collection<OnderwijsproductAfnameContext> keuzes)
	{
		List<OnderwijsproductAfname> afnames =
			getAfgeleideProductregelKeuzes(voorOpleiding, keuzes);
		return afnames.contains(afname);
	}

	public int getAantalDecimalen()
	{
		return aantalDecimalen;
	}

	public void setAantalDecimalen(int aantalDecimalen)
	{
		this.aantalDecimalen = aantalDecimalen;
	}

	@Exportable
	public BigDecimal getMinimaleWaarde()
	{
		return minimaleWaarde;
	}

	public void setMinimaleWaarde(BigDecimal minimaleWaarde)
	{
		this.minimaleWaarde = minimaleWaarde;
	}

	@Exportable
	public String getMinimaleWaardeTekst()
	{
		return minimaleWaardeTekst;
	}

	public void setMinimaleWaardeTekst(String minimaleWaardeTekst)
	{
		this.minimaleWaardeTekst = minimaleWaardeTekst;
	}

	/**
	 * @return Een set met toetscodes die gebruikt kunnen worden in de criteriumbank.
	 */
	public Set<String> getToetscodesVoorCriteriumbank(Opleiding getVoorOpleiding)
	{
		ResultaatstructuurDataAccessHelper helper =
			DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class);
		Set<String> res = new HashSet<String>();
		Set<Onderwijsproduct> producten = getOnderwijsproducten(getVoorOpleiding);
		for (Onderwijsproduct product : producten)
		{
			Resultaatstructuur structuur =
				helper.getSummatieveResultaatstructuur(product, getCohort());
			if (structuur != null)
			{
				for (Toets toets : structuur.getEindresultaat().getChildren())
				{
					res.add(toets.getCode());
				}
			}
		}
		return res;
	}

	/**
	 * @return Een set met toetscodes die gebruikt kunnen worden in de criteriumbank.
	 */
	public Set<String> getSchaalnamenVoorCriteriumbank(Opleiding getVoorOpleiding)
	{
		ResultaatstructuurDataAccessHelper helper =
			DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class);
		Set<String> res = new HashSet<String>();
		Set<Onderwijsproduct> producten = getOnderwijsproducten(getVoorOpleiding);
		for (Onderwijsproduct product : producten)
		{
			Resultaatstructuur structuur =
				helper.getSummatieveResultaatstructuur(product, getCohort());
			if (structuur != null && structuur.getEindresultaat() != null)
			{
				res.add(structuur.getEindresultaat().getSchaal().getNaam());
			}
		}
		return res;
	}

	public String getCriteriumbankResultaatVariabelenaam()
	{
		return "#{" + getAfkorting() + "}";
	}

	public String getCriteriumbankTekstVariabelenaam()
	{
		return "#{" + getAfkorting() + "_tekst}";
	}

	public String getCriteriumbankCijferVariabelenaam()
	{
		return "#{" + getAfkorting() + "_cijfer}";
	}

	public String getCriteriumbankBehaaldVariabelenaam()
	{
		return "#{" + getAfkorting() + "_behaald}";
	}

	public String getCriteriumbankIngevuldVariabelenaam()
	{
		return "#{" + getAfkorting() + "_ingevuld}";
	}

	public String getCriteriumbankVolgnummerVariabelenaam()
	{
		return "#{" + getAfkorting() + "_volgnummer}";
	}

	public String getCriteriumbankStudiepuntenVariabelenaam()
	{
		return "#{" + getAfkorting() + "_studiepunten}";
	}

	public String getCriteriumbankToetsVariabelenamen(Opleiding getVoorOpleiding)
	{
		Set<String> toetscodes = getToetscodesVoorCriteriumbank(getVoorOpleiding);
		boolean first = true;
		StringBuilder toetsen = new StringBuilder();
		for (String toetscode : toetscodes)
		{
			if (!first)
			{
				toetsen.append(", ");
			}
			first = false;
			toetsen.append("#{").append(getAfkorting()).append(".").append(toetscode).append("}");
		}
		return toetsen.toString();
	}

	public String getCriteriumbankSchaalnamen(Opleiding getVoorOpleiding)
	{
		Set<String> schaalnamen = getSchaalnamenVoorCriteriumbank(getVoorOpleiding);
		return StringUtil.maakCommaSeparatedString(schaalnamen);
	}

	public String getCriteriumbankCodeVariabelenaam()
	{
		return "#{" + getAfkorting() + "_code}";
	}

	public String getCriteriumbankExterneCodeVariabelenaam()
	{
		return "#{" + getAfkorting() + "_externeCode}";
	}

	public String getCriteriumbankTaxonomiecodeVariabelenaam()
	{
		return "#{" + getAfkorting() + "_taxonomiecode}";
	}

	public String getCriteriumbankMinVariabelenaam()
	{
		return "#{" + getAfkorting() + "_min}";
	}

	public String getCriteriumbankMinTekstVariabelenaam()
	{
		return "#{" + getAfkorting() + "_minTekst}";
	}

	@Exportable
	@Override
	public Productregel getProductregel()
	{
		return this;
	}

	public Fase getFase()
	{
		return fase;
	}

	public void setFase(Fase fase)
	{
		this.fase = fase;
	}

	public void setAlleOnderwijsproductenToestaanVan(
			OrganisatieEenheid alleOnderwijsproductenToestaanVan)
	{
		this.alleOnderwijsproductenToestaanVan = alleOnderwijsproductenToestaanVan;
	}

	public OrganisatieEenheid getAlleOnderwijsproductenToestaanVan()
	{
		return alleOnderwijsproductenToestaanVan;
	}

}
