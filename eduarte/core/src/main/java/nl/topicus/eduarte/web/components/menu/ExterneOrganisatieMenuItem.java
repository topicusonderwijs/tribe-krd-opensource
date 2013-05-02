package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * Menu-items voor het externe organisatie menu.
 * 
 * @author elferink
 */
public enum ExterneOrganisatieMenuItem implements AccessMenuItemKey
{
	/**
	 * Algemene externeOrganisatiekaart
	 */
	ExterneOrganisatiekaart('K'),
	Kenmerken('E');

	private final String label;

	private Character key;

	private ExterneOrganisatieMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private ExterneOrganisatieMenuItem(String label)
	{
		this.label = label;
	}

	private ExterneOrganisatieMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private ExterneOrganisatieMenuItem(String label, Character key)
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
