package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

public enum GraphType
{
	BAR("Staafdiagram"),
	BARCLUSTER("Geclusterd staafdiagram"),
	BOXPLOT("Boxplot"),
	SPIDER("Spindiagram");

	private String displayName;

	private GraphType(String displayName)
	{
		this.displayName = displayName;
	}

	@Override
	public String toString()
	{
		return displayName;
	}
}
