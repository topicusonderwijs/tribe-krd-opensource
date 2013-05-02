/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author loite
 */
public class OpleidingZoekFilterPanel extends AutoZoekFilterPanel<OpleidingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public OpleidingZoekFilterPanel(String id, OpleidingZoekFilter filter, final IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "code", "naam", "leerweg", "taxonomiecode",
			"verbintenisgebied"));
		addFieldModifier(new ConstructorArgModifier("verbintenisgebied",
			new TaxonomieElementZoekFilter(Verbintenisgebied.class)));
	}

	@SuppressWarnings("unused")
	public OpleidingZoekFilterPanel(String id, OpleidingZoekFilter filter,
			final IPageable pageable, boolean niveauZoekveld)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "code", "naam", "leerweg", "taxonomiecode",
			"verbintenisgebied", "verbintenisgebiedniveau"));
		addFieldModifier(new ConstructorArgModifier("verbintenisgebied",
			new TaxonomieElementZoekFilter(Verbintenisgebied.class)));
	}
}
