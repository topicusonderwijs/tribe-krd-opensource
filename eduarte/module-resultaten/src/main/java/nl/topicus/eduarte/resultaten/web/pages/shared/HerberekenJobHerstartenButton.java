package nl.topicus.eduarte.resultaten.web.pages.shared;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractConfirmationLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Status;
import nl.topicus.eduarte.resultaten.jobs.ResultatenHerberekenJob;
import nl.topicus.eduarte.resultaten.jobs.ResultatenHerberekenJobDataMap;
import nl.topicus.eduarte.resultaten.web.pages.onderwijs.MatchOnderwijsproductFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.quartz.SchedulerException;

public abstract class HerberekenJobHerstartenButton extends AbstractConfirmationLinkButton
{
	private static final long serialVersionUID = 1L;

	private IModel<Resultaatstructuur> structuurModel;

	private IModel<Boolean> visibleModel;

	public HerberekenJobHerstartenButton(BottomRowPanel bottomRow, String label,
			IModel<Resultaatstructuur> structuurModel)
	{
		super(bottomRow, label, CobraKeyAction.GEEN, ButtonAlignment.LEFT,
			"Weet u zeker dat u deze taak nogmaals wilt starten?");
		this.structuurModel = structuurModel;
		visibleModel = new LoadableDetachableModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Boolean load()
			{
				Resultaatstructuur structuur = getStructuur();
				return structuur != null && isVisibleVoor(structuur);
			}
		};
	}

	@Override
	protected void onClick()
	{
		Resultaatstructuur structuur = getStructuur();
		ResultatenHerberekenJobDataMap datamap =
			new ResultatenHerberekenJobDataMap(structuur, structuur.getToetsen(), false);
		try
		{
			EduArteApp.get().getEduarteScheduler().triggerJob(ResultatenHerberekenJob.class,
				datamap);
			info("De taak is op de achtergrond gestart. De voortgang van de taak kunt u hieronder zien.");
		}
		catch (SchedulerException e)
		{
			error("Er is een fout opgetreden bij het starten van de taak.");
		}
		postOnClick();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		visibleModel.detach();
	}

	private Resultaatstructuur getStructuur()
	{
		return structuurModel.getObject();
	}

	@Override
	public boolean isVisible()
	{
		return super.isVisible() && ((Boolean) visibleModel.getObject());
	}

	private boolean isVisibleVoor(Resultaatstructuur structuur)
	{
		if (Status.FOUTIEF.equals(structuur.getStatus()))
			return true;
		if (Status.IN_HERBEREKENING.equals(structuur.getStatus())
			&& structuur.getLastModifiedAt().before(
				TimeUtil.getInstance().addSeconds(TimeUtil.getInstance().currentDateTime(), -10)))
			return EduArteApp.get().getEduarteScheduler().getExecutingAndWaitingJobs(
				ResultatenHerberekenJob.class, EduArteContext.get().getInstelling(),
				new MatchOnderwijsproductFilter(structuur.getOnderwijsproduct())).isEmpty();
		return false;
	}

	abstract protected void postOnClick();
}