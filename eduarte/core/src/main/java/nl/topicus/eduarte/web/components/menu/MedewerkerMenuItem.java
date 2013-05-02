package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * Menu-items voor het medewerkermenu.
 * 
 * @author loite
 */
public enum MedewerkerMenuItem implements AccessMenuItemKey
{
	/**
	 * Algemene medewerkerkaart
	 */
	Medewerkerkaart('K'),

	/**
	 * Personalia
	 */
	Personalia('P'),
	Kenmerken('E'),

	/**
	 * Accountgegevenss
	 */
	Account('A'),

	/**
	 * Aanstelling
	 */
	Aanstelling('S'),

	/**
	 * Groepen waar medewerken een relatie mee heeft
	 */
	Groepen('G');

	private final String label;

	private Character key;

	private MedewerkerMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private MedewerkerMenuItem(String label)
	{
		this.label = label;
	}

	private MedewerkerMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private MedewerkerMenuItem(String label, Character key)
	{
		this.label = label;
		this.key = key;
	}

	/**
	 * @see nl.topicus.cobra.web.components.menu.MenuItemKey#getLabel()
	 */
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
