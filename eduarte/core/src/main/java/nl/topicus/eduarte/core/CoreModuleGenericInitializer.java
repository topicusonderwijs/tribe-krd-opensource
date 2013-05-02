package nl.topicus.eduarte.core;

import javax.persistence.Lob;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.BatchExtendableDataViewDataAccessHelper;
import nl.topicus.cobra.dao.helpers.BatchGroupPropertySettingDataAccessHelper;
import nl.topicus.cobra.dao.helpers.InGebruikCheckDataAccessHelper;
import nl.topicus.cobra.dao.helpers.IndexNummerGeneratorDataAccessHelper;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.dao.helpers.UniqueCheckDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.InGebruikCheckHibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.modules.InitializingModuleSupport;
import nl.topicus.cobra.security.CobraEncryptonProvider;
import nl.topicus.cobra.web.components.form.AutoFormRegistry;
import nl.topicus.cobra.web.components.form.FieldContainerType;
import nl.topicus.cobra.web.components.labels.NewlinePreservingLabel;
import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.DropDownCheckList;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.EduArteCriteriaInterceptor;
import nl.topicus.eduarte.dao.helpers.*;
import nl.topicus.eduarte.dao.helpers.bpv.BPVCriteriaDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.bpv.BPVKandidaatDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.bpv.BPVMatchDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.bpv.BPVPlaatsDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.dbs.*;
import nl.topicus.eduarte.dao.helpers.vasco.TokenDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.vasco.VascoDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.*;
import nl.topicus.eduarte.dao.hibernate.bpv.BPVCriteriaHibernateDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.bpv.BPVKandidaatHibernateDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.bpv.BPVMatchHibernateDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.bpv.BPVPlaatsHibernateDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.dbs.*;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieWaarnemingenDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.DeelnemerMedewerkerGroepDataAccesHelper;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekindelingOrganisatieEenheidLocatieDataAccesHelper;
import nl.topicus.eduarte.dao.participatie.helpers.VerzuimmeldingDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.hibernate.AbsentieWaarnemingenHibernateDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.hibernate.DeelnemerMedewerkerGroepHibernateDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.hibernate.LesweekIndelingHibernateDataAccessHelper;
import nl.topicus.eduarte.dao.participatie.hibernate.LesweekindelingOrganisatieEenheidLocatieHibernateDataAccesHelper;
import nl.topicus.eduarte.dao.webservices.PostcodeDataAccessHelper;
import nl.topicus.eduarte.dao.webservices.PostcodeDataAccessHelperImpl;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.bpv.BPVCriteria;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.VrijstellingType;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.landelijk.Verblijfsvergunning;
import nl.topicus.eduarte.entities.onderwijsproduct.Aggregatieniveau;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductNiveauaanduiding;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortOnderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.TypeToets;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoonRol;
import nl.topicus.eduarte.entities.personen.Functie;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatstructuurCategorie;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilter;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.IJkpunt;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Meeteenheid;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.MeeteenheidKoppelType;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.AbsentieRedenComboBox;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.AfspraakTypeCombobox;
import nl.topicus.eduarte.web.components.choice.*;
import nl.topicus.eduarte.web.components.panels.filter.renderer.ZoekFilterMarkupRenderer;
import nl.topicus.eduarte.web.components.quicksearch.bpvcriteria.BPVCriteriaSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.brin.BrinSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.contract.ContractSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.deelnemer.VerbintenisSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.ExterneOrganisatieSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.contactpersoon.ExterneOrganisatieContactPersoonSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.gemeente.GemeenteSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.groep.GroepSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.land.LandSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.medewerker.MedewerkerSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.nationaliteit.NationaliteitSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct.OnderwijsproductSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.opleiding.OpleidingSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.persoon.PersoonSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.rol.RolSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.soortvooropleiding.SoortVooropleidingSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.taxonomie.TaxonomieElementSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.verblijfsvergunning.VerblijfsvergunningSearchEditor;
import nl.topicus.eduarte.web.components.text.EnumSelectField;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Initializes the core module for use in EduArte. The {@link InitializingBean} interface
 * ensures that this module is initialized after the Hibernate settings haven been
 * processed. In the {@link #afterPropertiesSet()} method all remaining initialization can
 * happen.
 */
public class CoreModuleGenericInitializer extends InitializingModuleSupport
{
	public CoreModuleGenericInitializer()
	{
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet()
	{
		initializeDataAccessRegistry();
		initializeAutoFormRegistry();
	}

	/**
	 * Initializes the data access registry for the given key. Each helper has its own
	 * hibernate session provider that manages the session specific for the thread. For
	 * Wicket requests the provider uses the Wicket request cycle to store the hibernate
	 * session in. In background threads the helpers get the multithreaded provider that
	 * stores the session in a thread local variable.
	 */
	@SuppressWarnings("unchecked")
	public void initializeDataAccessRegistry()
	{
		DataAccessRegistry registry = DataAccessRegistry.getInstance();

		HibernateSessionProvider provider = getProvider();
		QueryInterceptor interceptor = new EduArteCriteriaInterceptor();

		// some general purpose helpers
		registry.register(BatchDataAccessHelper.class, new HibernateDataAccessHelper(provider,
			interceptor));
		registry.register(DataAccessHelper.class, new HibernateDataAccessHelper(provider,
			interceptor));
		registry.register(SessionDataAccessHelper.class, new HibernateSessionDataAccessHelper(
			provider, interceptor));

		// more specific helpers
		// in alfabetische volgorde. Graag zo houden!
		registry.register(AbsentieWaarnemingenDataAccessHelper.class,
			new AbsentieWaarnemingenHibernateDataAccessHelper(provider, interceptor));
		registry.register(AccountDataAccessHelper.class, new AccountHibernateDataAccessHelper(
			provider, new CobraEncryptonProvider(), interceptor));
		registry.register(BeheerDataAccessHelper.class, new BeheerHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(BijlageDataAccessHelper.class, new BijlageHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(BookmarkDataAccessHelper.class, new BookmarkHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(BookmarkFolderDataAccessHelper.class,
			new BookmarkFolderHibernateDataAccessHelper(provider, interceptor));
		registry.register(BPVInschrijvingDataAccessHelper.class,
			new BPVInschrijvingHibernateDataAccessHelper(provider, interceptor));
		registry.register(BPVBedrijfsgegevenDataAccessHelper.class,
			new BPVBedrijfsgegevenHibernateDataAccessHelper(provider, interceptor));
		registry.register(BrinDataAccessHelper.class, new BrinHibernateDataAccessHelper(provider,
			interceptor));
		registry.register(CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class,
			new CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper(provider,
				interceptor));
		registry.register(CohortDataAccessHelper.class, new CohortHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(CompetentieDataAccessHelper.class,
			new CompetentieHibernateDataAccessHelper(provider, interceptor));
		registry.register(CompetentieMatrixDataAccessHelper.class,
			new CompetentieMatrixHibernateDataAccessHelper(provider, interceptor));
		registry.register(ContractDataAccessHelper.class, new ContractHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(KerntaakDataAccessHelper.class, new KerntaakHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(ContractVerplichtingDataAccesHelper.class,
			new ContractVerplichtingHibernateDataAccesHelper(provider, interceptor));
		registry.register(CriteriumDataAccessHelper.class, new CriteriumHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(CurriculumDataAccessHelper.class,
			new CurriculumHibernateDataAccessHelper(provider, interceptor));
		registry.register(CurriculumOnderwijsproductDataAccessHelper.class,
			new CurriculumOnderwijsproductHibernateDataAccessHelper(provider, interceptor));
		registry.register(DeelnemerDataAccessHelper.class, new DeelnemerHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(DeelnemerZoekOpdrachtDataAccessHelper.class,
			new DeelnemerZoekOpdrachtHibernateDataAccessHelper(provider, interceptor));
		registry.register(DeelnemerBijlageDataAccessHelper.class,
			new DeelnemerBijlageHibernateDataAccessHelper(provider, interceptor));
		registry.register(PersoonDataAccessHelper.class, new PersoonHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(DocumentCategorieDataAccessHelper.class,
			new DocumentCategorieHibernateDataAccessHelper(provider, interceptor));
		registry.register(DocumentTemplateDataAccessHelper.class,
			new DocumentTemplateHibernateDataAccessHelper(provider, interceptor));
		registry.register(DocumentTypeDataAccessHelper.class,
			new DocumentTypeHibernateDataAccessHelper(provider, interceptor));
		registry.register(EventAbonnementSettingDataAccessHelper.class,
			new EventAbonnementSettingHibernateDataAccessHelper(provider, interceptor));
		registry.register(EventDataAccessHelper.class, new EventHibernateDataAccessHelper(provider,
			interceptor));
		registry.register(ExamenStatusDataAccessHelper.class,
			new ExamenStatusHibernateDataAccessHelper(provider, interceptor));
		registry.register(ExamenWorkflowDataAccessHelper.class,
			new ExamenWorkflowHibernateDataAccessHelper(provider, interceptor));
		registry.register(ExterneOrganisatieAdresDataAccessHelper.class,
			new ExterneOrganisatieAdresHibernateDataAccessHelper(provider, interceptor));
		registry.register(ExterneOrganisatieDataAccessHelper.class,
			new ExterneOrganisatieHibernateDataAccessHelper(provider, interceptor));
		registry.register(ExterneOrganisatieContactPersoonDataAccessHelper.class,
			new ExterneOrganisatieContactPersoonHibernateDataAccessHelper(provider, interceptor));
		registry
			.register(ExterneOrganisatieContactPersoonRolDataAccessHelper.class,
				new ExterneOrganisatieContactPersoonRolHibernateDataAccessHelper(provider,
					interceptor));
		registry.register(BatchExtendableDataViewDataAccessHelper.class,
			new ExtendableDataViewHibernateDataAccessHelper(provider, interceptor));
		registry.register(FunctieDataAccessHelper.class, new FunctieHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(GebruiksmiddelDataAccessHelper.class,
			new GebruiksmiddelHibernateDataAccessHelper(provider, interceptor));
		registry.register(GemeenteDataAccessHelper.class, new GemeenteHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(PlaatsDataAccessHelper.class, new PlaatsHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(GroepDataAccessHelper.class, new GroepHibernateDataAccessHelper(provider,
			interceptor));
		registry.register(GroepsdeelnameDataAccessHelper.class,
			new GroepsdeelnameHibernateDataAccessHelper(provider, interceptor));
		registry.register(GroepstypeDataAccessHelper.class,
			new GroepstypeHibernateDataAccessHelper(provider, interceptor));
		registry.register(BatchGroupPropertySettingDataAccessHelper.class,
			new GroupPropertySettingHibernateDataAccessHelper(provider, interceptor));
		registry.register(IndexNummerGeneratorDataAccessHelper.class,
			new IndexNummerGeneratorHibernateDataAccessHelper(provider, interceptor));
		registry.register(InGebruikCheckDataAccessHelper.class,
			new InGebruikCheckHibernateDataAccessHelper(provider, interceptor));
		registry.register(InstellingDataAccessHelper.class,
			new InstellingHibernateDataAccessHelper(provider, interceptor));
		registry.register(InstellingsLogoDataAccessHelper.class,
			new InstellingsLogoHibernateDataAccessHelper(provider, interceptor));
		registry.register(IrisKoppelingKeyDataAccessHelper.class,
			new IrisKoppelingKeyHibernateDataAccessHelper(provider, interceptor));
		registry.register(IntakegesprekDataAccessHelper.class,
			new IntakegesprekHibernateDataAccessHelper(provider, interceptor));
		registry.register(JobRunDataAccessHelper.class, new JobRunHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(JobRunDetailDataAccessHelper.class,
			new JobRunDetailHibernateDataAccessHelper(provider, interceptor));
		registry.register(JobScheduleDataAccessHelper.class,
			new JobScheduleHibernateDataAccessHelper(provider, interceptor));
		registry.register(KenmerkCategorieDataAccessHelper.class,
			new KenmerkCategorieHibernateDataAccessHelper(provider, interceptor));
		registry.register(KenmerkDataAccessHelper.class, new KenmerkHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(LandDataAccessHelper.class, new LandHibernateDataAccessHelper(provider,
			interceptor));
		registry.register(VerblijfsvergunningDataAccessHelper.class,
			new VerblijfsvergunningHibernateDataAccessHelper(provider, interceptor));
		registry.register(SoortVooropleidingHODataAccessHelper.class,
			new SoortVooropleidingHOHibernateDataAccessHelper(provider, interceptor));
		registry.register(SoortVooropleidingBuitenlandsDataAccessHelper.class,
			new SoortVooropleidingBuitenlandsHibernateDataAccessHelper(provider, interceptor));
		registry.register(VooropleidingVakDataAccessHelper.class,
			new VooropleidingVakHibernateDataAccessHelper(provider, interceptor));
		registry.register(LeerpuntDataAccessHelper.class, new LeerpuntHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(LocatieDataAccessHelper.class, new LocatieHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(MedewerkerDataAccessHelper.class,
			new MedewerkerHibernateDataAccessHelper(provider, interceptor));
		registry.register(MeeteenheidDataAccessHelper.class,
			new MeeteenheidHibernateDataAccessHelper(provider, interceptor));
		registry.register(ModuleAfnameDataAccessHelper.class,
			new ModuleAfnameHibernateDataAccessHelper(provider, interceptor));
		registry.register(NationaliteitDataAccessHelper.class,
			new NationaliteitHibernateDataAccessHelper(provider, interceptor));
		registry.register(NummerGeneratorDataAccessHelper.class,
			new NummerGeneratorHibernateDataAccessHelper(provider, interceptor));
		registry.register(OnderwijsproductAanbodDataAccessHelper.class,
			new OnderwijsproductAanbodHibernateDataAccessHelper(provider, interceptor));
		registry.register(OnderwijsproductAfnameContextDataAccessHelper.class,
			new OnderwijsproductAfnameContextHibernateDataAccessHelper(provider, interceptor));
		registry.register(OnderwijsproductAfnameDataAccessHelper.class,
			new OnderwijsproductAfnameHibernateDataAccessHelper(provider, interceptor));
		registry.register(OnderwijsproductDataAccessHelper.class,
			new OnderwijsproductHibernateDataAccessHelper(provider, interceptor));
		registry.register(OpleidingDataAccessHelper.class, new OpleidingHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(OrganisatieDataAccessHelper.class,
			new OrganisatieHibernateDataAccessHelper(provider, interceptor));
		registry.register(OrganisatieEenheidDataAccessHelper.class,
			new OrganisatieEenheidHibernateDataAccessHelper(provider, interceptor));
		registry.register(PersoonExterneOrganisatieDataAccessHelper.class,
			new PersoonExterneOrganisatieHibernateDataAccessHelper(provider, interceptor));
		registry.register(PostcodeDataAccessHelper.class, new PostcodeDataAccessHelperImpl());
		registry.register(ProvincieDataAccessHelper.class, new ProvincieHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(ProductregelDataAccessHelper.class,
			new ProductregelHibernateDataAccessHelper(provider, interceptor));
		registry.register(PlaatsingDataAccessHelper.class, new PlaatsingHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(RapportageTemplateDataAccessHelper.class,
			new RapportageTemplateHibernateDataAccessHelper(provider, interceptor));
		registry.register(RegioDataAccessHelper.class, new RegioHibernateDataAccessHelper(provider,
			interceptor));
		registry.register(RelatieDataAccessHelper.class, new RelatieHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(ResultaatDataAccessHelper.class, new ResultaatHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(ResultaatstructuurDataAccessHelper.class,
			new ResultaatstructuurHibernateDataAccessHelper(provider, interceptor));
		registry.register(ResultaatstructuurCategorieDataAccessHelper.class,
			new ResultaatstructuurCategorieHibernateDataAccessHelper(provider, interceptor));
		registry.register(ResultaatZoekFilterInstellingDataAccessHelper.class,
			new ResultaatZoekFilterInstellingHibernateDataAccessHelper(provider, interceptor));
		registry.register(RolDataAccessHelper.class, new RolHibernateDataAccessHelper(provider,
			interceptor));
		registry.register(SchaalDataAccessHelper.class, new SchaalHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(SchaalwaardeDataAccessHelper.class,
			new SchaalwaardeHibernateDataAccessHelper(provider, interceptor));
		registry.register(SettingsDataAccessHelper.class, new SettingsHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(SignaalDataAccessHelper.class, new SignaalHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(SoortContactgegevenDataAccessHelper.class,
			new SoortContactgegevenHibernateDataAccessHelper(provider, interceptor));
		registry.register(SoortContractDataAccessHelper.class,
			new SoortContractHibernateDataAccessHelper(provider, interceptor));
		registry.register(SoortOnderwijsproductDataAccessHelper.class,
			new SoortOnderwijsproductHibernateDataAccessHelper(provider, interceptor));
		registry.register(SoortOrganisatieEenheidDataAccessHelper.class,
			new SoortOrganisatieEenheidHibernateDataAccessHelper(provider, interceptor));
		registry.register(SoortProductregelDataAccessHelper.class,
			new SoortProductregelHibernateDataAccessHelper(provider, interceptor));
		registry.register(SSLCertificaatDataAccessHelper.class,
			new SSLCertificaatHibernateDataAccessHelper(provider, interceptor));
		registry.register(StandaardToetsCodeFilterDataAccessHelper.class,
			new StandaardToetsCodeFilterHibernateDataAccessHelper(provider, interceptor));
		registry.register(TaalDataAccessHelper.class, new TaalHibernateDataAccessHelper(provider,
			interceptor));
		registry.register(TaalTypeDataAccessHelper.class, new TaalTypeHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(TaalkeuzeDataAccessHelper.class, new TaalkeuzeHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(TaalbeoordelingDataAccessHelper.class,
			new TaalbeoordelingHibernateDataAccessHelper(provider, interceptor));
		registry.register(TaxonomieElementDataAccessHelper.class,
			new TaxonomieElementHibernateDataAccessHelper(provider, interceptor));
		registry.register(TaxonomieElementTypeDataAccessHelper.class,
			new TaxonomieElementTypeHibernateDataAccessHelper(provider, interceptor));
		registry.register(ToegestaneExamenstatusOvergangDataAccessHelper.class,
			new ToegestaneExamenstatusOvergangHibernateDataAccessHelper(provider, interceptor));
		registry.register(ToetsCodeFilterDataAccessHelper.class,
			new ToetsCodeFilterHibernateDataAccessHelper(provider, interceptor));
		registry.register(ToetsDataAccessHelper.class, new ToetsHibernateDataAccessHelper(provider,
			interceptor));
		registry.register(TokenDataAccessHelper.class, new TokenHibernateDataAccessHelper(provider,
			interceptor));
		registry.register(TypeFinancieringDataAccessHelper.class,
			new TypeFinancieringHibernateDataAccessHelper(provider, interceptor));
		registry.register(UitkomstIntakegesprekDataAccessHelper.class,
			new UitkomstIntakegesprekHibernateDataAccessHelper(provider, interceptor));
		registry.register(UniqueCheckDataAccessHelper.class,
			new nl.topicus.cobra.dao.hibernate.UniqueCheckDataAccessHelper(provider, interceptor));
		registry.register(VerbintenisDataAccessHelper.class,
			new VerbintenisHibernateDataAccessHelper(provider, interceptor));
		registry.register(VerbruiksmiddelDataAccessHelper.class,
			new VerbruiksmiddelHibernateDataAccessHelper(provider, interceptor));
		registry.register(VerzuimmeldingDataAccessHelper.class,
			new VerzuimmeldingHibernateDataAccessHelper(provider, interceptor));
		registry.register(VooropleidingDataAccessHelper.class,
			new VooropleidingHibernateDataAccessHelper(provider, interceptor));
		registry.register(OpleidingAanbodDataAccessHelper.class,
			new OpleidingAanbodHibernateDataAccessHelper(provider, interceptor));
		registry.register(VoorvoegselDataAccessHelper.class,
			new VoorvoegselHibernateDataAccessHelper(provider, interceptor));
		registry.register(VrijVeldDataAccessHelper.class, new VrijVeldHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(RelatieSoortDataAccesHelper.class,
			new RelatieSoortHibernateDataAccessHelper(provider, interceptor));

		// registry.register(ExterneAgendaDataAccessHelper.class,
		// new ExterneAgendaHibernateDataAccessHelper(provider, interceptor));

		registry.register(DeelnemerMedewerkerGroepDataAccesHelper.class,
			new DeelnemerMedewerkerGroepHibernateDataAccessHelper(provider, interceptor));
		registry.register(LesweekIndelingDataAccessHelper.class,
			new LesweekIndelingHibernateDataAccessHelper(provider, interceptor));
		registry.register(LesweekindelingOrganisatieEenheidLocatieDataAccesHelper.class,
			new LesweekindelingOrganisatieEenheidLocatieHibernateDataAccesHelper(provider,
				interceptor));
		registry.register(BeoordelingDataAccessHelper.class,
			new BeoordelingHibernateDataAccessHelper(provider, interceptor));
		registry.register(MeeteenheidKoppelDataAccessHelper.class,
			new MeeteenheidKoppelHibernateDataAccessHelper(provider, interceptor));
		registry.register(LokaalCompetentieMaximumDataAccessHelper.class,
			new LokaalCompetentieMaximumHibernateDataAccessHelper(provider, interceptor));
		registry.register(IJkpuntDataAccessHelper.class, new IJkpuntHibernateDataAccessHelper(
			provider, interceptor));

		registry.register(CrohoOpleidingDataAccessHelper.class,
			new CrohoOpleidingHibernateDataAccessHelper(provider, interceptor));
		registry.register(CrohoOpleidingAanbodDataAccessHelper.class,
			new CrohoOpleidingAanbodHibernateDataAccessHelper(provider, interceptor));

		registry.register(InstroommomentDataAccessHelper.class,
			new InstroommomentHibernateDataAccessHelper(provider, interceptor));

		initDbsHelpers(registry, provider, interceptor);
		initHOHelpers(registry, provider, interceptor);
	}

	private void initDbsHelpers(DataAccessRegistry registry, HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		registry.register(BijzonderheidCategorieDataAccessHelper.class,
			new BijzonderheidCategorieHibernateDataAccessHelper(provider, interceptor));
		registry.register(BijzonderheidDataAccessHelper.class,
			new BijzonderheidHibernateDataAccessHelper(provider, interceptor));
		registry.register(BegeleidingsHandelingDataAccessHelper.class,
			new BegeleidingsHandelingHibernateDataAccessHelper(provider, interceptor));
		registry.register(DBSMedewerkerDataAccessHelper.class,
			new DBSMedewerkerHibernateDataAccessHelper(provider, interceptor));
		registry.register(DeelnemerTestDataAccessHelper.class,
			new DeelnemerTestHibernateDataAccessHelper(provider, interceptor));
		registry.register(GesprekDataAccessHelper.class, new GesprekHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(GesprekSamenvattingTemplateDataAccessHelper.class,
			new GesprekSamenvattingTemplateHibernateDataAccessHelper(provider, interceptor));
		registry.register(GesprekSamenvattingZinDataAccessHelper.class,
			new GesprekSamenvattingZinHibernateDataAccessHelper(provider, interceptor));
		registry.register(GesprekSoortDataAccessHelper.class,
			new GesprekSoortHibernateDataAccessHelper(provider, interceptor));
		registry.register(HulpmiddelDataAccessHelper.class,
			new HulpmiddelHibernateDataAccessHelper(provider, interceptor));
		registry.register(IncidentCategorieDataAccessHelper.class,
			new IncidentCategorieHibernateDataAccessHelper(provider, interceptor));
		registry.register(IncidentDataAccessHelper.class, new IncidentHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(IrisIncidentDataAccessHelper.class,
			new IrisIncidentHibernateDataAccessHelper(provider, interceptor));
		registry.register(MogelijkeAanleidingDataAccessHelper.class,
			new MogelijkeAanleidingHibernateDataAccessHelper(provider, interceptor));
		registry.register(NotitieDataAccessHelper.class, new NotitieHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(TaakSoortDataAccessHelper.class, new TaakSoortHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(TestCategorieDataAccessHelper.class,
			new TestCategorieHibernateDataAccessHelper(provider, interceptor));
		registry.register(TestDefinitieDataAccessHelper.class,
			new TestDefinitieHibernateDataAccessHelper(provider, interceptor));
		registry.register(TrajectDataAccessHelper.class, new TrajectHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(TrajectSoortDataAccessHelper.class,
			new TrajectSoortHibernateDataAccessHelper(provider, interceptor));
		registry.register(TrajectStatusSoortDataAccessHelper.class,
			new TrajectStatusSoortHibernateDataAccessHelper(provider, interceptor));
		registry.register(TrajectTemplateDataAccessHelper.class,
			new TrajectTemplateHibernateDataAccessHelper(provider, interceptor));

		registry.register(VerzuimTaakSignaalDefinitieDataAccessHelper.class,
			new VerzuimTaakSignaalDefinitieHibernateDataAccesHelper(provider, interceptor));

		registry.register(
			VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppelDataAccessHelper.class,
			new VerzuimTaakSignaalDefinitieEnEventConfiguratieKoppelHibernateDataAccessHelper(
				provider, interceptor));

		registry.register(BPVKandidaatDataAccessHelper.class,
			new BPVKandidaatHibernateDataAccessHelper(provider, interceptor));

		registry.register(BPVMatchDataAccessHelper.class, new BPVMatchHibernateDataAccessHelper(
			provider, interceptor));
		registry.register(BPVCriteriaDataAccessHelper.class,
			new BPVCriteriaHibernateDataAccessHelper(provider, interceptor));
		registry.register(BPVPlaatsDataAccessHelper.class, new BPVPlaatsHibernateDataAccessHelper(
			provider, interceptor));
	}

	private void initHOHelpers(DataAccessRegistry registry, HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		registry.register(FaseDataAccessHelper.class, new FaseHibernateDataAccessHelper(provider,
			interceptor));
	}

	public void initializeAutoFormRegistry()
	{
		initializeAutoFormRegistry(AutoFormRegistry.getInstance());
	}

	public static void initializeAutoFormRegistry(AutoFormRegistry autoFormReg)
	{
		autoFormReg.registerMarkupRenderer(ZoekFilterMarkupRenderer.NAME,
			new ZoekFilterMarkupRenderer());

		autoFormReg.registerEditor(AbsentieReden.class, AbsentieRedenComboBox.class);
		autoFormReg.registerEditor(Aggregatieniveau.class, AggregatieniveauCombobox.class);
		autoFormReg.registerEditor(Groepstype.class, GroepstypeCombobox.class);
		autoFormReg.registerEditor(Locatie.class, LocatieCombobox.class);
		autoFormReg.registerEditor(Functie.class, FunctieCombobox.class);
		autoFormReg.registerEditor(ExterneOrganisatie.class, ExterneOrganisatieSearchEditor.class);
		autoFormReg.registerEditor(Land.class, LandSearchEditor.class);
		autoFormReg.registerEditor(Brin.class, BrinSearchEditor.class);
		autoFormReg.registerEditor(Contract.class, ContractSearchEditor.class);
		autoFormReg.registerEditor(Medewerker.class, MedewerkerSearchEditor.class);
		autoFormReg.registerEditor(Groep.class, GroepSearchEditor.class);
		autoFormReg.registerEditor(Opleiding.class, OpleidingSearchEditor.class);
		autoFormReg.registerEditor(Gemeente.class, GemeenteSearchEditor.class);
		autoFormReg.registerEditor(Nationaliteit.class, NationaliteitSearchEditor.class);
		autoFormReg.registerEditor(ExterneOrganisatieContactPersoonRol.class,
			ExterneOrganisatieContactPersoonRolCombobox.class);
		autoFormReg.registerEditor(OrganisatieEenheid.class, OrganisatieEenheidCombobox.class);
		autoFormReg.registerEditor(Cohort.class, CohortCombobox.class);
		autoFormReg.registerEditor(Onderwijsproduct.class, OnderwijsproductSearchEditor.class);
		autoFormReg
			.registerEditor(SoortOnderwijsproduct.class, SoortOnderwijsproductCombobox.class);
		autoFormReg.registerEditor(OnderwijsproductNiveauaanduiding.class,
			OnderwijsproductNiveauaanduidingCombobox.class);
		autoFormReg.registerEditor(ExterneOrganisatieContactPersoon.class,
			ExterneOrganisatieContactPersoonSearchEditor.class);
		autoFormReg.registerEditor(ToetsCodeFilter.class, ToetsCodeFilterCombobox.class);
		autoFormReg.registerEditor(TypeToets.class, TypeToetsCombobox.class);
		autoFormReg.registerEditor(SoortVooropleiding.class, SoortVooropleidingSearchEditor.class);
		autoFormReg.registerEditor(KenmerkCategorie.class, KenmerkCategorieCombobox.class);
		autoFormReg.registerEditor(DocumentCategorie.class, DocumentCategorieCombobox.class);
		autoFormReg.registerEditor(Rol.class, RolSearchEditor.class);
		autoFormReg.registerEditor(Meeteenheid.class, MeeteenheidCombobox.class);
		autoFormReg
			.registerEditor(MeeteenheidKoppelType.class, MeeteenheidKoppelTypeCombobox.class);
		autoFormReg.registerEditor(IJkpunt.class, IJkpuntCombobox.class);
		autoFormReg.registerEditor(Persoon.class, PersoonSearchEditor.class);
		autoFormReg.registerEditor(Deelnemer.class, VerbintenisSearchEditor.class);
		autoFormReg.registerEditor(Verbintenisgebied.class, TaxonomieElementSearchEditor.class);
		autoFormReg.registerEditor(ResultaatstructuurCategorie.class,
			ResultaatstructuurCategorieCombobox.class);
		autoFormReg
			.registerEditor(Verblijfsvergunning.class, VerblijfsvergunningSearchEditor.class);

		autoFormReg.registerEditor(AfspraakType.class, AfspraakTypeCombobox.class);

		autoFormReg.registerContainerForEditor(Object.class, QuickSearchField.class,
			FieldContainerType.INPUT_TEXT);
		autoFormReg.registerContainerForEditor(Object.class, AbstractSearchEditor.class,
			FieldContainerType.DIV);
		autoFormReg.registerContainerForEditor(Object.class, DropDownCheckList.class,
			FieldContainerType.SELECT);

		autoFormReg.registerContainerForEditor(Enum.class, EnumSelectField.class,
			FieldContainerType.LABEL);

		autoFormReg.registerDisplay(String.class, Lob.class, NewlinePreservingLabel.class,
			FieldContainerType.LABEL);

		autoFormReg.registerEditor(BPVCriteria.class, BPVCriteriaSearchEditor.class);

		autoFormReg.registerEditor(VrijstellingType.class, VrijstellingTypeCombobox.class);
	}

	@Autowired
	public void setVascoHelper(VascoDataAccessHelper vasco)
	{
		DataAccessRegistry.getInstance().register(VascoDataAccessHelper.class, vasco);
	}
}
