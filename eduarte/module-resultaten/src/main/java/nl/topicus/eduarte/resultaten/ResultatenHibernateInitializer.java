package nl.topicus.eduarte.resultaten;

import nl.topicus.cobra.modules.HibernateInitializer;
import nl.topicus.eduarte.resultaten.entities.*;

import org.hibernate.cfg.AnnotationConfiguration;

/**
 * @author loite
 */
public class ResultatenHibernateInitializer implements HibernateInitializer
{

	@Override
	public void initializeHibernate(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(ResultaatstructurenImporterenJobRun.class);
		config.addAnnotatedClass(ResultaatstructurenImporterenJobRunDetail.class);
		config.addAnnotatedClass(ResultaatstructurenExporterenJobRun.class);
		config.addAnnotatedClass(ResultaatstructurenKopierenJobRun.class);
		config.addAnnotatedClass(ResultaatstructurenKopierenJobRunDetail.class);
		config.addAnnotatedClass(ResultatenHerberekenenJobRun.class);
		config.addAnnotatedClass(ResultatenImporterenJobRun.class);
		config.addAnnotatedClass(SeResultatenInlezenJobRun.class);
	}
}
