package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

public enum CategoryProperty
{
	WERKPROCESSEN("leerpunt.werkproces", "Werkprocessen"),

	COMPETENTIES("leerpunt.competentie", "Competenties");

	private String propertyLocation;

	private String label;

	CategoryProperty(String propertyLocation, String label)
	{
		this.propertyLocation = propertyLocation;
		this.label = label;
	}

	public String getPropertyLocation()
	{
		return propertyLocation;
	}

	public String getLabel()
	{
		return label;
	}
}
