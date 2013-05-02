package nl.topicus.eduarte.entities.hogeronderwijs;

public enum InschrijvingsverzoekStatus implements KeyValue
{
	AanmeldVervolg("B", "Aanmelding vervolg"),
	Inschrijving("I"),
	GeannlIngetrk("G", "Geannuleerd/ingetrokken"),
	Uitgeschreven("U"),
	StudieStaken("S", "Studie staken"),
	Afgewezen("R"),
	VerzoekInschr("V", "Verzoek tot inschrijving");

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

	private InschrijvingsverzoekStatus(String key)
	{
		this.key = key;
		this.value = name();
	}

	private InschrijvingsverzoekStatus(String key, String value)
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
