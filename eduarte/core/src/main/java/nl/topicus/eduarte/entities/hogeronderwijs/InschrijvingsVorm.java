package nl.topicus.eduarte.entities.hogeronderwijs;

public enum InschrijvingsVorm implements KeyValue
{
	Extraneus("E"),
	Student("S");

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

	private InschrijvingsVorm(String key)
	{
		this.key = key;
		this.value = name();
	}
}
