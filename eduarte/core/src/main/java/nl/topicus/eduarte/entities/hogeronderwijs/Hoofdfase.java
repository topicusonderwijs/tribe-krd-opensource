package nl.topicus.eduarte.entities.hogeronderwijs;

public enum Hoofdfase implements KeyValue
{
	PropBach("D", "Propedeuse bachelor"),
	Bachelor("B"),
	Propedeuse("P"),
	Master("M"),
	Vervolgopl("V", "Vervolgopleiding"),
	Kandidaats("K"),
	Initieel("I"),
	AssDegree("A", "Associate Degree");

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

	private Hoofdfase(String key)
	{
		this.key = key;
		this.value = name();
	}

	private Hoofdfase(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString()
	{
		return getValue();
	}

	public static Hoofdfase get(String key)
	{
		for (Hoofdfase fase : values())
			if (fase.key.equals(key))
				return fase;
		return null;
	}
}
