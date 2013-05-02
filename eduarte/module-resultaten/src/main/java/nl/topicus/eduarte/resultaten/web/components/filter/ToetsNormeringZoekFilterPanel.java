package nl.topicus.eduarte.resultaten.web.components.filter;

import java.util.Arrays;

import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class ToetsNormeringZoekFilterPanel extends AutoZoekFilterPanel<ToetsZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ToetsNormeringZoekFilterPanel(String id, ToetsZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("resultaatstructuurFilter.cohort",
			"resultaatstructuurFilter.onderwijsproduct", "resultaatstructuurFilter.taxonomiecode",
			"code", "resultaatstructuurFilter.organisatieEenheid",
			"resultaatstructuurFilter.locatie"));

		OrganisatieEenheidLocatieFieldModifier orgEhdLocModifier =
			new OrganisatieEenheidLocatieFieldModifier();
		orgEhdLocModifier
			.setOrganisatieEenheidPropertyName("resultaatstructuurFilter.organisatieEenheid");
		orgEhdLocModifier.setLocatiePropertyName("resultaatstructuurFilter.locatie");
		addFieldModifier(orgEhdLocModifier);
	}
}
