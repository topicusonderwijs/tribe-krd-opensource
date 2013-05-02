package nl.topicus.cobra.xml.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AbstractEnumAdapter<T extends Enum<T>> extends XmlAdapter<String, T>
{
	private Class<T> enumClass;

	public AbstractEnumAdapter(Class<T> enumClass)
	{
		this.enumClass = enumClass;
	}

	@Override
	public String marshal(T v)
	{
		if (v == null)
			return null;

		return v.name();
	}

	@Override
	public T unmarshal(String v)
	{
		if (v == null)
			return null;

		return Enum.valueOf(enumClass, v);
	}
}
