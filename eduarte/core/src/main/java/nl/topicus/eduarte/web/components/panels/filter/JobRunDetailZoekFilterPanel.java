package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.zoekfilters.JobRunDetailZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Zoekfilterpanel voor roosterimport jobdetails.
 * 
 * @author loite
 */
public class JobRunDetailZoekFilterPanel extends
		AutoZoekFilterPanel<JobRunDetailZoekFilter< ? extends JobRunDetail>>
{
	private static final long serialVersionUID = 1L;

	public JobRunDetailZoekFilterPanel(String id,
			JobRunDetailZoekFilter< ? extends JobRunDetail> filter, final IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("uitkomst"));
	}
}
