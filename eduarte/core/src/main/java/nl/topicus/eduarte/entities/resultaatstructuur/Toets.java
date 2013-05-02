package nl.topicus.eduarte.entities.resultaatstructuur;

import static nl.topicus.eduarte.web.components.resultaat.ResultatenModel.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.persistence.*;

import net.sourceforge.jeval.EvaluationException;
import nl.topicus.cobra.app.CobraApplication;
import nl.topicus.cobra.converters.BigDecimalConverter;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.DecimalUtil;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal.Schaaltype;
import nl.topicus.eduarte.web.components.choice.PogingCombobox;
import nl.topicus.eduarte.web.components.choice.SchaalCombobox;
import nl.topicus.eduarte.web.components.resultaat.CompenseerbaarField;
import nl.topicus.eduarte.web.components.text.ReferentieCodeEditPanel;

import org.apache.wicket.util.string.Strings;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een toets die onderdeel is van een resultaatstructuur.
 * 
 * @author loite
 */
// deze worden in CreateDatabaseSchemaImporter toevoegd, maar dan
// "initially deferred deferrable"
// @javax.persistence.Table(uniqueConstraints = {
// @UniqueConstraint(columnNames = {"code", "resultaatstructuur", "parent"}),
// @UniqueConstraint(columnNames = {"volgnummer", "resultaatstructuur", "parent"})})
@Exportable()
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
public class Toets extends InstellingEntiteit implements IBevriezing
{
	private static final long serialVersionUID = 1L;

	public static final int MAX_HERKANSINGEN = 10;

	public static class BerekendResultaat
	{
		private BigDecimal onafgerondCijfer;

		private Object resultaat;

		public BerekendResultaat(BigDecimal onafgerondCijfer, Object resultaat)
		{
			this.onafgerondCijfer = onafgerondCijfer;
			this.resultaat = resultaat;
		}

		public BigDecimal getOnafgerondCijfer()
		{
			return onafgerondCijfer;
		}

		public Object getResultaat()
		{
			return resultaat;
		}
	}

	/**
	 * @author loite
	 */
	public static enum Scoreschaal
	{
		/**
		 * Geen scoreschaal
		 */
		Geen,
		/**
		 * Lineaire scoreschaal
		 */
		Lineair,
		/**
		 * Scoretabel
		 */
		Tabel;
	}

	public static enum Herkansingsscore
	{
		Hoogste
		{
			@Override
			public boolean isGeldend(Resultaat oud, Resultaat nieuw)
			{
				return !nieuw.isNullResultaat() && nieuw.isHogerDan(oud);
			}
		},
		Laatste
		{
			@Override
			public boolean isGeldend(Resultaat oud, Resultaat nieuw)
			{
				return !nieuw.isNullResultaat() && nieuw.isLaterDan(oud);
			}
		};

		public abstract boolean isGeldend(Resultaat oud, Resultaat nieuw);
	}

	/**
	 * Sommige toetsen kunnen een speciale status hebben binnen een resultaatstructuur.
	 * Dat wordt hiermee aangegeven.
	 * 
	 * @author loite
	 * 
	 */
	public static enum SoortToets
	{
		/**
		 * Toets die verder geen specifieke betekenis voor het KRD heeft.
		 */
		Toets(false),
		/**
		 * SE-resultaat voor VO/VAVO
		 */
		Schoolexamen(true),
		/**
		 * CE-resultaat voor VO/VAVO
		 */
		CentraalExamen(true),
		/**
		 * Vaardigheid 'Spreken' voor talen (NT2, Nederlands MBO en moderne vreemde talen
		 * MBO)
		 */
		Spreken(true),
		/**
		 * Vaardigheid 'Luisteren' voor talen (NT2, Nederlands MBO en moderne vreemde
		 * talen MBO)
		 */
		Luisteren(true),
		/**
		 * Vaardigheid 'Lezen' voor talen (NT2, Nederlands MBO en moderne vreemde talen
		 * MBO)
		 */
		Lezen(true),
		/**
		 * Vaardigheid 'Schrijven' voor talen (NT2, Nederlands MBO en moderne vreemde
		 * talen MBO)
		 */
		Schrijven(true),
		/**
		 * Vaardigheid 'Gesprekken' voor talen (NT2, Nederlands MBO en moderne vreemde
		 * talen MBO)
		 */
		Gesprekken(true),
		/**
		 * Toets geeft het instroomniveau van de deelnemer aan (voor NT2)
		 */
		Instroomniveau(false),
		/**
		 * Toets geeft het behaalde niveau van de deelnemer aan (voor NT2)
		 */
		BehaaldNiveau(false),

		Getallen(false),

		RuimteVorm(false),

		Gegevensverwerking(false),

		Verbanden(false),
		/**
		 * Toets is examenonderdeel voor inburgering
		 */
		ExamenonderdeelInburgering(false);

		private SoortToets(boolean uniekBinnenResultaatstructuur)
		{
			this.uniekBinnenResultaatstructuur = uniekBinnenResultaatstructuur;
		}

		private final boolean uniekBinnenResultaatstructuur;

		@Override
		public String toString()
		{
			return StringUtil.convertCamelCase(name());
		}

		public boolean isUniekBinnenResultaatstructuur()
		{
			return uniekBinnenResultaatstructuur;
		}

		public boolean isNT2VaardigheidToets()
		{
			return tussen(SoortToets.Spreken, SoortToets.Gesprekken);
		}

		public static List<SoortToets> nt2vaardigheidToetsen()
		{
			ArrayList<SoortToets> vaardigheden = new ArrayList<SoortToets>();
			for (SoortToets soortToets : SoortToets.values())
			{
				if (soortToets.isNT2VaardigheidToets())
				{
					vaardigheden.add(soortToets);
				}
			}
			return vaardigheden;
		}

		public boolean tussen(SoortToets toets1, SoortToets toets2)
		{
			return (toets1.ordinal() <= this.ordinal()) && this.ordinal() <= toets2.ordinal();
		}

		public static ArrayList<SoortToets> mogelijkeWaarden(Onderwijsproduct onderwijsproduct)
		{
			ArrayList<SoortToets> res = new ArrayList<SoortToets>();
			res.add(SoortToets.Toets);
			if (onderwijsproduct.isGekoppeldAanVOTaxonomie())
			{
				res.add(SoortToets.Schoolexamen);
				res.add(SoortToets.CentraalExamen);
			}
			if (onderwijsproduct.isGekoppeldAanEducatieTaxonomie()
				|| onderwijsproduct.isGekoppeldAanInburgeringTaxonomie()
				|| onderwijsproduct.isGekoppeldAanCGOTaxonomie()
				|| onderwijsproduct.getOnderwijsproductTaxonomieList().isEmpty())
			{
				res.add(SoortToets.Spreken);
				res.add(SoortToets.Luisteren);
				res.add(SoortToets.Lezen);
				res.add(SoortToets.Schrijven);
				res.add(SoortToets.Gesprekken);
				res.add(SoortToets.Instroomniveau);
				res.add(SoortToets.BehaaldNiveau);
				res.add(SoortToets.Getallen);
				res.add(SoortToets.RuimteVorm);
				res.add(SoortToets.Gegevensverwerking);
				res.add(SoortToets.Verbanden);
				res.add(SoortToets.ExamenonderdeelInburgering);
			}

			return res;
		}
	}

	public static enum Rekenregel
	{
		Gemiddelde
		{
			@Override
			public BerekendResultaat calculateSamengesteldResultaat(
					Map<Toets, Resultaat> deeltoetsen, Toets samengesteldeToets)
			{
				BigDecimal ret = BigDecimal.ZERO;
				int totaalWeging = 0;
				for (Resultaat curDeeltoetsResultaat : deeltoetsen.values())
				{
					if (curDeeltoetsResultaat != null)
					{
						Integer curWeging = curDeeltoetsResultaat.getWegingVoorBerekening();
						if (curWeging != null)
						{
							totaalWeging += curWeging;
							ret =
								ret.add(curDeeltoetsResultaat.getCijfer().multiply(
									BigDecimal.valueOf(curWeging)));
						}
					}
				}
				if (totaalWeging == 0)
					return null;

				return samengesteldeToets.getSchaal().calculateCijferOfWaarde(ret, totaalWeging);
			}
		},

		Prioriteit
		{
			@Override
			public BerekendResultaat calculateSamengesteldResultaat(
					Map<Toets, Resultaat> deeltoetsen, Toets samengesteldeToets)
			{
				BigDecimal ret = null;
				int hoogsteWeging = 0;
				for (Resultaat curDeeltoetsResultaat : deeltoetsen.values())
				{
					if (curDeeltoetsResultaat != null)
					{
						Integer curWeging = curDeeltoetsResultaat.getWegingVoorBerekening();
						if (curWeging != null && curWeging > hoogsteWeging)
						{
							hoogsteWeging = curWeging;
							ret = curDeeltoetsResultaat.getCijfer();
						}
					}
				}
				if (ret == null)
					return null;

				return samengesteldeToets.getSchaal().calculateCijferOfWaarde(ret, 1);
			}
		},

		Formule
		{
			@Override
			public BerekendResultaat calculateSamengesteldResultaat(
					Map<Toets, Resultaat> deeltoetsen, Toets samengesteldeToets)
			{
				try
				{
					return samengesteldeToets.getSchaal().calculateCijferOfWaarde(
						FormuleBerekening.bereken(deeltoetsen, samengesteldeToets), 1);
				}
				catch (EvaluationException e)
				{
					return null;
				}
			}
		};

		public abstract BerekendResultaat calculateSamengesteldResultaat(
				Map<Toets, Resultaat> deeltoetsen, Toets samengesteldeToets);
	}

	/**
	 * Geeft aan welke modes gebruikt moet worden voor het genereren van toetscode paden.
	 * 
	 * @author papegaaij
	 */
	public enum ToetsCodePathMode
	{
		/**
		 * Genereer standaard paden. Toetsen met dezelfde codes, maar binnen een andere
		 * resultaatstructuur zullen hetzelfde pad krijgen.
		 */
		STANDAARD,
		/**
		 * Genereer paden die uniek zijn binnen een resultaatstructuur, ook als de codes
		 * van de toetsen hetzelfde zijn.
		 */
		STRUCTUUR_LOKAAL
	}

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	@AutoForm(required = true)
	private SoortToets soort = SoortToets.Toets;

	/**
	 * Een toets hoort altijd bij een resultaatstructuur.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resultaatstructuur", nullable = false)
	@Index(name = "idx_Toets_resultaatstructuur")
	@AutoForm(include = false)
	private Resultaatstructuur resultaatstructuur;

	@Column(nullable = false, length = 10)
	private String code;

	@Column(nullable = false, length = 200)
	@AutoForm(include = false)
	private String codePath;

	@Column(nullable = false, length = 100)
	private String naam;

	@Column(nullable = true, length = 20)
	@AutoForm(label = "Referentie", editorClass = ReferentieCodeEditPanel.class)
	private String referentieCode;

	@Column(nullable = true)
	private Integer referentieVersie = 1;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schaal", nullable = false)
	@Index(name = "idx_Toets_schaal")
	@AutoForm(editorClass = SchaalCombobox.class)
	private Schaal schaal;

	/**
	 * De parent toets van deze deeltoets.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "parent")
	@Index(name = "idx_Toets_parent")
	@AutoForm(include = false)
	private Toets parent;

	@Column(nullable = false)
	@AutoForm(include = false)
	private int volgnummer;

	/**
	 * Is dit een eindtoets? Dit komt overeen met 'parent==null', maar het is veel
	 * efficienter te zoeken naar 'eindtoets==true'. Standaard wordt de property op true
	 * gezet, maar als een parent gezet wordt, wordt het property automatisch aangepast.
	 */
	@Column(nullable = false)
	@Index(name = "idx_Toets_eind")
	@AutoForm(include = false)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private boolean eindtoets = true;

	/**
	 * De children toetsen van deze deeltoets.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@AutoForm(include = false)
	@BatchSize(size = 100)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	@OrderBy("volgnummer ASC")
	private List<Toets> children = new ArrayList<Toets>();

	/**
	 * Weging van de toets.
	 */
	@Column(nullable = true)
	@AutoForm(required = true)
	private Integer weging;

	@Column(nullable = false)
	@AutoForm(label = "...som van onderliggende", description = "Als de weging de som van "
		+ "ingevulde onderliggende toetsen is, zal het resultaat van de bovenliggende toets het "
		+ "voortschrijdend gemiddelde zijn van de resultaten onder deze toets. Dit werkt door op "
		+ "onderliggende toetsen als daar deze optie ook aan staat.")
	private boolean automatischeWeging;

	@Column(nullable = false)
	@AutoForm(description = "Geeft aan of de toets wel of niet verplicht is voor de bovenliggende "
		+ "toets. Het resultaat van de bovenliggende toets wordt alleen berekend op het moment "
		+ "dat alle onderliggende verplichte toetsen zijn ingevuld.")
	private boolean verplicht;

	/**
	 * Is dit een samengestelde toets met een berekend resultaat.
	 */
	@Column(nullable = false)
	private boolean samengesteld;

	@AutoForm(label = "...met herkansingen", description = "Bij een samengestelde toets met "
		+ "herkansingen worden de herkansingen overgenomen uit onderliggende deeltoetsen. "
		+ "Hierbij is het van belang dat alle onderliggende deeltoetsen met herkansingen "
		+ "evenveel herkansingen hebben.")
	private boolean samengesteldMetHerkansing;

	@AutoForm(label = "...met varianten", description = "Voor een samengestelde toets met "
		+ "varianten worden voor elke poging één of meer varianten gedefinieerd.")
	private boolean samengesteldMetVarianten;

	@Column(nullable = true)
	@AutoForm(label = "Variant voor herkansing", required = true, editorClass = PogingCombobox.class)
	private Integer variantVoorPoging;

	@Column(nullable = false)
	@AutoForm(description = "Geeft aan of het is toegestaan om deze toets als doel van "
		+ "een toetsverwijzing te gebruiken.")
	private boolean verwijsbaar;

	@Column(nullable = false)
	@AutoForm(description = "Geeft aan of resultaten handigmatig ingeleverd moeten worden. Als "
		+ "dit uit staat, worden resultaten altijd direct doorgezet over toetsverwijzingen.")
	private boolean handmatigInleveren;

	/**
	 * Is het berekende resultaat van deze toets overschrijfbaar (alleen van toepassing
	 * bij samengestelde toetsen of toetsen met een schaal).
	 */
	@Column(nullable = false)
	@AutoForm(description = "Geeft aan of het is toegestaan om het berekende resultaat van deze "
		+ "toets handmatig te overschrijven.")
	private boolean overschrijfbaar;

	@Column(nullable = true)
	@AutoForm(label = "Max. aantal niet behaald", description = "Het maximum aantal deeltoetsen "
		+ "dat niet behaald mag zijn om voor deze toets een resultaat te mogen berekenen.")
	private Integer maxAantalNietBehaald;

	@Column(nullable = true)
	@AutoForm(label = "Min. aantal ingevuld", description = "Het minimum aantal deeltoetsen "
		+ "dat ingevuld moet zijn om voor deze toets een resultaat te mogen berekenen. Het maakt "
		+ "hierbij niet uit of de deeltoets behaald is of niet.")
	private Integer minAantalIngevuld;

	@Column(nullable = true)
	@AutoForm(label = "Max. aantal ingevuld", description = "Het maximum aantal deeltoetsen "
		+ "dat ingevuld mag zijn om voor deze toets een resultaat te mogen berekenen. Hiermee "
		+ "kan bijvoorbeeld afgedwongen dat maximaal 1 van de onderliggende deeltoetsen "
		+ "ingevuld wordt en vervolgens omhoog doorberekend wordt.")
	private Integer maxAantalIngevuld;

	@Column(nullable = true)
	@AutoForm(label = "Min. studiepunten", description = "Het minimum aantal studiepunten dat de "
		+ "deelnemer in onderliggende toetsen behaald moet hebben voor deze toets behaald is.")
	private Integer minStudiepuntenVoorBehaald;

	@Column(nullable = true, scale = 10, precision = 20)
	@AutoForm(description = "Het minimum resultaat dat behaald moet worden om bovenliggende "
		+ "toetsen te behalen. Bij een lager resultaat kan de bovenliggende toets niet behaald "
		+ "worden.", required = false, editorClass = CompenseerbaarField.class)
	private BigDecimal compenseerbaarVanaf;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	@AutoForm(description = "De manier waarop het resultaat van de samengestelde toets berekend "
		+ "moet worden: het gewogen gemiddelde of het resultaat met de hoogste weging.")
	private Rekenregel rekenregel;

	@Column(nullable = true, length = 1000)
	@AutoForm(required = true)
	private String formule;

	/**
	 * Het aantal herkansingen voor deze toets. Is alleen van toepassing voor niet
	 * samengestelde toetsen.
	 */
	@Column(nullable = true)
	private int aantalHerkansingen;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@AutoForm(required = true)
	private Herkansingsscore scoreBijHerkansing;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(description = "Geeft aan of bij deze toets een lineaire scoreschaal of een "
		+ "scoretabel hoort. Het eerste betekent dat er een lengte en een normering "
		+ "ingevoerd kan worden, en dat de resultaten als scores ingevuld kunnen worden. "
		+ "De scores worden vervolgens vertaald naar cijfers. Deze optie is alleen "
		+ "beschikbaar voor cijfertoetsen. Een scoretabel is alleen mogelijk bij tekstuele "
		+ "schalen. Hierbij worden de scores omgerekend naar resultaten middels een tabel.")
	private Scoreschaal scoreschaal;

	@Column(nullable = true)
	@AutoForm(description = "Het aantal studiepunten dat de deelnemer ontvangt bij het behalen "
		+ "van de toets.")
	private Integer studiepunten;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "toets")
	@OrderBy("vanafScore")
	@AutoForm(include = false)
	private List<Scoreschaalwaarde> scoreschaalwaarden = new ArrayList<Scoreschaalwaarde>();

	@Column(nullable = true)
	@AutoForm(label = "Lengte tijdvak 1", description = "De lengte van de scoreschaal, oftewel "
		+ "het maximaal aantal punten dat haalbaar is voor de toets.", required = false)
	private Integer scoreschaalLengteTijdvak1;

	@Column(nullable = true, scale = 10, precision = 20)
	@AutoForm(label = "Normering tijdvak 1", description = "Een correctiefactor waarmee het de "
		+ "berekening van het cijfer iets omhoog of omlaag bijgesteld kan worden.", required = false)
	private BigDecimal scoreschaalNormeringTijdvak1 = BigDecimal.ONE;

	@Column(nullable = true)
	@AutoForm(label = "Lengte tijdvak 2", description = "De lengte van de scoreschaal, oftewel "
		+ "het maximaal aantal punten dat haalbaar is voor de toets.", required = false)
	private Integer scoreschaalLengteTijdvak2;

	@Column(nullable = true, scale = 10, precision = 20)
	@AutoForm(label = "Normering tijdvak 2", description = "Een correctiefactor waarmee het de "
		+ "berekening van het cijfer iets omhoog of omlaag bijgesteld kan worden.", required = false)
	private BigDecimal scoreschaalNormeringTijdvak2 = BigDecimal.ONE;

	@Column(nullable = true)
	@AutoForm(label = "Lengte tijdvak 3", description = "De lengte van de scoreschaal, oftewel "
		+ "het maximaal aantal punten dat haalbaar is voor de toets.", required = false)
	private Integer scoreschaalLengteTijdvak3;

	@Column(nullable = true, scale = 10, precision = 20)
	@AutoForm(label = "Normering tijdvak 3", description = "Een correctiefactor waarmee het de "
		+ "berekening van het cijfer iets omhoog of omlaag bijgesteld kan worden.", required = false)
	private BigDecimal scoreschaalNormeringTijdvak3 = BigDecimal.ONE;

	@Column(nullable = false)
	private boolean alternatiefResultaatMogelijk;

	@Column(nullable = false)
	@AutoForm(label = "...combineren met hoofdresultaat")
	private boolean alternatiefCombinerenMetHoofd;

	@Column(nullable = false)
	@AutoForm(include = false)
	private long bevrorenPogingen;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "toets")
	@AutoForm(include = false)
	@FieldPersistance(FieldPersistenceMode.SKIP)
	private List<Resultaat> resultaten = new ArrayList<Resultaat>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lezenUit")
	@AutoForm(include = false)
	@BatchSize(size = 100)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<ToetsVerwijzing> uitgaandeVerwijzingen = new ArrayList<ToetsVerwijzing>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "schrijvenIn")
	@AutoForm(include = false)
	@BatchSize(size = 100)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<ToetsVerwijzing> inkomendeVerwijzingen = new ArrayList<ToetsVerwijzing>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "toets")
	@AutoForm(include = false)
	@BatchSize(size = 100)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
	private List<PersoonlijkeToetscode> persoonlijkeToetscodes =
		new ArrayList<PersoonlijkeToetscode>();

	public Toets()
	{
	}

	public Toets(Resultaatstructuur resultaatstructuur)
	{
		setResultaatstructuur(resultaatstructuur);
		setRekenregel(Rekenregel.Gemiddelde);
	}

	public Toets(Toets parent)
	{
		setResultaatstructuur(parent.getResultaatstructuur());
		setParent(parent);
		setVolgnummer(parent.getVolgendeVolgnummer());
		setVerplicht(true);
		setRekenregel(Rekenregel.Gemiddelde);
	}

	@Exportable()
	public Resultaatstructuur getResultaatstructuur()
	{
		return resultaatstructuur;
	}

	public void setResultaatstructuur(Resultaatstructuur resultaatstructuur)
	{
		this.resultaatstructuur = resultaatstructuur;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
		if (code != null)
			updateCodePath();
	}

	private void updateCodePath()
	{
		StringBuilder sb = new StringBuilder();
		buildCodePath(sb);
		setCodePath(sb.toString());
		updateCodePathOnChildren();
	}

	private void updateCodePathOnChildren()
	{
		if (getChildren() == null)
			return;

		for (Toets curChild : getChildren())
		{
			curChild.setCodePath(getCodePath() + "/" + getCode());
			curChild.updateCodePathOnChildren();
		}
	}

	private void buildCodePath(StringBuilder sb)
	{
		if (getParent() != null)
			getParent().buildCodePath(sb);
		sb.append("/");
		sb.append(getCode());
	}

	public String getCodePath()
	{
		return codePath;
	}

	public void setCodePath(String codePath)
	{
		this.codePath = codePath;
	}

	public String getCodeHierarchisch()
	{
		int depth = getDepth();
		return StringUtil.repeatString("&nbsp;&nbsp;&nbsp;&nbsp;", depth)
			+ Strings.escapeMarkup(getCode(), false, false);
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@AutoForm(description = "Als een persoonlijke toetscode ingesteld is, zal deze getoond worden "
		+ "op de resultatenpagina's voor groepen. Deze code is persoonlijk en zal dus voor andere "
		+ "gebruikers niet zichtbaar zijn.")
	public String getPersoonlijkeToetscode()
	{
		Medewerker medewerker = EduArteContext.get().getMedewerker();
		if (medewerker == null)
			return null;

		for (PersoonlijkeToetscode curToetscode : getPersoonlijkeToetscodes())
		{
			if (curToetscode.getMedewerker().equals(medewerker))
				return curToetscode.getCode();
		}
		return null;
	}

	public void setPersoonlijkeToetscode(String persoonlijkeToetscode)
	{
		Medewerker medewerker = EduArteContext.get().getMedewerker();
		if (medewerker == null)
			return;

		Iterator<PersoonlijkeToetscode> it = getPersoonlijkeToetscodes().iterator();
		while (it.hasNext())
		{
			PersoonlijkeToetscode curToetscode = it.next();
			if (curToetscode.getMedewerker().equals(medewerker))
			{
				if (StringUtil.isEmpty(persoonlijkeToetscode))
					it.remove();
				else
					curToetscode.setCode(persoonlijkeToetscode);
				return;
			}
		}
		if (!StringUtil.isEmpty(persoonlijkeToetscode))
		{
			PersoonlijkeToetscode newCode = new PersoonlijkeToetscode(medewerker, this);
			newCode.setCode(persoonlijkeToetscode);
			getPersoonlijkeToetscodes().add(newCode);
		}
	}

	public String getCodeVoorWeergave()
	{
		String persoonlijk = getPersoonlijkeToetscode();
		return persoonlijk == null ? getCode() : persoonlijk;
	}

	public String getReferentieCode()
	{
		return referentieCode;
	}

	public void setReferentieCode(String referentieCode)
	{
		this.referentieCode = referentieCode;
	}

	public Integer getReferentieVersie()
	{
		return referentieVersie;
	}

	public void setReferentieVersie(Integer referentieVersie)
	{
		this.referentieVersie = referentieVersie;
	}

	public Schaal getSchaal()
	{
		return schaal;
	}

	public void setSchaal(Schaal schaal)
	{
		this.schaal = schaal;
	}

	public Integer getStudiepunten()
	{
		return studiepunten;
	}

	public void setStudiepunten(Integer studiepunten)
	{
		this.studiepunten = studiepunten;
	}

	public Toets getParent()
	{
		return parent;
	}

	public void setParent(Toets parent)
	{
		this.parent = parent;
		setEindtoets(parent == null);
	}

	public void setChildren(List<Toets> children)
	{
		this.children = children;
	}

	public List<Toets> getChildren()
	{
		return children;
	}

	public Integer getWeging()
	{
		return weging;
	}

	public void setWeging(Integer weging)
	{
		this.weging = weging;
	}

	public boolean isAutomatischeWeging()
	{
		return automatischeWeging;
	}

	public void setAutomatischeWeging(boolean automatischeWeging)
	{
		this.automatischeWeging = automatischeWeging;
	}

	public void setVerplicht(boolean verplicht)
	{
		this.verplicht = verplicht;
	}

	public boolean isVerplicht()
	{
		return verplicht;
	}

	public boolean isSamengesteld()
	{
		return samengesteld;
	}

	public void setSamengesteld(boolean samengesteld)
	{
		this.samengesteld = samengesteld;
	}

	public boolean isSamengesteldMetHerkansing()
	{
		return samengesteldMetHerkansing;
	}

	public void setSamengesteldMetHerkansing(boolean samengesteldMetHerkansing)
	{
		this.samengesteldMetHerkansing = samengesteldMetHerkansing;
	}

	public boolean isSamengesteldMetVarianten()
	{
		return samengesteldMetVarianten;
	}

	public void setSamengesteldMetVarianten(boolean samengesteldMetVarianten)
	{
		this.samengesteldMetVarianten = samengesteldMetVarianten;
	}

	public Integer getVariantVoorPoging()
	{
		return variantVoorPoging;
	}

	public void setVariantVoorPoging(Integer variantVoorPoging)
	{
		this.variantVoorPoging = variantVoorPoging;
	}

	public boolean isVerwijsbaar()
	{
		return verwijsbaar;
	}

	public void setVerwijsbaar(boolean verwijsbaar)
	{
		this.verwijsbaar = verwijsbaar;
	}

	public boolean isHandmatigInleveren()
	{
		return handmatigInleveren;
	}

	public void setHandmatigInleveren(boolean handmatigInleveren)
	{
		this.handmatigInleveren = handmatigInleveren;
	}

	public boolean isOverschrijfbaar()
	{
		return overschrijfbaar;
	}

	public void setOverschrijfbaar(boolean overschrijfbaar)
	{
		this.overschrijfbaar = overschrijfbaar;
	}

	public Integer getMaxAantalNietBehaald()
	{
		return maxAantalNietBehaald;
	}

	public void setMaxAantalNietBehaald(Integer maxAantalNietBehaald)
	{
		this.maxAantalNietBehaald = maxAantalNietBehaald;
	}

	public void setMinAantalIngevuld(Integer minAantalIngevuld)
	{
		this.minAantalIngevuld = minAantalIngevuld;
	}

	public Integer getMinAantalIngevuld()
	{
		return minAantalIngevuld;
	}

	public Integer getMaxAantalIngevuld()
	{
		return maxAantalIngevuld;
	}

	public void setMaxAantalIngevuld(Integer maxAantalIngevuld)
	{
		this.maxAantalIngevuld = maxAantalIngevuld;
	}

	public Integer getMinStudiepuntenVoorBehaald()
	{
		return minStudiepuntenVoorBehaald;
	}

	public void setMinStudiepuntenVoorBehaald(Integer minStudiepuntenVoorBehaald)
	{
		this.minStudiepuntenVoorBehaald = minStudiepuntenVoorBehaald;
	}

	public BigDecimal getCompenseerbaarVanaf()
	{
		return compenseerbaarVanaf;
	}

	public void setCompenseerbaarVanaf(BigDecimal compenseerbaarVanaf)
	{
		this.compenseerbaarVanaf = compenseerbaarVanaf;
	}

	public Rekenregel getRekenregel()
	{
		return rekenregel;
	}

	public void setRekenregel(Rekenregel rekenregel)
	{
		this.rekenregel = rekenregel;
	}

	public String getFormule()
	{
		return formule;
	}

	public void setFormule(String formule)
	{
		this.formule = formule;
	}

	public int getAantalHerkansingen()
	{
		return aantalHerkansingen;
	}

	public void setAantalHerkansingen(int aantalHerkansingen)
	{
		this.aantalHerkansingen = aantalHerkansingen;
	}

	public void setScoreBijHerkansing(Herkansingsscore scoreBijHerkansing)
	{
		this.scoreBijHerkansing = scoreBijHerkansing;
	}

	public Herkansingsscore getScoreBijHerkansing()
	{
		return scoreBijHerkansing;
	}

	public Scoreschaal getScoreschaal()
	{
		return scoreschaal;
	}

	public void setScoreschaal(Scoreschaal scoreschaal)
	{
		this.scoreschaal = scoreschaal;
	}

	public void setScoreschaalwaarden(List<Scoreschaalwaarde> scoreschaalwaardes)
	{
		this.scoreschaalwaarden = scoreschaalwaardes;
	}

	public List<Scoreschaalwaarde> getScoreschaalwaarden()
	{
		return scoreschaalwaarden;
	}

	public Integer getScoreschaalLengteTijdvak1()
	{
		return scoreschaalLengteTijdvak1;
	}

	public void setScoreschaalLengteTijdvak1(Integer scoreschaalLengteTijdvak1)
	{
		this.scoreschaalLengteTijdvak1 = scoreschaalLengteTijdvak1;
	}

	public BigDecimal getScoreschaalNormeringTijdvak1()
	{
		return scoreschaalNormeringTijdvak1;
	}

	public void setScoreschaalNormeringTijdvak1(BigDecimal scoreschaalNormeringTijdvak1)
	{
		this.scoreschaalNormeringTijdvak1 = scoreschaalNormeringTijdvak1;
	}

	public Integer getScoreschaalLengteTijdvak2()
	{
		return scoreschaalLengteTijdvak2;
	}

	public void setScoreschaalLengteTijdvak2(Integer scoreschaalLengteTijdvak2)
	{
		this.scoreschaalLengteTijdvak2 = scoreschaalLengteTijdvak2;
	}

	public BigDecimal getScoreschaalNormeringTijdvak2()
	{
		return scoreschaalNormeringTijdvak2;
	}

	public void setScoreschaalNormeringTijdvak2(BigDecimal scoreschaalNormeringTijdvak2)
	{
		this.scoreschaalNormeringTijdvak2 = scoreschaalNormeringTijdvak2;
	}

	public Integer getScoreschaalLengteTijdvak3()
	{
		return scoreschaalLengteTijdvak3;
	}

	public void setScoreschaalLengteTijdvak3(Integer scoreschaalLengteTijdvak3)
	{
		this.scoreschaalLengteTijdvak3 = scoreschaalLengteTijdvak3;
	}

	public BigDecimal getScoreschaalNormeringTijdvak3()
	{
		return scoreschaalNormeringTijdvak3;
	}

	public void setScoreschaalNormeringTijdvak3(BigDecimal scoreschaalNormeringTijdvak3)
	{
		this.scoreschaalNormeringTijdvak3 = scoreschaalNormeringTijdvak3;
	}

	public Integer getScoreschaalLengte(int poging)
	{
		switch (poging)
		{
			case 1:
				return getScoreschaalLengteTijdvak1();
			case 2:
				return getScoreschaalLengteTijdvak2();
			case 3:
				return getScoreschaalLengteTijdvak3();
		}
		throw new IllegalArgumentException("poging moet 1, 2 of 3 zijn");
	}

	public BigDecimal getScoreschaalNormering(int poging)
	{
		switch (poging)
		{
			case 1:
				return getScoreschaalNormeringTijdvak1();
			case 2:
				return getScoreschaalNormeringTijdvak2();
			case 3:
				return getScoreschaalNormeringTijdvak3();
		}
		throw new IllegalArgumentException("poging moet 1, 2 of 3 zijn");
	}

	public boolean isAlternatiefResultaatMogelijk()
	{
		return alternatiefResultaatMogelijk;
	}

	public void setAlternatiefResultaatMogelijk(boolean alternatiefResultaatMogelijk)
	{
		this.alternatiefResultaatMogelijk = alternatiefResultaatMogelijk;
	}

	public boolean isAlternatiefCombinerenMetHoofd()
	{
		return alternatiefCombinerenMetHoofd;
	}

	public void setAlternatiefCombinerenMetHoofd(boolean alternatiefCombinerenMetHoofd)
	{
		this.alternatiefCombinerenMetHoofd = alternatiefCombinerenMetHoofd;
	}

	public void setVolgnummer(int volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	public int getVolgnummer()
	{
		return volgnummer;
	}

	public long getBevrorenPogingen()
	{
		return bevrorenPogingen;
	}

	public void setBevrorenPogingen(long bevrorenPogingen)
	{
		this.bevrorenPogingen = bevrorenPogingen;
	}

	public List<Resultaat> getResultaten()
	{
		return resultaten;
	}

	public void setResultaten(List<Resultaat> resultaten)
	{
		this.resultaten = resultaten;
	}

	public List<ToetsVerwijzing> getUitgaandeVerwijzingen()
	{
		return uitgaandeVerwijzingen;
	}

	public void setUitgaandeVerwijzingen(List<ToetsVerwijzing> uitgaandeVerwijzingen)
	{
		this.uitgaandeVerwijzingen = uitgaandeVerwijzingen;
	}

	public List<ToetsVerwijzing> getInkomendeVerwijzingen()
	{
		return inkomendeVerwijzingen;
	}

	public void setInkomendeVerwijzingen(List<ToetsVerwijzing> inkomendeVerwijzingen)
	{
		this.inkomendeVerwijzingen = inkomendeVerwijzingen;
	}

	public List<PersoonlijkeToetscode> getPersoonlijkeToetscodes()
	{
		return persoonlijkeToetscodes;
	}

	public void setPersoonlijkeToetscodes(List<PersoonlijkeToetscode> persoonlijkeToetscodes)
	{
		this.persoonlijkeToetscodes = persoonlijkeToetscodes;
	}

	public String getToetsverwijzingenFormatted()
	{
		return StringUtil.toString(getUitgaandeVerwijzingen(), "",
			new StringUtil.StringConverter<ToetsVerwijzing>()
			{
				@Override
				public String getSeparator(int listIndex)
				{
					return ", ";
				}

				@Override
				public String toString(ToetsVerwijzing object, int listIndex)
				{
					return object.getSchrijvenIn().toString();
				}
			});
	}

	@Override
	public BitSet getBevorenPogingenAsSet()
	{
		return JavaUtil.longToBitSet(getBevrorenPogingen());
	}

	@Override
	public void setBevorenPogingenAsSet(BitSet pogingen)
	{
		setBevrorenPogingen(JavaUtil.bitSetToLong(pogingen));
	}

	public boolean isBevroren(int pogingNr)
	{
		if (!isEindresultaat() && getParent().isBevroren(RESULTAAT_NR))
			return true;

		BitSet bevroren = getBevorenPogingenAsSet();
		if (pogingNr > RESULTAAT_NR)
		{
			return bevroren.get(RESULTAAT_IDX) || bevroren.get(pogingNr + OFFSET);
		}
		return bevroren.get(pogingNr + OFFSET);
	}

	public SoortToets getSoort()
	{
		return soort;
	}

	public void setSoort(SoortToets soort)
	{
		this.soort = soort;
	}

	public int getDepth()
	{
		if (getParent() == null)
			return 0;
		else
			return getParent().getDepth() + 1;
	}

	public Toets getParentAtLevel(int depth)
	{
		int thisDepth = getDepth();
		if (thisDepth < depth)
		{
			throw new IllegalArgumentException("Invalid depth");
		}
		Toets ret = this;
		while (thisDepth > depth)
		{
			ret = ret.getParent();
			thisDepth--;
		}
		return ret;
	}

	public boolean isAncestorOf(Toets other)
	{
		if (other.getParent() == null)
		{
			return false;
		}
		if (other.getParent().equals(this))
		{
			return true;
		}
		return isAncestorOf(other.getParent());
	}

	public boolean isCodeUsed(Toets toets)
	{
		return isCodeUsed(toets, toets.getCode());
	}

	/**
	 * Toetst of de meegegeven toets en zijn ingevulde waarde (apart ivm het feit dat
	 * tijdens valideren models niet zijn geupdate).
	 */
	public boolean isCodeUsed(Toets toets, @SuppressWarnings("hiding") String code)
	{
		for (Toets curChild : getChildren())
		{
			if (curChild.getCode().equals(code) && !curChild.equals(toets))
				return true;
		}
		return false;
	}

	public int getAantalPogingen()
	{
		if (isVariant())
			return getParent().getAantalPogingen();

		if (isSamengesteld() && !isSamengesteldMetVarianten())
		{
			if (isSamengesteldMetHerkansing())
			{
				int max = 0;
				for (Toets curChild : getChildren())
					max = Math.max(max, curChild.getAantalPogingen());
				return max;
			}
			else
				return 0;
		}
		return getAantalHerkansingen() + 1;
	}

	public boolean isVariant()
	{
		return getParent() != null && getParent().isSamengesteldMetVarianten();
	}

	public Class< ? > getInvoerType()
	{
		if (getSchaal().getSchaaltype().equals(Schaaltype.Cijfer))
			return BigDecimal.class;
		return String.class;
	}

	public boolean hasScoreschaal()
	{
		return !getScoreschaal().equals(Scoreschaal.Geen);
	}

	public boolean hasCijferSchaal()
	{
		return getSchaal().getSchaaltype().equals(Schaaltype.Cijfer);
	}

	public boolean isGeldigeScore(int value, int poging)
	{
		switch (getScoreschaal())
		{
			case Lineair:
				Integer lengte = getScoreschaalLengte(poging);
				return value >= 0 && (lengte == null || value <= lengte);
			case Tabel:
				for (Scoreschaalwaarde curWaarde : getScoreschaalwaarden())
				{
					if (curWaarde.isScoreBinnenWaarde(value))
						return true;
				}
				return false;
			case Geen:
				return false;
		}
		throw new IllegalArgumentException("Ongeldige schaal " + getScoreschaal());
	}

	public boolean isGeldigCijfer(Object value)
	{
		return getSchaal().isGeldigCijfer(value);
	}

	public Object convertWaarde(String textueleWaarde, int pogingNr)
			throws ResultaatFormatException
	{
		if (hasScoreschaal())
		{
			if (!StringUtil.isNumeric(textueleWaarde))
			{
				throw new ResultaatFormatException(String
					.format("%s is geen getal", textueleWaarde));
			}
			Integer score = Integer.parseInt(textueleWaarde);
			if (!isGeldigeScore(score, pogingNr + 1))
			{
				throw new ResultaatFormatException(String.format(
					"%s is geen geldige score voor deze toets", textueleWaarde));
			}
			return score;
		}
		else if (hasCijferSchaal())
		{
			if (!StringUtil.isDecimal(textueleWaarde))
			{
				throw new ResultaatFormatException(String.format("%s is geen (decimaal) getal",
					textueleWaarde));
			}
			BigDecimalConverter converter =
				new BigDecimalConverter(getSchaal().getAantalDecimalen(), true);
			BigDecimal cijfer =
				converter.convertToObject(textueleWaarde, CobraApplication.DUTCH_LOCALE);
			if (!isGeldigCijfer(cijfer))
			{
				throw new ResultaatFormatException(String.format(
					"%s is geen geldige waarde voor deze toets", textueleWaarde));
			}
			return cijfer;
		}
		else
		{
			if (!isGeldigCijfer(textueleWaarde))
			{
				throw new ResultaatFormatException(String.format(
					"%s is geen geldige waarde voor deze toets", textueleWaarde));
			}
			return textueleWaarde;
		}
	}

	public List<Toets> getParents()
	{
		LinkedList<Toets> ret = new LinkedList<Toets>();
		Toets curToets = this;
		while ((curToets = curToets.getParent()) != null)
		{
			ret.add(curToets);
		}
		return ret;
	}

	public boolean isCijferBerekenbaar(int score, int poging)
	{
		return !Scoreschaal.Lineair.equals(getScoreschaal())
			|| (getScoreschaalLengte(poging) != null && getScoreschaalNormering(poging) != null && getScoreschaalLengte(poging) >= score);
	}

	/**
	 * Berekening van het cijfer volgens de cito normeringen.
	 * http://www.cito.nl/VO/ce/algemeen/eind_fr3.htm
	 */
	public BigDecimal berekenCijfer(int score, int poging)
	{
		if (!isCijferBerekenbaar(score, poging))
			return null;

		BigDecimal scoreBD = BigDecimal.valueOf(score);
		BigDecimal schaallengteBD = BigDecimal.valueOf(getScoreschaalLengte(poging));
		long cijfer2bl = formula1(score, poging);
		if (getScoreschaalNormering(poging).compareTo(DecimalUtil.ONE) < 0)
		{
			// snijpunt lijn 1 en 3
			// S2=2L*(1-N)/9
			BigDecimal s2 =
				schaallengteBD.multiply(DecimalUtil.TWO).multiply(
					DecimalUtil.ONE.subtract(getScoreschaalNormering(poging))).divide(
					DecimalUtil.NINE, DecimalUtil.RESULTAAT_MATH_CONTEXT);
			if (scoreBD.compareTo(s2) <= 0)
				cijfer2bl = formula3(score, poging);

			// snijpunt lijn 1 en 4
			// s3=l*(n+8)/9
			BigDecimal s3 =
				schaallengteBD.multiply(DecimalUtil.EIGHT.add(getScoreschaalNormering(poging)))
					.divide(DecimalUtil.NINE, DecimalUtil.RESULTAAT_MATH_CONTEXT);
			if (scoreBD.compareTo(s3) >= 0)
				cijfer2bl = formula4(score, poging);
		}
		else if (getScoreschaalNormering(poging).compareTo(DecimalUtil.ONE) > 0)
		{
			// snijpunt lijn 1 en 2
			// s1=l*(n-1)/9
			BigDecimal s1 =
				schaallengteBD.multiply(getScoreschaalNormering(poging).subtract(DecimalUtil.ONE))
					.divide(DecimalUtil.NINE, DecimalUtil.RESULTAAT_MATH_CONTEXT);
			if (scoreBD.compareTo(s1) <= 0)
				cijfer2bl = formula2(score, poging);

			// snijpunt lijn 1 en 5
			// s4=l*(11-2n)/9
			BigDecimal s4 =
				schaallengteBD.multiply(
					DecimalUtil.ELEVEN.subtract(DecimalUtil.TWO
						.multiply(getScoreschaalNormering(poging)))).divide(DecimalUtil.NINE,
					DecimalUtil.RESULTAAT_MATH_CONTEXT);
			if (scoreBD.compareTo(s4) >= 0)
				cijfer2bl = formula5(score, poging);
		}

		long b = getNormeringDeler(poging);
		long l = getScoreschaalLengte(poging);
		BigDecimal bl2 = BigDecimal.valueOf(2 * b * l);
		return max10(BigDecimal.valueOf(cijfer2bl).divide(bl2, getSchaal().getAantalDecimalen(),
			RoundingMode.HALF_UP));
	}

	/**
	 * Kapt resultaten af op 10,0. Dit kan nl gebeuren als de schaallengte later wordt
	 * aangepast en de score dan boven de schaallengte komt te liggen.
	 * 
	 * @param resultaat
	 *            het resultaat dat niet groter dan 10 mag zijn
	 * @return het originele resultaat of 10.
	 */
	private BigDecimal max10(BigDecimal resultaat)
	{
		if (DecimalUtil.compare(DecimalUtil.TEN, resultaat) > 0)
			return resultaat;
		return DecimalUtil.TEN;
	}

	/**
	 * formule om hoge scores met een hoge normering te berekenen.
	 * 
	 * @return 2blc
	 */
	private long formula5(long s, int poging)
	{
		// c=(9bs+11bl)/2bl
		long b = getNormeringDeler(poging);
		long l = getScoreschaalLengte(poging);

		return 9 * b * s + 11 * b * l;

	}

	/**
	 * formule om hoge scores met een lage normering te berekenen.
	 * 
	 * @return 2blc
	 */
	private long formula4(long s, int poging)
	{
		// c=(36bs-16bl)/2bl
		long b = getNormeringDeler(poging);
		long l = getScoreschaalLengte(poging);

		return 36 * b * s - 16 * b * l;
	}

	/**
	 * formule om lage scores met een lage normering te berekenen.
	 * 
	 * @return 2blc
	 */
	private long formula3(long s, int poging)
	{
		// c=(9bs+2bl)/2bl
		long b = getNormeringDeler(poging);
		long l = getScoreschaalLengte(poging);

		return 9 * b * s + 2 * b * l;
	}

	/**
	 * formule om lage scores met een hoge normering te berekenen.
	 * 
	 * @return 2blc
	 */
	private long formula2(long s, int poging)
	{
		// c=(36bs+2bl)/2bl
		long b = getNormeringDeler(poging);
		long l = getScoreschaalLengte(poging);

		return 36 * b * s + 2 * b * l;
	}

	/**
	 * Standaard formule om score om te zetten in een cijfer.
	 * 
	 * @return 2blc
	 */
	private long formula1(long s, int poging)
	{
		// c=(18*bs+2al)/2bl
		long a = getNormeringTeller(poging);
		long b = getNormeringDeler(poging);
		long l = getScoreschaalLengte(poging);

		return 18 * b * s + 2 * a * l;
	}

	private long getNormeringTeller(int poging)
	{
		return getScoreschaalNormering(poging).unscaledValue().longValue();
	}

	private long getNormeringDeler(int poging)
	{
		if (getScoreschaalNormering(poging).scale() > 10)
			throw new IllegalArgumentException("De schaal van de normering is > 10 ("
				+ getScoreschaalNormering(poging).scale() + ")");

		long ret = 1;
		for (int count = 0; count < getScoreschaalNormering(poging).scale(); count++)
		{
			ret *= 10;
		}
		return ret;
	}

	public String toLabelValueJSON()
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		appendLabelValue(sb, "Code", getCode()).append(',');
		appendLabelValue(sb, "Naam", getNaam()).append(',');
		appendLabelValue(sb, "Schaal", getSchaal().getNaam());
		if (Scoreschaal.Tabel.equals(getScoreschaal()))
		{
			sb.append(',');
			appendLabelValue(sb, "Scoreschaal", getTabelScoreschaalBeschrijving());
		}
		if (Scoreschaal.Lineair.equals(getScoreschaal()))
		{
			for (int tijdvak = 1; tijdvak <= 3; tijdvak++)
			{
				if (!isVariant() || getVariantVoorPoging() == (tijdvak - 1))
				{
					sb.append(',');
					Integer curLengte = getScoreschaalLengte(tijdvak);
					if (curLengte == null)
						appendLabelValue(sb, "Scoreschaal T" + tijdvak, "Onbekende lengte");
					else
						appendLabelValue(sb, "Scoreschaal T" + tijdvak, "0 tot " + curLengte);
				}
			}
		}
		sb.append(']');
		return sb.toString();
	}

	public String getTabelScoreschaalBeschrijving()
	{
		if (getScoreschaalwaarden().isEmpty())
			return "-";
		int max = 0;
		int min = Integer.MAX_VALUE;
		for (Scoreschaalwaarde curWaarde : getScoreschaalwaarden())
		{
			if (curWaarde.getTotScore() > max)
				max = curWaarde.getTotScore() - 1;
			if (curWaarde.getVanafScore() < min)
				min = curWaarde.getVanafScore();
		}
		return min + " - " + max;
	}

	private StringBuilder appendLabelValue(StringBuilder sb, String label, String value)
	{
		sb.append('{');
		sb.append("label:\"").append(label).append("\",");
		sb.append("value:\"").append(StringUtil.escapeForJavascriptString(value)).append('\"');
		sb.append('}');
		return sb;
	}

	public boolean isEindresultaat()
	{
		return getParent() == null;
	}

	public String getCodeOfOnderwijsproduct()
	{
		if (isEindresultaat())
			return getResultaatstructuur().getOnderwijsproduct().getCode();
		return getCode();
	}

	public boolean isEindtoets()
	{
		return eindtoets;
	}

	protected void setEindtoets(boolean eindtoets)
	{
		this.eindtoets = eindtoets;
	}

	public Schaalwaarde berekenWaarde(int score)
	{
		for (Scoreschaalwaarde curWaarde : getScoreschaalwaarden())
		{
			if (curWaarde.isScoreBinnenWaarde(score))
			{
				return curWaarde.getWaarde();
			}
		}
		return null;
	}

	public int getAantalResultaten()
	{
		if (isSaved())
		{
			return DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).getAantalResultaten(
				this);
		}
		return 0;
	}

	public boolean getHeeftResultaten()
	{
		if (isSaved())
		{
			return DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).heeftResultaten(this);
		}
		return false;
	}

	public boolean getHeeftResultaten(Deelnemer deelnemer)
	{
		if (isSaved())
		{
			return DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).heeftResultaten(this,
				deelnemer);
		}
		return false;
	}

	public String getToetscodePath(ToetsCodePathMode mode)
	{
		switch (mode)
		{
			case STANDAARD:
				return getToetscodePath();
			case STRUCTUUR_LOKAAL:
				return getResultaatstructuur().getCode() + "/" + getToetscodePath();
		}
		throw new IllegalArgumentException("Ongeldige mode: " + mode);
	}

	public String getToetscodePath()
	{
		Toets toets = this;
		String ret = "";
		while (toets.getParent() != null)
		{
			ret = toets.getCode() + "/" + ret;
			toets = toets.getParent();
		}
		return toets.getCode() + "/" + ret;
	}

	@Override
	public String toString()
	{
		String ret =
			getResultaatstructuur() == null ? "" : getResultaatstructuur().toString() + " - ";
		String codepath = getToetscodePath();
		return ret + codepath.substring(0, codepath.length() - 1);
	}

	/**
	 * @return De eerste toets van de gegeven soort die gevonden kan worden onder de
	 *         children van deze toets. Daarna wordt onder de children hiervan weer
	 *         gezocht.
	 */
	public Toets getChild(SoortToets soortToets)
	{
		for (Toets toets : getChildren())
		{
			if (toets.getSoort() == soortToets)
			{
				return toets;
			}
		}
		for (Toets toets : getChildren())
		{
			Toets res = toets.getChild(soortToets);
			if (res != null)
				return res;
		}
		return null;
	}

	/**
	 * Het directe child met de gegeven code, of null
	 */
	public Toets getChild(String childCode)
	{
		for (Toets curChild : getChildren())
		{
			if (curChild.getCode().equals(childCode))
				return curChild;
		}
		return null;
	}

	public int getVolgendeVolgnummer()
	{
		int ret = 0;
		for (Toets toets : getChildren())
			ret = Math.max(ret, toets.getVolgnummer());
		return ret + 1;
	}

	public boolean isEersteChild()
	{
		int index = getParent().getChildren().indexOf(this);
		return index == 0;
	}

	public boolean isLaatsteChild()
	{
		int index = getParent().getChildren().indexOf(this);
		int size = getParent().getChildren().size();
		return (size - 1) == index;
	}

	public void swapToetsen(int index1, int index2)
	{
		Toets toets1 = getChildren().get(index1);
		Toets toets2 = getChildren().get(index2);
		int swapVolgnr = toets1.getVolgnummer();
		toets1.setVolgnummer(toets2.getVolgnummer());
		toets2.setVolgnummer(swapVolgnr);
		getChildren().set(index1, toets2);
		getChildren().set(index2, toets1);
	}

	public List<Toets> getDescendants()
	{
		List<Toets> ret = new ArrayList<Toets>();
		ret.add(this);
		for (Toets curChild : getChildren())
		{
			ret.addAll(curChild.getDescendants());
		}
		return ret;
	}

	public Set<Toets> getRecalcRequired()
	{
		Set<Toets> ret = new HashSet<Toets>();
		internalRecalcRequired(ret);
		return ret;
	}

	private void internalRecalcRequired(Set<Toets> ret)
	{
		for (ToetsVerwijzing curVerwijzing : getUitgaandeVerwijzingen())
		{
			if (ret.add(curVerwijzing.getSchrijvenIn()))
				curVerwijzing.getSchrijvenIn().internalRecalcRequired(ret);
		}
		if (getParent() != null)
		{
			if (ret.add(getParent()))
				getParent().internalRecalcRequired(ret);
		}
	}

	public boolean isInvulbaar(int pogingNr)
	{
		if (isSamengesteld() && !(isOverschrijfbaar() && pogingNr == 0))
			return false;
		if (pogingNr > getAantalPogingen())
			return false;
		return true;
	}
}
