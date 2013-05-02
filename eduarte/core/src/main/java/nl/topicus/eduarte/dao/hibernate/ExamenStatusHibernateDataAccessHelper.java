package nl.topicus.eduarte.dao.hibernate;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.ExamenStatusDataAccessHelper;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.Examenstatus;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author vandekamp
 */
public class ExamenStatusHibernateDataAccessHelper extends HibernateDataAccessHelper<Examenstatus>
		implements ExamenStatusDataAccessHelper
{

	/**
	 * Constructor
	 * 
	 * @param provider
	 */
	public ExamenStatusHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public Examenstatus getByNaam(String naam, ExamenWorkflow examenWorkflow)
	{
		Criteria criteria = createCriteria(Examenstatus.class);
		criteria.add(Restrictions.eq("examenWorkflow", examenWorkflow));
		criteria.add(Restrictions.eq("naam", naam));
		return cachedUnique(criteria);
	}

}
