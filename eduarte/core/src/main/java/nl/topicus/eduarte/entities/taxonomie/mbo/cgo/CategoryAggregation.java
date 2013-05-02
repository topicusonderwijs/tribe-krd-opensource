package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

public enum CategoryAggregation
{
	WERKPROCESSEN("Werkprocessen"),

	KERNTAKEN("Kerntaken");

	private String label;

	CategoryAggregation(String label)
	{
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}
}
