package nl.topicus.eduarte.entities.hogeronderwijs;

public enum TaaltoetsStatus implements KeyValue
{
	NVT("N", "Niet van toepassing"),
	Open("O"),
	Afgewezen("A"),
	GeselGeslgdInst("G", "Geselecteerd/geslaagd volgens instelling"),
	GeselGeslgdIB("H", "Geselecteerd/geslaagd volgens IB-Groep");

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

	private TaaltoetsStatus(String key)
	{
		this.key = key;
		this.value = name();
	}

	private TaaltoetsStatus(String key, String value)
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
		return this.equals(NVT) || this.equals(GeselGeslgdIB) || this.equals(GeselGeslgdInst);
	}
}
