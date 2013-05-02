package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.IncidentCategorieDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.gedrag.IncidentCategorie;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

public class IncidentCategorieHibernateDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper<IncidentCategorie, CodeNaamActiefZoekFilter<IncidentCategorie>>
		implements IncidentCategorieDataAccessHelper
{
	public IncidentCategorieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}
}
