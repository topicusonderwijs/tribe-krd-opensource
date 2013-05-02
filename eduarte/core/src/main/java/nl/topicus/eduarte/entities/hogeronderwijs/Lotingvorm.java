package nl.topicus.eduarte.entities.hogeronderwijs;


public enum Lotingvorm implements KeyValue
{
	DecentrLoting("I", "Decentrale loting"),
	CentrLoting("C", "Centrale loting"),
	GeenLoting("N", "Geen loting");

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

	private Lotingvorm(String key)
	{
		this.key = key;
		this.value = name();
	}

	private Lotingvorm(String key, String value)
	{
		this.key = key;
		this.value = value;
	}
}
