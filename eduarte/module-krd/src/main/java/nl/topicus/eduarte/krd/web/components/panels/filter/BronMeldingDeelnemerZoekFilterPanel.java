package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class BronMeldingDeelnemerZoekFilterPanel extends AutoZoekFilterPanel<BronMeldingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BronMeldingDeelnemerZoekFilterPanel(String id, BronMeldingZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("deelnemerNummer"));
	}

}
