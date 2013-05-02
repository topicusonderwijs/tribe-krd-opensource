package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Filterpanel voor criteria.
 * 
 * @author loite
 */
public class ProductregelZoekFilterPanel extends AutoZoekFilterPanel<ProductregelZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public ProductregelZoekFilterPanel(String id, ProductregelZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("cohort"));
	}
}
