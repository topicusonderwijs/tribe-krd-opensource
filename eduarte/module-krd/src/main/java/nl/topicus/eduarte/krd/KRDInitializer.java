package nl.topicus.eduarte.krd;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.modules.InitializingModuleSupport;
import nl.topicus.cobra.web.components.form.AutoFormRegistry;
import nl.topicus.eduarte.dao.EduArteCriteriaInterceptor;
import nl.topicus.eduarte.dao.helpers.RedenUitDienstDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.RedenUitschrijvingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SchooladviesDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.personen.Functie;
import nl.topicus.eduarte.entities.personen.RedenUitDienst;
import nl.topicus.eduarte.krd.dao.helpers.*;
import nl.topicus.eduarte.krd.dao.hibernate.*;
import nl.topicus.eduarte.krd.web.components.choice.RedenUitDienstComboBox;
import nl.topicus.eduarte.web.components.choice.FunctieCombobox;
import nl.topicus.eduarte.web.components.choice.RedenUitschrijvingComboBox;

import org.springframework.beans.factory.InitializingBean;

/**
 * Initializes the krd module for use in EduArte. The {@link InitializingBean} interface
 * ensures that this module is initialized after the Hibernate settings haven been
 * processed. In the {@link #afterPropertiesSet()} method all remaining initialization can
 * happen.
 */
public class KRDInitializer extends InitializingModuleSupport
{
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
		registry.register(BronAanleverpuntDataAccessHelper.class,
			new BronAanleverpuntHibernateDataAccessHelper(provider, interceptor));
		registry.register(BronCfiTerugmeldingDataAccessHelper.class,
			new BronCfiTerugmeldingHibernateDataAccessHelper(provider, interceptor));
		registry.register(BronCfiTerugmeldingRegelDataAccessHelper.class,
			new BronCfiTerugmeldingRegelHibernateDataAccessHelper(provider, interceptor));
		registry.register(BronExamenverzamenlingDataAccessHelper.class,
			new BronExamenverzamelingHibernateDataAccessHelper(provider, interceptor));
		registry.register(BronFotobestandDataAccessHelper.class,
			new BronFotobestandHibernateDataAccessHelper(provider, interceptor));
		registry.register(BronFotobestandVerschilDataAccessHelper.class,
			new BronFotobestandVerschilHibernateDataAccessHelper(provider, interceptor));
		registry.register(BronFotoRecordDataAccessHelper.class,
			new BronFotoRecordHibernateDataAccessHelper(provider, interceptor));
		registry.register(BronSchooljaarStatusDataAccessHelper.class,
			new BronSchooljaarStatusHibernateDataAccessHelper(provider, interceptor));
		registry.register(ExamendeelnameDataAccessHelper.class,
			new ExamendeelnameHibernateDataAccessHelper(provider, interceptor));
		registry.register(BronDataAccessHelper.class, new BronHibernateDataAccessHelper(provider,
			interceptor));

		registry.register(MutatieLogVerwerkenJobRunDataAccessHelper.class,
			new MutatieLogVerwerkenJobRunHibernateDataAccesshelper(provider, interceptor));
		registry.register(MutatieLogVerwerkersDataAccessHelper.class,
			new MutatieLogVerwerkersHibernateDataAccessHelper(provider, interceptor));
		registry.register(MutatieLogVerwerkersLogDataAccessHelper.class,
			new MutatieLogVerwerkersLogHibernateDataAccessHelper(provider, interceptor));

		registry.register(RedenUitDienstDataAccessHelper.class,
			new RedenUitDienstHibernateDataAccessHelper(provider, interceptor));
		registry.register(RedenUitschrijvingDataAccessHelper.class,
			new RedenUitschrijvingHibernateDataAccessHelper(provider, interceptor));
		registry.register(SchooladviesDataAccessHelper.class,
			new SchooladviesHibernateDataAccessHelper(provider, interceptor));
		registry.register(SoortVooropleidingDataAccessHelper.class,
			new SoortVooropleidingHibernateDataAccessHelper(provider, interceptor));
	}

	public void initializeAutoFormRegistry()
	{
		initializeAutoFormRegistry(AutoFormRegistry.getInstance());
	}

	public static void initializeAutoFormRegistry(AutoFormRegistry autoFormReg)
	{
		autoFormReg.registerEditor(Functie.class, FunctieCombobox.class);
		autoFormReg.registerEditor(RedenUitDienst.class, RedenUitDienstComboBox.class);
		autoFormReg.registerEditor(RedenUitschrijving.class, RedenUitschrijvingComboBox.class);
	}
}
