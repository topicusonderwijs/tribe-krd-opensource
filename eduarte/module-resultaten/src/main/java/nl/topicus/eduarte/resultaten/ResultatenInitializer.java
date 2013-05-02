package nl.topicus.eduarte.resultaten;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.modules.InitializingModuleSupport;
import nl.topicus.eduarte.dao.EduArteCriteriaInterceptor;
import nl.topicus.eduarte.resultaten.dao.helpers.ResultaatHerberekenenJobRunDataAccesshelper;
import nl.topicus.eduarte.resultaten.dao.hibernate.ResultaatHerberekenenJobRunHibernateDataAccesshelper;

import org.springframework.beans.factory.InitializingBean;

/**
 * Initializes the krd module for use in EduArte. The {@link InitializingBean} interface
 * ensures that this module is initialized after the Hibernate settings haven been
 * processed. In the {@link #afterPropertiesSet()} method all remaining initialization can
 * happen.
 */
public class ResultatenInitializer extends InitializingModuleSupport
{
	// private static final Logger log =
	// LoggerFactory.getLogger(ResultatenInitializer.class);

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
	 * Initializes the data access registry for the current key.
	 */
	public void initializeDataAccessRegistry()
	{
		initializeDataAccessHelpers();
	}

	private void initializeDataAccessHelpers()
	{
		DataAccessRegistry registry = DataAccessRegistry.getInstance();
		HibernateSessionProvider provider = getProvider();
		QueryInterceptor interceptor = new EduArteCriteriaInterceptor();

		// Op alfabetische volgorde, graag zo houden.
		registry.register(ResultaatHerberekenenJobRunDataAccesshelper.class,
			new ResultaatHerberekenenJobRunHibernateDataAccesshelper(provider, interceptor));
	}

	public void initializeAutoFormRegistry()
	{
		// AutoFormRegistry autoFormReg = AutoFormRegistry.getInstance();

	}
}
