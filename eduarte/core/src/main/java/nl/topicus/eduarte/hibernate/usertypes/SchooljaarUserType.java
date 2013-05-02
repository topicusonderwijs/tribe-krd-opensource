package nl.topicus.eduarte.hibernate.usertypes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import nl.topicus.eduarte.entities.landelijk.Schooljaar;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * {@link UserType} voor het uit de database lezen en naar de database schrijven van
 * {@link Schooljaar} waardes. Een schooljaar wordt geserializeerd als "2008/2009".
 */
public class SchooljaarUserType implements UserType
{
	@Override
	public Object deepCopy(Object value) throws HibernateException
	{
		return value;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException
	{
		if (x == y)
			return true;
		if (x == null || y == null)
			return false;
		return x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException
	{
		return x.hashCode();
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException
	{
		return Schooljaar.valueOf((Integer) cached);
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException
	{
		return ((Schooljaar) value).getStartJaar();
	}

	@Override
	public boolean isMutable()
	{
		return false;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException
	{
		String value = rs.getString(names[0]);
		if (rs.wasNull())
			return null;

		return Schooljaar.parse(value);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException
	{
		if (value == null)
		{
			st.setNull(index, Hibernate.STRING.sqlType());
		}
		else
		{
			st.setString(index, ((Schooljaar) value).getOmschrijving());
		}
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException
	{
		return original;
	}

	@Override
	public Class< ? > returnedClass()
	{
		return Schooljaar.class;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int[] sqlTypes()
	{
		return new int[] {Hibernate.STRING.sqlType()};
	}
}
