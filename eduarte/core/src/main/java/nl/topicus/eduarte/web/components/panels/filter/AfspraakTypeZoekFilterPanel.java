package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.participatie.zoekfilters.AfspraakTypeZoekFilter;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author loite
 */
public class AfspraakTypeZoekFilterPanel extends AutoZoekFilterPanel<AfspraakTypeZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public AfspraakTypeZoekFilterPanel(String id, AfspraakTypeZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("organisatieEenheid", "locatie", "naam", "omschrijving",
			"actief"));
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
	}
}