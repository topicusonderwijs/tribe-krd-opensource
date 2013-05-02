package nl.topicus.eduarte.resultaten.web.components.filter;

import java.util.Arrays;

import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class ToetsSelectieZoekFilterPanel extends AutoZoekFilterPanel<ToetsZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ToetsSelectieZoekFilterPanel(String id, ToetsZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("code", "naam", "resultaatstructuurFilter.cohort",
			"resultaatstructuurFilter.onderwijsproduct",
			"resultaatstructuurFilter.organisatieEenheid", "resultaatstructuurFilter.locatie"));

		OrganisatieEenheidLocatieFieldModifier orgEhdLocModifier =
			new OrganisatieEenheidLocatieFieldModifier();
		orgEhdLocModifier
			.setOrganisatieEenheidPropertyName("resultaatstructuurFilter.organisatieEenheid");
		orgEhdLocModifier.setLocatiePropertyName("resultaatstructuurFilter.locatie");
		addFieldModifier(orgEhdLocModifier);

		addFieldModifier(new ConstructorArgModifier("resultaatstructuurFilter.onderwijsproduct",
			OnderwijsproductZoekFilter.createDefaultFilter()));

	}
}
