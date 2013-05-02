package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.zoekfilters.CurriculumOnderwijsproductZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class CurriculumOnderwijsproductZoekFilterPanel extends
		AutoZoekFilterPanel<CurriculumOnderwijsproductZoekFilter>
{

	private static final long serialVersionUID = 1L;

	public CurriculumOnderwijsproductZoekFilterPanel(String id,
			CurriculumOnderwijsproductZoekFilter zoekfilter, IPageable pageable)
	{
		super(id, zoekfilter, pageable);
		setPropertyNames(Arrays.asList("organisatieEenheid", "locatie", "opleiding", "cohort",
			"onderwijsproduct"));
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
	}
}
