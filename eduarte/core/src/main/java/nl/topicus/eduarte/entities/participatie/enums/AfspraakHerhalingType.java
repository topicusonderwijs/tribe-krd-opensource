package nl.topicus.eduarte.entities.participatie.enums;

public enum AfspraakHerhalingType
{
	DAGELIJKS,
	WEKELIJKS,
	MAANDELIJKS;

	@Override
	public String toString()
	{
		return super.toString().toLowerCase();
	}
}
