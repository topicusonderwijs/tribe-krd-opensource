package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

public enum BeoordelingsType
{
	EVC_EVK("EVC/EVK"),
	BEOORDELING("Hoofdbeoordeling"),
	DOCENTBEOORDELING("Docentbeoordeling");

	private String displayName;

	private BeoordelingsType(String displayName)
	{
		this.displayName = displayName;
	}

	@Override
	public String toString()
	{
		return displayName;
	}
}
