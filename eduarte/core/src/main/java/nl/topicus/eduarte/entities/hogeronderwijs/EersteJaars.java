package nl.topicus.eduarte.entities.hogeronderwijs;

public enum EersteJaars
{
	NVT("Niet van toepassing"),
	Geaccepteerd("Geaccepteerd"),
	NietGeaccepteerd("Niet geaccepteerd");

	private String value;

	public String getValue()
	{
		return value;
	}

	private EersteJaars(String value)
	{
		this.value = value;
	}
}
