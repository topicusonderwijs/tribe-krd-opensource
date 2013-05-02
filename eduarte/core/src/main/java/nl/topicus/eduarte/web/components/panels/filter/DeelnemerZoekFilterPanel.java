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
import nl.topicus.eduarte.web.components.autoform.OpleidingOrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.PropertyModel;

/**
 * @author loite
 */
public class DeelnemerZoekFilterPanel extends AutoZoekFilterPanel<VerbintenisZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerZoekFilterPanel(String id, VerbintenisZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("peildatum", "officieelofaanspreek", "organisatieEenheid",
			"locatie", "opleiding", "groep"));
		addFieldModifier(new OpleidingOrganisatieEenheidLocatieFieldModifier());
		addFieldModifier(new LabelModifier("officieelofaanspreek", "Achternaam"));

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

	@Override
	protected void onZoek(final IPageable pageable, AjaxRequestTarget target)
	{
		super.onZoek(pageable, target);
		VerbintenisZoekFilter filter = getZoekfilter();
		if (filter.getOrderByList().isEmpty())
		{
			filter.addOrderByProperty("beeindigd");
			filter.addOrderByProperty("deelnemer.deelnemernummer");
			filter.addOrderByProperty("persoon.roepnaam");
			filter.addOrderByProperty("persoon.achternaam");
		}
	}
}
