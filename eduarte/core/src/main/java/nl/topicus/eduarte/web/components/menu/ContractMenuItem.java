package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * Menu-items voor het contract menu.
 * 
 * @author schimmel
 */
public enum ContractMenuItem implements AccessMenuItemKey
{
	/**
	 * Algemene contractKaart
	 */
	Contractkaart('K'),
	Factuurregeldefinities('D'),
	Factuurregels('R'),
	Facturen('F'),
	Betalingen('B');

	private final String label;

	private Character key;

	private ContractMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private ContractMenuItem(String label)
	{
		this.label = label;
	}

	private ContractMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private ContractMenuItem(String label, Character key)
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
