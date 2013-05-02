package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Zoekfilterpanel voor onderwijsproductenAfname.
 * 
 * @author vandekamp
 */
public class OnderwijsproductAfnameZoekFilterPanel extends
		AutoZoekFilterPanel<OnderwijsproductAfnameZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductAfnameZoekFilterPanel(String id,
			OnderwijsproductAfnameZoekFilter filter, IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("code", "titel", "summatief", "soortOnderwijsproduct"));
	}
}
