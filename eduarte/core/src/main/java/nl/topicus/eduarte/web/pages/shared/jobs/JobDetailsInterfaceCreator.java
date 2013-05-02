package nl.topicus.eduarte.web.pages.shared.jobs;

import java.io.Serializable;
import java.util.Collection;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.jobs.logging.JobRunDetail;
import nl.topicus.eduarte.zoekfilters.JobRunDetailZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.repeater.data.IDataProvider;

public interface JobDetailsInterfaceCreator<T extends JobRun> extends Serializable
{
	public IDataProvider<JobRunDetail> createDetailsDataProvider(T run);

	public CustomDataPanelContentDescription<JobRunDetail> getDetailContent();

	public Component createDetailsFilterPanel(String id,
			JobRunDetailZoekFilter< ? extends JobRunDetail> filter, IPageable pageable);

	public void addExtraBottomRowButtons(BottomRowPanel panel, T run);

	public Collection<String> addExtraFieldSetProperties();

	public AutoFieldSet< ? > addExtraFieldSet(T jobRun);

}
