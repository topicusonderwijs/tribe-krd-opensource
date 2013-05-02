package nl.topicus.eduarte.krdparticipatie;

import nl.topicus.cobra.modules.HibernateInitializer;
import nl.topicus.eduarte.krdparticipatie.entities.AbsentiemeldingenImporterenJobRun;
import nl.topicus.eduarte.krdparticipatie.entities.AbsentiemeldingenImporterenJobRunDetail;
import nl.topicus.eduarte.krdparticipatie.entities.KRDWaarnemingenImporterenJobRun;
import nl.topicus.eduarte.krdparticipatie.entities.KRDWaarnemingenImporterenJobRunDetail;

import org.hibernate.cfg.AnnotationConfiguration;

public class KRDParticipatieHibernateInitializer implements HibernateInitializer
{
	@Override
	public void initializeHibernate(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(AbsentiemeldingenImporterenJobRun.class);
		config.addAnnotatedClass(AbsentiemeldingenImporterenJobRunDetail.class);
		config.addAnnotatedClass(KRDWaarnemingenImporterenJobRun.class);
		config.addAnnotatedClass(KRDWaarnemingenImporterenJobRunDetail.class);
	}
}
