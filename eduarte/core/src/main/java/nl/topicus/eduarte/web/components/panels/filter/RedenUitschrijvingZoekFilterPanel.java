package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * @author hop
 */
public class RedenUitschrijvingZoekFilterPanel extends
		AutoZoekFilterPanel<RedenUitschrijvingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public RedenUitschrijvingZoekFilterPanel(String id, RedenUitschrijvingZoekFilter zoekfilter,
			IPageable pageable)
	{
		super(id, zoekfilter, pageable);
		setPropertyNames(Arrays.asList("code", "naam", "actief", "redenUitval", "uitstroomredenWI",
			"soort"));
	}
}
