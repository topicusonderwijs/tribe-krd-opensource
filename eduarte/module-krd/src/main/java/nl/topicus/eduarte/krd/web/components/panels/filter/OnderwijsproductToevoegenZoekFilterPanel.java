package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Zoekfilterpanel voor onderwijsproducten.
 * 
 * @author vandekamp
 */
public class OnderwijsproductToevoegenZoekFilterPanel extends
		AutoZoekFilterPanel<OnderwijsproductZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductToevoegenZoekFilterPanel(String id, OnderwijsproductZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("status", "code", "titel", "typeToets",
			"soortOnderwijsproduct", "organisatieEenheid", "locatie"));
		addFieldModifier(new OrganisatieEenheidLocatieFieldModifier());
	}
}
