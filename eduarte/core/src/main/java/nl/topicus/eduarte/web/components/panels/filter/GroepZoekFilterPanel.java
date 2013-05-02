/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author hoeve
 */
public class GroepZoekFilterPanel extends AutoZoekFilterPanel<GroepZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public GroepZoekFilterPanel(String id, GroepZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "code", "naam", "organisatieEenheid",
			"locatie", "type", "opleiding"));
		if (filter.getPlaatsingsgroep() != null)
			addFieldModifier(new ConstructorArgModifier("type", filter.getPlaatsingsgroep()));
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
	}
}
