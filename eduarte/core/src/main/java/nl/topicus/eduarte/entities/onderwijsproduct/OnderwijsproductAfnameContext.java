package nl.topicus.eduarte.entities.onderwijsproduct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.entities.BronEntiteitStatus;
import nl.topicus.eduarte.entities.IBronStatusEntiteit;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.onderwijs.duo.bron.Bron;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.ToepassingResultaatExamenvak;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.HogerNiveau;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ToepassingResultaat;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel productregel, onderwijsproductafname, en verbintenis
 * 
 * @author vandekamp
 */
@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {"onderwijsproductAfname", "verbintenis"}),
	@UniqueConstraint(columnNames = {"productregel", "verbintenis"})})
public class OnderwijsproductAfnameContext extends InstellingEntiteit implements
		IBronStatusEntiteit, Comparable<OnderwijsproductAfnameContext>
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Bron
	@Transient
	private Enum handmatigVersturenNaarBronMutatie = null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "productregel")
	@Index(name = "idx_PrAfnCont_regel")
	private Productregel productregel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproductAfname")
	@Index(name = "idx_PrAfnCont_afname")
	@Bron
	private OnderwijsproductAfname onderwijsproductAfname;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "verbintenis")
	@Index(name = "idx_PrAfnCont_verbint")
	private Verbintenis verbintenis;

	/**
	 * De indicatie die aangeeft of het cijfer op de cijferlijst of de beoordeling voor
	 * het schoolexamen voor het betreffende vak is meegenomen bij de vaststelling van de
	 * uitslag van het examen. Default true.
	 */
	@Bron
	private boolean diplomavak = true;

	/**
	 * Geeft aan of het resultaat van een vak afgedrukt moet worden op de cijferlijst.
	 * Default true.
	 */
	@Column(nullable = false)
	private boolean toonOpCijferlijst = true;

	/**
	 * De aanduiding die aangeeft of het resultaat voor het betreffende vak is behaald in
	 * het examenjaar dan wel de reden waarom dit niet het geval is.(Alleen gebruiken voor
	 * BVE). Default: GeexamineerdInJaarMelding
	 */
	@Enumerated(EnumType.STRING)
	private ToepassingResultaatExamenvak toepassingResultaatExamenvak =
		ToepassingResultaatExamenvak.GeexamineerdInJaarMelding;

	/**
	 * De aanduiding die aangeeft of het resultaat voor het betreffende vak is behaald in
	 * het examenjaar dan wel de reden waarom dit niet het geval is.(Alleen gebruiken voor
	 * VO). Default: GeexamineerdInJaarVanMelding
	 */
	@Enumerated(EnumType.STRING)
	@Bron
	private ToepassingResultaat toepassingResultaat =
		ToepassingResultaat.GeexamineerdInJaarVanMelding;

	/**
	 * De indicatie die aangeeft of het bij het betreffende examen behorende werkstuk
	 * betrekking heeft op het betreffende examenvak.
	 */
	@Bron
	private boolean werkstukHoortBijProduct;

	/**
	 * De indicatie die aangeeft of de deelnemer voor het betreffende examenvak nogmaals
	 * een examen gaat afleggen tijdens het volgende tijdvak.
	 */
	@Bron
	private boolean verwezenNaarVolgendTijdvak;

	/**
	 * De indicatie die aangeeft of de deelnemer voor het betreffende examenvak een
	 * certificaat heeft behaald tijdens het betreffende examenjaar van het betreffende
	 * examen.
	 */
	@Bron
	private boolean certificaatBehaald;

	/**
	 * Vakvolgnummer, voor communicatie met BRON.
	 */
	@Bron(verplicht = true)
	private Integer volgnummer = null;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@AutoForm(label = "BRON-status")
	private BronEntiteitStatus bronStatus = BronEntiteitStatus.Geen;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = true)
	@AutoForm(label = "BRON-datum")
	private Date bronDatum;

	public OnderwijsproductAfnameContext()
	{
	}

	@Exportable
	public Productregel getProductregel()
	{
		return productregel;
	}

	public void setProductregel(Productregel productregel)
	{
		this.productregel = productregel;
	}

	@Exportable
	public String getLandelijkeNaamMetHogerNiveau()
	{
		if (isHogerNiveau())
		{
			return getOnderwijsproductAfname().getOnderwijsproduct().getLandelijkeNaam() + " ("
				+ getHogerNiveau().getIdentifier() + ")";
		}
		else
		{
			return getOnderwijsproductAfname().getOnderwijsproduct().getLandelijkeNaam();
		}
	}

	@Exportable
	public OnderwijsproductAfname getOnderwijsproductAfname()
	{
		return onderwijsproductAfname;
	}

	public void setOnderwijsproductAfname(OnderwijsproductAfname onderwijsproductAfname)
	{
		this.onderwijsproductAfname = onderwijsproductAfname;
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	public boolean isDiplomavak()
	{
		return diplomavak;
	}

	public void setDiplomavak(boolean diplomavak)
	{
		this.diplomavak = diplomavak;
	}

	public ToepassingResultaatExamenvak getToepassingResultaatExamenvak()
	{
		return toepassingResultaatExamenvak;
	}

	public void setToepassingResultaatExamenvak(
			ToepassingResultaatExamenvak toepassingResultaatExamenvak)
	{
		this.toepassingResultaatExamenvak = toepassingResultaatExamenvak;
	}

	public boolean isWerkstukHoortBijProduct()
	{
		return werkstukHoortBijProduct;
	}

	public void setWerkstukHoortBijProduct(boolean werkstukHoortBijProduct)
	{
		this.werkstukHoortBijProduct = werkstukHoortBijProduct;
	}

	public boolean isVerwezenNaarVolgendTijdvak()
	{
		return verwezenNaarVolgendTijdvak;
	}

	public void setVerwezenNaarVolgendTijdvak(boolean verwezenNaarVolgendTijdvak)
	{
		this.verwezenNaarVolgendTijdvak = verwezenNaarVolgendTijdvak;
	}

	public boolean isCertificaatBehaald()
	{
		return certificaatBehaald;
	}

	public void setCertificaatBehaald(boolean certificaatBehaald)
	{
		this.certificaatBehaald = certificaatBehaald;
	}

	public void setVolgnummer(Integer volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	public Integer getVolgnummer()
	{
		return volgnummer;
	}

	@Override
	public String toString()
	{
		if (getOnderwijsproductAfname() != null
			&& getOnderwijsproductAfname().getOnderwijsproduct() != null)
			return getOnderwijsproductAfname().getOnderwijsproduct().getCode() + " - "
				+ getOnderwijsproductAfname().getOnderwijsproduct().getTitel();
		return super.toString();
	}

	/**
	 * @return Het taxonomie-element dat aan het gekozen onderwijsproduct is gekoppeld.
	 *         Als aan het onderwijsproduct meerdere taxonomie-elementen gekoppeld zijn,
	 *         wordt het taxonomie-element teruggegeven dat hoort bij het
	 *         verbintenisgebied van de deelnemer. Dit kan bijvoorbeeld bij
	 *         deelkwalificaties in het MBO oud die bij verschillende kwalificaties horen.
	 */
	public TaxonomieElement getTaxonomieElement()
	{
		if (getOnderwijsproductAfname().getOnderwijsproduct().getOnderwijsproductTaxonomieList()
			.size() == 1)
		{
			return getOnderwijsproductAfname().getOnderwijsproduct()
				.getOnderwijsproductTaxonomieList().get(0).getTaxonomieElement();
		}
		Verbintenisgebied verbintenisgebied = null;
		if (getVerbintenis() != null && getVerbintenis().getOpleiding() != null)
		{
			verbintenisgebied = getVerbintenis().getOpleiding().getVerbintenisgebied();
		}
		if (verbintenisgebied != null)
		{
			for (OnderwijsproductTaxonomie ot : getOnderwijsproductAfname().getOnderwijsproduct()
				.getOnderwijsproductTaxonomieList())
			{
				if (verbintenisgebied.equals(ot.getTaxonomieElement()
					.getEersteVerbintenisgebiedParent()))
				{
					return ot.getTaxonomieElement();
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @return true indien dit onderwijsproduct afgenomen wordt op een hoger niveau dan de
	 *         verbintenis van de deelnemer.
	 */
	public boolean isHogerNiveau()
	{
		Verbintenisgebied hoortBij = getVerbintenisgebied();
		if (hoortBij != null)
		{
			String hoortBijCode = hoortBij.getTaxonomiecode();
			if (!getVerbintenis().getOpleiding().getVerbintenisgebied().getTaxonomiecode()
				.startsWith(hoortBijCode))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return Geeft het verbintenisgebied waarbij dit onderwijsproduct hoort. Dit kan een
	 *         ander verbintenisgebied zijn dan het verbintenisgebied van de verbintenis
	 *         waarbij deze keuze gemaakt is als de deelnemer bijvoorbeeld een
	 *         onderwijsproduct op een hoger niveau heeft gekozen.
	 */
	private Verbintenisgebied getVerbintenisgebied()
	{
		if (getVerbintenis() == null || getVerbintenis().getOpleiding() == null)
		{
			return null;
		}
		// Zoek eerst naar het verbintenisgebied dat overeenkomt met het verbintenisgebied
		// van de opleiding.
		for (OnderwijsproductTaxonomie ot : getOnderwijsproductAfname().getOnderwijsproduct()
			.getOnderwijsproductTaxonomieList())
		{
			TaxonomieElement element = ot.getTaxonomieElement();
			Verbintenisgebied gebied = element.getEersteVerbintenisgebiedParent();
			if (getVerbintenis().getOpleiding().getVerbintenisgebied().getTaxonomiecode()
				.startsWith(gebied.getTaxonomiecode()))
			{
				return gebied;
			}
		}
		if (getOnderwijsproductAfname().getOnderwijsproduct().getOnderwijsproductTaxonomieList()
			.size() > 0)
		{
			TaxonomieElement element =
				getOnderwijsproductAfname().getOnderwijsproduct()
					.getOnderwijsproductTaxonomieList().get(0).getTaxonomieElement();
			// Als het element niet van hetzelfde niveau is als het verbintenisgebied van
			// de deelnemer, is dit een vak op hoger niveau.
			return element.getEersteVerbintenisgebiedParent();
		}
		return null;
	}

	/**
	 * 
	 * @return Het hoger niveau voor BRON voor deze keuze, of null als deze keuze niet op
	 *         een hoger niveau is.
	 */
	public HogerNiveau getHogerNiveau()
	{
		if (isHogerNiveau())
		{
			Verbintenisgebied verbintenisgebied = getVerbintenisgebied();
			String taxonomiecode = verbintenisgebied.getTaxonomiecode();
			if (taxonomiecode.startsWith("3.2.1"))
			{
				return HogerNiveau.VWO;
			}
			if (taxonomiecode.startsWith("3.2.2"))
			{
				return HogerNiveau.HAVO;
			}
			if (taxonomiecode.startsWith("3.2.3"))
			{
				// TL of GL?
				return HogerNiveau.VMBOTheoretischeLeerweg;
			}
			if (taxonomiecode.startsWith("3.2.4"))
			{
				return HogerNiveau.VMBOKaderBeroepsegerichteLeerweg;
			}
		}
		return null;
	}

	/**
	 * 
	 * @return De examenvakcode voor BRON voor deze keuze. Dit komt meestal overeen met de
	 *         externe code van het gekozen onderwijsproduct, behalve in sommige
	 *         specifieke gevallen waarbij een onderwijsproduct op hoger niveau wordt
	 *         afgenomen.
	 */
	public String getExamenvakcode()
	{
		return getOnderwijsproductAfname().getOnderwijsproduct().getExterneCode();
	}

	/**
	 * 
	 * @return true indien deze keuze een onderdeel is van het combinatiecijfer (VO).
	 */
	public Boolean isOnderdeelVanCombinatiecijfer()
	{
		Map<Productregel, OnderwijsproductAfnameContext> keuzes =
			DataAccessRegistry.getHelper(OnderwijsproductAfnameContextDataAccessHelper.class).list(
				getVerbintenis());
		return isOnderdeelVanCombinatiecijfer(keuzes.values());
	}

	/**
	 * @param keuzes
	 *            Een collectie met alle keuzes die de deelnemer heeft gemaakt voor deze
	 *            verbintenis.
	 * @return true indien deze keuze een onderdeel is van het combinatiecijfer (VO).
	 */
	public Boolean isOnderdeelVanCombinatiecijfer(Collection<OnderwijsproductAfnameContext> keuzes)
	{
		// Moet gevuld zijn bij nieuwe 2e fase opleidingen(Havo-VWO nieuw, ILT-code is
		// gelijk aan 0170-0179, 0270-0279, 0370-0379, 0670-0679)
		List<Integer> iltCodes = new ArrayList<Integer>();
		for (int i = 170; i < 180; i++)
			iltCodes.add(i);
		for (int i = 270; i < 280; i++)
			iltCodes.add(i);
		for (int i = 370; i < 380; i++)
			iltCodes.add(i);
		for (int i = 670; i < 680; i++)
			iltCodes.add(i);
		for (int i = 5170; i < 5180; i++)
			iltCodes.add(i);
		for (int i = 5270; i < 5280; i++)
			iltCodes.add(i);
		for (int i = 5370; i < 5380; i++)
			iltCodes.add(i);
		for (int i = 5670; i < 5680; i++)
			iltCodes.add(i);

		if (!iltCodes.contains(Integer.parseInt(getVerbintenis().getOpleiding()
			.getVerbintenisgebied().getExterneCode())))
			return null;
		if (getProductregel().getTypeProductregel() == TypeProductregel.AfgeleideProductregel)
			return false;
		if (getVerbintenis().getOpleiding() == null)
			return false;
		// Haal de afgeleide productregels van deze opleiding op.
		boolean onderdeelVan = false;
		List<Productregel> productregels =
			getVerbintenis().getOpleiding().getLandelijkeEnLokaleProductregels(
				getVerbintenis().getCohort());
		for (Productregel regel : productregels)
		{
			if (regel.getTypeProductregel() == TypeProductregel.AfgeleideProductregel)
			{
				onderdeelVan =
					regel.isOnderdeelVanAfgeleideProductregel(getVerbintenis().getOpleiding(),
						getOnderwijsproductAfname(), keuzes);
				if (onderdeelVan)
				{
					break;
				}
			}
		}
		return onderdeelVan;
	}

	public void setToepassingResultaat(ToepassingResultaat toepassingResultaat)
	{
		this.toepassingResultaat = toepassingResultaat;
	}

	public ToepassingResultaat getToepassingResultaat()
	{
		return toepassingResultaat;
	}

	public void setHandmatigVersturenNaarBron(Enum< ? > soortMutatie)
	{
		this.handmatigVersturenNaarBronMutatie = soortMutatie;
	}

	public nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie getHandmatigeBronBveSoortMutatie()
	{
		if (handmatigVersturenNaarBronMutatie instanceof nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie)
		{
			return (nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie) handmatigVersturenNaarBronMutatie;
		}
		return null;
	}

	public nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie getHandmatigeBronVoSoortMutatie()
	{
		if (handmatigVersturenNaarBronMutatie instanceof nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie)
		{
			return (nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie) handmatigVersturenNaarBronMutatie;
		}
		return null;
	}

	public boolean isHandmatigVersturenNaarBron()
	{
		return handmatigVersturenNaarBronMutatie != null;
	}

	public void bepaalVolgnummer()
	{
		if (getVolgnummer() != null)
		{
			return;
		}
		setVolgnummer(getProductregel().getVolgnummer());

		// int max = 0;
		// List<OnderwijsproductAfnameContext> contexten =
		// getVerbintenis().getAfnameContexten();
		// for (OnderwijsproductAfnameContext context : contexten)
		// {
		// Integer nr = context.getVolgnummer();
		// if (nr != null)
		// {
		// max = Math.max(max, nr);
		// }
		// }
		// volgnummer = max + 1;
	}

	public void setBronStatus(BronEntiteitStatus bronStatus)
	{
		this.bronStatus = bronStatus;
	}

	public BronEntiteitStatus getBronStatus()
	{
		return bronStatus;
	}

	public void setBronDatum(Date bronDatum)
	{
		this.bronDatum = bronDatum;
	}

	public Date getBronDatum()
	{
		return bronDatum;
	}

	public void setToonOpCijferlijst(boolean toonOpCijferlijst)
	{
		this.toonOpCijferlijst = toonOpCijferlijst;
	}

	public boolean isToonOpCijferlijst()
	{
		return toonOpCijferlijst;
	}

	@Override
	public int compareTo(OnderwijsproductAfnameContext o)
	{
		if (o == null)
			return -1;
		return this.getProductregel().compareTo(o.getProductregel());
	}
}
