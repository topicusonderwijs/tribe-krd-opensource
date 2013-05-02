/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.LabelModifier;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Zoekfilter panel voor groepsdeelnames van een groep.
 * 
 * @author loite
 */
public class GroepsdeelnameZoekFilterPanel extends AutoZoekFilterPanel<GroepsdeelnameZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public GroepsdeelnameZoekFilterPanel(String id, GroepsdeelnameZoekFilter filter,
			IPageable pageable)
	{
		this(id, filter, pageable, false);
	}

	public GroepsdeelnameZoekFilterPanel(String id, GroepsdeelnameZoekFilter filter,
			IPageable pageable, boolean klassenlijstFilter)
	{
		super(id, filter, pageable);
		if (!klassenlijstFilter)
		{
			setPropertyNames(Arrays.asList("peildatum", "deelnemerFilter.deelnemernummer",
				"deelnemerFilter.roepnaam", "deelnemerFilter.achternaam",
				"toonToekomstigeDeelnames"));
			addFieldModifier(new LabelModifier("deelnemerFilter.deelnemernummer", EduArteApp.get()
				.getDeelnemerTerm()
				+ "nummer"));
		}
		else
		{
			setPropertyNames(Arrays.asList("groepFilter.peildatum",
				"groepFilter.organisatieEenheid", "groepFilter.locatie", "groepFilter.type",
				"groepFilter.code"));
			if (filter.getGroepFilter().getPlaatsingsgroep() != null)
				addFieldModifier(new ConstructorArgModifier("type", filter.getGroepFilter()
					.getPlaatsingsgroep()));
			OrganisatieEenheidLocatieFieldModifier organisatieMod =
				new OrganisatieEenheidLocatieFieldModifier();
			organisatieMod.setOrganisatieEenheidPropertyName("groepFilter.organisatieEenheid");
			organisatieMod.setLocatiePropertyName("groepFilter.locatie");
			addFieldModifier(organisatieMod);
		}
	}
}
