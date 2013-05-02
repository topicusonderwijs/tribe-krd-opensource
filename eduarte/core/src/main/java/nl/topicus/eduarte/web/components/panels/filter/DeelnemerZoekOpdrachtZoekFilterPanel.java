package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.zoekfilters.DeelnemerZoekOpdrachtZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

public class DeelnemerZoekOpdrachtZoekFilterPanel extends
		AutoZoekFilterPanel<DeelnemerZoekOpdrachtZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerZoekOpdrachtZoekFilterPanel(String id,
			DeelnemerZoekOpdrachtZoekFilter zoekfilter, IPageable pageable)
	{
		super(id, zoekfilter, pageable);
		setPropertyNames(Arrays.asList("omschrijving", "persoonlijk"));
	}
}
