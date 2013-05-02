package nl.topicus.eduarte.entities.participatie.enums;

public enum StandaardUitnodigingenVersturen
{
	NIET("Niet", false, false),
	HANDMATIGE_AFSPRAKEN("Alleen bij handmatig gemaakte afspraken", true, false),
	AUTOMATISCH_AFSPRAKEN("Alleen bij automatisch gemaakte afspraken", false, true),
	ALLE("Bij alle afspraken", true, true);

	private String naam;

	private boolean handmatig;

	private boolean automatisch;

	StandaardUitnodigingenVersturen(String naam, boolean handmatig, boolean automatisch)
	{
		this.naam = naam;
		this.handmatig = handmatig;
		this.automatisch = automatisch;
	}

	public String getNaam()
	{
		return naam;
	}

	public boolean isHandmatig()
	{
		return handmatig;
	}

	public boolean isAutomatisch()
	{
		return automatisch;
	}

	public boolean isEnabled(boolean handmatigeAfspraak)
	{
		return handmatigeAfspraak ? isHandmatig() : isAutomatisch();
	}

	@Override
	public String toString()
	{
		return naam;
	}

}
