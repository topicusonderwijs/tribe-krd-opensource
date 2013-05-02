package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * Menu-items voor het taxonomieelementmenu.
 * 
 * @author loite
 */
public enum TaxonomieElementMenuItem implements AccessMenuItemKey
{
	/**
	 * Algemene taxonomieelementkaart
	 */
	Algemeen('A'),
	/**
	 * Pagina met de verbintenisgebieden van een taxonomie-element.
	 */
	Verbintenisgebieden('V'),
	/**
	 * Pagina met de deelgebieden van een taxonomie-element.
	 */
	Deelgebieden('D'),
	/**
	 * Productregels die gedefinieerd zijn voor een taxonomie-element.
	 */
	Productregels('P'),
	/**
	 * Criteria die gedefinieerd zijn voor een taxonomie-element.
	 */
	Criteria('C'),
	/**
	 * Pagina met de taxonomie-elementtypes van een bepaalde taxonomie.
	 */
	TaxonomieElementTypes("Taxonomie-elementtypes", 'T');

	private final String label;

	private Character key;

	private TaxonomieElementMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private TaxonomieElementMenuItem(String label)
	{
		this.label = label;
	}

	private TaxonomieElementMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private TaxonomieElementMenuItem(String label, Character key)
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
