package nl.topicus.eduarte.krd;

import nl.topicus.cobra.hibernate.InterceptorFactory;
import nl.topicus.cobra.modules.HibernateInitializer;
import nl.topicus.eduarte.krd.bron.BronHibernateInterceptor;
import nl.topicus.eduarte.krd.entities.*;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpuntLocatie;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzamelingenAanmakenJobRun;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.entities.bron.BronTerugkoppelbestandInlezenJobRun;
import nl.topicus.eduarte.krd.entities.bron.cfi.*;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestandVerschil;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.*;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOExamengegevens;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOInschrijvingen;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronVakGegegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.*;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkenJobRun;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkenJobsStarterJobRun;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerker;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersLog;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken.CollectiefAanmakenJobRun;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken.CollectiefAanmakenJobRunDetail;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv.BPVInschrijvingCollectiefEditJobRun;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv.BPVInschrijvingCollectiefEditJobRunDetail;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bronmutatie.CollectiefBronmutatieJobRun;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bronmutatie.CollectiefBronmutatieJobRunDetail;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.orgehdwijzigen.OrganisatieEenheidLocatieCollectiefWijzigenJobRun;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.orgehdwijzigen.OrganisatieEenheidLocatieCollectiefWijzigenJobRunDetail;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.verbintenis.VerbintenissenStatusEditJobRun;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.verbintenis.VerbintenissenStatusEditJobRunDetail;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.keuzescontroleren.DeelnemerKeuzesControlerenJobRun;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.keuzescontroleren.DeelnemerKeuzesControlerenJobRunDetail;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * @author loite
 */
public class KRDHibernateInitializer implements HibernateInitializer, InterceptorFactory
{
	/**
	 * Variabele voor wanneer je niet de BronHibernateInterceptor wilt gebruiken.
	 * Bijvoorbeeld bij update tasks.
	 */
	private boolean ignoreInterceptor = false;

	/**
	 * @see nl.topicus.cobra.modules.HibernateInitializer#initializeHibernate(org.hibernate.cfg.AnnotationConfiguration)
	 */
	@Override
	public void initializeHibernate(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(BronBatchBVE.class);
		config.addAnnotatedClass(BronBatchVOExamengegevens.class);
		config.addAnnotatedClass(BronBatchVOInschrijvingen.class);
		config.addAnnotatedClass(BronAanleverpunt.class);
		config.addAnnotatedClass(BronAanleverpuntLocatie.class);
		config.addAnnotatedClass(BronSchooljaarStatus.class);
		config.addAnnotatedClass(BronAanleverMelding.class);
		config.addAnnotatedClass(BronBveAanleverRecord.class);
		config.addAnnotatedClass(BronBveBatchgegevens.class);
		config.addAnnotatedClass(BronExamenresultaatVOMelding.class);
		config.addAnnotatedClass(BronExamenresultaatVOMelding.class);
		config.addAnnotatedClass(BronExamenverzameling.class);
		config.addAnnotatedClass(BronExamenverzamelingenAanmakenJobRun.class);
		config.addAnnotatedClass(BronInschrijvingsgegevensVOMelding.class);
		config.addAnnotatedClass(BronBveTerugkoppelbestand.class);
		config.addAnnotatedClass(BronTerugkoppelbestandInlezenJobRun.class);
		config.addAnnotatedClass(BronBveTerugkoppelMelding.class);
		config.addAnnotatedClass(BronBveTerugkoppelRecord.class);
		config.addAnnotatedClass(BronVakGegegevensVOMelding.class);
		config.addAnnotatedClass(BronVoBatchgegevens.class);
		config.addAnnotatedClass(BronVoExamenTerugkoppelMelding.class);
		config.addAnnotatedClass(BronVoInschrijvingTerugkoppelMelding.class);
		config.addAnnotatedClass(BronVoSignaal.class);
		config.addAnnotatedClass(BronVoTerugkoppelbestand.class);
		config.addAnnotatedClass(BronVoVakTerugkoppelMelding.class);

		config.addAnnotatedClass(CriteriaKopierenJobRun.class);
		config.addAnnotatedClass(CriteriaKopierenJobRunDetail.class);
		config.addAnnotatedClass(CriteriumbankControleJobRun.class);
		config.addAnnotatedClass(ProductregelsKopierenJobRun.class);
		config.addAnnotatedClass(ProductregelsKopierenJobRunDetail.class);
		config.addAnnotatedClass(OpleidingInrichtingExporterenJobRun.class);
		config.addAnnotatedClass(OpleidingInrichtingImporterenJobRun.class);
		config.addAnnotatedClass(OpleidingInrichtingImporterenJobRunDetail.class);

		config.addAnnotatedClass(BronCfiTerugmInlezenJobRun.class);
		config.addAnnotatedClass(BronCfiTerugmelding.class);
		config.addAnnotatedClass(BronCfiTerugmeldingRegel.class);
		config.addAnnotatedClass(BronCfiTerugmeldingBEKRegel.class);
		config.addAnnotatedClass(BronCfiTerugmeldingEXPRegel.class);
		config.addAnnotatedClass(BronCfiTerugmeldingSAGRegel.class);
		config.addAnnotatedClass(BronCfiTerugmeldingSINRegel.class);
		config.addAnnotatedClass(BronCfiTerugmeldingSBHRegel.class);
		config.addAnnotatedClass(BronCfiTerugmeldingSBLRegel.class);

		config.addAnnotatedClass(BronFotobestandInlezenJobRun.class);
		config.addAnnotatedClass(BronFotobestand.class);
		config.addAnnotatedClass(BronFotobestandVerschil.class);
		config.addAnnotatedClass(BronFotoBOBPVRecord.class);
		config.addAnnotatedClass(BronFotoBOControleRecordInstelling.class);
		config.addAnnotatedClass(BronFotoBODiplomaKwalificatieRecord.class);
		config.addAnnotatedClass(BronFotoBOInschrijvingRecord.class);
		config.addAnnotatedClass(BronFotoBOInstellingRecord.class);
		config.addAnnotatedClass(BronFotoBOOnderwijsontvangendeRecord.class);
		config.addAnnotatedClass(BronFotoBORecord.class);
		config.addAnnotatedClass(BronFotoRecord.class);
		config.addAnnotatedClass(BronFotoVEControleRecordInschrijving.class);
		config.addAnnotatedClass(BronFotoVEInschrijvingRecord.class);
		config.addAnnotatedClass(BronFotoVEInstellingRecord.class);
		config.addAnnotatedClass(BronFotoVEOnderwijsontvangendeRecord.class);
		config.addAnnotatedClass(BronFotoVERecord.class);
		config.addAnnotatedClass(BronFotoVEVaardigheidNT2Record.class);
		config.addAnnotatedClass(BronFotoVEVakBasiseducatieRecord.class);
		config.addAnnotatedClass(BronFotoVEVAVOExamenRecord.class);
		config.addAnnotatedClass(BronFotoVEVAVOExamenvakRecord.class);
		config.addAnnotatedClass(BronFotoVOInschrijvingRecord.class);
		config.addAnnotatedClass(BronFotoVOInstellingRecord.class);
		config.addAnnotatedClass(BronFotoVOOnderwijsontvangendeRecord.class);
		config.addAnnotatedClass(BronFotoVOOpleidingRecord.class);
		config.addAnnotatedClass(BronFotoVORecord.class);

		config.addAnnotatedClass(VerbintenissenStatusEditJobRun.class);
		config.addAnnotatedClass(VerbintenissenStatusEditJobRunDetail.class);
		config.addAnnotatedClass(BPVInschrijvingCollectiefEditJobRun.class);
		config.addAnnotatedClass(BPVInschrijvingCollectiefEditJobRunDetail.class);
		config.addAnnotatedClass(OrganisatieEenheidLocatieCollectiefWijzigenJobRun.class);
		config.addAnnotatedClass(OrganisatieEenheidLocatieCollectiefWijzigenJobRunDetail.class);
		config.addAnnotatedClass(CollectiefAanmakenJobRun.class);
		config.addAnnotatedClass(CollectiefAanmakenJobRunDetail.class);
		config.addAnnotatedClass(CollectiefBronmutatieJobRun.class);
		config.addAnnotatedClass(CollectiefBronmutatieJobRunDetail.class);

		config.addAnnotatedClass(DeelnemerKeuzesControlerenJobRun.class);
		config.addAnnotatedClass(DeelnemerKeuzesControlerenJobRunDetail.class);

		config.addAnnotatedClass(PersoonFotosInlezenJobRun.class);

		config.addAnnotatedClass(MutatieLogVerwerker.class);
		config.addAnnotatedClass(MutatieLogVerwerkersLog.class);
		config.addAnnotatedClass(MutatieLogVerwerkenJobRun.class);
		config.addAnnotatedClass(MutatieLogVerwerkenJobsStarterJobRun.class);

		config.addAnnotatedClass(AbstractGroepEvent.class);
		config.addAnnotatedClass(NieuweMentorVoorDeelnemerEvent.class);
	}

	@Override
	public Interceptor createInterceptor(SessionFactory sessionFactory)
	{
		if (ignoreInterceptor)
			return EmptyInterceptor.INSTANCE;

		return new BronHibernateInterceptor();
	}

	public boolean isIgnoreInterceptor()
	{
		return ignoreInterceptor;
	}

	public void setIgnoreInterceptor(boolean ignoreInterceptor)
	{
		this.ignoreInterceptor = ignoreInterceptor;
	}

}
