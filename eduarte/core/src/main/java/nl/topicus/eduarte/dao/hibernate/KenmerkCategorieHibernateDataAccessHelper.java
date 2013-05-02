package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.KenmerkCategorieDataAccessHelper;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;
import nl.topicus.eduarte.zoekfilters.KenmerkCategorieZoekFilter;

public class KenmerkCategorieHibernateDataAccessHelper
		extends
		CodeNaamActiefLandelijkOfInstellingEntiteitHibernateDataAccessHelper<KenmerkCategorie, KenmerkCategorieZoekFilter>
		implements KenmerkCategorieDataAccessHelper
{
	public KenmerkCategorieHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}
}
