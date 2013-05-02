/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;
import nl.topicus.eduarte.app.EduArteApp;

/**
 * @author loite
 */
public enum DeelnemerMenuItem implements AccessMenuItemKey
{
	/**
	 * Menuitems onder Deelnemerkaart
	 */
	Deelnemerkaart('K')
	{
		@Override
		public String getLabel()
		{
			return EduArteApp.get().getDeelnemerTerm() + "kaart";
		}
	},
	/**
	 * Menuitems onder Personalia
	 */
	Personalia('P'),
	Relaties('R'),
	Kenmerken('E'),
	/**
	 * Menuitems onder Onderwijs
	 */
	AfgOnderwijsproducten('A'),
	Productregels('P'),
	/**
	 * Menuitems onder Verbintenis
	 */
	Verbintenis('V'),
	Intake('I'),
	Vooropleidingen('O'),
	Groepen('G'),
	/**
	 * BPV onder verbintenis
	 */
	BPV('B'),
	/**
	 * Menuitems onder Resultaten
	 */
	Resultatenboom('R'),
	Resultatenmatrix('M'),
	Examens('X'),
	BereikbareDiplomas("Bereikbare diploma's", 'D'),
	Bron("BRON", 'N'),
	Documenten('C'),
	Bevriezen('Z');

	private final String label;

	private Character key;

	private DeelnemerMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = null;
	}

	private DeelnemerMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private DeelnemerMenuItem(String label)
	{
		this.label = label;
		this.key = null;
	}

	private DeelnemerMenuItem(String label, Character key)
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
