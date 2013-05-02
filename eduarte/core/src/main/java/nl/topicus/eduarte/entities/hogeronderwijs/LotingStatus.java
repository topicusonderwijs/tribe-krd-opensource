package nl.topicus.eduarte.entities.hogeronderwijs;

public enum LotingStatus implements KeyValue
{
	Ingeloot("I", "Ingeloot"),
	CentrUitgeloot("U", "Centraal uitgeloot"),
	DecentrUitgeloot("D", "Decentraal uitgeloot"),
	DecentrIngeloot("J", "Decentraal ingeloot"),
	VoorlPlaatsbewijs("V", "Voorlopig plaatsingsbewijs"),
	OnderVoorbIngel("O", "Onder voorbehoud ingeloot");

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

	private LotingStatus(String key)
	{
		this.key = key;
		this.value = name();
	}

	private LotingStatus(String key, String value)
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
		return !this.equals(CentrUitgeloot) && !this.equals(DecentrUitgeloot);
	}
}
