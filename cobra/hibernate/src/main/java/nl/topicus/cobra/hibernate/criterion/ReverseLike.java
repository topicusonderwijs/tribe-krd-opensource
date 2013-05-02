package nl.topicus.cobra.hibernate.criterion;

import java.sql.Types;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.TypedValue;
import org.hibernate.type.Type;

/**
 * Hibernate 'like' expression, maar dan andersom. De normale like expressie van Hibernate
 * geeft 'property like value'. Deze geeft 'value like property'.
 * 
 * @author loite
 */
public final class ReverseLike implements Criterion
{
	private static final long serialVersionUID = 1L;

	private final String propertyName;

	private final Object value;

	private boolean ignoreCase;

	private final String op = " like ";

	/**
	 * Constructor
	 * 
	 * @param propertyName
	 * @param value
	 */
	public ReverseLike(String propertyName, String value)
	{
		this.propertyName = propertyName;
		this.value = value;
	}

	/**
	 * @see org.hibernate.criterion.Criterion#getTypedValues(org.hibernate.Criteria,
	 *      org.hibernate.criterion.CriteriaQuery)
	 */
	@Override
	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException
	{
		Object icvalue = ignoreCase ? value.toString().toLowerCase() : value;
		return new TypedValue[] {criteriaQuery.getTypedValue(criteria, propertyName, icvalue)};
	}

	/**
	 * @see org.hibernate.criterion.Criterion#toSqlString(org.hibernate.Criteria,
	 *      org.hibernate.criterion.CriteriaQuery)
	 */
	@Override
	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
			throws HibernateException
	{
		String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
		Type type = criteriaQuery.getTypeUsingProjection(criteria, propertyName);
		StringBuffer fragment = new StringBuffer();
		if (columns.length > 1)
			fragment.append('(');
		SessionFactoryImplementor factory = criteriaQuery.getFactory();
		int[] sqlTypes = type.sqlTypes(factory);
		for (int i = 0; i < columns.length; i++)
		{
			fragment.append("?").append(getOp());
			boolean lower =
				ignoreCase && (sqlTypes[i] == Types.VARCHAR || sqlTypes[i] == Types.CHAR);
			if (lower)
			{
				fragment.append(factory.getDialect().getLowercaseFunction()).append('(');
			}
			fragment.append(columns[i]);
			if (lower)
				fragment.append(')');
			if (i < columns.length - 1)
				fragment.append(" and ");
		}
		if (columns.length > 1)
			fragment.append(')');
		return fragment.toString();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return value + getOp() + propertyName;
	}

	/**
	 * @return De operand
	 */
	protected final String getOp()
	{
		return op;
	}

}
