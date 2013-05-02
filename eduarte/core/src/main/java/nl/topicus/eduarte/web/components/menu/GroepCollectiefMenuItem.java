/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * @author hoeve
 */
public enum GroepCollectiefMenuItem implements AccessMenuItemKey
{

	/**
	 * Zoekscherm voor groepen
	 */
	GroepZoeken("Zoeken", 'N'),
	/**
	 * Rapportages.
	 */
	GroepsdeelnameZoeken("Groepsdeelname", 'G'),
	/**
	 * Groepen overzicht voor de huidige gebruiker.
	 */
	MijnGroepen("Mijn groepen", 'M'),
	/**
	 * Rapportages.
	 */
	Rapportages('R');

	private String label;

	private Character key;

	private GroepCollectiefMenuItem()
	{
		label = name();
	}

	private GroepCollectiefMenuItem(String label)
	{
		this.label = label;
	}

	private GroepCollectiefMenuItem(Character key)
	{
		label = name();
		this.key = key;
	}

	private GroepCollectiefMenuItem(String label, Character key)
	{
		this.label = label;
		this.key = key;
	}

	@Override
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
