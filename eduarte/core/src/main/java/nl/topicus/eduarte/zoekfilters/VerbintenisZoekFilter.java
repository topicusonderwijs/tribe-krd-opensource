package nl.topicus.eduarte.zoekfilters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.BronEntiteitStatus;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.entities.contract.SoortContract;
import nl.topicus.eduarte.entities.contract.TypeFinanciering;
import nl.topicus.eduarte.entities.contract.Contract.Onderaanneming;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.entities.inschrijving.NT2Niveau;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.inschrijving.Schooladvies;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek.IntakegesprekStatus;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Leerprofiel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.ProfielInburgering;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.RedenInburgering;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.SoortPraktijkexamen;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Vertrekstatus;
import nl.topicus.eduarte.entities.kenmerk.Kenmerk;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.Team;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.MBONiveau;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.vrijevelden.VerbintenisVrijVeld;
import nl.topicus.eduarte.web.components.choice.ExamenstatusCombobox;
import nl.topicus.eduarte.web.components.choice.TaxonomieCombobox;
import nl.topicus.eduarte.web.components.choice.TaxonomieElementTypeCombobox;
import nl.topicus.eduarte.zoekfilters.DeelnemerZoekFilter.TypeAdres;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class VerbintenisZoekFilter extends
		AbstractOrganisatieEenheidLocatieVrijVeldableZoekFilter<VerbintenisVrijVeld, Verbintenis>
		implements IMentorDocentZoekFilter<Verbintenis>,
		IVerantwoordelijkeUitvoerendeZoekFilter<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private Date actiefVanaf;

	private Date actiefTotEnMet;

	private IModel<List<OrganisatieEenheid>> organisatieEenheidList;

	private IModel<List<Locatie>> locatieList;

	private IModel<List<Team>> teamList;

	private DeelnemerZoekFilter deelnemerZoekFilter;

	private RelatieZoekFilter relatieZoekFilter;

	private PlaatsingZoekFilter plaatsingZoekFilter;

	private ContractZoekFilter contractZoekFilter;

	private GroepZoekFilter groepZoekFilter;

	private IntakegesprekZoekFilter intakegesprekZoekFilter;

	private OnderwijsproductZoekFilter onderwijsproductZoekFilter;

	private OpleidingZoekFilter opleidingZoekFilter;

	private VooropleidingZoekFilter vooropleidingZoekFilter;

	private UitschrijvingZoekFilter uitschrijvingZoekFilter;

	private BPVInschrijvingZoekFilter bpvInschrijvingZoekFilter;

	@AutoForm(label = "Eind peildatum")
	private Date peilEindDatum;

	private Date datumInschrijvingVanaf;

	private Date datumInschrijvingTotEnMet;

	private Date datumUitschrijvingVanaf;

	private Date datumUitschrijvingTotEnMet;

	private Date datumGeplandEindeVanaf;

	private Date datumGeplandEindeTotEnMet;

	private IModel<RedenUitschrijving> redenUitschrijving;

	private IModel<List<RedenUitschrijving>> redenUitschrijvingList;

	private IModel<Verbintenisgebied> verbintenisgebied;

	private IModel<List<Verbintenisgebied>> verbintenisgebiedList;

	@AutoForm(htmlClasses = "unit_150")
	private IModel<Opleiding> opleiding;

	private IModel<Opleiding> opleidingOngelijkAan;

	@AutoForm(label = "Opleidingen")
	private IModel<List<Opleiding>> opleidingList;

	private IModel<SoortVooropleiding> relevanteVooropleidingSoort;

	private IModel<Verbintenis> relevanteVerbintenis;

	private Boolean relevanteVooropleidingSoortLeeg;

	@AutoForm(htmlClasses = "unit_80")
	private IModel<Cohort> cohort;

	private IModel<Verbintenisgebied> verbintenisgebiedOngelijkAan;

	private IModel<ToegestaneExamenstatusOvergang> toegestaneExamenstatusOvergang;

	private Integer tijdvak;

	private String opleidingscode;

	private IModel<Onderwijsproduct> afgenomenOnderwijsproduct;

	private Boolean beeindigdeOnderwijsproductAfname;

	private List<BronEntiteitStatus> bronStatusList;

	/**
	 * De basisgroep waaraan de deelnemer (via een plaatsing) aan gekoppeld is.
	 */
	private IModel<Groep> basisgroep;

	private Integer leerjaar;

	private Integer praktijkjaar;

	private IModel<SoortContract> soortContract;

	private IModel<List<SoortContract>> soortContractList;

	private IModel<TypeFinanciering> typeFinanciering;

	private IModel<Contract> contract;

	private IModel<List<Contract>> contractList;

	private IModel<ContractOnderdeel> contractOnderdeel;

	private IModel<List<ContractOnderdeel>> contractOnderdeelList;

	private IModel<ExterneOrganisatie> contractExterneOrganisatie;

	private IModel<List<ExterneOrganisatie>> contractExterneOrganisatieList;

	private IModel<ExterneOrganisatieContactPersoon> contractContactpersoon;

	private IModel<Medewerker> contractBeheerder;

	private BigDecimal contractKostprijsVanaf;

	private BigDecimal contractKostprijsTotEnMet;

	private Boolean contractInburgering;

	private Onderaanneming contractOnderaanneming;

	@AutoForm(label = "Openstaande intake")
	private Boolean statusIntake;

	private IModel<Verbintenis> verbintenis;

	private IModel<VerbintenisStatus> verbintenisStatus;

	private List<VerbintenisStatus> verbintenisStatusList;

	private IModel<Intensiteit> intensiteit;

	private List<Intensiteit> intensiteitList;

	private Bekostigd bekostigd;

	private Integer contacturenPerWeek;

	private Vertrekstatus vertrekstatus;

	private IModel<MBOLeerweg> leerweg;

	private IModel<ExterneOrganisatie> bpvBedrijf;

	private IModel<List<ExterneOrganisatie>> bpvBedrijfList;

	private IModel<Medewerker> praktijkBegeleider;

	private Boolean praktijkBegeleiderLeeg;

	private BPVStatus bpvStatus;

	private List<BPVStatus> bpvStatusList;

	private BronEntiteitStatus bpvBronStatus;

	private IModel<RedenUitschrijving> bpvRedenBeeindiging;

	private IModel<Brin> bpvKenniscentrum;

	private Boolean bpvKenniscentrumLeeg;

	private IModel<ExterneOrganisatie> bpvContractpartner;

	private IModel<ExterneOrganisatieContactPersoon> praktijkopleiderBPVBedrijf;

	private Boolean praktijkopleiderBPVBedrijfLeeg;

	private Date bpvBeginDatum;

	private Date bpvEindDatum;

	private Integer bpvOmvangVanaf;

	private Integer bpvOmvangTotEnMet;

	private BigDecimal bpvUrenPerWeekVanaf;

	private BigDecimal bpvUrenPerWeekTotEnMet;

	private Integer bpvDagenPerWeekVanaf;

	private Integer bpvDagenPerWeekTotEnMet;

	private List<BPVStatus> bpvStatusOngelijkAan = new ArrayList<BPVStatus>();

	private Boolean indicatieGehandicapt;

	private Boolean indicatieLWOO;

	private boolean opleidingEnCohortVerplicht;

	private IModel<ExamenWorkflow> examenWorkflow;

	@AutoForm(editorClass = ExamenstatusCombobox.class, htmlClasses = "unit_80")
	private IModel<Examenstatus> examenstatus;

	private IModel<List<Examenstatus>> examenstatusList;

	private Date datumUitslagVanaf;

	private Date datumUitslagTotEnMet;

	private Boolean datumUitslagIsLeeg;

	private Integer examennummerVanaf;

	private Integer examennummerTotEnMet;

	private Boolean examendeelnameBekostigd;

	private Integer examendeelnameTijdvakVanaf;

	private Integer examendeelnameTijdvakTotEnMet;

	private Integer examendeelnameTijdvakGelijkAan;

	private Boolean examendeelnameTijdvakIsLeeg;

	private Integer examenjaar;

	@AutoForm(editorClass = TaxonomieCombobox.class)
	private IModel<Taxonomie> taxonomie;

	private String taxonomiecode;

	private IModel<List<Taxonomie>> taxonomieList;

	@AutoForm(editorClass = TaxonomieElementTypeCombobox.class)
	private IModel<TaxonomieElementType> taxonomieElementType;

	private IntakegesprekStatus intakegesprekStatus;

	private Date datumIntakegesprekVanaf;

	private Date datumIntakegesprekTotEnMet;

	private IModel<Medewerker> intaker;

	private IModel<List<Medewerker>> intakerList;

	private IModel<Opleiding> gewensteOpleiding;

	private IModel<List<Opleiding>> gewensteOpleidingList;

	private IModel<Locatie> gewensteLocatie;

	private IModel<List<Locatie>> gewensteLocatieList;

	private Date gewensteBegindatumVanaf;

	private Date gewensteBegindatumTotEnMet;

	private Date gewensteEinddatumVanaf;

	private Date gewensteEinddatumTotEnMet;

	private MBONiveau niveau;

	private IModel<Productregel> productregel;

	private RedenInburgering redenInburgering;

	private ProfielInburgering profielInburgering;

	private List<ProfielInburgering> profielInburgeringList;

	private Leerprofiel leerprofiel;

	private List<Leerprofiel> leerprofielList;

	private Boolean deelcursus;

	private SoortPraktijkexamen soortPraktijkexamen;

	private List<SoortPraktijkexamen> soortPraktijkexamenList;

	private Date datumAanmeldenVanaf;

	private Date datumAanmeldenTotEnMet;

	private Date datumAkkoordVanaf;

	private Date datumAkkoordTotEnMet;

	private Date datumEersteActiviteitVanaf;

	private Date datumEersteActiviteitTotEnMet;

	private NT2Niveau beginNiveauSchriftelijkeVaardigheden;

	private List<NT2Niveau> beginNiveauSchriftelijkeVaardighedenList;

	private NT2Niveau eindNiveauSchriftelijkeVaardigheden;

	private List<NT2Niveau> eindNiveauSchriftelijkeVaardighedenList;

	private List<VerbintenisStatus> verbintenisStatusOngelijkAan =
		new ArrayList<VerbintenisStatus>();

	private boolean verbergStatusAangemeld = true;

	private Boolean uitsluitenVanFacturatie;

	private Boolean lwoo;

	private Boolean einddatumIsNull;

	/**
	 * Is dit een zoekopdracht voor de DeelnemerZoekenPage?
	 */
	private boolean standaardDeelnemerZoeken = false;

	/**
	 * Wordt er op dit moment een count gedaan?
	 */
	private boolean countQuery = false;

	public static final VerbintenisZoekFilter getDefaultFilter()
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.addOrderByProperty("deelnemer.deelnemernummer");
		filter.addOrderByProperty("persoon.roepnaam");
		filter.addOrderByProperty("persoon.achternaam");

		return filter;
	}

	public VerbintenisZoekFilter()
	{
		super(VerbintenisVrijVeld.class);
		setDeelnemerZoekFilter(new DeelnemerZoekFilter());
		setRelatieFilter(new RelatieZoekFilter());
		setPlaatsingZoekFilter(new PlaatsingZoekFilter());
		setContractZoekFilter(new ContractZoekFilter());
		setGroepZoekFilter(new GroepZoekFilter());
		setIntakegesprekZoekFilter(new IntakegesprekZoekFilter());
		setOnderwijsproductZoekFilter(new OnderwijsproductZoekFilter());
		setOpleidingZoekFilter(new OpleidingZoekFilter());
		setVooropleidingZoekFilter(new VooropleidingZoekFilter());
		setUitschrijvingZoekFilter(new UitschrijvingZoekFilter());
		setBpvInschrijvingZoekFilter(new BPVInschrijvingZoekFilter());
	}

	/**
	 * @return true indien er minimaal 1 plaatsingscriterium ingevuld is.
	 */
	public boolean heeftPlaatsingCriteria()
	{
		return getBasisgroep() != null || getLeerjaar() != null || getIndicatieLWOO() != null
			|| getPraktijkjaar() != null
			|| StringUtil.containsStringStartingWith(getOrderByList(), "plaatsing.");
	}

	public boolean heeftOpleidingCriteria()
	{
		return getOpleiding() != null || getOpleidingOngelijkAan() != null
			|| getVerbintenisgebied() != null || getVerbintenisgebiedOngelijkAan() != null
			|| getQuickSearchQuery() != null || getOpleidingsCode() != null || getLeerweg() != null
			|| getNiveau() != null
			|| StringUtil.containsStringStartingWith(getOrderByList(), "opleiding.")
			|| StringUtil.containsStringStartingWith(getOrderByList(), "verbintenisgebied.")
			|| getTaxonomie() != null || getTaxonomieElementType() != null
			|| getTaxonomiecode() != null
			|| (getTaxonomieList() != null && !getTaxonomieList().isEmpty())
			|| (getOpleidingList() != null && !getOpleidingList().isEmpty())
			|| (getVerbintenisgebiedList() != null && !getVerbintenisgebiedList().isEmpty())
			|| (getTeamList() != null && !getTeamList().isEmpty());
	}

	public boolean heeftExamendeelnameCriteria()
	{
		return getExamenWorkflow() != null || getExamenstatus() != null
			|| getDatumUitslagVanaf() != null || getDatumUitslagTotEnMet() != null
			|| getDatumUitslagIsLeeg() != null || getExamennummerVanaf() != null
			|| getExamennummerTotEnMet() != null || getExamendeelnameBekostigd() != null
			|| getExamenjaar() != null || getExamendeelnameTijdvakVanaf() != null
			|| getExamendeelnameTijdvakTotEnMet() != null
			|| getExamendeelnameTijdvakIsLeeg() != null
			|| getExamendeelnameTijdvakGelijkAan() != null
			|| (getExamenstatusList() != null && !getExamenstatusList().isEmpty());
	}

	public boolean heeftRelatieCriteria()
	{
		return getRelatieBsn() != null || StringUtil.isNotEmpty(getRelatieAchternaam())
			|| getRelatieDebiteurenNummer() != null || getRelatieGeslacht() != null
			|| getRelatieTypeAdres() != null || getRelatieZelfdeAdresAlsDeelnemer() != null
			|| getRelatiePostcode() != null || getRelatieHuisnummer() != null
			|| getRelatieHuisnummerToevoeging() != null || getRelatieStraat() != null
			|| getRelatiePlaats() != null || getRelatieSoort() != null
			|| getRelatieExterneOrganisatie() != null
			|| getRelatieGeboortelandOngelijkAanNL() != null
			|| getRelatieNationaliteitOngelijkAanNL() != null;
	}

	public boolean heeftBPVCriteria()
	{
		return getBpvStatus() != null || getBpvKenniscentrum() != null || getBpvBedrijf() != null
			|| getBpvBeginDatum() != null || getBpvEindDatum() != null
			|| getPraktijkBegeleider() != null || getBpvContractpartner() != null
			|| getPraktijkopleiderBPVBedrijf() != null || getBpvOmvangVanaf() != null
			|| getBpvOmvangTotEnMet() != null || getBpvUrenPerWeekVanaf() != null
			|| getBpvUrenPerWeekTotEnMet() != null || getBpvDagenPerWeekVanaf() != null
			|| getBpvDagenPerWeekTotEnMet() != null
			|| (getBpvStatusOngelijkAan() != null && !getBpvStatusOngelijkAan().isEmpty())
			|| (getBpvStatusList() != null && !getBpvStatusList().isEmpty())
			|| getBpvBronStatus() != null
			|| (getBpvBedrijfList() != null && !getBpvBedrijfList().isEmpty())
			|| getPraktijkopleiderBPVBedrijfLeeg() != null || getPraktijkBegeleiderLeeg() != null
			|| getBpvKenniscentrumLeeg() != null || getBpvRedenBeeindiging() != null;
	}

	public boolean heeftContractCriteria()
	{
		return getSoortContract() != null
			|| getTypeFinanciering() != null
			|| getContract() != null
			|| getContractOnderdeel() != null
			|| getContractExterneOrganisatie() != null
			|| getContractContactpersoon() != null
			|| getContractBeheerder() != null
			|| getContractKostprijsVanaf() != null
			|| getContractKostprijsTotEnMet() != null
			|| getContractInburgering() != null
			|| getContractOnderaanneming() != null
			|| (getSoortContractList() != null && !getSoortContractList().isEmpty())
			|| (getContractOnderdeelList() != null && !getContractOnderdeelList().isEmpty())
			|| (getContractList() != null && !getContractList().isEmpty())
			|| (getContractExterneOrganisatieList() != null && !getContractExterneOrganisatieList()
				.isEmpty());
	}

	public boolean heeftIntakegesprekCriteria()
	{
		return getIntakegesprekStatus() != null || getDatumIntakegesprekVanaf() != null
			|| getDatumIntakegesprekTotEnMet() != null || getIntaker() != null
			|| getGewensteOpleiding() != null || getGewensteLocatie() != null
			|| getGewensteBegindatumVanaf() != null || getGewensteBegindatumTotEnMet() != null
			|| getGewensteEinddatumVanaf() != null || getGewensteEinddatumTotEnMet() != null
			|| (getIntakerList() != null && !getIntakerList().isEmpty());
	}

	@Override
	public void setAuthorizationContext(
			OrganisatieEenheidLocatieAuthorizationContext authorizationContext)
	{
		super.setAuthorizationContext(authorizationContext);
		groepZoekFilter.setAuthorizationContext(authorizationContext);
		onderwijsproductZoekFilter.setAuthorizationContext(authorizationContext);
		opleidingZoekFilter.setAuthorizationContext(authorizationContext);
	}

	public List<BronEntiteitStatus> getBronStatusList()
	{
		return bronStatusList;
	}

	public void setBronStatusList(List<BronEntiteitStatus> bronStatusList)
	{
		this.bronStatusList = bronStatusList;
	}

	public DeelnemerZoekFilter getDeelnemerZoekFilter()
	{
		return deelnemerZoekFilter;
	}

	public void setDeelnemerZoekFilter(DeelnemerZoekFilter deelnemerFilter)
	{
		this.deelnemerZoekFilter = deelnemerFilter;
	}

	public PlaatsingZoekFilter getPlaatsingZoekFilter()
	{
		return plaatsingZoekFilter;
	}

	public void setPlaatsingZoekFilter(PlaatsingZoekFilter plaatsingZoekFilter)
	{
		this.plaatsingZoekFilter = plaatsingZoekFilter;
	}

	public ContractZoekFilter getContractZoekFilter()
	{
		return contractZoekFilter;
	}

	public void setContractZoekFilter(ContractZoekFilter contractZoekFilter)
	{
		this.contractZoekFilter = contractZoekFilter;
	}

	public GroepZoekFilter getGroepZoekFilter()
	{
		return groepZoekFilter;
	}

	public void setGroepZoekFilter(GroepZoekFilter groepZoekFilter)
	{
		this.groepZoekFilter = groepZoekFilter;
	}

	public IntakegesprekZoekFilter getIntakegesprekZoekFilter()
	{
		return intakegesprekZoekFilter;
	}

	public void setIntakegesprekZoekFilter(IntakegesprekZoekFilter intakegesprekZoekFilter)
	{
		this.intakegesprekZoekFilter = intakegesprekZoekFilter;
	}

	public OnderwijsproductZoekFilter getOnderwijsproductZoekFilter()
	{
		return onderwijsproductZoekFilter;
	}

	public void setOnderwijsproductZoekFilter(OnderwijsproductZoekFilter onderwijsproductZoekFilter)
	{
		this.onderwijsproductZoekFilter = onderwijsproductZoekFilter;
	}

	public OpleidingZoekFilter getOpleidingZoekFilter()
	{
		return opleidingZoekFilter;
	}

	public void setOpleidingZoekFilter(OpleidingZoekFilter opleidingZoekFilter)
	{
		this.opleidingZoekFilter = opleidingZoekFilter;
	}

	public VooropleidingZoekFilter getVooropleidingZoekFilter()
	{
		return vooropleidingZoekFilter;
	}

	public void setVooropleidingZoekFilter(VooropleidingZoekFilter vooropleidingZoekFilter)
	{
		this.vooropleidingZoekFilter = vooropleidingZoekFilter;
	}

	public UitschrijvingZoekFilter getUitschrijvingZoekFilter()
	{
		return uitschrijvingZoekFilter;
	}

	public void setUitschrijvingZoekFilter(UitschrijvingZoekFilter uitschrijvingZoekFilter)
	{
		this.uitschrijvingZoekFilter = uitschrijvingZoekFilter;
	}

	public BPVInschrijvingZoekFilter getBpvInschrijvingZoekFilter()
	{
		if (bpvInschrijvingZoekFilter == null)
			setBpvInschrijvingZoekFilter(new BPVInschrijvingZoekFilter());
		return bpvInschrijvingZoekFilter;
	}

	public void setBpvInschrijvingZoekFilter(BPVInschrijvingZoekFilter bpvInschrijvingZoekFilter)
	{
		this.bpvInschrijvingZoekFilter = bpvInschrijvingZoekFilter;
	}

	public String getAchternaam()
	{
		return deelnemerZoekFilter.getAchternaam();
	}

	public String getRelatieAchternaam()
	{
		return relatieZoekFilter.getAchternaam();
	}

	public String getAanspreeknaam()
	{
		return deelnemerZoekFilter.getAanspreeknaam();
	}

	public String getOfficieelofaanspreek()
	{
		return deelnemerZoekFilter.getOfficieelofaanspreek();
	}

	public void setOfficieelofaanspreek(String officieelofaanspreek)
	{
		deelnemerZoekFilter.setOfficieelofaanspreek(officieelofaanspreek);
	}

	public Integer getDeelnemernummer()
	{
		return deelnemerZoekFilter.getDeelnemernummer();
	}

	public Integer getDeelnemernummerTotEnMet()
	{
		return deelnemerZoekFilter.getDeelnemernummerTotEnMet();
	}

	public Integer getDeelnemernummerVanaf()
	{
		return deelnemerZoekFilter.getDeelnemernummerVanaf();
	}

	public Date getGeboortedatumTotEnMet()
	{
		return deelnemerZoekFilter.getGeboortedatumTotEnMet();
	}

	public Date getGeboortedatumVanaf()
	{
		return deelnemerZoekFilter.getGeboortedatumVanaf();
	}

	public Date getGeboortedatum()
	{
		return deelnemerZoekFilter.getGeboortedatum();
	}

	public void setGeboortedatum(Date geboortedatum)
	{
		deelnemerZoekFilter.setGeboortedatum(geboortedatum);
	}

	public Gemeente getGeboorteGemeente()
	{
		return deelnemerZoekFilter.getGeboorteGemeente();
	}

	public Land getGeboorteland()
	{
		return deelnemerZoekFilter.getGeboorteland();
	}

	public Geslacht getGeslacht()
	{
		return deelnemerZoekFilter.getGeslacht();
	}

	public Geslacht getRelatieGeslacht()
	{
		return relatieZoekFilter.getGeslacht();
	}

	@AutoForm(htmlClasses = "unit_80")
	public Groep getGroep()
	{
		return deelnemerZoekFilter.getGroep();
	}

	public Long getOnderwijsnummer()
	{
		return deelnemerZoekFilter.getOnderwijsnummer();
	}

	public Long getRelatieDebiteurenNummer()
	{
		return relatieZoekFilter.getDebiteurennummer();
	}

	public void setRelatieDebiteurenNummer(Long debiteurennummer)
	{
		this.relatieZoekFilter.setDebiteurennummer(debiteurennummer);
	}

	public String getRoepnaam()
	{
		return deelnemerZoekFilter.getRoepnaam();
	}

	public void setAchternaam(String achternaam)
	{
		deelnemerZoekFilter.setAchternaam(achternaam);
	}

	public void setRelatieAchternaam(String achternaam)
	{
		relatieZoekFilter.setAchternaam(achternaam);
	}

	public void setAanspreeknaam(String aanspreeknaam)
	{
		deelnemerZoekFilter.setAanspreeknaam(aanspreeknaam);
	}

	public void setDeelnemernummer(Integer deelnemernummer)
	{
		deelnemerZoekFilter.setDeelnemernummer(deelnemernummer);
	}

	public void setDeelnemernummerTotEnMet(Integer deelnemernummerTotEnMet)
	{
		deelnemerZoekFilter.setDeelnemernummerTotEnMet(deelnemernummerTotEnMet);
	}

	public void setDeelnemernummerVanaf(Integer deelnemernummerVanaf)
	{
		deelnemerZoekFilter.setDeelnemernummerVanaf(deelnemernummerVanaf);
	}

	public Long getDebiteurennummer()
	{
		return deelnemerZoekFilter.getDebiteurennummer();
	}

	public void setDebiteurennummer(Long debiteurennummer)
	{
		deelnemerZoekFilter.setDebiteurennummer(debiteurennummer);
	}

	public void setGeboortedatumTotEnMet(Date geboortedatumTotEnMet)
	{
		deelnemerZoekFilter.setGeboortedatumTotEnMet(geboortedatumTotEnMet);
	}

	public void setGeboortedatumVanaf(Date geboortedatumVanaf)
	{
		deelnemerZoekFilter.setGeboortedatumVanaf(geboortedatumVanaf);
	}

	public void setGeboorteGemeente(Gemeente geboorteGemeente)
	{
		deelnemerZoekFilter.setGeboorteGemeente(geboorteGemeente);
	}

	public void setGeboorteland(Land geboorteLand)
	{
		deelnemerZoekFilter.setGeboorteland(geboorteLand);
	}

	public void setGeslacht(Geslacht geslacht)
	{
		deelnemerZoekFilter.setGeslacht(geslacht);
	}

	public void setRelatieGeslacht(Geslacht geslacht)
	{
		relatieZoekFilter.setGeslacht(geslacht);
	}

	public void setGroep(Groep groep)
	{
		deelnemerZoekFilter.setGroep(groep);
	}

	/**
	 * @see DeelnemerZoekFilter#setGroepsdeelnameBeeindigd(Boolean)
	 */
	public void setGroepsdeelnameBeeindigd(Boolean groepsdeelnameBeeindigd)
	{
		deelnemerZoekFilter.setGroepsdeelnameBeeindigd(groepsdeelnameBeeindigd);
	}

	/**
	 * @see DeelnemerZoekFilter#getGroepsdeelnameBeeindigd()
	 */
	public Boolean getGroepsdeelnameBeeindigd()
	{
		return deelnemerZoekFilter.getGroepsdeelnameBeeindigd();
	}

	public void setDatumInNL(Date datumInNL)
	{
		deelnemerZoekFilter.setDatumInNL(datumInNL);
	}

	public Date getDatumInNL()
	{
		return deelnemerZoekFilter.getDatumInNL();
	}

	public String getOpleidingsCode()
	{
		return opleidingscode;
	}

	public void setOpleidingsCode(String opleidingscode)
	{
		this.opleidingscode = opleidingscode;
	}

	public void setOnderwijsnummer(Long onderwijsnummer)
	{
		deelnemerZoekFilter.setOnderwijsnummer(onderwijsnummer);
	}

	public void setRoepnaam(String roepnaam)
	{
		deelnemerZoekFilter.setRoepnaam(roepnaam);
	}

	public String getVolledigeNaam()
	{
		return deelnemerZoekFilter.getVolledigeNaam();
	}

	public void setVolledigeNaam(String volledigeNaam)
	{
		deelnemerZoekFilter.setVolledigeNaam(volledigeNaam);
	}

	public Date getDatumInschrijvingVanaf()
	{
		return datumInschrijvingVanaf;
	}

	public void setDatumInschrijvingVanaf(Date datumInschrijvingVanaf)
	{
		this.datumInschrijvingVanaf = datumInschrijvingVanaf;
	}

	public Date getDatumInschrijvingTotEnMet()
	{
		return datumInschrijvingTotEnMet;
	}

	public void setDatumInschrijvingTotEnMet(Date datumInschrijvingTotEnMet)
	{
		this.datumInschrijvingTotEnMet = datumInschrijvingTotEnMet;
	}

	public Date getDatumUitschrijvingVanaf()
	{
		return datumUitschrijvingVanaf;
	}

	public void setDatumUitschrijvingVanaf(Date datumUitschrijvingVanaf)
	{
		this.datumUitschrijvingVanaf = datumUitschrijvingVanaf;
	}

	public Date getDatumUitschrijvingTotEnMet()
	{
		return datumUitschrijvingTotEnMet;
	}

	public void setDatumUitschrijvingTotEnMet(Date datumUitschrijvingTotEnMet)
	{
		this.datumUitschrijvingTotEnMet = datumUitschrijvingTotEnMet;
	}

	public Date getDatumGeplandEindeVanaf()
	{
		return datumGeplandEindeVanaf;
	}

	public void setDatumGeplandEindeVanaf(Date datumGeplandEindeVanaf)
	{
		this.datumGeplandEindeVanaf = datumGeplandEindeVanaf;
	}

	public Date getDatumGeplandEindeTotEnMet()
	{
		return datumGeplandEindeTotEnMet;
	}

	public void setDatumGeplandEindeTotEnMet(Date datumGeplandEindeTotEnMet)
	{
		this.datumGeplandEindeTotEnMet = datumGeplandEindeTotEnMet;
	}

	public Gemeente getGemeente()
	{
		return deelnemerZoekFilter.getGemeente();
	}

	public Gemeente getGemeenteOngelijkAan()
	{
		return deelnemerZoekFilter.getGemeenteOngelijkAan();
	}

	public String getHuisnummer()
	{
		return deelnemerZoekFilter.getHuisnummer();
	}

	public Land getLand()
	{
		return deelnemerZoekFilter.getLand();
	}

	public Land getLandOngelijkAan()
	{
		return deelnemerZoekFilter.getLandOngelijkAan();
	}

	public String getPlaats()
	{
		return deelnemerZoekFilter.getPlaats();
	}

	public String getPlaatsOngelijkAan()
	{
		return deelnemerZoekFilter.getPlaatsOngelijkAan();
	}

	public String getPostcode()
	{
		return deelnemerZoekFilter.getPostcode();
	}

	public String getPostcodeTotEnMet()
	{
		return deelnemerZoekFilter.getPostcodeTotEnMet();
	}

	public String getPostcodeVanaf()
	{
		return deelnemerZoekFilter.getPostcodeVanaf();
	}

	public Provincie getProvincie()
	{
		return deelnemerZoekFilter.getProvincie();
	}

	public Provincie getProvincieOngelijkAan()
	{
		return deelnemerZoekFilter.getProvincieOngelijkAan();
	}

	public String getStraat()
	{
		return deelnemerZoekFilter.getStraat();
	}

	public void setGemeente(Gemeente gemeente)
	{
		deelnemerZoekFilter.setGemeente(gemeente);
	}

	public void setGemeenteOngelijkAan(Gemeente gemeenteOngelijkAan)
	{
		deelnemerZoekFilter.setGemeenteOngelijkAan(gemeenteOngelijkAan);
	}

	public void setHuisnummer(String huisnummer)
	{
		deelnemerZoekFilter.setHuisnummer(huisnummer);
	}

	public void setLand(Land land)
	{
		deelnemerZoekFilter.setLand(land);
	}

	public void setLandOngelijkAan(Land landOngelijkAan)
	{
		deelnemerZoekFilter.setLandOngelijkAan(landOngelijkAan);
	}

	public void setPlaats(String plaats)
	{
		deelnemerZoekFilter.setPlaats(plaats);
	}

	public void setPlaatsOngelijkAan(String plaatsOngelijkAan)
	{
		deelnemerZoekFilter.setPlaatsOngelijkAan(plaatsOngelijkAan);
	}

	public void setPostcode(String postcode)
	{
		deelnemerZoekFilter.setPostcode(postcode);
	}

	public void setPostcodeTotEnMet(String postcodeTotEnMet)
	{
		deelnemerZoekFilter.setPostcodeTotEnMet(postcodeTotEnMet);
	}

	public void setPostcodeVanaf(String postcodeVanaf)
	{
		deelnemerZoekFilter.setPostcodeVanaf(postcodeVanaf);
	}

	public void setProvincie(Provincie provincie)
	{
		deelnemerZoekFilter.setProvincie(provincie);
	}

	public void setProvincieOngelijkAan(Provincie provincieOngelijkAan)
	{
		deelnemerZoekFilter.setProvincieOngelijkAan(provincieOngelijkAan);
	}

	public void setStraat(String straat)
	{
		deelnemerZoekFilter.setStraat(straat);
	}

	@AutoForm(editorClass = TextField.class, label = "BSN")
	public Long getBsn()
	{
		return deelnemerZoekFilter.getBsn();
	}

	public Long getRelatieBsn()
	{
		return relatieZoekFilter.getBsn();
	}

	public void setBsn(Long bsn)
	{
		deelnemerZoekFilter.setBsn(bsn);
	}

	public void setRelatieBsn(Long bsn)
	{
		relatieZoekFilter.setBsn(bsn);
	}

	public Verbintenisgebied getVerbintenisgebied()
	{
		return getModelObject(verbintenisgebied);
	}

	public void setVerbintenisgebied(Verbintenisgebied verbintenisgebied)
	{
		this.verbintenisgebied = makeModelFor(verbintenisgebied);
	}

	public Verbintenisgebied getVerbintenisgebiedOngelijkAan()
	{
		return getModelObject(verbintenisgebiedOngelijkAan);
	}

	public void setVerbintenisgebiedOngelijkAan(Verbintenisgebied verbintenisgebiedOngelijkAan)
	{
		this.verbintenisgebiedOngelijkAan = makeModelFor(verbintenisgebiedOngelijkAan);
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Opleiding getOpleidingOngelijkAan()
	{
		return getModelObject(opleidingOngelijkAan);
	}

	public void setOpleidingOngelijkAan(Opleiding opleidingOngelijkAan)
	{
		this.opleidingOngelijkAan = makeModelFor(opleidingOngelijkAan);
	}

	public Groep getBasisgroep()
	{
		return getModelObject(basisgroep);
	}

	public void setBasisgroep(Groep basisgroep)
	{
		this.basisgroep = makeModelFor(basisgroep);
	}

	public Integer getLeerjaar()
	{
		return leerjaar;
	}

	public void setLeerjaar(Integer leerjaar)
	{
		this.leerjaar = leerjaar;
	}

	public Cohort getCohort()
	{
		return getModelObject(cohort);
	}

	public void setCohort(Cohort cohort)
	{
		if (this.cohort == null)
			this.cohort = makeModelFor(cohort);
		else
			this.cohort.setObject(cohort);
	}

	public void setCohortModel(IModel<Cohort> cohortModel)
	{
		this.cohort = cohortModel;
	}

	public ToegestaneExamenstatusOvergang getToegestaneExamenstatusOvergang()
	{
		return getModelObject(toegestaneExamenstatusOvergang);
	}

	public void setToegestaneExamenstatusOvergang(
			ToegestaneExamenstatusOvergang toegestaneExamenstatusOvergang)
	{
		this.toegestaneExamenstatusOvergang = makeModelFor(toegestaneExamenstatusOvergang);
	}

	public Integer getTijdvak()
	{
		return tijdvak;
	}

	public void setTijdvak(Integer tijdvak)
	{
		this.tijdvak = tijdvak;
	}

	public Groepstype getGroepstype()
	{
		return deelnemerZoekFilter.getGroepstype();
	}

	public void setGroepstype(Groepstype groepstype)
	{
		deelnemerZoekFilter.setGroepstype(groepstype);
	}

	public SoortContract getSoortContract()
	{
		return getModelObject(soortContract);
	}

	public void setSoortContract(SoortContract soortContract)
	{
		this.soortContract = makeModelFor(soortContract);
	}

	public TypeFinanciering getTypeFinanciering()
	{
		return getModelObject(typeFinanciering);
	}

	public void setTypeFinanciering(TypeFinanciering typeFinanciering)
	{
		this.typeFinanciering = makeModelFor(typeFinanciering);
	}

	public Contract getContract()
	{
		return getModelObject(contract);
	}

	public void setContract(Contract contract)
	{
		this.contract = makeModelFor(contract);
	}

	public ContractOnderdeel getContractOnderdeel()
	{
		return getModelObject(contractOnderdeel);
	}

	public void setContractOnderdeel(ContractOnderdeel contractOnderdeel)
	{
		this.contractOnderdeel = makeModelFor(contractOnderdeel);
	}

	public void setVerbintenisStatus(VerbintenisStatus verbintenisStatus)
	{
		this.verbintenisStatus = makeModelFor(verbintenisStatus);
	}

	public VerbintenisStatus getVerbintenisStatus()
	{
		return getModelObject(verbintenisStatus);
	}

	public Intensiteit getIntensiteit()
	{
		return getModelObject(intensiteit);
	}

	public void setIntensiteit(Intensiteit intensiteit)
	{
		this.intensiteit = makeModelFor(intensiteit);
	}

	public List<BPVStatus> getBpvStatusOngelijkAan()
	{
		return bpvStatusOngelijkAan;
	}

	public void setBpvStatusOngelijkAan(List<BPVStatus> bpvStatusOngelijkAan)
	{
		this.bpvStatusOngelijkAan = bpvStatusOngelijkAan;
	}

	public void setVerbintenisStatusOngelijkAan(List<VerbintenisStatus> verbintenisStatusOngelijkAan)
	{
		this.verbintenisStatusOngelijkAan = verbintenisStatusOngelijkAan;
	}

	public List<VerbintenisStatus> getVerbintenisStatusOngelijkAan()
	{
		return verbintenisStatusOngelijkAan;
	}

	public Boolean getStatusIntake()
	{
		return statusIntake;
	}

	public void setStatusIntake(Boolean statusIntake)
	{
		this.statusIntake = statusIntake;
	}

	@Override
	public void setCustomPeildatumModel(IModel<Date> peildatumModel)
	{
		super.setCustomPeildatumModel(peildatumModel);
		deelnemerZoekFilter.setCustomPeildatumModel(peildatumModel);
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = makeModelFor(verbintenis);
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject(verbintenis);
	}

	@Override
	public boolean isResultCacheable()
	{
		return deelnemerZoekFilter.isResultCacheable() && super.isResultCacheable();
	}

	public Boolean getAllochtoon()
	{
		return deelnemerZoekFilter.getAllochtoon();
	}

	public Boolean getLgf()
	{
		return deelnemerZoekFilter.getLgf();
	}

	public Boolean getNieuwkomer()
	{
		return deelnemerZoekFilter.getNieuwkomer();
	}

	public void setAllochtoon(Boolean allochtoon)
	{
		deelnemerZoekFilter.setAllochtoon(allochtoon);
	}

	public void setLgf(Boolean lgf)
	{
		deelnemerZoekFilter.setLgf(lgf);
	}

	public Boolean getIndicatieGehandicapt()
	{
		return indicatieGehandicapt;
	}

	public void setIndicatieGehandicapt(Boolean indicatieGehandicapt)
	{
		this.indicatieGehandicapt = indicatieGehandicapt;
	}

	public Boolean getIndicatieLWOO()
	{
		return indicatieLWOO;
	}

	public void setIndicatieLWOO(Boolean indicatieLWOO)
	{
		this.indicatieLWOO = indicatieLWOO;
	}

	public void setNieuwkomer(Boolean nieuwkomer)
	{
		deelnemerZoekFilter.setNieuwkomer(nieuwkomer);
	}

	public void setMeerderjarig(Boolean meerderjarig)
	{
		deelnemerZoekFilter.setMeerderjarig(meerderjarig);
	}

	public Boolean isMeerderjarig()
	{
		return deelnemerZoekFilter.isMeerderjarig();
	}

	public MBOLeerweg getLeerweg()
	{
		return getModelObject(leerweg);
	}

	public void setLeerweg(MBOLeerweg leerweg)
	{
		this.leerweg = makeModelFor(leerweg);
	}

	public Boolean isVertegenwoordiger()
	{
		return deelnemerZoekFilter.isVertegenwoordiger();
	}

	public void setVertegenwoordiger(Boolean vertegenwoordiger)
	{
		deelnemerZoekFilter.setVertegenwoordiger(vertegenwoordiger);
	}

	public Boolean isBetalingsplichtige()
	{
		return deelnemerZoekFilter.isBetalingsplichtige();
	}

	public void setBetalingsplichtige(Boolean betalingsplichtige)
	{
		deelnemerZoekFilter.setBetalingsplichtige(betalingsplichtige);
	}

	public RelatieZoekFilter getRelatieZoekFilter()
	{
		return relatieZoekFilter;
	}

	public void setRelatieFilter(RelatieZoekFilter relatieFilter)
	{
		this.relatieZoekFilter = relatieFilter;
	}

	public void setRelatiePostcode(String postcode)
	{
		this.relatieZoekFilter.setPostcode(postcode);
	}

	public String getRelatiePostcode()
	{
		return relatieZoekFilter.getPostcode();
	}

	public void setRelatieHuisnummer(String huisnummer)
	{
		this.relatieZoekFilter.setHuisnummer(huisnummer);
	}

	public String getRelatieHuisnummer()
	{
		return relatieZoekFilter.getHuisnummer();
	}

	public String getRelatieStraat()
	{
		return relatieZoekFilter.getStraat();
	}

	public void setRelatieStraat(String straat)
	{
		this.relatieZoekFilter.setStraat(straat);
	}

	public RelatieSoort getRelatieSoort()
	{
		return relatieZoekFilter.getRelatieSoort();
	}

	public void setRelatieSoort(RelatieSoort relatieSoort)
	{
		this.relatieZoekFilter.setRelatieSoort(relatieSoort);
	}

	public void setRelatieExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.relatieZoekFilter.setExterneOrganisatie(externeOrganisatie);
	}

	public ExterneOrganisatie getRelatieExterneOrganisatie()
	{
		return relatieZoekFilter.getExterneOrganisatie();
	}

	public String getRelatieHuisnummerToevoeging()
	{
		return relatieZoekFilter.getHuisnummerToevoeging();
	}

	public void setRelatieHuisnummerToevoeging(String huisnummerToevoeging)
	{
		relatieZoekFilter.setHuisnummerToevoeging(huisnummerToevoeging);
	}

	public String getRelatiePlaats()
	{
		return relatieZoekFilter.getPlaats();
	}

	public void setRelatiePlaats(String plaats)
	{
		relatieZoekFilter.setPlaats(plaats);
	}

	public TypeAdres getRelatieTypeAdres()
	{
		return relatieZoekFilter.getTypeAdres();
	}

	public void setRelatieTypeAdres(TypeAdres typeAdres)
	{
		relatieZoekFilter.setTypeAdres(typeAdres);
	}

	public Boolean getRelatieZelfdeAdresAlsDeelnemer()
	{
		return relatieZoekFilter.getZelfdeAdresAlsDeelnemer();
	}

	public void setRelatieZelfdeAdresAlsDeelnemer(Boolean zelfdeAdresAlsDeelnemer)
	{
		relatieZoekFilter.setZelfdeAdresAlsDeelnemer(zelfdeAdresAlsDeelnemer);
	}

	public Date getBpvBeginDatum()
	{
		return bpvBeginDatum;
	}

	public void setBpvBeginDatum(Date bpvBeginDatum)
	{
		this.bpvBeginDatum = bpvBeginDatum;
	}

	public Date getBpvEindDatum()
	{
		return bpvEindDatum;
	}

	public void setBpvEindDatum(Date bpvEindDatum)
	{
		this.bpvEindDatum = bpvEindDatum;
	}

	public ExterneOrganisatie getBpvBedrijf()
	{
		return getModelObject(bpvBedrijf);
	}

	public void setBpvBedrijf(ExterneOrganisatie bpvBedrijf)
	{
		this.bpvBedrijf = makeModelFor(bpvBedrijf);
	}

	public Medewerker getPraktijkBegeleider()
	{
		return getModelObject(praktijkBegeleider);
	}

	public void setPraktijkBegeleider(Medewerker praktijkBegeleider)
	{
		this.praktijkBegeleider = makeModelFor(praktijkBegeleider);
	}

	@Override
	public Medewerker getDocent()
	{
		return deelnemerZoekFilter.getDocent();
	}

	@Override
	public void setDocent(Medewerker docent)
	{
		deelnemerZoekFilter.setDocent(docent);
	}

	@Override
	public Medewerker getMentor()
	{
		return deelnemerZoekFilter.getMentor();
	}

	@Override
	public void setMentor(Medewerker mentor)
	{
		deelnemerZoekFilter.setMentor(mentor);
	}

	@Override
	public Medewerker getMentorOfDocent()
	{
		return deelnemerZoekFilter.getMentorOfDocent();
	}

	@Override
	public void setMentorOfDocent(Medewerker mentorOfDocent)
	{
		deelnemerZoekFilter.setMentorOfDocent(mentorOfDocent);
	}

	@Override
	public Medewerker getVerantwoordelijke()
	{
		return deelnemerZoekFilter.getVerantwoordelijke();
	}

	@Override
	public void setVerantwoordelijke(Medewerker verantwoordelijke)
	{
		deelnemerZoekFilter.setVerantwoordelijke(verantwoordelijke);
	}

	@Override
	public Medewerker getUitvoerende()
	{
		return deelnemerZoekFilter.getUitvoerende();
	}

	@Override
	public void setUitvoerende(Medewerker uitvoerende)
	{
		deelnemerZoekFilter.setUitvoerende(uitvoerende);
	}

	@Override
	public Medewerker getVerantwoordelijkeOfUitvoerende()
	{
		return deelnemerZoekFilter.getVerantwoordelijkeOfUitvoerende();
	}

	@Override
	public void setVerantwoordelijkeOfUitvoerende(Medewerker verantwoordelijkeOfUitvoerende)
	{
		deelnemerZoekFilter.setVerantwoordelijkeOfUitvoerende(verantwoordelijkeOfUitvoerende);
	}

	public boolean isMentorOfDocentRequired()
	{
		return deelnemerZoekFilter.isMentorOfDocentRequired();
	}

	public void setMentorOfDocentRequired(boolean mentorOfDocentRequired)
	{
		deelnemerZoekFilter.setMentorOfDocentRequired(mentorOfDocentRequired);
	}

	public boolean isOpleidingEnCohortVerplicht()
	{
		return opleidingEnCohortVerplicht;
	}

	public void setOpleidingEnCohortVerplicht(boolean opleidingEnCohortVerplicht)
	{
		this.opleidingEnCohortVerplicht = opleidingEnCohortVerplicht;
	}

	public Date getPeilEindDatum()
	{
		return peilEindDatum;
	}

	public void setPeilEindDatum(Date peilEindDatum)
	{
		this.peilEindDatum = peilEindDatum;
		if (deelnemerZoekFilter != null)
			deelnemerZoekFilter.setPeilEindDatum(peilEindDatum);
	}

	public RedenUitschrijving getRedenUitschrijving()
	{
		return getModelObject(redenUitschrijving);
	}

	public void setRedenUitschrijving(RedenUitschrijving redenUitschrijving)
	{
		this.redenUitschrijving = makeModelFor(redenUitschrijving);
	}

	public void setExamenstatus(Examenstatus examenstatus)
	{
		this.examenstatus = makeModelFor(examenstatus);
	}

	public Examenstatus getExamenstatus()
	{
		return getModelObject(examenstatus);
	}

	public Taxonomie getTaxonomie()
	{
		return getModelObject(taxonomie);
	}

	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomie = makeModelFor(taxonomie);
	}

	public TaxonomieElementType getTaxonomieElementType()
	{
		return getModelObject(taxonomieElementType);
	}

	public void setTaxonomieElementType(TaxonomieElementType taxonomieElementType)
	{
		this.taxonomieElementType = makeModelFor(taxonomieElementType);
	}

	public Kenmerk getKenmerk()
	{
		return deelnemerZoekFilter.getKenmerk();
	}

	public KenmerkCategorie getKenmerkCategorie()
	{
		return deelnemerZoekFilter.getKenmerkCategorie();
	}

	public void setKenmerk(Kenmerk kenmerk)
	{
		deelnemerZoekFilter.setKenmerk(kenmerk);
	}

	public void setKenmerkCategorie(KenmerkCategorie kenmerkCategorie)
	{
		deelnemerZoekFilter.setKenmerkCategorie(kenmerkCategorie);
	}

	public Boolean getBetalingsplichtige()
	{
		return deelnemerZoekFilter.getBetalingsplichtige();
	}

	public Boolean getBsnIsLeeg()
	{
		return deelnemerZoekFilter.getBsnIsLeeg();
	}

	public Boolean getDatumInNLIsLeeg()
	{
		return deelnemerZoekFilter.getDatumInNLIsLeeg();
	}

	public Date getDatumInNLTotEnMet()
	{
		return deelnemerZoekFilter.getDatumInNLTotEnMet();
	}

	public Date getDatumInNLVanaf()
	{
		return deelnemerZoekFilter.getDatumInNLVanaf();
	}

	public Boolean getDubbeleNationaliteit()
	{
		return deelnemerZoekFilter.getDubbeleNationaliteit();
	}

	public Boolean getGeboortedatumIsLeeg()
	{
		return deelnemerZoekFilter.getGeboortedatumIsLeeg();
	}

	public Boolean getGeboortelandIsLeeg()
	{
		return deelnemerZoekFilter.getGeboortelandIsLeeg();
	}

	public Boolean getGeboortelandOngelijkAanNL()
	{
		return deelnemerZoekFilter.getGeboortelandOngelijkAanNL();
	}

	public Boolean getHeeftMeerdereVerbintenissen()
	{
		return deelnemerZoekFilter.getHeeftMeerdereVerbintenissen();
	}

	public Nationaliteit getNationaliteit()
	{
		return deelnemerZoekFilter.getNationaliteit();
	}

	public Boolean getNationaliteitIsLeeg()
	{
		return deelnemerZoekFilter.getNationaliteitIsLeeg();
	}

	public Boolean getNationaliteitOngelijkAanNL()
	{
		return deelnemerZoekFilter.getNationaliteitOngelijkAanNL();
	}

	public Boolean getOfficieleNaamWijktAf()
	{
		return deelnemerZoekFilter.getOfficieleNaamWijktAf();
	}

	public String getOfficieleVoorvoegsel()
	{
		return deelnemerZoekFilter.getOfficieleVoorvoegsel();
	}

	public Boolean getOnderwijsnummerIsLeeg()
	{
		return deelnemerZoekFilter.getOnderwijsnummerIsLeeg();
	}

	public Boolean getOverleden()
	{
		return deelnemerZoekFilter.getOverleden();
	}

	public Boolean getRoepnaamIsLeeg()
	{
		return deelnemerZoekFilter.getRoepnaamIsLeeg();
	}

	public String getVoorletters()
	{
		return deelnemerZoekFilter.getVoorletters();
	}

	public Boolean getVoorlettersIsLeeg()
	{
		return deelnemerZoekFilter.getVoorlettersIsLeeg();
	}

	public String getVoornamen()
	{
		return deelnemerZoekFilter.getVoornamen();
	}

	public Boolean getVoornamenIsLeeg()
	{
		return deelnemerZoekFilter.getVoornamenIsLeeg();
	}

	public String getVoorvoegsel()
	{
		return deelnemerZoekFilter.getVoorvoegsel();
	}

	public void setBsnIsLeeg(Boolean bsnIsLeeg)
	{
		deelnemerZoekFilter.setBsnIsLeeg(bsnIsLeeg);
	}

	public void setDatumInNLIsLeeg(Boolean datumInNLIsLeeg)
	{
		deelnemerZoekFilter.setDatumInNLIsLeeg(datumInNLIsLeeg);
	}

	public void setDatumInNLTotEnMet(Date datumInNLTotEnMet)
	{
		deelnemerZoekFilter.setDatumInNLTotEnMet(datumInNLTotEnMet);
	}

	public void setDatumInNLVanaf(Date datumInNLVanaf)
	{
		deelnemerZoekFilter.setDatumInNLVanaf(datumInNLVanaf);
	}

	public void setDubbeleNationaliteit(Boolean dubbeleNationaliteit)
	{
		deelnemerZoekFilter.setDubbeleNationaliteit(dubbeleNationaliteit);
	}

	public void setGeboortedatumIsLeeg(Boolean geboortedatumIsLeeg)
	{
		deelnemerZoekFilter.setGeboortedatumIsLeeg(geboortedatumIsLeeg);
	}

	public void setGeboortelandIsLeeg(Boolean geboortelandIsLeeg)
	{
		deelnemerZoekFilter.setGeboortelandIsLeeg(geboortelandIsLeeg);
	}

	public void setGeboortelandOngelijkAanNL(Boolean geboortelandOngelijkAanNL)
	{
		deelnemerZoekFilter.setGeboortelandOngelijkAanNL(geboortelandOngelijkAanNL);
	}

	public void setNationaliteit(Nationaliteit nationaliteit)
	{
		deelnemerZoekFilter.setNationaliteit(nationaliteit);
	}

	public void setNationaliteitIsLeeg(Boolean nationaliteitIsLeeg)
	{
		deelnemerZoekFilter.setNationaliteitIsLeeg(nationaliteitIsLeeg);
	}

	public void setNationaliteitOngelijkAanNL(Boolean nationaliteitOngelijkAanNL)
	{
		deelnemerZoekFilter.setNationaliteitOngelijkAanNL(nationaliteitOngelijkAanNL);
	}

	public void setOfficieleNaamWijktAf(Boolean officieleNaamWijktAf)
	{
		deelnemerZoekFilter.setOfficieleNaamWijktAf(officieleNaamWijktAf);
	}

	public void setOfficieleVoorvoegsel(String officieleVoorvoegsel)
	{
		deelnemerZoekFilter.setOfficieleVoorvoegsel(officieleVoorvoegsel);
	}

	public void setOnderwijsnummerIsLeeg(Boolean onderwijsnummerIsLeeg)
	{
		deelnemerZoekFilter.setOnderwijsnummerIsLeeg(onderwijsnummerIsLeeg);
	}

	public void setOverleden(Boolean overleden)
	{
		deelnemerZoekFilter.setOverleden(overleden);
	}

	public void setRoepnaamIsLeeg(Boolean roepnaamIsLeeg)
	{
		deelnemerZoekFilter.setRoepnaamIsLeeg(roepnaamIsLeeg);
	}

	public void setVoorletters(String voorletters)
	{
		deelnemerZoekFilter.setVoorletters(voorletters);
	}

	public void setVoorlettersIsLeeg(Boolean voorlettersIsLeeg)
	{
		deelnemerZoekFilter.setVoorlettersIsLeeg(voorlettersIsLeeg);
	}

	public void setVoornamen(String voornamen)
	{
		deelnemerZoekFilter.setVoornamen(voornamen);
	}

	public void setVoornamenIsLeeg(Boolean voornamenIsLeeg)
	{
		deelnemerZoekFilter.setVoornamenIsLeeg(voornamenIsLeeg);
	}

	public void setVoorvoegsel(String voorvoegsel)
	{
		deelnemerZoekFilter.setVoorvoegsel(voorvoegsel);
	}

	public Boolean getGeheimAdres()
	{
		return deelnemerZoekFilter.getGeheimAdres();
	}

	public String getHuisnummerToevoeging()
	{
		return deelnemerZoekFilter.getHuisnummerToevoeging();
	}

	public TypeAdres getTypeAdres()
	{
		return deelnemerZoekFilter.getTypeAdres();
	}

	public void setGeheimAdres(Boolean geheimAdres)
	{
		deelnemerZoekFilter.setGeheimAdres(geheimAdres);
	}

	public void setHuisnummerToevoeging(String huisnummerToevoeging)
	{
		deelnemerZoekFilter.setHuisnummerToevoeging(huisnummerToevoeging);
	}

	public void setTypeAdres(TypeAdres typeAdres)
	{
		deelnemerZoekFilter.setTypeAdres(typeAdres);
	}

	public IntakegesprekStatus getIntakegesprekStatus()
	{
		return intakegesprekStatus;
	}

	public void setIntakegesprekStatus(IntakegesprekStatus intakegesprekStatus)
	{
		this.intakegesprekStatus = intakegesprekStatus;
	}

	public Date getDatumIntakegesprekVanaf()
	{
		return datumIntakegesprekVanaf;
	}

	public void setDatumIntakegesprekVanaf(Date datumIntakegesprekVanaf)
	{
		this.datumIntakegesprekVanaf = datumIntakegesprekVanaf;
	}

	public Date getDatumIntakegesprekTotEnMet()
	{
		return datumIntakegesprekTotEnMet;
	}

	public void setDatumIntakegesprekTotEnMet(Date datumIntakegesprekTotEnMet)
	{
		this.datumIntakegesprekTotEnMet =
			TimeUtil.getInstance().maakEindeVanDagVanDatum(datumIntakegesprekTotEnMet);
	}

	public Opleiding getGewensteOpleiding()
	{
		return getModelObject(gewensteOpleiding);
	}

	public void setGewensteOpleiding(Opleiding gewensteOpleiding)
	{
		this.gewensteOpleiding = makeModelFor(gewensteOpleiding);
	}

	public Locatie getGewensteLocatie()
	{
		return getModelObject(gewensteLocatie);
	}

	public void setGewensteLocatie(Locatie gewensteLocatie)
	{
		this.gewensteLocatie = makeModelFor(gewensteLocatie);
	}

	public Date getGewensteBegindatumVanaf()
	{
		return gewensteBegindatumVanaf;
	}

	public void setGewensteBegindatumVanaf(Date gewensteBegindatumVanaf)
	{
		this.gewensteBegindatumVanaf = gewensteBegindatumVanaf;
	}

	public Date getGewensteBegindatumTotEnMet()
	{
		return gewensteBegindatumTotEnMet;
	}

	public void setGewensteBegindatumTotEnMet(Date gewensteBegindatumTotEnMet)
	{
		this.gewensteBegindatumTotEnMet = gewensteBegindatumTotEnMet;
	}

	public Date getGewensteEinddatumVanaf()
	{
		return gewensteEinddatumVanaf;
	}

	public void setGewensteEinddatumVanaf(Date gewensteEinddatumVanaf)
	{
		this.gewensteEinddatumVanaf = gewensteEinddatumVanaf;
	}

	public Date getGewensteEinddatumTotEnMet()
	{
		return gewensteEinddatumTotEnMet;
	}

	public void setGewensteEinddatumTotEnMet(Date gewensteEinddatumTotEnMet)
	{
		this.gewensteEinddatumTotEnMet = gewensteEinddatumTotEnMet;
	}

	public Medewerker getIntaker()
	{
		return getModelObject(intaker);
	}

	public void setIntaker(Medewerker intaker)
	{
		this.intaker = makeModelFor(intaker);
	}

	public Brin getBrinVooropleiding()
	{
		return deelnemerZoekFilter.getBrinVooropleiding();
	}

	public ExterneOrganisatie getVooropleidingExterneOrganisatie()
	{
		return deelnemerZoekFilter.getVooropleidingExterneOrganisatie();
	}

	public void setVooropleidingExterneOrganisatie(
			ExterneOrganisatie vooropleidingExterneOrganisatie)
	{
		deelnemerZoekFilter.setVooropleidingExterneOrganisatie(vooropleidingExterneOrganisatie);
	}

	public SoortOnderwijs getCategorieVooropleiding()
	{
		return deelnemerZoekFilter.getCategorieVooropleiding();
	}

	public String getNaamOnderwijsinstellingVooropleiding()
	{
		return deelnemerZoekFilter.getNaamOnderwijsinstellingVooropleiding();
	}

	public SoortVooropleiding getSoortVooropleiding()
	{
		return deelnemerZoekFilter.getSoortVooropleiding();
	}

	public Boolean getVooropleidingAantalJarenOnderwijsIsLeeg()
	{
		return deelnemerZoekFilter.getVooropleidingAantalJarenOnderwijsIsLeeg();
	}

	public Integer getVooropleidingAantalJarenOnderwijsTotEnMet()
	{
		return deelnemerZoekFilter.getVooropleidingAantalJarenOnderwijsTotEnMet();
	}

	public Integer getVooropleidingAantalJarenOnderwijsVanaf()
	{
		return deelnemerZoekFilter.getVooropleidingAantalJarenOnderwijsVanaf();
	}

	public Date getVooropleidingBegindatumTotEnMet()
	{
		return deelnemerZoekFilter.getVooropleidingBegindatumTotEnMet();
	}

	public Date getVooropleidingBegindatumVanaf()
	{
		return deelnemerZoekFilter.getVooropleidingBegindatumVanaf();
	}

	public Boolean getVooropleidingDiplomaBehaald()
	{
		return deelnemerZoekFilter.getVooropleidingDiplomaBehaald();
	}

	public Date getVooropleidingEinddatumTotEnMet()
	{
		return deelnemerZoekFilter.getVooropleidingEinddatumTotEnMet();
	}

	public Date getVooropleidingEinddatumVanaf()
	{
		return deelnemerZoekFilter.getVooropleidingEinddatumVanaf();
	}

	public Schooladvies getVooropleidingSchooladvies()
	{
		return deelnemerZoekFilter.getVooropleidingSchooladvies();
	}

	public Integer getVooropleidingCitoscore()
	{
		return deelnemerZoekFilter.getVooropleidingCitoscore();
	}

	public void setBrinVooropleiding(Brin brinVooropleiding)
	{
		deelnemerZoekFilter.setBrinVooropleiding(brinVooropleiding);
	}

	public void setCategorieVooropleiding(SoortOnderwijs categorieVooropleiding)
	{
		deelnemerZoekFilter.setCategorieVooropleiding(categorieVooropleiding);
	}

	public void setNaamOnderwijsinstellingVooropleiding(String naamOnderwijsinstellingVooropleiding)
	{
		deelnemerZoekFilter
			.setNaamOnderwijsinstellingVooropleiding(naamOnderwijsinstellingVooropleiding);
	}

	public void setSoortVooropleiding(SoortVooropleiding soortVooropleiding)
	{
		deelnemerZoekFilter.setSoortVooropleiding(soortVooropleiding);
	}

	public void setVooropleidingAantalJarenOnderwijsIsLeeg(
			Boolean vooropleidingAantalJarenOnderwijsIsLeeg)
	{
		deelnemerZoekFilter
			.setVooropleidingAantalJarenOnderwijsIsLeeg(vooropleidingAantalJarenOnderwijsIsLeeg);
	}

	public void setVooropleidingAantalJarenOnderwijsTotEnMet(
			Integer vooropleidingAantalJarenOnderwijsTotEnMet)
	{
		deelnemerZoekFilter
			.setVooropleidingAantalJarenOnderwijsTotEnMet(vooropleidingAantalJarenOnderwijsTotEnMet);
	}

	public void setVooropleidingAantalJarenOnderwijsVanaf(
			Integer vooropleidingAantalJarenOnderwijsVanaf)
	{
		deelnemerZoekFilter
			.setVooropleidingAantalJarenOnderwijsVanaf(vooropleidingAantalJarenOnderwijsVanaf);
	}

	public void setVooropleidingBegindatumTotEnMet(Date vooropleidingBegindatumTotEnMet)
	{
		deelnemerZoekFilter.setVooropleidingBegindatumTotEnMet(vooropleidingBegindatumTotEnMet);
	}

	public void setVooropleidingBegindatumVanaf(Date vooropleidingBegindatumVanaf)
	{
		deelnemerZoekFilter.setVooropleidingBegindatumVanaf(vooropleidingBegindatumVanaf);
	}

	public void setVooropleidingDiplomaBehaald(Boolean vooropleidingDiplomaBehaald)
	{
		deelnemerZoekFilter.setVooropleidingDiplomaBehaald(vooropleidingDiplomaBehaald);
	}

	public void setVooropleidingEinddatumTotEnMet(Date vooropleidingEinddatumTotEnMet)
	{
		deelnemerZoekFilter.setVooropleidingEinddatumTotEnMet(vooropleidingEinddatumTotEnMet);
	}

	public void setVooropleidingEinddatumVanaf(Date vooropleidingEinddatumVanaf)
	{
		deelnemerZoekFilter.setVooropleidingEinddatumVanaf(vooropleidingEinddatumVanaf);
	}

	public void setVooropleidingSchooladvies(Schooladvies vooropleidingSchooladvies)
	{
		deelnemerZoekFilter.setVooropleidingSchooladvies(vooropleidingSchooladvies);
	}

	public void setVooropleidingCitoscore(Integer vooropleidingCitoscore)
	{
		deelnemerZoekFilter.setVooropleidingCitoscore(vooropleidingCitoscore);
	}

	public Date getDatumUitslagVanaf()
	{
		return datumUitslagVanaf;
	}

	public void setDatumUitslagVanaf(Date datumUitslagVanaf)
	{
		this.datumUitslagVanaf = datumUitslagVanaf;
	}

	public Date getDatumUitslagTotEnMet()
	{
		return datumUitslagTotEnMet;
	}

	public void setDatumUitslagTotEnMet(Date datumUitslagTotEnMet)
	{
		this.datumUitslagTotEnMet =
			TimeUtil.getInstance().maakEindeVanDagVanDatum(datumUitslagTotEnMet);
	}

	public Boolean getDatumUitslagIsLeeg()
	{
		return datumUitslagIsLeeg;
	}

	public void setDatumUitslagIsLeeg(Boolean datumUitslagIsLeeg)
	{
		this.datumUitslagIsLeeg = datumUitslagIsLeeg;
	}

	public Integer getExamennummerVanaf()
	{
		return examennummerVanaf;
	}

	public void setExamennummerVanaf(Integer examennummerVanaf)
	{
		this.examennummerVanaf = examennummerVanaf;
	}

	public Integer getExamennummerTotEnMet()
	{
		return examennummerTotEnMet;
	}

	public void setExamennummerTotEnMet(Integer examennummerTotEnMet)
	{
		this.examennummerTotEnMet = examennummerTotEnMet;
	}

	public Boolean getExamendeelnameBekostigd()
	{
		return examendeelnameBekostigd;
	}

	public void setExamendeelnameBekostigd(Boolean examendeelnameBekostigd)
	{
		this.examendeelnameBekostigd = examendeelnameBekostigd;
	}

	public Integer getExamendeelnameTijdvakVanaf()
	{
		return examendeelnameTijdvakVanaf;
	}

	public void setExamendeelnameTijdvakVanaf(Integer examendeelnameTijdvakVanaf)
	{
		this.examendeelnameTijdvakVanaf = examendeelnameTijdvakVanaf;
	}

	public Integer getExamendeelnameTijdvakTotEnMet()
	{
		return examendeelnameTijdvakTotEnMet;
	}

	public void setExamendeelnameTijdvakTotEnMet(Integer examendeelnameTijdvakTotEnMet)
	{
		this.examendeelnameTijdvakTotEnMet = examendeelnameTijdvakTotEnMet;
	}

	public Boolean getExamendeelnameTijdvakIsLeeg()
	{
		return examendeelnameTijdvakIsLeeg;
	}

	public void setExamendeelnameTijdvakIsLeeg(Boolean examendeelnameTijdvakIsLeeg)
	{
		this.examendeelnameTijdvakIsLeeg = examendeelnameTijdvakIsLeeg;
	}

	public ExamenWorkflow getExamenWorkflow()
	{
		return getModelObject(examenWorkflow);
	}

	public void setExamenWorkflow(ExamenWorkflow examenWorkflow)
	{
		this.examenWorkflow = makeModelFor(examenWorkflow);
	}

	public ExterneOrganisatie getContractExterneOrganisatie()
	{
		return getModelObject(contractExterneOrganisatie);
	}

	public void setContractExterneOrganisatie(ExterneOrganisatie contractExterneOrganisatie)
	{
		this.contractExterneOrganisatie = makeModelFor(contractExterneOrganisatie);
	}

	public ExterneOrganisatieContactPersoon getContractContactpersoon()
	{
		return getModelObject(contractContactpersoon);
	}

	public void setContractContactpersoon(ExterneOrganisatieContactPersoon contractContactpersoon)
	{
		this.contractContactpersoon = makeModelFor(contractContactpersoon);
	}

	public Medewerker getContractBeheerder()
	{
		return getModelObject(contractBeheerder);
	}

	public void setContractBeheerder(Medewerker contractBeheerder)
	{
		this.contractBeheerder = makeModelFor(contractBeheerder);
	}

	public BigDecimal getContractKostprijsVanaf()
	{
		return contractKostprijsVanaf;
	}

	public void setContractKostprijsVanaf(BigDecimal contractKostprijsVanaf)
	{
		this.contractKostprijsVanaf = contractKostprijsVanaf;
	}

	public BigDecimal getContractKostprijsTotEnMet()
	{
		return contractKostprijsTotEnMet;
	}

	public void setContractKostprijsTotEnMet(BigDecimal contractKostprijsTotEnMet)
	{
		this.contractKostprijsTotEnMet = contractKostprijsTotEnMet;
	}

	public Boolean getContractInburgering()
	{
		return contractInburgering;
	}

	public void setContractInburgering(Boolean contractInburgering)
	{
		this.contractInburgering = contractInburgering;
	}

	public Onderaanneming getContractOnderaanneming()
	{
		return contractOnderaanneming;
	}

	public void setContractOnderaanneming(Onderaanneming contractOnderaanneming)
	{
		this.contractOnderaanneming = contractOnderaanneming;
	}

	public BPVStatus getBpvStatus()
	{
		return bpvStatus;
	}

	public void setBpvStatus(BPVStatus bpvStatus)
	{
		this.bpvStatus = bpvStatus;
	}

	public Brin getBpvKenniscentrum()
	{
		return getModelObject(bpvKenniscentrum);
	}

	public void setBpvKenniscentrum(Brin bpvKenniscentrum)
	{
		this.bpvKenniscentrum = makeModelFor(bpvKenniscentrum);
	}

	public ExterneOrganisatie getBpvContractpartner()
	{
		return getModelObject(bpvContractpartner);
	}

	public void setBpvContractpartner(ExterneOrganisatie bpvContractpartner)
	{
		this.bpvContractpartner = makeModelFor(bpvContractpartner);
	}

	public Integer getBpvOmvangVanaf()
	{
		return bpvOmvangVanaf;
	}

	public void setBpvOmvangVanaf(Integer bpvOmvangVanaf)
	{
		this.bpvOmvangVanaf = bpvOmvangVanaf;
	}

	public Integer getBpvOmvangTotEnMet()
	{
		return bpvOmvangTotEnMet;
	}

	public void setBpvOmvangTotEnMet(Integer bpvOmvangTotEnMet)
	{
		this.bpvOmvangTotEnMet = bpvOmvangTotEnMet;
	}

	public BigDecimal getBpvUrenPerWeekVanaf()
	{
		return bpvUrenPerWeekVanaf;
	}

	public void setBpvUrenPerWeekVanaf(BigDecimal bpvUrenPerWeekVanaf)
	{
		this.bpvUrenPerWeekVanaf = bpvUrenPerWeekVanaf;
	}

	public BigDecimal getBpvUrenPerWeekTotEnMet()
	{
		return bpvUrenPerWeekTotEnMet;
	}

	public void setBpvUrenPerWeekTotEnMet(BigDecimal bpvUrenPerWeekTotEnMet)
	{
		this.bpvUrenPerWeekTotEnMet = bpvUrenPerWeekTotEnMet;
	}

	public Integer getBpvDagenPerWeekVanaf()
	{
		return bpvDagenPerWeekVanaf;
	}

	public void setBpvDagenPerWeekVanaf(Integer bpvDagenPerWeekVanaf)
	{
		this.bpvDagenPerWeekVanaf = bpvDagenPerWeekVanaf;
	}

	public Integer getBpvDagenPerWeekTotEnMet()
	{
		return bpvDagenPerWeekTotEnMet;
	}

	public void setBpvDagenPerWeekTotEnMet(Integer bpvDagenPerWeekTotEnMet)
	{
		this.bpvDagenPerWeekTotEnMet = bpvDagenPerWeekTotEnMet;
	}

	public MBONiveau getNiveau()
	{
		return niveau;
	}

	public void setNiveau(MBONiveau niveau)
	{
		this.niveau = niveau;
	}

	public Productregel getProductregel()
	{
		return getModelObject(productregel);
	}

	public void setProductregel(Productregel productregel)
	{
		this.productregel = makeModelFor(productregel);
	}

	public List<OrganisatieEenheid> getOrganisatieEenheidList()
	{
		return getModelObject(organisatieEenheidList);
	}

	public void setOrganisatieEenheidList(List<OrganisatieEenheid> organisatieEenheidList)
	{
		this.organisatieEenheidList = makeModelFor(organisatieEenheidList);
	}

	public List<Locatie> getLocatieList()
	{
		return getModelObject(locatieList);
	}

	public void setLocatieList(List<Locatie> locatieList)
	{
		this.locatieList = makeModelFor(locatieList);
	}

	public List<Land> getGeboortelandList()
	{
		return deelnemerZoekFilter.getGeboortelandList();
	}

	public void setGeboortelandList(List<Land> geboortelandList)
	{
		deelnemerZoekFilter.setGeboortelandList(geboortelandList);
	}

	public List<Nationaliteit> getNationaliteitList()
	{
		return deelnemerZoekFilter.getNationaliteitList();
	}

	public void setNationaliteitList(List<Nationaliteit> nationaliteitList)
	{
		deelnemerZoekFilter.setNationaliteitList(nationaliteitList);
	}

	public List<Opleiding> getOpleidingList()
	{
		return getModelObject(opleidingList);
	}

	public void setOpleidingList(List<Opleiding> opleidingList)
	{
		this.opleidingList = makeModelFor(opleidingList);
	}

	public List<Verbintenisgebied> getVerbintenisgebiedList()
	{
		return getModelObject(verbintenisgebiedList);
	}

	public void setVerbintenisgebiedList(List<Verbintenisgebied> verbintenisgebiedList)
	{
		this.verbintenisgebiedList = makeModelFor(verbintenisgebiedList);
	}

	public List<VerbintenisStatus> getVerbintenisStatusList()
	{
		return verbintenisStatusList;
	}

	public void setVerbintenisStatusList(List<VerbintenisStatus> verbintenisStatusList)
	{
		this.verbintenisStatusList = verbintenisStatusList;
	}

	public List<Intensiteit> getIntensiteitList()
	{
		return intensiteitList;
	}

	public void setIntensiteitList(List<Intensiteit> intensiteitList)
	{
		this.intensiteitList = intensiteitList;
	}

	public List<RedenUitschrijving> getRedenUitschrijvingList()
	{
		return getModelObject(redenUitschrijvingList);
	}

	public void setRedenUitschrijvingList(List<RedenUitschrijving> redenUitschrijvingList)
	{
		this.redenUitschrijvingList = makeModelFor(redenUitschrijvingList);
	}

	public List<Groep> getGroepList()
	{
		return deelnemerZoekFilter.getGroepList();
	}

	public void setGroepList(List<Groep> groepList)
	{
		deelnemerZoekFilter.setGroepList(groepList);
	}

	public RedenInburgering getRedenInburgering()
	{
		return redenInburgering;
	}

	public void setRedenInburgering(RedenInburgering redenInburgering)
	{
		this.redenInburgering = redenInburgering;
	}

	public ProfielInburgering getProfielInburgering()
	{
		return profielInburgering;
	}

	public void setProfielInburgering(ProfielInburgering profielInburgering)
	{
		this.profielInburgering = profielInburgering;
	}

	public List<ProfielInburgering> getProfielInburgeringList()
	{
		return profielInburgeringList;
	}

	public void setProfielInburgeringList(List<ProfielInburgering> profielInburgeringList)
	{
		this.profielInburgeringList = profielInburgeringList;
	}

	public Leerprofiel getLeerprofiel()
	{
		return leerprofiel;
	}

	public void setLeerprofiel(Leerprofiel leerprofiel)
	{
		this.leerprofiel = leerprofiel;
	}

	public List<Leerprofiel> getLeerprofielList()
	{
		return leerprofielList;
	}

	public void setLeerprofielList(List<Leerprofiel> leerprofielList)
	{
		this.leerprofielList = leerprofielList;
	}

	public Boolean getDeelcursus()
	{
		return deelcursus;
	}

	public void setDeelcursus(Boolean deelcursus)
	{
		this.deelcursus = deelcursus;
	}

	public SoortPraktijkexamen getSoortPraktijkexamen()
	{
		return soortPraktijkexamen;
	}

	public void setSoortPraktijkexamen(SoortPraktijkexamen soortPraktijkexamen)
	{
		this.soortPraktijkexamen = soortPraktijkexamen;
	}

	public List<SoortPraktijkexamen> getSoortPraktijkexamenList()
	{
		return soortPraktijkexamenList;
	}

	public void setSoortPraktijkexamenList(List<SoortPraktijkexamen> soortPraktijkexamenList)
	{
		this.soortPraktijkexamenList = soortPraktijkexamenList;
	}

	public Date getDatumAanmeldenVanaf()
	{
		return datumAanmeldenVanaf;
	}

	public void setDatumAanmeldenVanaf(Date datumAanmeldenVanaf)
	{
		this.datumAanmeldenVanaf = datumAanmeldenVanaf;
	}

	public Date getDatumAanmeldenTotEnMet()
	{
		return datumAanmeldenTotEnMet;
	}

	public void setDatumAanmeldenTotEnMet(Date datumAanmeldenTotEnMet)
	{
		this.datumAanmeldenTotEnMet =
			TimeUtil.getInstance().maakEindeVanDagVanDatum(datumAanmeldenTotEnMet);
	}

	public Date getDatumAkkoordVanaf()
	{
		return datumAkkoordVanaf;
	}

	public void setDatumAkkoordVanaf(Date datumAkkoordVanaf)
	{
		this.datumAkkoordVanaf = datumAkkoordVanaf;
	}

	public Date getDatumAkkoordTotEnMet()
	{
		return datumAkkoordTotEnMet;
	}

	public void setDatumAkkoordTotEnMet(Date datumAkkoordTotEnMet)
	{
		this.datumAkkoordTotEnMet =
			TimeUtil.getInstance().maakEindeVanDagVanDatum(datumAkkoordTotEnMet);
	}

	public Date getDatumEersteActiviteitVanaf()
	{
		return datumEersteActiviteitVanaf;
	}

	public void setDatumEersteActiviteitVanaf(Date datumEersteActiviteitVanaf)
	{
		this.datumEersteActiviteitVanaf = datumEersteActiviteitVanaf;
	}

	public Date getDatumEersteActiviteitTotEnMet()
	{
		return datumEersteActiviteitTotEnMet;
	}

	public void setDatumEersteActiviteitTotEnMet(Date datumEersteActiviteitTotEnMet)
	{
		this.datumEersteActiviteitTotEnMet =
			TimeUtil.getInstance().maakEindeVanDagVanDatum(datumEersteActiviteitTotEnMet);
	}

	public NT2Niveau getBeginNiveauSchriftelijkeVaardigheden()
	{
		return beginNiveauSchriftelijkeVaardigheden;
	}

	public void setBeginNiveauSchriftelijkeVaardigheden(
			NT2Niveau beginNiveauSchriftelijkeVaardigheden)
	{
		this.beginNiveauSchriftelijkeVaardigheden = beginNiveauSchriftelijkeVaardigheden;
	}

	public List<NT2Niveau> getBeginNiveauSchriftelijkeVaardighedenList()
	{
		return beginNiveauSchriftelijkeVaardighedenList;
	}

	public void setBeginNiveauSchriftelijkeVaardighedenList(
			List<NT2Niveau> beginNiveauSchriftelijkeVaardighedenList)
	{
		this.beginNiveauSchriftelijkeVaardighedenList = beginNiveauSchriftelijkeVaardighedenList;
	}

	public NT2Niveau getEindNiveauSchriftelijkeVaardigheden()
	{
		return eindNiveauSchriftelijkeVaardigheden;
	}

	public void setEindNiveauSchriftelijkeVaardigheden(NT2Niveau eindNiveauSchriftelijkeVaardigheden)
	{
		this.eindNiveauSchriftelijkeVaardigheden = eindNiveauSchriftelijkeVaardigheden;
	}

	public List<NT2Niveau> getEindNiveauSchriftelijkeVaardighedenList()
	{
		return eindNiveauSchriftelijkeVaardighedenList;
	}

	public void setEindNiveauSchriftelijkeVaardighedenList(
			List<NT2Niveau> eindNiveauSchriftelijkeVaardighedenList)
	{
		this.eindNiveauSchriftelijkeVaardighedenList = eindNiveauSchriftelijkeVaardighedenList;
	}

	public List<Medewerker> getIntakerList()
	{
		return getModelObject(intakerList);
	}

	public void setIntakerList(List<Medewerker> intakerList)
	{
		this.intakerList = makeModelFor(intakerList);
	}

	public List<Opleiding> getGewensteOpleidingList()
	{
		return getModelObject(gewensteOpleidingList);
	}

	public void setGewensteOpleidingList(List<Opleiding> gewensteOpleidingList)
	{
		this.gewensteOpleidingList = makeModelFor(gewensteOpleidingList);
	}

	public List<Locatie> getGewensteLocatieList()
	{
		return getModelObject(gewensteLocatieList);
	}

	public void setGewensteLocatieList(List<Locatie> gewensteLocatieList)
	{
		this.gewensteLocatieList = makeModelFor(gewensteLocatieList);
	}

	public List<Brin> getBrinVooropleidingList()
	{
		return deelnemerZoekFilter.getBrinVooropleidingList();
	}

	public void setBrinVooropleidingList(List<Brin> brinVooropleidingList)
	{
		deelnemerZoekFilter.setBrinVooropleidingList(brinVooropleidingList);
	}

	public List<Examenstatus> getExamenstatusList()
	{
		return getModelObject(examenstatusList);
	}

	public void setExamenstatusList(List<Examenstatus> examenstatusList)
	{
		this.examenstatusList = makeModelFor(examenstatusList);
	}

	public List<Contract> getContractList()
	{
		return getModelObject(contractList);
	}

	public void setContractList(List<Contract> contractList)
	{
		this.contractList = makeModelFor(contractList);
	}

	public List<ContractOnderdeel> getContractOnderdeelList()
	{
		return getModelObject(contractOnderdeelList);
	}

	public void setContractOnderdeelList(List<ContractOnderdeel> contractOnderdeelList)
	{
		this.contractOnderdeelList = makeModelFor(contractOnderdeelList);
	}

	public List<ExterneOrganisatie> getContractExterneOrganisatieList()
	{
		return getModelObject(contractExterneOrganisatieList);
	}

	public void setContractExterneOrganisatieList(
			List<ExterneOrganisatie> contractExterneOrganisatieList)
	{
		this.contractExterneOrganisatieList = makeModelFor(contractExterneOrganisatieList);
	}

	public List<BPVStatus> getBpvStatusList()
	{
		return bpvStatusList;
	}

	public void setBpvStatusList(List<BPVStatus> bpvStatusList)
	{
		this.bpvStatusList = bpvStatusList;
	}

	public BronEntiteitStatus getBpvBronStatus()
	{
		return bpvBronStatus;
	}

	public void setBpvBronStatus(BronEntiteitStatus bpvBronStatus)
	{
		this.bpvBronStatus = bpvBronStatus;
	}

	public RedenUitschrijving getBpvRedenBeeindiging()
	{
		return getModelObject(bpvRedenBeeindiging);
	}

	public void setBpvRedenBeeindiging(RedenUitschrijving bpvRedenBeeindiging)
	{
		this.bpvRedenBeeindiging = makeModelFor(bpvRedenBeeindiging);
	}

	public List<ExterneOrganisatie> getBpvBedrijfList()
	{
		return getModelObject(bpvBedrijfList);
	}

	public void setBpvBedrijfList(List<ExterneOrganisatie> bpvBedrijfList)
	{
		this.bpvBedrijfList = makeModelFor(bpvBedrijfList);
	}

	public List<SoortContract> getSoortContractList()
	{
		return getModelObject(soortContractList);
	}

	public void setSoortContractList(List<SoortContract> soortContractList)
	{
		this.soortContractList = makeModelFor(soortContractList);
	}

	public List<Taxonomie> getTaxonomieList()
	{
		return getModelObject(taxonomieList);
	}

	public void setTaxonomieList(List<Taxonomie> taxonomieList)
	{
		this.taxonomieList = makeModelFor(taxonomieList);
	}

	public void setTypeContactgegeven(TypeContactgegeven typeContactgegeven)
	{
		deelnemerZoekFilter.setTypeContactgegeven(typeContactgegeven);
	}

	public TypeContactgegeven getTypeContactgegeven()
	{
		return deelnemerZoekFilter.getTypeContactgegeven();
	}

	public void setContactgegeven(String contactgegeven)
	{
		deelnemerZoekFilter.setContactgegeven(contactgegeven);
	}

	public String getContactgegeven()
	{
		return deelnemerZoekFilter.getContactgegeven();
	}

	public void setRelatieGeboortelandOngelijkAanNL(Boolean geboortelandOngelijkAanNL)
	{
		relatieZoekFilter.setGeboortelandOngelijkAanNL(geboortelandOngelijkAanNL);
	}

	public Boolean getRelatieGeboortelandOngelijkAanNL()
	{
		return relatieZoekFilter.getGeboortelandOngelijkAanNL();
	}

	public void setRelatieNationaliteitOngelijkAanNL(Boolean nationaliteitOngelijkAanNL)
	{
		relatieZoekFilter.setNationaliteitOngelijkAanNL(nationaliteitOngelijkAanNL);
	}

	public Boolean getRelatieNationaliteitOngelijkAanNL()
	{
		return relatieZoekFilter.getNationaliteitOngelijkAanNL();
	}

	public void setPraktijkBegeleiderLeeg(Boolean praktijkBegeleiderLeeg)
	{
		this.praktijkBegeleiderLeeg = praktijkBegeleiderLeeg;
	}

	public Boolean getPraktijkBegeleiderLeeg()
	{
		return praktijkBegeleiderLeeg;
	}

	public void setBpvKenniscentrumLeeg(Boolean bpvKenniscentrumLeeg)
	{
		this.bpvKenniscentrumLeeg = bpvKenniscentrumLeeg;
	}

	public Boolean getBpvKenniscentrumLeeg()
	{
		return bpvKenniscentrumLeeg;
	}

	public void setBekostigd(Bekostigd bekostigd)
	{
		this.bekostigd = bekostigd;
	}

	public Bekostigd getBekostigd()
	{
		return bekostigd;
	}

	public void setVertrekstatus(Vertrekstatus vertrekstatus)
	{
		this.vertrekstatus = vertrekstatus;
	}

	public Vertrekstatus getVertrekstatus()
	{
		return vertrekstatus;
	}

	public void setContacturenPerWeek(Integer contacturenPerWeek)
	{
		this.contacturenPerWeek = contacturenPerWeek;
	}

	public Integer getContacturenPerWeek()
	{
		return contacturenPerWeek;
	}

	public void setPraktijkjaar(Integer praktijkjaar)
	{
		this.praktijkjaar = praktijkjaar;
	}

	public Integer getPraktijkjaar()
	{
		return praktijkjaar;
	}

	public void setRelevanteVooropleidingSoort(SoortVooropleiding relevanteVooropleidingSoort)
	{
		this.relevanteVooropleidingSoort = makeModelFor(relevanteVooropleidingSoort);
	}

	public SoortVooropleiding getRelevanteVooropleidingSoort()
	{
		return getModelObject(relevanteVooropleidingSoort);
	}

	public void setRelevanteVooropleidingSoortLeeg(Boolean relevanteVooropleidingSoortLeeg)
	{
		this.relevanteVooropleidingSoortLeeg = relevanteVooropleidingSoortLeeg;
	}

	public Boolean getRelevanteVooropleidingSoortLeeg()
	{
		return relevanteVooropleidingSoortLeeg;
	}

	public void setAfgenomenOnderwijsproduct(Onderwijsproduct afgenomenOnderwijsproduct)
	{
		this.afgenomenOnderwijsproduct = makeModelFor(afgenomenOnderwijsproduct);
	}

	public Onderwijsproduct getAfgenomenOnderwijsproduct()
	{
		return getModelObject(afgenomenOnderwijsproduct);
	}

	public void setPraktijkopleiderBPVBedrijfLeeg(Boolean praktijkopleiderBPVBedrijfLeeg)
	{
		this.praktijkopleiderBPVBedrijfLeeg = praktijkopleiderBPVBedrijfLeeg;
	}

	public Boolean getPraktijkopleiderBPVBedrijfLeeg()
	{
		return praktijkopleiderBPVBedrijfLeeg;
	}

	public void setPraktijkopleiderBPVBedrijf(
			ExterneOrganisatieContactPersoon praktijkopleiderBPVBedrijf)
	{
		this.praktijkopleiderBPVBedrijf = makeModelFor(praktijkopleiderBPVBedrijf);
	}

	public ExterneOrganisatieContactPersoon getPraktijkopleiderBPVBedrijf()
	{
		return getModelObject(praktijkopleiderBPVBedrijf);
	}

	public void setTeamList(List<Team> teamList)
	{
		this.teamList = makeModelFor(teamList);
	}

	public List<Team> getTeamList()
	{
		return getModelObject(teamList);
	}

	public void setDocumentTypeList(List<DocumentType> documentTypeList)
	{
		deelnemerZoekFilter.setDocumentTypeList(documentTypeList);
	}

	public List<DocumentType> getDocumentTypeList()
	{
		return deelnemerZoekFilter.getDocumentTypeList();
	}

	public void setDocumentCategorieList(List<DocumentCategorie> documentCategorieList)
	{
		deelnemerZoekFilter.setDocumentCategorieList(documentCategorieList);
	}

	public List<DocumentCategorie> getDocumentCategorieList()
	{
		return deelnemerZoekFilter.getDocumentCategorieList();
	}

	public Date getRegistratieDatumVanaf()
	{
		return deelnemerZoekFilter.getRegistratieDatumVanaf();
	}

	public void setRegistratieDatumVanaf(Date registratieDatumVanaf)
	{
		deelnemerZoekFilter.setRegistratieDatumVanaf(registratieDatumVanaf);
	}

	public Date getRegistratieDatumTotEnMet()
	{
		return deelnemerZoekFilter.getRegistratieDatumTotEnMet();
	}

	public void setRegistratieDatumTotEnMet(Date registratieDatumTotEnMet)
	{
		deelnemerZoekFilter.setRegistratieDatumTotEnMet(registratieDatumTotEnMet);
	}

	public Date getRegistratieDatum()
	{
		return deelnemerZoekFilter.getRegistratieDatum();
	}

	public void setRegistratieDatum(Date registratieDatum)
	{
		deelnemerZoekFilter.setRegistratieDatum(registratieDatum);
	}

	public Boolean getRegistratieDatumIsLeeg()
	{
		return deelnemerZoekFilter.getRegistratieDatumIsLeeg();
	}

	public void setRegistratieDatumIsLeeg(Boolean registratieDatumIsLeeg)
	{
		deelnemerZoekFilter.setRegistratieDatumIsLeeg(registratieDatumIsLeeg);
	}

	public void setExamenjaar(Integer examenjaar)
	{
		this.examenjaar = examenjaar;
	}

	public Integer getExamenjaar()
	{
		return examenjaar;
	}

	public String getTaxonomiecode()
	{
		return taxonomiecode;
	}

	public void setTaxonomiecode(String taxonomiecode)
	{
		this.taxonomiecode = taxonomiecode;
	}

	public void setVerbergStatusAangemeld(boolean verbergStatusAangemeld)
	{
		this.verbergStatusAangemeld = verbergStatusAangemeld;
	}

	public boolean isVerbergStatusAangemeld()
	{
		return verbergStatusAangemeld;
	}

	public void setUitsluitenVanFacturatie(Boolean uitsluitenVanFacturatie)
	{
		this.uitsluitenVanFacturatie = uitsluitenVanFacturatie;
	}

	public Boolean getUitsluitenVanFacturatie()
	{
		return uitsluitenVanFacturatie;
	}

	public void setLwoo(Boolean lwoo)
	{
		this.lwoo = lwoo;
	}

	public Boolean getLwoo()
	{
		return lwoo;
	}

	/**
	 * @param beeindigdeOnderwijsproductAfname
	 *            selecteer alleen (niet) in de peilperiode beeindigde
	 *            onderwijsproductafnames; wordt gebruikt icm afgenomenOnderwijsproduct
	 */
	public void setBeeindigdeOnderwijsproductAfname(Boolean beeindigdeOnderwijsproductAfname)
	{
		this.beeindigdeOnderwijsproductAfname = beeindigdeOnderwijsproductAfname;
	}

	/**
	 * return selecteer alleen (niet) in de peilperiode beeindigde
	 * onderwijsproductafnames; wordt gebruikt icm afgenomenOnderwijsproduct
	 */
	public Boolean getBeeindigdeOnderwijsproductAfname()
	{
		return beeindigdeOnderwijsproductAfname;
	}

	public void setRelevanteVerbintenis(Verbintenis v)
	{
		this.relevanteVerbintenis = makeModelFor(v);
	}

	public Verbintenis getRelevanteVerbintenis()
	{
		return getModelObject(relevanteVerbintenis);
	}

	public void setExamendeelnameTijdvakGelijkAan(Integer examendeelnameTijdvakGelijkAan)
	{
		this.examendeelnameTijdvakGelijkAan = examendeelnameTijdvakGelijkAan;
	}

	public Integer getExamendeelnameTijdvakGelijkAan()
	{
		return examendeelnameTijdvakGelijkAan;
	}

	public boolean isStandaardDeelnemerZoeken()
	{
		return standaardDeelnemerZoeken;
	}

	public void setStandaardDeelnemerZoeken(boolean standaardDeelnemerZoeken)
	{
		this.standaardDeelnemerZoeken = standaardDeelnemerZoeken;
	}

	public boolean isCountQuery()
	{
		return countQuery;
	}

	public void setCountQuery(boolean countQuery)
	{
		this.countQuery = countQuery;
	}

	public void setEinddatumIsNull(Boolean einddatumIsNull)
	{
		this.einddatumIsNull = einddatumIsNull;
	}

	public Boolean getEinddatumIsNull()
	{
		return einddatumIsNull;
	}

	public Date getActiefVanaf()
	{
		return actiefVanaf;
	}

	public void setActiefVanaf(Date actiefVanaf)
	{
		this.actiefVanaf = actiefVanaf;
	}

	public Date getActiefTotEnMet()
	{
		return actiefTotEnMet;
	}

	public void setActiefTotEnMet(Date actiefTotEnMet)
	{
		this.actiefTotEnMet = actiefTotEnMet;
	}
}
