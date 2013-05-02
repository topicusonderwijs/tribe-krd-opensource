package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.INaamActiefZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * 
 * 
 * @author vanharen
 */
public class NaamActiefZoekFilterPanel extends AutoZoekFilterPanel<INaamActiefZoekFilter< ? >>
{

	private static final long serialVersionUID = 1L;

	public NaamActiefZoekFilterPanel(String id, INaamActiefZoekFilter< ? > zoekfilter,
			IPageable pageable)
	{
		super(id, zoekfilter, pageable);
		setPropertyNames(Arrays.asList("naam", "actief"));
	}

}
