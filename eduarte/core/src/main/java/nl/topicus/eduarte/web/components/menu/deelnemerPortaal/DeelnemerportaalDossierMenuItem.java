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
public enum DeelnemerportaalDossierMenuItem implements MenuItemKey
{
	/**
	 * Algemene pagina.
	 */
	Personalia,

	/**
	 * Pagina voor de groepen waar de deelnemer onderdeel van uit maakt.
	 */
	Groepen,

	/**
	 * Pagina voor de groepen waar de deelnemer onderdeel van uit maakt.
	 */
	Inschrijving;

	private final String label;

	private DeelnemerportaalDossierMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private DeelnemerportaalDossierMenuItem(String label)
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
