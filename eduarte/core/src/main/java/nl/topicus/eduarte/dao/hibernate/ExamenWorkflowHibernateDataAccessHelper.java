package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.eduarte.dao.helpers.ExamenWorkflowDataAccessHelper;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author vandekamp
 */
public class ExamenWorkflowHibernateDataAccessHelper extends
		HibernateDataAccessHelper<ExamenWorkflow> implements ExamenWorkflowDataAccessHelper
{

	/**
	 * Constructor
	 * 
	 * @param provider
	 */
	public ExamenWorkflowHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	@Override
	public List<ExamenWorkflow> list()
	{
		Criteria criteria = createCriteria(ExamenWorkflow.class);
		return cachedTypedList(criteria);
	}

	@Override
	public ExamenWorkflow getByNaam(String naam)
	{
		Criteria criteria = createCriteria(ExamenWorkflow.class);
		criteria.add(Restrictions.eq("naam", naam));
		return cachedUnique(criteria);
	}

}
