package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.TestCategorieDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.testen.TestCategorie;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

public class TestCategorieHibernateDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper<TestCategorie, CodeNaamActiefZoekFilter<TestCategorie>>
		implements TestCategorieDataAccessHelper
{
	public TestCategorieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}
}
