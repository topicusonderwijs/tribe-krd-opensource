package nl.topicus.cobra.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

/**
 * Hetzelfde als CriteriaBuilder, maar dan voor DetachedCriteria.
 * 
 * @author loite
 */
public class DetachedCriteriaBuilder extends AbstractCriteriaBuilder
{
	private final DetachedCriteria dc;

	public DetachedCriteriaBuilder(DetachedCriteria dc)
	{
		this.dc = dc;
	}

	public DetachedCriteria getCriteria()
	{
		return dc;
	}

	@Override
	protected void addCriterion(Criterion criterion)
	{
		getCriteria().add(criterion);
	}

	@Override
	public void createAlias(String associationPath, String alias)
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
