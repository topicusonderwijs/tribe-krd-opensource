package nl.topicus.eduarte.entities.hogeronderwijs;

public enum Betaler implements KeyValue
{
	Ander("A"),
	Ouders("O"),
	Zelf("Z"),
	Elders("E"),
	EerdDoorgegvn("X", "Eerder doorgegeven");

	private String key;

	private String value;

	@Override
	public String getKey()
	{
		return key;
	}

	@Override
	public String getValue()
	{
		return value;
	}

	private Betaler(String key)
	{
		this.key = key;
		this.value = name();
	}

	private Betaler(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString()
	{
		return value;
	}
}
