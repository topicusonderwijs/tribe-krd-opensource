/*
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.eduarte.web.pages.SnelZoekForm;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author loite
 */
public class MainMenuSearchBox extends Panel
{
	private static final long serialVersionUID = 1L;

	public MainMenuSearchBox(String id)
	{
		super(id);

		SnelZoekForm deelnemerZoekenForm = new SnelZoekForm("form");
		add(deelnemerZoekenForm);
	}
}
