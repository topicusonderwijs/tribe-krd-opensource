package nl.topicus.eduarte.krd.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.krd.zoekfilters.BronExamenmeldingZoekFilter;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class BronExamenMeldingZoekFilterPanel extends
		AutoZoekFilterPanel<BronExamenmeldingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public BronExamenMeldingZoekFilterPanel(String id, BronExamenmeldingZoekFilter filter,
			IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("aanleverpunt", "bronOnderwijssoort"));
	}
}
