/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Filter panel voor rollen
 * 
 * @author loite
 */
public class RolZoekFilterPanel extends AutoZoekFilterPanel<RolZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public RolZoekFilterPanel(String id, RolZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("naam", "categorie"));
		addFieldModifier(new ConstructorArgModifier("categorie", false));
	}
}
