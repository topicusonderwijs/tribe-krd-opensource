/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.zoekfilters.PersoonZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author loite
 */
public class PersoonZoekFilterPanel extends AutoZoekFilterPanel<PersoonZoekFilter<Persoon>>
{
	private static final long serialVersionUID = 1L;

	public PersoonZoekFilterPanel(String id, PersoonZoekFilter<Persoon> filter,
			final IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("roepnaam", "voornamen", "voorvoegsel", "achternaam"));
	}
}
