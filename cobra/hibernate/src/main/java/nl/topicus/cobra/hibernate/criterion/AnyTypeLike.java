package nl.topicus.cobra.hibernate.criterion;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.engine.TypedValue;

/**
 * AnyTypeLike maakt het mogelijk om een like te doen met een willekeurig type. Hibernate
 * staat dit normaal gesproken niet toe, maar voor Oracle is het geen probleem.
 * 
 * @author papegaaij
 */
public final class AnyTypeLike extends SimpleExpression
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param propertyName
	 * @param value
	 * @param matchMode
	 */
	public AnyTypeLike(String propertyName, Object value, MatchMode matchMode)
	{
		super(propertyName, matchMode.toMatchString(value.toString()), " like ");
	}

	@Override
	@SuppressWarnings("deprecation")
	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException
	{
		TypedValue[] ret = super.getTypedValues(criteria, criteriaQuery);
		assert (ret.length == 1);
		return new TypedValue[] {new TypedValue(Hibernate.STRING, ret[0].getValue(),
			EntityMode.POJO)};
	}
}
