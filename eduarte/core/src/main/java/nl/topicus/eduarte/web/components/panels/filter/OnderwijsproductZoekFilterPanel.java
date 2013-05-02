package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Zoekfilterpanel voor onderwijsproducten.
 * 
 * @author loite
 */
public class OnderwijsproductZoekFilterPanel extends
		AutoZoekFilterPanel<OnderwijsproductZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductZoekFilterPanel(String id, OnderwijsproductZoekFilter filter,
			final IPageable pageable)
	{
		this(id, filter, pageable, false);
	}

	public OnderwijsproductZoekFilterPanel(String id, final OnderwijsproductZoekFilter filter,
			final IPageable pageable, boolean toonCohort)
	{
		super(id, filter, pageable);
		if (toonCohort)
		{
			setPropertyNames(Arrays.asList("code", "titel", "status", "organisatieEenheid",
				"locatie", "cohort"));
		}
		else
		{
			if (!EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS))
			{
				setPropertyNames(Arrays.asList("peildatum", "code", "titel", "status",
					"taxonomiecode", "organisatieEenheid", "locatie", "soortOnderwijsproduct"));
			}
			else
			{
				setPropertyNames(Arrays.asList("peildatum", "code", "titel", "status",
					"organisatieEenheid", "locatie", "credits"));
			}

		}
		addFieldModifier(new EnableModifier(filter.isStaOrganisatieEenheidAanpassingToe(),
			"organisatieEenheid"));
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
	}
}
