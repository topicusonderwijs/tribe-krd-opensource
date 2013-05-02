package nl.topicus.cobra.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;

/**
 * Helper class voor het construeren van Criteria objecten, zonder al die enorme lappen
 * code te hoeven schrijven.
 * 
 * @author Martijn Dashorst
 */
public class CriteriaBuilder extends AbstractCriteriaBuilder
{
	private final Criteria criteria;

	public CriteriaBuilder(Criteria criteria)
	{
		this.criteria = criteria;
	}

	public Criteria getCriteria()
	{
		return criteria;
	}

	@Override
	protected void addCriterion(Criterion criterion)
	{
		getCriteria().add(criterion);
	}

	@Override
	public void createAlias(String associationPath, String alias) throws HibernateException
	{
		if (aliases.add(associationPath))
			getCriteria().createAlias(associationPath, alias);
	}

	@Override
	public void createAlias(String associationPath, String alias, int joinType)
			throws HibernateException
	{
		if (aliases.add(associationPath))
			getCriteria().createAlias(associationPath, alias, joinType);
	}
}
