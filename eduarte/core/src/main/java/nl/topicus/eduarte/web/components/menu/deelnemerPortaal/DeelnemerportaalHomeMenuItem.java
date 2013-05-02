/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.deelnemerPortaal;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.MenuItemKey;

/**
 * @author marrink
 */
public enum DeelnemerportaalHomeMenuItem implements MenuItemKey
{
	/**
	 * Algemene pagina.
	 */
	Home,
	/**
	 * Account gegevens van de gebruiker.
	 */
	Account;

	private final String label;

	private DeelnemerportaalHomeMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private DeelnemerportaalHomeMenuItem(String label)
	{
		this.label = label;
	}

	/**
	 * @see nl.topicus.cobra.web.components.menu.MenuItemKey#getLabel()
	 */
	public String getLabel()
	{
		return label;
	}
}
