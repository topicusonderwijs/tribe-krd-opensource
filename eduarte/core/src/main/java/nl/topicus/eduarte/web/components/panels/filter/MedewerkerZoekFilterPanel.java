/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author loite
 */
public class MedewerkerZoekFilterPanel extends AutoZoekFilterPanel<MedewerkerZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public MedewerkerZoekFilterPanel(String id, MedewerkerZoekFilter filter, IPageable pageable,
			boolean toonHeeftAccount)
	{
		super(id, filter, pageable);
		List<String> properties =
			new ArrayList<String>(Arrays.asList("peildatum", "achternaam", "afkorting",
				"organisatieEenheid", "locatie", "functie"));
		if (toonHeeftAccount)
			properties.add("heeftAccount");
		setPropertyNames(properties);
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
	}
}
