package nl.topicus.eduarte.entities.hogeronderwijs;

public enum WerkzaamhedenStatus implements KeyValue
{
	NVT("N", "Niet van toepassing"),
	Open("O", "Open"),
	Afgewezen("A", "Afgewezen"),
	GeselGeslgd("G", "Geselecteerd/geslaagd");

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

	private WerkzaamhedenStatus(String key)
	{
		this.key = key;
		this.value = name();
	}

	private WerkzaamhedenStatus(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString()
	{
		return value;
	}

	public Boolean isOK()
	{
		return this.equals(NVT) || this.equals(GeselGeslgd);
	}
}
