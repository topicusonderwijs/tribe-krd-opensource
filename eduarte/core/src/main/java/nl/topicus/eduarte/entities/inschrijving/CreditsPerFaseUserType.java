package nl.topicus.eduarte.entities.inschrijving;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.hogeronderwijs.Hoofdfase;

import org.apache.wicket.util.lang.Objects;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

public class CreditsPerFaseUserType implements UserType
{
	private Map<Hoofdfase, Integer> decode(String encodedString)
	{
		if (StringUtil.isEmpty(encodedString))
			return null;

		Map<Hoofdfase, Integer> map = new HashMap<Hoofdfase, Integer>();
		String[] faseStrings = encodedString.split(";");
		for (String s : faseStrings)
		{
			if (s.length() == 0)
				continue;
			String[] faseString = s.split("=");
			if (faseString.length != 2)
				throw new IllegalArgumentException("Illegal credits per fase encoded string "
					+ encodedString);
			Hoofdfase fase = Hoofdfase.get(faseString[0]);
			if (fase == null)
				throw new IllegalArgumentException("Illegal credits per fase encoded string "
					+ encodedString);
			Integer credits = null;
			if (faseString[1].length() > 0)
				credits = Integer.valueOf(faseString[1]);
			map.put(fase, credits);
		}
		return map;
	}

	private String encode(Map<Hoofdfase, Integer> creditsPerFase)
	{
		if (creditsPerFase == null || creditsPerFase.isEmpty())
			return null;

		StringBuilder b = new StringBuilder();
		boolean first = true;
		for (Map.Entry<Hoofdfase, Integer> entry : creditsPerFase.entrySet())
		{
			Hoofdfase hoofdfase = entry.getKey();
			Integer credits = entry.getValue();
			if (credits != null)
			{
				if (!first)
					b.append(";");
				first = false;
				b.append(hoofdfase.getKey());
				b.append("=");
				b.append(credits);
			}
		}
		return b.toString();
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException
	{
		return decode((String) cached);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object deepCopy(Object value) throws HibernateException
	{
		if (value == null)
			return null;

		return new HashMap<Hoofdfase, Integer>((Map<Hoofdfase, Integer>) value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Serializable disassemble(Object value) throws HibernateException
	{
		if (value == null)
			return null;
		return encode((Map<Hoofdfase, Integer>) value);
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException
	{
		return Objects.equal(x, y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException
	{
		if (x != null)
			return x.hashCode();
		return 0;
	}

	@Override
	public boolean isMutable()
	{
		return true;
	}

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException
	{
		String code = rs.getString(names[0]);

		return decode(code);
	}

	@Override
	@SuppressWarnings( {"deprecation", "unchecked"})
	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException
	{
		if (value == null)
		{
			st.setNull(index, Hibernate.STRING.sqlType());
		}
		else
		{
			st.setString(index, encode((Map<Hoofdfase, Integer>) value));
		}
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException
	{
		return deepCopy(original);
	}

	@Override
	public Class< ? > returnedClass()
	{
		return Map.class;
	}

	@Override
	@SuppressWarnings("deprecation")
	public int[] sqlTypes()
	{
		return new int[] {Hibernate.STRING.sqlType()};
	}
}
