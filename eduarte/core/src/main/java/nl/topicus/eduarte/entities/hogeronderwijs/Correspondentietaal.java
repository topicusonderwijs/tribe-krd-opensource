package nl.topicus.eduarte.entities.hogeronderwijs;

public enum Correspondentietaal implements KeyValue
{
	Nederlands("NL"),
	Engels("EN"),
	Duits("DE");

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

	private Correspondentietaal(String key)
	{
		this.key = key;
		this.value = name();
	}
}
