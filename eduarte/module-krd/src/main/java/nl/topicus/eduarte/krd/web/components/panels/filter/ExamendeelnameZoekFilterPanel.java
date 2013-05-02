/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.HtmlClassModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.krd.zoekfilters.ExamendeelnameZoekFilter;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.PropertyModel;

/**
 * @author loite
 */
public class ExamendeelnameZoekFilterPanel extends AutoZoekFilterPanel<ExamendeelnameZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ExamendeelnameZoekFilterPanel(String id, ExamendeelnameZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		if (filter.getExamenstatus() != null)
		{
			setPropertyNames(Arrays.asList("peildatum", "officieelofaanspreek",
				"organisatieEenheid", "locatie", "opleiding", "groep", "cohort"));
		}
		else
		{
			setPropertyNames(Arrays.asList("peildatum", "officieelofaanspreek", "examenstatus",
				"organisatieEenheid", "locatie", "opleiding", "groep", "cohort"));
		}
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
		addFieldModifier(new LabelModifier("officieelofaanspreek", "Achternaam"));
		addFieldModifier(new HtmlClassModifier("unit_100", "opleiding"));
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

		if (filter.getExamenworkflow() != null)
		{
			addFieldModifier(new ConstructorArgModifier("examenstatus",
				new PropertyModel<ExamenWorkflow>(filter, "examenworkflow")));
		}
	}
}
