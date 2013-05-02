package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

public enum BronMenuItem implements AccessMenuItemKey
{
	BRON("BRON", 'B'),
	Instellingen('I');

	private final String label;

	private Character key;

	private BronMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private BronMenuItem(String label)
	{
		this.label = label;
	}

	private BronMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private BronMenuItem(String label, Character key)
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
