package nl.topicus.eduarte.resultaten.web.pages.shared;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractConfirmationLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.resultaten.dao.helpers.ResultaatHerberekenenJobRunDataAccesshelper;
import nl.topicus.eduarte.resultaten.entities.ResultatenHerberekenenJobRun;
import nl.topicus.eduarte.resultaten.jobs.ResultatenHerberekenJob;
import nl.topicus.eduarte.resultaten.zoekfilters.ResultaatHerberekenenJobRunZoekFilter;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPanel;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.quartz.JobDataMap;

public class HerberekenJobBeheerPanel extends AbstractJobBeheerPanel<ResultatenHerberekenenJobRun>
{

	private static final long serialVersionUID = 1L;

	public HerberekenJobBeheerPanel(String id)
	{
		super(id, ResultatenHerberekenJob.class, "");
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	protected ResultaatHerberekenenJobRunZoekFilter createJobRunFilter()
	{
		ResultaatHerberekenenJobRunZoekFilter filter = new ResultaatHerberekenenJobRunZoekFilter();
		filter.addOrderByProperty("runStart");
		filter.setAscending(false);
		return filter;
	}

	@Override
	protected Class<ResultaatHerberekenenJobRunDataAccesshelper> getAccessHelper()
	{
		return ResultaatHerberekenenJobRunDataAccesshelper.class;
	}

	@Override
	public void addExtraBottomRowButtons(BottomRowPanel panel, ResultatenHerberekenenJobRun run)
	{
		final Long structuurId = run.getResultaatstructuurId();
		AbstractConfirmationLinkButton retryButton =
			new HerberekenJobHerstartenButton(panel, "Opnieuw starten",
				new AbstractReadOnlyModel<Resultaatstructuur>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Resultaatstructuur getObject()
					{
						return DataAccessRegistry.getHelper(
							ResultaatstructuurDataAccessHelper.class).get(structuurId);
					}
				})
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void postOnClick()
				{
					setResponsePage(HerberekenJobBeheerPanel.this.findPage());
				}
			};
		panel.addButton(retryButton);
	}

	@Override
	public AutoFieldSet< ? > addExtraFieldSet(ResultatenHerberekenenJobRun jobRun)
	{
		return null;
	}
}