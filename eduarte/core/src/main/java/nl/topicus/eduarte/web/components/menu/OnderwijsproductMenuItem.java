package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * Menu-items voor het opleidingmenu.
 * 
 * @author loite
 */
public enum OnderwijsproductMenuItem implements AccessMenuItemKey
{
	/**
	 * Algemene opleidingkaart
	 */
	Algemeen('A'),
	/**
	 * Verschillende eigenaren (juridisch, gebruiksrecht, aanbieder)
	 */
	Eigenaar('E'),
	/**
	 * Verschillende 'versies' van het onderwijsproduct. Elke versie is een op zich staand
	 * product, maar er kan wel een opvolger voor een onderwijsproduct aangegeven worden.
	 */
	Opvolgers('O'),
	/**
	 * Koppeling met taxonomie
	 */
	Taxonomie('T'),
	/**
	 * Voorwaardelijke onderwijsproducten
	 */
	Voorwaarden('W'),
	/**
	 * Paklijst
	 */
	Paklijst('P'),
	/**
	 * Kalender
	 */
	Kalender('K'),
	/**
	 * Toegankelijkeheid
	 */
	Toegankelijkheid('G'),
	/**
	 * Personeel
	 */
	Personeel('P'),
	/**
	 * Algemeen tabje voor verbruiksmiddelen en gebruiksmiddelen
	 */
	Middelen('M'),
	/**
	 * Gebruiksmiddelen
	 */
	Gebruiksmiddelen('G'),
	/**
	 * Verbruiksmiddelen
	 */
	Verbruiksmiddelen('V'),
	/**
	 * Bijlagen
	 */
	Bijlagen('B'),
	/**
	 * Resultaten structuur
	 */
	SummatieveStructuur('S'),
	FormatieveStructuren('F'),
	/**
	 * Staat niet echt in het menu, maar wordt gebruikt om een edit knop aan te
	 * registreren
	 */
	Bevriezen('B'),
	/**
	 * Resultaten herberekeningen
	 */
	Herberekeningen('H'),
	/**
	 * Metadata voor Amarantis
	 */
	AmarantisMetadata("Metadata", 'R'),
	/**
	 * Belasting van Onderwijsprodut voor Amarantis
	 */
	AmarantisBelasting("Belasting", 'R');

	private final String label;

	private Character key;

	private OnderwijsproductMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private OnderwijsproductMenuItem(String label)
	{
		this.label = label;
	}

	private OnderwijsproductMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private OnderwijsproductMenuItem(String label, Character key)
	{
		this.label = label;
		this.key = key;
	}

	public String getLabel()
	{
		return label;
	}

	@Override
	public Character getAccessKey()
	{
		return key;
	}
}
