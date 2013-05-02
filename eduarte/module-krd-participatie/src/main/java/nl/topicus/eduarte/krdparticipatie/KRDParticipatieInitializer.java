package nl.topicus.eduarte.krdparticipatie;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.modules.InitializingModuleSupport;
import nl.topicus.cobra.web.components.form.AutoFormRegistry;
import nl.topicus.eduarte.dao.EduArteCriteriaInterceptor;

import org.springframework.beans.factory.InitializingBean;

/**
 * Initializes the krd-participatie module for use in EduArte. The
 * {@link InitializingBean} interface ensures that this module is initialized after the
 * Hibernate settings haven been processed. In the {@link #afterPropertiesSet()} method
 * all remaining initialization can happen.
 */
@SuppressWarnings("unused")
public class KRDParticipatieInitializer extends InitializingModuleSupport
{
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

	}

	public void initializeAutoFormRegistry()
	{
		initializeAutoFormRegistry(AutoFormRegistry.getInstance());
	}

	public static void initializeAutoFormRegistry(AutoFormRegistry autoFormReg)
	{
	}
}
