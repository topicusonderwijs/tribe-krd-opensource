/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.PropertyModel;

/**
 * Zoekfilter panel voor groepsdeelnames van een deelnemer.
 * 
 * @author loite
 */
public class DeelnemerGroepsdeelnameZoekFilterPanel extends
		AutoZoekFilterPanel<GroepsdeelnameZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerGroepsdeelnameZoekFilterPanel(String id, GroepsdeelnameZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "groep", "groepstype"));
		GroepZoekFilter groepFilter = GroepZoekFilter.createDefaultFilter();
		groepFilter.setAuthorizationContext(filter.getAuthorizationContext());
		groepFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(filter,
			"organisatieEenheid"));
		groepFilter.setLocatieModel(new PropertyModel<Locatie>(filter, "locatie"));
		addFieldModifier(new ConstructorArgModifier("groep", groepFilter));
	}
}
