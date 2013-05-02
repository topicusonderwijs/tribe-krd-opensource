package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * Menu-items voor het opleidingmenu.
 * 
 * @author loite
 */
public enum OpleidingMenuItem implements AccessMenuItemKey
{
	Opleidingkaart('K'),
	Productregels('P'),
	Criteria('C'),
	Toetsfilters('F'),
	Resultaatstructuren('S'),
	Curriculum('R');

	private final String label;

	private Character key;

	private OpleidingMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private OpleidingMenuItem(String label)
	{
		this.label = label;
	}

	private OpleidingMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private OpleidingMenuItem(String label, Character key)
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
