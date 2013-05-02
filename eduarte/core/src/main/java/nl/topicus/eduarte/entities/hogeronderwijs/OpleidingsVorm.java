package nl.topicus.eduarte.entities.hogeronderwijs;

public enum OpleidingsVorm implements KeyValue
{
	Voltijd("1"),
	Deeltijd("2"),
	Duaal("3");

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

	private OpleidingsVorm(String key)
	{
		this.key = key;
		this.value = name();
	}
}
