//$Id: MultiArgCastFunction.java,v 1.1 2008-02-26 09:24:58 marrink Exp $
package org.hibernate.dialect.function;

import java.util.List;

import org.hibernate.QueryException;
import org.hibernate.engine.Mapping;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.type.Type;
import org.hibernate.type.TypeFactory;

/**
 * This function is a workaraound for the inability of the original {@link CastFunction}
 * to properly parse other functions that have more then one argument. ANSI-SQL style
 * <tt>cast(foo as type)</tt> where the type is a Hibernate type
 * 
 * @author Gavin King
 */
public class MultiArgCastFunction implements SQLFunction
{

	/**
	 * @see org.hibernate.dialect.function.SQLFunction#getReturnType(org.hibernate.type.Type,
	 *      org.hibernate.engine.Mapping)
	 */
	@SuppressWarnings("deprecation")
	public Type getReturnType(Type columnType, Mapping mapping) throws QueryException
	{
		return columnType; // note there is a wierd implementation in the client side
	}

	/**
	 * @see org.hibernate.dialect.function.SQLFunction#hasArguments()
	 */
	public boolean hasArguments()
	{
		return true;
	}

	public boolean hasParenthesesIfNoArguments()
	{
		return true;
	}

	/**
	 * @see org.hibernate.dialect.function.SQLFunction#render(java.util.List,
	 *      org.hibernate.engine.SessionFactoryImplementor)
	 */
	@SuppressWarnings( {"unchecked", "deprecation"})
	public String render(List args, SessionFactoryImplementor factory) throws QueryException
	{
		if (args.size() < 2)
		{
			throw new QueryException("cast() requires at least two arguments");
		}
		String type = (String) args.get(args.size() - 1);
		int[] sqlTypeCodes = TypeFactory.heuristicType(type).sqlTypes(factory);
		if (sqlTypeCodes.length != 1)
		{
			throw new QueryException("invalid Hibernate type for cast()");
		}
		String sqlType = factory.getDialect().getCastTypeName(sqlTypeCodes[0]);
		if (sqlType == null)
		{
			// never reached, since getTypeName() actually throws an exception!
			sqlType = type;
		}
		/*
		 * else { //trim off the length/precision/scale int loc = sqlType.indexOf('('); if
		 * (loc>-1) { sqlType = sqlType.substring(0, loc); } }
		 */
		StringBuffer sb = new StringBuffer(args.get(0).toString());
		for (int i = 1; i < args.size() - 1; i++)
		{
			sb.append(',');
			sb.append(args.get(i));
		}
		return "cast(" + sb.toString() + " as " + sqlType + ')';
	}

}
