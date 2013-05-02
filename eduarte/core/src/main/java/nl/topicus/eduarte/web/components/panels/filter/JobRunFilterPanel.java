package nl.topicus.eduarte.web.components.panels.filter;

import java.util.Arrays;

import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.zoekfilters.JobRunZoekFilter;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Filterpanel voor eerdere trajecten jobruns.
 * 
 * @author loite
 */
public class JobRunFilterPanel extends AutoZoekFilterPanel<JobRunZoekFilter< ? extends JobRun>>
{
	private static final long serialVersionUID = 1L;

	public JobRunFilterPanel(String id, JobRunZoekFilter< ? extends JobRun> filter,
			final IPageable pageable)
	{
		super(id, filter, pageable);
		setPropertyNames(Arrays.asList("begindatum", "einddatum", "samenvatting"));
	}
}
