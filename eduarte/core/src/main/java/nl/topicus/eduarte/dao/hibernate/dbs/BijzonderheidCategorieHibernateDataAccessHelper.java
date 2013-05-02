package nl.topicus.eduarte.dao.hibernate.dbs;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.dbs.BijzonderheidCategorieDataAccessHelper;
import nl.topicus.eduarte.dao.hibernate.CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.BijzonderheidCategorie;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

public class BijzonderheidCategorieHibernateDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper<BijzonderheidCategorie, CodeNaamActiefZoekFilter<BijzonderheidCategorie>>
		implements BijzonderheidCategorieDataAccessHelper
{
	public BijzonderheidCategorieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}
}
