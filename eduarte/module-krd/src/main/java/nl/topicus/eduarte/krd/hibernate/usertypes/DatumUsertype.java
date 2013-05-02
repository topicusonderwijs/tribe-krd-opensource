package nl.topicus.eduarte.krd.hibernate.usertypes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * Slaat een GBA datum op als integer in de database (en als geserialiseerde waarde tbv
 * caching in Hibernate).
 * 
 * @author dashorst
 */
public class DatumUsertype implements UserType
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
		return Datum.valueOf((Long) cached);
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException
	{
		if (value != null)
			return ((Datum) value).getWaarde();
		return null;
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
		Integer value = rs.getInt(names[0]);
		if (rs.wasNull())
			return null;

		return Datum.valueOf(value);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException
	{
		if (value == null)
		{
			st.setNull(index, Hibernate.INTEGER.sqlType());
		}
		else
		{
			st.setLong(index, ((Datum) value).getWaarde());
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
		return Datum.class;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int[] sqlTypes()
	{
		return new int[] {Hibernate.INTEGER.sqlType()};
	}
}
