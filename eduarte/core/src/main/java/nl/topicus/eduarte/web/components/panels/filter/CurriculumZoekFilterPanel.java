package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.zoekfilters.CurriculumZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class CurriculumZoekFilterPanel extends AutoZoekFilterPanel<CurriculumZoekFilter>
{

	private static final long serialVersionUID = 1L;

	public CurriculumZoekFilterPanel(String id, CurriculumZoekFilter zoekfilter, IPageable pageable)
	{
		super(id, zoekfilter, pageable);
		setPropertyNames(Arrays.asList("organisatieEenheid", "locatie", "opleiding", "cohort"));
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
	}
}
