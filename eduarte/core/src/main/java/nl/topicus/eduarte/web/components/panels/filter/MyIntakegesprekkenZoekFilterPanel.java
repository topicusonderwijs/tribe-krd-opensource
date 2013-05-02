/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.eduarte.zoekfilters.IntakegesprekZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class MyIntakegesprekkenZoekFilterPanel extends AutoZoekFilterPanel<IntakegesprekZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public MyIntakegesprekkenZoekFilterPanel(String id, IntakegesprekZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("datumTijd", "achternaam", "status"));
		addFieldModifier(new LabelModifier("datumTijd", "Datum vanaf"));
	}
}
