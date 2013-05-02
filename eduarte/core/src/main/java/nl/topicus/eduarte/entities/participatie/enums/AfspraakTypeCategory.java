package nl.topicus.eduarte.entities.participatie.enums;

import java.awt.Color;

/**
 * Categorisering van afspraaktypes zodat het systeem default gedrag kan toekennen aan
 * afspraaktypes.
 * 
 * @author loite
 */
public enum AfspraakTypeCategory
{

	/**
	 * Individuele afspraken aangemaakt door de medewerker zelf.
	 */
	INDIVIDUEEL(Color.PINK, "Individueel"),
	/**
	 * Afspraken die afkomstig zijn uit het rooster.
	 */
	ROOSTER(Color.GREEN, "Rooster"),
	/**
	 * Priveafspraken
	 */
	PRIVE(Color.BLUE, "Prive"),
	/**
	 * Priveafspraken
	 */
	BESCHERMD(Color.YELLOW.darker(), "Beschermd"),
	/**
	 * Afspraken die uit externe agenda's worden opgehaald.
	 */
	EXTERN(Color.RED, "Externe agenda");

	private Color standaardKleur;

	private String naam;

	AfspraakTypeCategory(Color standaardKleur, String naam)
	{
		this.standaardKleur = standaardKleur;
		this.naam = naam;
	}

	public Color getStandaardKleur()
	{
		return standaardKleur;
	}

	public String getNaam()
	{
		return naam;
	}

	@Override
	public String toString()
	{
		return naam;
	}
}
