/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krdparticipatie.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.IActiefZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author loite
 */
public class OrganisatieEenheidLocatieActiefZoekFilterPanel<ET extends IdObject, T extends AbstractOrganisatieEenheidLocatieZoekFilter<ET> & IActiefZoekFilter<ET>>
		extends AutoZoekFilterPanel<T>
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidLocatieActiefZoekFilterPanel(String id, T filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("organisatieEenheid", "locatie", "actief"));
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
	}
}
