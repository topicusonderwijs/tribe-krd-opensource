package nl.topicus.eduarte.entities.hogeronderwijs;

public enum AanvullendeEisenStatus implements KeyValue
{
	NVT("N", "Niet van toepassing"),
	Open("O"),
	Afgewezen("A"),
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

	private AanvullendeEisenStatus(String key)
	{
		this.key = key;
		this.value = name();
	}

	private AanvullendeEisenStatus(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString()
	{
		return value;
	}

	public boolean isOK()
	{
		return this.equals(NVT) || this.equals(GeselGeslgd);
	}
}
