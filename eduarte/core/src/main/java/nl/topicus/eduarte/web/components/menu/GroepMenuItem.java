/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * @author hoeve
 */
public enum GroepMenuItem implements AccessMenuItemKey
{
	/**
	 * Groepkaart van een groep
	 */
	Groepkaart('K'),

	/**
	 * De pasfotos van de deelnemers in een groep
	 */
	Pasfotos("Pasfoto's", 'F'),

	/**
	 * De documenten van de groep
	 */
	Documenten('D'),
	Overzichten('O'),
	Dossieroverzicht('Z'),
	Hokjeslijst('H'),
	/**
	 * Resultaten invoeren
	 */
	Invoeren('I');

	private final String label;

	private Character key;

	private GroepMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private GroepMenuItem(String label)
	{
		this.label = label;
	}

	private GroepMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private GroepMenuItem(String label, Character key)
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
