/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;
import nl.topicus.eduarte.web.components.autoform.OpleidingOrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.PropertyModel;

/**
 * @author loite
 */
public class VerbintenisAbsentieMeldingenZoekFilterPanel extends
		AutoZoekFilterPanel<AbsentieMeldingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public VerbintenisAbsentieMeldingenZoekFilterPanel(String id, AbsentieMeldingZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);

		setPropertyNames(Arrays.asList("achternaam", "organisatieEenheid", "opleiding", "locatie",
			"groep", "beginDatumTijd", "eindDatumTijd", "absentieReden"));

		addFieldModifier(new LabelModifier("beginDatumTijd", "Begin"));
		addFieldModifier(new LabelModifier("eindDatumTijd", "Eind"));
		addFieldModifier(new OpleidingOrganisatieEenheidLocatieFieldModifier());

		GroepZoekFilter groepFilter = GroepZoekFilter.createDefaultFilter();
		groepFilter.setAuthorizationContext(filter.getAuthorizationContext());
		groepFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(filter,
			"organisatieEenheid"));
		groepFilter.setLocatieModel(new PropertyModel<Locatie>(filter, "locatie"));
		addFieldModifier(new ConstructorArgModifier(getGroepPropertyName(), groepFilter));
	}

	protected String getGroepPropertyName()
	{
		return "groep";
	}
}
