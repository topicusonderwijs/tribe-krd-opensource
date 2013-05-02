package nl.topicus.eduarte.entities.vrijevelden;

public enum VrijVeldType
{
	TEKST("Tekst"),
	LANGETEKST("Lange tekst"),
	DATUM("Datum"),
	NUMERIEK("Numeriek"),
	AANKRUISVAK("Aankruisvak"),
	KEUZELIJST("Keuzelijst"),
	MULTISELECTKEUZELIJST("Multiselect keuzelijst");

	private String label;

	VrijVeldType(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
