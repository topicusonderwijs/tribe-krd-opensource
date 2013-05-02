package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * 
 * 
 * @author vanharen
 */
public enum RelatieBeheerMenuItem implements AccessMenuItemKey
{

	ExterneOrganisaties('E'),
	Contracten('C');

	private final String label;

	private Character key;

	private RelatieBeheerMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private RelatieBeheerMenuItem(String label)
	{
		this.label = label;
	}

	private RelatieBeheerMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private RelatieBeheerMenuItem(String label, Character key)
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
