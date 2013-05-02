package nl.topicus.eduarte.entities.hogeronderwijs;

public enum VooropleidingVakVerificatieStatus implements KeyValue
{
	VakUitslagGeverif("UG", "Vak en uitslag geverifieerd"),
	VakGeverif("VG", "Vak geverifieerd"),
	VakNietGeverif("VN", "Vak niet geverifieerd"),
	NietGeverif("N", "Niet geverifieerd");

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

	private VooropleidingVakVerificatieStatus(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString()
	{
		return getValue();
	}
}
