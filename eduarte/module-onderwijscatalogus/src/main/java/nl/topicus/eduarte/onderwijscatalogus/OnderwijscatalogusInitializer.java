package nl.topicus.eduarte.onderwijscatalogus;

import nl.topicus.cobra.modules.InitializingModuleSupport;

import org.springframework.beans.factory.InitializingBean;

/**
 * Initializes the krd module for use in EduArte. The {@link InitializingBean} interface
 * ensures that this module is initialized after the Hibernate settings haven been
 * processed. In the {@link #afterPropertiesSet()} method all remaining initialization can
 * happen.
 */
public class OnderwijscatalogusInitializer extends InitializingModuleSupport
{
	// private static final Logger log =
	// LoggerFactory.getLogger(OnderwijscatalogusInitializer.class);

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
		// DataAccessRegistry registry = DataAccessRegistry.getInstance();
		// HibernateSessionProvider provider = getProvider();

		// Op alfabetische volgorde, graag zo houden.
	}

	public void initializeAutoFormRegistry()
	{
		// AutoFormRegistry autoFormReg = AutoFormRegistry.getInstance();

	}
}
