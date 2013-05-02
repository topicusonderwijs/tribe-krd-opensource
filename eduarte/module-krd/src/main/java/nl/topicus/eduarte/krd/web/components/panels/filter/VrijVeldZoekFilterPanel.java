/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.VrijVeldZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author hoeve
 */
public class VrijVeldZoekFilterPanel extends AutoZoekFilterPanel<VrijVeldZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public VrijVeldZoekFilterPanel(String id, VrijVeldZoekFilter filter, final IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("naam", "actief", "type", "categorie"));
	}
}
