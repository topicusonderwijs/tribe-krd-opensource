package nl.topicus.cobra.templates.resolvers;

import java.sql.ResultSet;
import java.sql.SQLException;

import nl.topicus.cobra.templates.FieldInfo;

/**
 * @author hop
 */
public class ResultSetResolver implements FieldResolver
{
	private final ResultSet resultSet;

	/**
	 * @param resultSet
	 */
	public ResultSetResolver(ResultSet resultSet)
	{
		this.resultSet = resultSet;
	}

	/**
	 * @see nl.topicus.cobra.templates.resolvers.FieldResolver#getInfo(java.lang.String)
	 */
	@Override
	public FieldInfo getInfo(String name)
	{
		return null;
	}

	/**
	 * @see nl.topicus.cobra.templates.resolvers.FieldResolver#next(java.lang.String)
	 */
	@Override
	public Object next(String name)
	{
		Object result = null;
		try
		{
			if (resultSet.next())
				result = Boolean.TRUE;
		}
		catch (SQLException e)
		{
			// silently ignore; return null
		}
		return result;
	}

	/**
	 * @see nl.topicus.cobra.templates.resolvers.FieldResolver#resolve(java.lang.String)
	 */
	@Override
	public Object resolve(String name)
	{
		Object result = null;
		try
		{
			result = resultSet.getObject(name);
		}
		catch (SQLException e)
		{
			// silently ignore; return null
		}

		return result;
	}

}
