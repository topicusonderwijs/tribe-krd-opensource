package nl.topicus.eduarte.onderwijscatalogus;

import nl.topicus.cobra.modules.HibernateInitializer;
import nl.topicus.eduarte.onderwijscatalogus.entities.OndProdImportJobRunDetail;
import nl.topicus.eduarte.onderwijscatalogus.entities.OnderwijsproductImportJobRun;
import nl.topicus.eduarte.onderwijscatalogus.entities.ToegestaanOndProdImportJobRun;

import org.hibernate.cfg.AnnotationConfiguration;

/**
 * @author loite
 */
public class OnderwijscatalogusHibernateInitializer implements HibernateInitializer
{

	/**
	 * @see nl.topicus.cobra.modules.HibernateInitializer#initializeHibernate(org.hibernate.cfg.AnnotationConfiguration)
	 */
	@Override
	public void initializeHibernate(AnnotationConfiguration config)
	{
		config.addAnnotatedClass(OnderwijsproductImportJobRun.class);
		config.addAnnotatedClass(ToegestaanOndProdImportJobRun.class);
		config.addAnnotatedClass(OndProdImportJobRunDetail.class);
	}
}
