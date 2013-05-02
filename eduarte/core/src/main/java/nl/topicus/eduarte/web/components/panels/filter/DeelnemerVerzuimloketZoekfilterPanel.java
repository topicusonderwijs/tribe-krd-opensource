package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.DeelnemerVerzuimloketZoekfilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class DeelnemerVerzuimloketZoekfilterPanel extends
		AutoZoekFilterPanel<DeelnemerVerzuimloketZoekfilter>
{

	private static final long serialVersionUID = 1L;

	public DeelnemerVerzuimloketZoekfilterPanel(String id,
			DeelnemerVerzuimloketZoekfilter zoekfilter, IPageable pageable)
	{
		super(id, zoekfilter, pageable);
		setPropertyNames(Arrays.asList("meldingsnummer", "verzuimsoort", "vanafDatum", "tmDatum",
			"status"));
	}

}
