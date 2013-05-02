package nl.topicus.eduarte.dao.participatie.hibernate;

import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.participatie.helpers.AgendaInstellingenDataAccesHelper;
import nl.topicus.eduarte.entities.participatie.AgendaInstellingen;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.hibernate.Criteria;

/**
 * @author N Henzen
 */
public class AgendaInstellingenHibernateDataAccesHelper extends
		HibernateDataAccessHelper<AgendaInstellingen> implements AgendaInstellingenDataAccesHelper
{

	/**
	 * @param provider
	 */
	public AgendaInstellingenHibernateDataAccesHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public AgendaInstellingen getAgendaInstellingen(Persoon persoon)
	{
		Asserts.assertNotNull("persoon", persoon);
		AgendaInstellingen instellingen = null;
		Criteria criteria = createCriteria(AgendaInstellingen.class);
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("persoon", persoon);
		instellingen = cachedTypedUnique(criteria);
		if (instellingen == null)
		{
			instellingen = new AgendaInstellingen(persoon);
		}
		return instellingen;
	}
}
