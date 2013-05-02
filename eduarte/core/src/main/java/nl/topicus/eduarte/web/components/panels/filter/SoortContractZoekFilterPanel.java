package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.SoortContractZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author hop
 */
public class SoortContractZoekFilterPanel extends AutoZoekFilterPanel<SoortContractZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public SoortContractZoekFilterPanel(String id, SoortContractZoekFilter zoekfilter,
			IPageable pageable)
	{
		super(id, zoekfilter, pageable);
		setPropertyNames(Arrays.asList("code", "naam", "inburgering", "actief"));
	}

}
