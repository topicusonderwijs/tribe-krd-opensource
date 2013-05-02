package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * Collectiefmenu voor medewerkers.
 * 
 * @author loite
 */
public enum MedewerkerCollectiefMenuItem implements AccessMenuItemKey
{
	/**
	 * Medewerker zoeken
	 */
	Zoeken('N');

	private final String label;

	private Character key;

	private MedewerkerCollectiefMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private MedewerkerCollectiefMenuItem(String label)
	{
		this.label = label;
	}

	private MedewerkerCollectiefMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private MedewerkerCollectiefMenuItem(String label, Character key)
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
