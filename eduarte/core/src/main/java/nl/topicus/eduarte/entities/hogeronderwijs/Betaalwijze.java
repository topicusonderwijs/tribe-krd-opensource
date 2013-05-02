package nl.topicus.eduarte.entities.hogeronderwijs;

public enum Betaalwijze implements KeyValue
{
	Acceptgiro("0"),
	MachtAfdrInst("1", "Machtiging af te drukken door instelling"),
	MachtAfdrStud("2", "Machtiging af te drukken door student"),
	BuitlndsReknr("3", "Buitenlands rekeningnummer"),
	Anders("4"),
	DigBetalen("5", "Digitaal betalen"),
	DigMachtiging("6", "Digitale machtiging");

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

	private Betaalwijze(String key)
	{
		this.key = key;
		this.value = name();
	}

	private Betaalwijze(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString()
	{
		return value;
	}
}
