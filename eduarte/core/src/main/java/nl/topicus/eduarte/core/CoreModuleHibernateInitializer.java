package nl.topicus.eduarte.core;

import nl.topicus.cobra.hibernate.InterceptorFactory;
import nl.topicus.cobra.modules.HibernateInitializer;
import nl.topicus.cobra.update.dbupdate.entity.DBVersionMark;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.EntiteitAuditInterceptor;
import nl.topicus.eduarte.entities.NaamEntiteit;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.adres.AdresEntiteit;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.entities.bpv.*;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;
import nl.topicus.eduarte.entities.contract.ContractVerplichting;
import nl.topicus.eduarte.entities.contract.SoortContract;
import nl.topicus.eduarte.entities.contract.SoortContractVerplichting;
import nl.topicus.eduarte.entities.contract.TypeFinanciering;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;
import nl.topicus.eduarte.entities.dbs.NietTonenInZorgvierkant;
import nl.topicus.eduarte.entities.dbs.bijlagen.BegeleidingsBijlage;
import nl.topicus.eduarte.entities.dbs.bijlagen.BijzonderheidBijlage;
import nl.topicus.eduarte.entities.dbs.bijlagen.IncidentBijlage;
import nl.topicus.eduarte.entities.dbs.bijlagen.IrisIncidentBijlage;
import nl.topicus.eduarte.entities.dbs.bijlagen.NotitieBijlage;
import nl.topicus.eduarte.entities.dbs.bijlagen.TestBijlage;
import nl.topicus.eduarte.entities.dbs.bijlagen.TrajectBijlage;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.Bijzonderheid;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.BijzonderheidCategorie;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.BijzonderheidNietTonenInZorgvierkant;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.Hulpmiddel;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.ToegekendHulpmiddel;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.ToegestaanHulpmiddel;
import nl.topicus.eduarte.entities.dbs.gedrag.BetrokkenMedewerker;
import nl.topicus.eduarte.entities.dbs.gedrag.Gedrag;
import nl.topicus.eduarte.entities.dbs.gedrag.Incident;
import nl.topicus.eduarte.entities.dbs.gedrag.IncidentCategorie;
import nl.topicus.eduarte.entities.dbs.gedrag.IrisIncidentNietTonenInZorgvierkant;
import nl.topicus.eduarte.entities.dbs.gedrag.Notitie;
import nl.topicus.eduarte.entities.dbs.gedrag.NotitieNietTonenInZorgvierkant;
import nl.topicus.eduarte.entities.dbs.incident.IrisBetrokkene;
import nl.topicus.eduarte.entities.dbs.incident.IrisBetrokkeneAfhandeling;
import nl.topicus.eduarte.entities.dbs.incident.IrisBetrokkeneMotief;
import nl.topicus.eduarte.entities.dbs.incident.IrisIncident;
import nl.topicus.eduarte.entities.dbs.incident.IrisIncidentLocatie;
import nl.topicus.eduarte.entities.dbs.incident.IrisIncidentVoorwerp;
import nl.topicus.eduarte.entities.dbs.incident.IrisKoppelingKey;
import nl.topicus.eduarte.entities.dbs.testen.*;
import nl.topicus.eduarte.entities.dbs.trajecten.*;
import nl.topicus.eduarte.entities.dbs.trajecten.templates.*;
import nl.topicus.eduarte.entities.examen.*;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.GroepBijlage;
import nl.topicus.eduarte.entities.groep.GroepDocent;
import nl.topicus.eduarte.entities.groep.GroepMentor;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.entities.hogeronderwijs.*;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimdag;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.ibgverzuimloket.SSLCertificaat;
import nl.topicus.eduarte.entities.inschrijving.*;
import nl.topicus.eduarte.entities.jobs.AccountsImportJobRun;
import nl.topicus.eduarte.entities.jobs.ExactCsvExportJobRun;
import nl.topicus.eduarte.entities.jobs.LvsCsvExportJobRun;
import nl.topicus.eduarte.entities.jobs.PersistentJobSchedule;
import nl.topicus.eduarte.entities.jobs.logging.DataPanelExportJobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.entities.jobs.logging.RapportageJobRun;
import nl.topicus.eduarte.entities.jobs.logging.TerugdraaibareJobRun;
import nl.topicus.eduarte.entities.kenmerk.DeelnemerKenmerk;
import nl.topicus.eduarte.entities.kenmerk.ExterneOrganisatieKenmerk;
import nl.topicus.eduarte.entities.kenmerk.Kenmerk;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;
import nl.topicus.eduarte.entities.kenmerk.MedewerkerKenmerk;
import nl.topicus.eduarte.entities.landelijk.*;
import nl.topicus.eduarte.entities.onderwijsproduct.*;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.OpleidingAanbod;
import nl.topicus.eduarte.entities.opleiding.OpleidingBijlage;
import nl.topicus.eduarte.entities.opleiding.Opleidingsvariant;
import nl.topicus.eduarte.entities.opleiding.Team;
import nl.topicus.eduarte.entities.organisatie.*;
import nl.topicus.eduarte.entities.participatie.*;
import nl.topicus.eduarte.entities.participatie.enums.AbsentieSoort;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakHerhalingDag;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakHerhalingType;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakRol;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.DeelnemerPresentieRegistratie;
import nl.topicus.eduarte.entities.participatie.enums.ExterneAgendaConnection;
import nl.topicus.eduarte.entities.participatie.settings.DeelnemerportaalMeldingstermijnSetting;
import nl.topicus.eduarte.entities.participatie.settings.DeelnemerportaalOneindigeAbsentieMeldingEnabledSetting;
import nl.topicus.eduarte.entities.participatie.settings.DeelnemerportaalWelkomsttekstSetting;
import nl.topicus.eduarte.entities.personen.*;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.SoortProductregel;
import nl.topicus.eduarte.entities.productregel.ToegestaanDeelgebied;
import nl.topicus.eduarte.entities.productregel.ToegestaanOnderwijsproduct;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdracht;
import nl.topicus.eduarte.entities.rapportage.DeelnemerZoekOpdrachtRecht;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateRecht;
import nl.topicus.eduarte.entities.rapportage.OnderwijsDocumentTemplate;
import nl.topicus.eduarte.entities.resultaatstructuur.*;
import nl.topicus.eduarte.entities.security.authentication.*;
import nl.topicus.eduarte.entities.security.authentication.vasco.Token;
import nl.topicus.eduarte.entities.security.authorization.Recht;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.entities.settings.*;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.BookmarkConstructorArgument;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.Signaal;
import nl.topicus.eduarte.entities.signalering.settings.AbstractEventAbonnementConfiguration;
import nl.topicus.eduarte.entities.signalering.settings.DeadlineEventAbonnementConfiguration;
import nl.topicus.eduarte.entities.signalering.settings.EventAbonnementSetting;
import nl.topicus.eduarte.entities.signalering.settings.GlobaalAbonnementSetting;
import nl.topicus.eduarte.entities.signalering.settings.MedewerkerDeelnemerAbonnering;
import nl.topicus.eduarte.entities.signalering.settings.MedewerkerGroepAbonnering;
import nl.topicus.eduarte.entities.signalering.settings.PersoonlijkAbonnementSetting;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementMBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.taxonomie.VerbintenisgebiedOnderdeel;
import nl.topicus.eduarte.entities.taxonomie.educatie.BasiseducatieElementcode;
import nl.topicus.eduarte.entities.taxonomie.educatie.BasiseducatieVak;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleiding;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleidingAanbod;
import nl.topicus.eduarte.entities.taxonomie.mbo.AbstractMBOVerbintenisgebied;
import nl.topicus.eduarte.entities.taxonomie.mbo.Deelkwalificatie;
import nl.topicus.eduarte.entities.taxonomie.mbo.Kwalificatie;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.*;
import nl.topicus.eduarte.entities.taxonomie.vo.Elementcode;
import nl.topicus.eduarte.entities.taxonomie.vo.LandelijkVak;
import nl.topicus.eduarte.entities.taxonomie.wetinburgering.DoelInburgering;
import nl.topicus.eduarte.entities.taxonomie.wetinburgering.InburgeringExamenonderdeel;
import nl.topicus.eduarte.entities.vasco.VascoTokensImporterenJobRun;
import nl.topicus.eduarte.entities.vrijevelden.*;

import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.dialect.function.MultiArgCastFunction;

/**
 * 
 */
public class CoreModuleHibernateInitializer implements HibernateInitializer, InterceptorFactory
{
	@Override
	public Interceptor createInterceptor(SessionFactory sessionFactory)
	{
		return new EntiteitAuditInterceptor();
	}

	/**
	 * @see nl.topicus.cobra.modules.HibernateInitializer#initializeHibernate(org.hibernate.cfg.AnnotationConfiguration)
	 */
	@Override
	public void initializeHibernate(AnnotationConfiguration config)
	{
		config.addSqlFunction("cast", new MultiArgCastFunction());
		initAutorisatie(config);
		initBasis(config);
		initBijlage(config);
		initBPV(config);
		initContract(config);
		initPersonen(config);
		initOpleidingen(config);
		initAdres(config);
		initLandelijk(config);
		initSettings(config);
		initKwalificatie(config);
		initCGO(config);
		initMBO(config);
		initVO(config);
		initHO(config);
		initBasiseducatie(config);
		initInburgering(config);
		initGroep(config);
		initInschrijving(config);
		initSidebar(config);
		initOnderwijscatalogus(config);
		initResultaatstructuur(config);
		initProductregels(config);
		initRapportage(config);
		initJobs(config);
		initExamens(config);
		initCriteriumbank(config);
		initVrijVeld(config);
		initParticipatie(config);
		initSignalering(config);
		initKenmerken(config);
		initDbs(config);
	}

	private void initDbs(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(NietTonenInZorgvierkant.class);

		initBijlagen(config);
		initBijzonderheden(config);
		initGedrag(config);
		initTesten(config);
		initTrajecten(config);
		initTrajecttemplates(config);
		initIrisIncidenten(config);
	}

	private void initBijlagen(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(BegeleidingsBijlage.class);
		config.addAnnotatedClass(BijzonderheidBijlage.class);
		config.addAnnotatedClass(IncidentBijlage.class);
		config.addAnnotatedClass(NotitieBijlage.class);
		config.addAnnotatedClass(TestBijlage.class);
		config.addAnnotatedClass(TrajectBijlage.class);
		config.addAnnotatedClass(TrajectTemplateBijlage.class);

	}

	private void initBijzonderheden(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Bijzonderheid.class);
		config.addAnnotatedClass(BijzonderheidNietTonenInZorgvierkant.class);
		config.addAnnotatedClass(BijzonderheidCategorie.class);
		config.addAnnotatedClass(Hulpmiddel.class);
		config.addAnnotatedClass(ToegekendHulpmiddel.class);
		config.addAnnotatedClass(ToegestaanHulpmiddel.class);
	}

	private void initGedrag(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(BetrokkenMedewerker.class);
		config.addAnnotatedClass(Gedrag.class);
		config.addAnnotatedClass(IrisIncidentNietTonenInZorgvierkant.class);
		config.addAnnotatedClass(NotitieNietTonenInZorgvierkant.class);
		config.addAnnotatedClass(Incident.class);
		config.addAnnotatedClass(IncidentCategorie.class);
		config.addAnnotatedClass(Notitie.class);
	}

	private void initIrisIncidenten(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(IrisIncident.class);
		config.addAnnotatedClass(IrisBetrokkene.class);
		config.addAnnotatedClass(IrisBetrokkeneAfhandeling.class);
		config.addAnnotatedClass(IrisBetrokkeneMotief.class);
		config.addAnnotatedClass(IrisIncidentLocatie.class);
		config.addAnnotatedClass(IrisIncidentVoorwerp.class);
		config.addAnnotatedClass(IrisIncidentBijlage.class);
		config.addAnnotatedClass(IrisKoppelingKey.class);
	}

	private void initTesten(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(DeelnemerTest.class);
		config.addAnnotatedClass(DeelnemerTestNietTonenInZorgvierkant.class);
		config.addAnnotatedClass(GroepTest.class);
		config.addAnnotatedClass(NumeriekeVeldwaarde.class);
		config.addAnnotatedClass(TekstueleVeldwaarde.class);
		config.addAnnotatedClass(TestCategorie.class);
		config.addAnnotatedClass(TestDefinitie.class);
		config.addAnnotatedClass(TestVeld.class);
		config.addAnnotatedClass(Veldwaarde.class);
	}

	private void initTrajecten(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Aanleiding.class);
		config.addAnnotatedClass(BegeleidingsHandeling.class);
		config.addAnnotatedClass(BegeleidingsHandelingStatusovergang.class);
		config.addAnnotatedClass(GeplandeBegeleidingsHandeling.class);
		config.addAnnotatedClass(Gesprek.class);
		config.addAnnotatedClass(GesprekSamenvattingTemplate.class);
		config.addAnnotatedClass(GesprekSamenvattingZin.class);
		config.addAnnotatedClass(GesprekSoort.class);
		config.addAnnotatedClass(MogelijkeAanleiding.class);
		config.addAnnotatedClass(Taak.class);
		config.addAnnotatedClass(TaakSoort.class);
		config.addAnnotatedClass(TestAfname.class);
		config.addAnnotatedClass(ToegestaneStatusSoort.class);
		config.addAnnotatedClass(Traject.class);
		config.addAnnotatedClass(TrajectNietTonenInZorgvierkant.class);
		config.addAnnotatedClass(TrajectSoort.class);
		config.addAnnotatedClass(TrajectStatusovergang.class);
		config.addAnnotatedClass(TrajectStatusSoort.class);
		config.addAnnotatedClass(TrajectUitvoerder.class);
		config.addAnnotatedClass(VervolgHandeling.class);
	}

	private void initTrajecttemplates(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(AanleidingTemplate.class);
		config.addAnnotatedClass(AanwezigenTemplate.class);
		config.addAnnotatedClass(BegeleidingsHandelingTemplate.class);
		config.addAnnotatedClass(EigenaarTemplate.class);
		config.addAnnotatedClass(GekoppeldeTemplate.class);
		config.addAnnotatedClass(GeplandeBegeleidingsHandelingTemplate.class);
		config.addAnnotatedClass(GesprekTemplate.class);
		config.addAnnotatedClass(PlanningTemplate.class);
		config.addAnnotatedClass(TaakTemplate.class);
		config.addAnnotatedClass(TestAfnameTemplate.class);
		config.addAnnotatedClass(TrajectBegeleidingsHandelingTemplate.class);
		config.addAnnotatedClass(TrajectTemplate.class);
		config.addAnnotatedClass(TrajectTemplateAutomatischeKoppeling.class);
		config.addAnnotatedClass(TrajectTemplateKoppeling.class);
		config.addAnnotatedClass(TrajectTemplateKoppelingOpleiding.class);
		config.addAnnotatedClass(TrajectTemplateKoppelingOrganisatie.class);
		config.addAnnotatedClass(TrajectTemplateKoppelingKenmerk.class);
	}

	private void initVrijVeld(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(VrijVeld.class);
		config.addAnnotatedClass(VrijVeldOptieKeuze.class);
		config.addAnnotatedClass(VrijVeldKeuzeOptie.class);
		config.addAnnotatedClass(ExterneOrganisatieVrijVeld.class);
		config.addAnnotatedClass(MedewerkerVrijVeld.class);
		config.addAnnotatedClass(PersoonVrijVeld.class);
		config.addAnnotatedClass(PlaatsingVrijVeld.class);
		config.addAnnotatedClass(VerbintenisVrijVeld.class);
		config.addAnnotatedClass(OpleidingVrijVeld.class);
		config.addAnnotatedClass(GroepVrijVeld.class);
		config.addAnnotatedClass(ContractVrijVeld.class);
		config.addAnnotatedClass(IntakegesprekVrijVeld.class);
		config.addAnnotatedClass(OnderwijsproductVrijVeld.class);
		config.addAnnotatedClass(VooropleidingVrijVeld.class);
		config.addAnnotatedClass(RelatieVrijVeld.class);
		config.addAnnotatedClass(BPVInschrijvingVrijVeld.class);
	}

	private void initRapportage(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(DocumentTemplate.class);
		config.addAnnotatedClass(DocumentTemplateRecht.class);
		config.addAnnotatedClass(OnderwijsDocumentTemplate.class);
		config.addAnnotatedClass(DeelnemerZoekOpdracht.class);
		config.addAnnotatedClass(DeelnemerZoekOpdrachtRecht.class);
	}

	private void initBijlage(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Bijlage.class);
		config.addAnnotatedClass(InstellingsLogo.class);
		config.addAnnotatedClass(PersoonBijlage.class);
		config.addAnnotatedClass(ExterneOrganisatieBijlage.class);
		config.addAnnotatedClass(DeelnemerBijlage.class);
		config.addAnnotatedClass(VerbintenisBijlage.class);
		config.addAnnotatedClass(BPVInschrijvingBijlage.class);
		config.addAnnotatedClass(GroepBijlage.class);
		config.addAnnotatedClass(OnderwijsproductBijlage.class);
		config.addAnnotatedClass(ExamendeelnameBijlage.class);
		config.addAnnotatedClass(DocumentCategorie.class);
		config.addAnnotatedClass(DocumentType.class);
	}

	private void initBPV(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(BPVBedrijfsgegeven.class);
		config.addAnnotatedClass(BPVInschrijving.class);
		config.addAnnotatedClass(BPVColoPlaats.class);
		config.addAnnotatedClass(BPVCriteria.class);
		config.addAnnotatedClass(BPVCriteriaBPVDeelnemerProfiel.class);
		config.addAnnotatedClass(BPVCriteriaBPVKandidaat.class);
		config.addAnnotatedClass(BPVCriteriaBPVPlaats.class);
		config.addAnnotatedClass(BPVCriteriaExterneOrganisatie.class);
		config.addAnnotatedClass(BPVCriteriaOnderwijsproduct.class);
		config.addAnnotatedClass(BPVDeelnemerProfiel.class);
		config.addAnnotatedClass(BPVKandidaat.class);
		config.addAnnotatedClass(BPVMatch.class);
		config.addAnnotatedClass(BPVPlaats.class);
		config.addAnnotatedClass(BPVPlaatsOpleiding.class);
		config.addAnnotatedClass(BPVKandidaatOnderwijsproduct.class);
	}

	private void initContract(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Contract.class);
		config.addAnnotatedClass(ContractOnderdeel.class);
		config.addAnnotatedClass(ContractVerplichting.class);
		config.addAnnotatedClass(SoortContract.class);
		config.addAnnotatedClass(SoortContractVerplichting.class);
		config.addAnnotatedClass(TypeFinanciering.class);
	}

	private void initOpleidingen(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Opleidingsvariant.class);
		config.addAnnotatedClass(Opleiding.class);
		config.addAnnotatedClass(OpleidingAanbod.class);
		config.addAnnotatedClass(OpleidingBijlage.class);
		config.addAnnotatedClass(Team.class);
	}

	private void initSidebar(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Bookmark.class);
		config.addAnnotatedClass(BookmarkConstructorArgument.class);
		config.addAnnotatedClass(BookmarkFolder.class);
	}

	private void initInschrijving(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Plaatsing.class);
		config.addAnnotatedClass(PlaatsingVrijVeld.class);
		config.addAnnotatedClass(Aanmelding.class);
		config.addAnnotatedClass(Verbintenis.class);

		config.addAnnotatedClass(Intakegesprek.class);
		config.addAnnotatedClass(UitkomstIntakegesprek.class);
		config.addAnnotatedClass(VerbintenisVrijVeld.class);
		config.addAnnotatedClass(VerbintenisContract.class);
		config.addAnnotatedClass(RedenUitschrijving.class);
		config.addAnnotatedClass(Bekostigingsperiode.class);
		config.addAnnotatedClass(Schooladvies.class);
		config.addAnnotatedClass(SoortVooropleiding.class);
		config.addAnnotatedClass(Vooropleiding.class);
		config.addAnnotatedClass(Vervolgonderwijs.class);
		config.addAnnotatedClass(VooropleidingVak.class);
	}

	private void initGroep(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Groep.class);
		config.addAnnotatedClass(Groepsdeelname.class);
		config.addAnnotatedClass(GroepDocent.class);
		config.addAnnotatedClass(GroepMentor.class);
		config.addAnnotatedClass(Groepstype.class);
	}

	private void initKwalificatie(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Deelgebied.class);
		config.addAnnotatedClass(Taxonomie.class);
		config.addAnnotatedClass(TaxonomieElement.class);
		config.addAnnotatedClass(TaxonomieElementMBOLeerweg.class);
		config.addAnnotatedClass(TaxonomieElementType.class);
		config.addAnnotatedClass(Verbintenisgebied.class);
		config.addAnnotatedClass(VerbintenisgebiedOnderdeel.class);
	}

	private void initCGO(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Beoordeling.class);
		config.addAnnotatedClass(Competentie.class);
		config.addAnnotatedClass(CompetentieComponent.class);
		config.addAnnotatedClass(CompetentieMatrix.class);
		config.addAnnotatedClass(CompetentieNiveauVerzameling.class);
		config.addAnnotatedClass(CompetentieNiveau.class);
		config.addAnnotatedClass(LokaalCompetentieMaximum.class);
		config.addAnnotatedClass(Diplomagebied.class);
		config.addAnnotatedClass(DeelnemerMatrix.class);
		config.addAnnotatedClass(Kerntaak.class);
		config.addAnnotatedClass(Kwalificatiedossier.class);
		config.addAnnotatedClass(Leerpunt.class);
		config.addAnnotatedClass(LeerpuntComponent.class);
		config.addAnnotatedClass(LeerpuntVaardigheid.class);
		config.addAnnotatedClass(LLBMatrix.class);
		config.addAnnotatedClass(Meeteenheid.class);
		config.addAnnotatedClass(MeeteenheidWaarde.class);
		config.addAnnotatedClass(ModerneTaal.class);
		config.addAnnotatedClass(Opleidingsdomein.class);
		config.addAnnotatedClass(Taalbeoordeling.class);
		config.addAnnotatedClass(Taalscore.class);
		config.addAnnotatedClass(TaalscoreNiveauVerzameling.class);
		config.addAnnotatedClass(Taalkeuze.class);
		config.addAnnotatedClass(TaalType.class);
		config.addAnnotatedClass(TaalTypeKoppel.class);
		config.addAnnotatedClass(Taalvaardigheid.class);
		config.addAnnotatedClass(Taalvaardigheidseis.class);
		config.addAnnotatedClass(TaalvaardigheidseisLlb.class);
		config.addAnnotatedClass(Uitstroom.class);
		config.addAnnotatedClass(Vaardigheid.class);
		config.addAnnotatedClass(Werkproces.class);
		config.addAnnotatedClass(MeeteenheidKoppel.class);
		config.addAnnotatedClass(Groepsbeoordeling.class);
		config.addAnnotatedClass(GroepsbeoordelingOverschrijving.class);
		config.addAnnotatedClass(IJkpunt.class);
		config.addAnnotatedClass(RapportageTemplateIJkpunt.class);
		config.addAnnotatedClass(RapportageTemplate.class);
		config.addAnnotatedClass(SamenvoegenHtmlConfig.class);
		config.addAnnotatedClass(SamenvoegenPdfConfig.class);
		config.addAnnotatedClass(VoortgangHtmlConfig.class);
		config.addAnnotatedClass(VoortgangPdfConfig.class);
		config.addAnnotatedClass(VrijeMatrix.class);
	}

	private void initMBO(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(AbstractMBOVerbintenisgebied.class);
		config.addAnnotatedClass(Kwalificatie.class);
		config.addAnnotatedClass(Deelkwalificatie.class);
	}

	private void initVO(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Elementcode.class);
		config.addAnnotatedClass(LandelijkVak.class);
	}

	private void initHO(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(CrohoOpleiding.class);
		config.addAnnotatedClass(CrohoOpleidingAanbod.class);
		config.addAnnotatedClass(Fase.class);
		config.addAnnotatedClass(OpleidingFase.class);
		config.addAnnotatedClass(SoortVooropleidingHO.class);
		config.addAnnotatedClass(SoortVooropleidingBuitenlands.class);

		config.addAnnotatedClass(Inschrijvingsverzoek.class);
		config.addAnnotatedClass(SpecifiekeVraag.class);
		config.addAnnotatedClass(SpecifiekeVraagAntwoord.class);
		config.addAnnotatedClass(VooropleidingSignaalcode.class);
		config.addAnnotatedClass(VooropleidingVakResultaat.class);
		config.addAnnotatedClass(Instroommoment.class);
		config.addAnnotatedClass(StudielinkBericht.class);
		config.addAnnotatedClass(OnderwijsproductAanbodPeriode.class);
		config.addAnnotatedClass(AanbodPeriode.class);
		config.addAnnotatedClass(OPAanbodPeriodeOPAfname.class);
	}

	private void initBasiseducatie(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(BasiseducatieElementcode.class);
		config.addAnnotatedClass(BasiseducatieVak.class);
	}

	private void initInburgering(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(DoelInburgering.class);
		config.addAnnotatedClass(InburgeringExamenonderdeel.class);
	}

	private void initBasis(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(DBVersionMark.class);
		config.addAnnotatedClass(Entiteit.class);
		config.addAnnotatedClass(NaamEntiteit.class);
		config.addAnnotatedClass(Organisatie.class);
		config.addAnnotatedClass(Beheer.class);
		config.addAnnotatedClass(Instelling.class);
		config.addAnnotatedClass(OrganisatieEenheid.class);
		config.addAnnotatedClass(SoortOrganisatieEenheid.class);
		config.addAnnotatedClass(OrganisatieEenheidAdres.class);
		config.addAnnotatedClass(OrganisatieEenheidContactgegeven.class);
		config.addAnnotatedClass(OrganisatieEenheidContactPersoon.class);
		config.addAnnotatedClass(Locatie.class);
		config.addAnnotatedClass(LocatieAdres.class);
		config.addAnnotatedClass(LocatieContactgegeven.class);
		config.addAnnotatedClass(OrganisatieEenheidLocatie.class);
		config.addAnnotatedClass(AbstractRelatie.class);
		config.addAnnotatedClass(ExterneOrganisatie.class);
		config.addAnnotatedClass(ExterneOrganisatieAdres.class);
		config.addAnnotatedClass(ExterneOrganisatieContactgegeven.class);
		config.addAnnotatedClass(ExterneOrganisatieContactPersoon.class);
		config.addAnnotatedClass(ExterneOrganisatieContactPersoonRol.class);
		config.addAnnotatedClass(ExterneOrganisatieOpmerking.class);
		config.addAnnotatedClass(ExterneOrganisatiePraktijkbegeleider.class);
		config.addAnnotatedClass(SoortExterneOrganisatie.class);
		config.addAnnotatedClass(Brin.class);
		config.addAnnotatedClass(ModuleAfname.class);
	}

	private void initLandelijk(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Cohort.class);
		config.addAnnotatedClass(Gemeente.class);
		config.addAnnotatedClass(Land.class);
		config.addAnnotatedClass(Nationaliteit.class);
		config.addAnnotatedClass(Provincie.class);
		config.addAnnotatedClass(Regio.class);
		config.addAnnotatedClass(Plaats.class);
		config.addAnnotatedClass(Verblijfsvergunning.class);
	}

	private void initAdres(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Adres.class);
		config.addAnnotatedClass(AdresEntiteit.class);
	}

	private void initPersonen(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(ContactPersoon.class);
		config.addAnnotatedClass(Deelnemer.class);
		config.addAnnotatedClass(Functie.class);
		config.addAnnotatedClass(RelatieSoort.class);
		config.addAnnotatedClass(Medewerker.class);
		config.addAnnotatedClass(OrganisatieMedewerker.class);
		config.addAnnotatedClass(Persoon.class);
		config.addAnnotatedClass(PersoonAdres.class);
		config.addAnnotatedClass(PersoonContactgegeven.class);
		config.addAnnotatedClass(PersoonVrijVeld.class);
		config.addAnnotatedClass(RedenUitDienst.class);
		config.addAnnotatedClass(Relatie.class);
		config.addAnnotatedClass(SoortContactgegeven.class);
		config.addAnnotatedClass(Voorvoegsel.class);
		config.addAnnotatedClass(PersoonExterneOrganisatie.class);
		config.addAnnotatedClass(PersoonExterneOrganisatieContactPersoon.class);
	}

	private void initAutorisatie(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Account.class);
		config.addAnnotatedClass(AccountRol.class);
		config.addAnnotatedClass(BeheerderAccount.class);
		config.addAnnotatedClass(MedewerkerAccount.class);
		config.addAnnotatedClass(DeelnemerAccount.class);
		config.addAnnotatedClass(DigitaalAanmelderAccount.class);
		config.addAnnotatedClass(ExterneOrganisatieContactPersoonAccount.class);
		config.addAnnotatedClass(Recht.class);
		config.addAnnotatedClass(Rol.class);
		config.addAnnotatedClass(Sessie.class);
		config.addAnnotatedClass(Token.class);
		config.addAnnotatedClass(VascoTokensImporterenJobRun.class);
	}

	private void initSettings(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(ExtendableDataViewComponentSetting.class);
		config.addAnnotatedClass(GroupPropertySetting.class);
		config.addAnnotatedClass(LoginSetting.class);
		config.addAnnotatedClass(OrganisatieSetting.class);
		config.addAnnotatedClass(OrganisatieEenheidSetting.class);
		config.addAnnotatedClass(PasswordSetting.class);
		config.addAnnotatedClass(ScreenSaverSetting.class);
		config.addAnnotatedClass(RadiusServerSetting.class);
		config.addAnnotatedClass(ResultaatControleSetting.class);
		config.addAnnotatedClass(GebruikLandelijkeExterneOrganisatiesSetting.class);
		config.addAnnotatedClass(OrganisatieIpAdresSetting.class);
		config.addAnnotatedClass(DebiteurNummerSetting.class);
		config.addAnnotatedClass(APIKeySetting.class);
		config.addAnnotatedClass(InstellingSequence.class);
		config.addAnnotatedClass(ManierVanAanmeldenSetting.class);
		config.addAnnotatedClass(MutatieBlokkedatumSetting.class);
	}

	private void initOnderwijscatalogus(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Aggregatieniveau.class);
		config.addAnnotatedClass(Curriculum.class);
		config.addAnnotatedClass(CurriculumOnderwijsproduct.class);
		config.addAnnotatedClass(Gebruiksmiddel.class);
		config.addAnnotatedClass(Leerstijl.class);
		config.addAnnotatedClass(Onderwijsproduct.class);
		config.addAnnotatedClass(OnderwijsproductAanbod.class);
		config.addAnnotatedClass(OnderwijsproductAfname.class);
		config.addAnnotatedClass(OnderwijsproductAfnameContext.class);
		config.addAnnotatedClass(OnderwijsproductGebruiksmiddel.class);
		config.addAnnotatedClass(OnderwijsproductNiveauaanduiding.class);
		config.addAnnotatedClass(OnderwijsproductOpvolger.class);
		config.addAnnotatedClass(OnderwijsproductSamenstelling.class);
		config.addAnnotatedClass(OnderwijsproductTaxonomie.class);
		config.addAnnotatedClass(OnderwijsproductVerbruiksmiddel.class);
		config.addAnnotatedClass(OnderwijsproductVoorwaarde.class);
		config.addAnnotatedClass(OnderwijsproductZoekterm.class);
		config.addAnnotatedClass(SoortOnderwijsproduct.class);
		config.addAnnotatedClass(SoortPraktijklokaal.class);
		config.addAnnotatedClass(TypeLocatie.class);
		config.addAnnotatedClass(TypeToets.class);
		config.addAnnotatedClass(Verbruiksmiddel.class);
	}

	private void initResultaatstructuur(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(DeelnemerResultaatVersie.class);
		config.addAnnotatedClass(DeelnemerToetsBevriezing.class);
		config.addAnnotatedClass(GroepResultaatZoekFilterInstelling.class);
		config.addAnnotatedClass(PersoonlijkeToetscode.class);
		config.addAnnotatedClass(Resultaat.class);
		config.addAnnotatedClass(Resultaatstructuur.class);
		config.addAnnotatedClass(ResultaatstructuurCategorie.class);
		config.addAnnotatedClass(ResultaatstructuurDeelnemer.class);
		config.addAnnotatedClass(ResultaatstructuurMedewerker.class);
		config.addAnnotatedClass(ResultaatZoekFilterInstelling.class);
		config.addAnnotatedClass(Schaal.class);
		config.addAnnotatedClass(Schaalwaarde.class);
		config.addAnnotatedClass(Scoreschaalwaarde.class);
		config.addAnnotatedClass(StandaardToetsCodeFilter.class);
		config.addAnnotatedClass(Toets.class);
		config.addAnnotatedClass(ToetsCodeFilter.class);
		config.addAnnotatedClass(ToetsCodeFilterOrganisatieEenheidLocatie.class);
		config.addAnnotatedClass(ToetsVerwijzing.class);
	}

	private void initProductregels(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Productregel.class);
		config.addAnnotatedClass(SoortProductregel.class);
		config.addAnnotatedClass(ToegestaanDeelgebied.class);
		config.addAnnotatedClass(ToegestaanOnderwijsproduct.class);
	}

	private void initJobs(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(JobRun.class);
		config.addAnnotatedClass(DataPanelExportJobRun.class);
		config.addAnnotatedClass(RapportageJobRun.class);
		config.addAnnotatedClass(JobRunDetail.class);
		config.addAnnotatedClass(PersistentJobSchedule.class);
		config.addAnnotatedClass(TerugdraaibareJobRun.class);
		config.addAnnotatedClass(ExactCsvExportJobRun.class);
		config.addAnnotatedClass(LvsCsvExportJobRun.class);
		config.addAnnotatedClass(AccountsImportJobRun.class);
	}

	private void initExamens(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Examendeelname.class);
		config.addAnnotatedClass(Examenstatus.class);
		config.addAnnotatedClass(ExamenstatusOvergang.class);
		config.addAnnotatedClass(ExamenWorkflow.class);
		config.addAnnotatedClass(ExamenWorkflowTaxonomie.class);
		config.addAnnotatedClass(ToegestaneBeginstatus.class);
		config.addAnnotatedClass(ToegestaneExamenstatusOvergang.class);
	}

	private void initCriteriumbank(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Criterium.class);
	}

	private void initParticipatie(AnnotationConfiguration config)
	{
		initParticipatieAbsentie(config);
		initParticipatiePersoonlijkeGroep(config);
		initParticipatieInloopCollege(config);
		initParticipatieMaatregel(config);
		initParticipatieWaarneming(config);
		initParticipatieAgenda(config);
		initParticipatieTijdIndeling(config);
		initParticipatieDeelnemerPortaal(config);

		config.addAnnotatedClass(Budget.class);
		config.addAnnotatedClass(CacheRegion.class);
		config.addAnnotatedClass(ParticipatieMaandOverzicht.class);
		config.addAnnotatedClass(DeelnemerMedewerkerGroep.class);
	}

	private void initParticipatieAbsentie(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(AbsentieMelding.class);
		config.addAnnotatedClass(AbsentieReden.class);
		config.addAnnotatedClass(AbsentieSoort.class);
		config.addAnnotatedClass(HerhalendeAbsentieMelding.class);
		config.addAnnotatedClass(DeelnemerPresentieRegistratie.class);
		config.addAnnotatedClass(IbgVerzuimmelding.class);
		config.addAnnotatedClass(IbgVerzuimdag.class);
		config.addAnnotatedClass(SSLCertificaat.class);
	}

	private void initParticipatiePersoonlijkeGroep(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(PersoonlijkeGroep.class);
		config.addAnnotatedClass(PersoonlijkeGroepDeelnemer.class);
		config.addAnnotatedClass(DeelnemerPersoonlijkeGroep.class);
	}

	private void initParticipatieInloopCollege(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(InloopCollege.class);
		config.addAnnotatedClass(InloopCollegeGroep.class);
		config.addAnnotatedClass(InloopCollegeOpleiding.class);
	}

	private void initParticipatieMaatregel(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Maatregel.class);
		config.addAnnotatedClass(MaatregelToekenning.class);
		config.addAnnotatedClass(MaatregelToekenningsRegel.class);
	}

	private void initParticipatieWaarneming(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Waarneming.class);
		config.addAnnotatedClass(ExterneWaarneming.class);
	}

	private void initParticipatieAgenda(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Afspraak.class);
		config.addAnnotatedClass(AfspraakBijlage.class);
		config.addAnnotatedClass(AfspraakDeelnemer.class);
		config.addAnnotatedClass(AfspraakHerhalingDag.class);
		config.addAnnotatedClass(AfspraakHerhalingType.class);
		config.addAnnotatedClass(AfspraakParticipant.class);
		config.addAnnotatedClass(AfspraakRol.class);
		config.addAnnotatedClass(AfspraakType.class);
		config.addAnnotatedClass(AfspraakTypeCategory.class);
		config.addAnnotatedClass(HerhalendeAfspraak.class);
		config.addAnnotatedClass(AgendaInstellingen.class);

		config.addAnnotatedClass(ExterneAgenda.class);
		config.addAnnotatedClass(ExterneAgendaConnection.class);
		config.addAnnotatedClass(ExterneAgendaException.class);
		config.addAnnotatedClass(ExterneAgendaKoppeling.class);

		config.addAnnotatedClass(ExternPersoon.class);
		config.addAnnotatedClass(ExternSysteem.class);

		config.addAnnotatedClass(GoogleCalendarConnection.class);
		config.addAnnotatedClass(GoogleCalendarKoppeling.class);
	}

	private void initParticipatieTijdIndeling(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(Basisrooster.class);
		config.addAnnotatedClass(DefaultLesweekIndeling.class);

		config.addAnnotatedClass(Periode.class);
		config.addAnnotatedClass(PeriodeIndeling.class);

		config.addAnnotatedClass(LesuurIndeling.class);
		config.addAnnotatedClass(LesdagIndeling.class);
		config.addAnnotatedClass(LesweekIndeling.class);
		config.addAnnotatedClass(LesweekIndelingOrganisatieEenheidLocatie.class);

		config.addAnnotatedClass(Vakantie.class);
	}

	private void initParticipatieDeelnemerPortaal(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(DeelnemerportaalOneindigeAbsentieMeldingEnabledSetting.class);
		config.addAnnotatedClass(DeelnemerportaalMeldingstermijnSetting.class);
		config.addAnnotatedClass(DeelnemerportaalWelkomsttekstSetting.class);
	}

	private void initSignalering(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(EventAbonnementSetting.class);
		config.addAnnotatedClass(GlobaalAbonnementSetting.class);
		config.addAnnotatedClass(MedewerkerDeelnemerAbonnering.class);
		config.addAnnotatedClass(MedewerkerGroepAbonnering.class);
		config.addAnnotatedClass(PersoonlijkAbonnementSetting.class);

		config.addAnnotatedClass(AbstractEventAbonnementConfiguration.class);
		config.addAnnotatedClass(DeadlineEventAbonnementConfiguration.class);

		config.addAnnotatedClass(Event.class);
		config.addAnnotatedClass(Signaal.class);
	}

	private void initKenmerken(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(DeelnemerKenmerk.class);
		config.addAnnotatedClass(MedewerkerKenmerk.class);
		config.addAnnotatedClass(ExterneOrganisatieKenmerk.class);
		config.addAnnotatedClass(Kenmerk.class);
		config.addAnnotatedClass(KenmerkCategorie.class);
	}
}