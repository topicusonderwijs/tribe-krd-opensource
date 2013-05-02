package nl.topicus.eduarte.entities.participatie.enums;

/**
 * Status van een uitnodiging bij een afspraak.
 * 
 * @author loite
 */
public enum UitnodigingStatus
{
	/**
	 * Participant is direct geplaatst.
	 */
	DIRECTE_PLAATSING("Direct geplaatst", true),
	/**
	 * Participant is uitgenodigd
	 */
	UITGENODIGD("Uitgenodigd", false),
	/**
	 * Participant heeft de uitnodiging geaccepteerd
	 */
	GEACCEPTEERD("Uitnodiging geaccepteerd", true),
	/**
	 * Participant heeft de uitnodiging geweigerd.
	 */
	GEWEIGERD("Uitnodiging geweigerd", false),
	/**
	 * Participant heeft zich ingeschreven voor een inloopcollege.
	 */
	INGETEKEND("Ingetekend voor het college", true);

	private String naam;

	/**
	 * Geeft aan of aanwezigheidsregistratie mogelijk is bij deze status.
	 */
	private boolean aanwezigheidsregistratieMogelijk;

	/**
	 * Constructor
	 * 
	 * @param naam
	 */
	UitnodigingStatus(String naam, boolean aanwezigheidsregistratieMogelijk)
	{
		this.naam = naam;
		this.aanwezigheidsregistratieMogelijk = aanwezigheidsregistratieMogelijk;
	}

	@Override
	public String toString()
	{
		return naam;
	}

	/**
	 * @return Returns the aanwezigheidsregistratieMogelijk.
	 */
	public boolean isAanwezigheidsregistratieMogelijk()
	{
		return aanwezigheidsregistratieMogelijk;
	}
}
