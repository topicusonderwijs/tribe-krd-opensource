/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.zoekfilters.BPVInschrijvingZoekFilter;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.PropertyModel;

public class BPVInschrijvingZoekFilterPanel extends AutoZoekFilterPanel<BPVInschrijvingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BPVInschrijvingZoekFilterPanel(String id, BPVInschrijvingZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "achternaam", "organisatieEenheid", "locatie",
			"opleiding", "groep"));
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
		OpleidingZoekFilter opleidingFilter = OpleidingZoekFilter.createDefaultFilter();
		opleidingFilter.setAuthorizationContext(filter.getAuthorizationContext());
		opleidingFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(filter,
			"organisatieEenheid"));
		opleidingFilter.setLocatieModel(new PropertyModel<Locatie>(filter, "locatie"));
		addFieldModifier(new ConstructorArgModifier("opleiding", opleidingFilter));

		GroepZoekFilter groepFilter = GroepZoekFilter.createDefaultFilter();
		groepFilter.setAuthorizationContext(filter.getAuthorizationContext());
		groepFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(filter,
			"organisatieEenheid"));
		groepFilter.setLocatieModel(new PropertyModel<Locatie>(filter, "locatie"));
		addFieldModifier(new ConstructorArgModifier("groep", groepFilter));
	}
}
