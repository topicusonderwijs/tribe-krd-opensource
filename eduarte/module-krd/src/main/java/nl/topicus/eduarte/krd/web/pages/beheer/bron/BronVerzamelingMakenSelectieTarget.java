package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteScheduler;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.krd.bron.jobs.BronExamenverzamelingenAanmakenJob;
import nl.topicus.eduarte.krd.bron.jobs.BronExamenverzamelingenAanmakenJobDataMap;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;

import org.apache.wicket.markup.html.link.Link;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BronVerzamelingMakenSelectieTarget extends
		AbstractSelectieTarget<Examendeelname, Examendeelname>
{
	private static final long serialVersionUID = 1L;

	private BronExamenverzamelingenAanmakenJobDataMap datamap;

	private SecurePage returnPage;

	private static final Logger log =
		LoggerFactory.getLogger(BronVerzamelingMakenSelectieTarget.class);

	public BronVerzamelingMakenSelectieTarget(Class< ? > target, String label,
			BronExamenverzamelingenAanmakenJobDataMap datamap, SecurePage returnPage)
	{
		super(target, label);
		this.datamap = datamap;
		this.returnPage = returnPage;
	}

	@Override
	public Link<Void> createLink(String linkId,
			final ISelectionComponent<Examendeelname, Examendeelname> base)
	{
		Link<Void> link = new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				List<Long> deelnameIds = new ArrayList<Long>();
				for (Examendeelname deelname : base.getSelectedElements())
					deelnameIds.add(deelname.getId());
				datamap.setExamendeelnames(deelnameIds);
				try
				{
					EduArteScheduler scheduler = EduArteApp.get().getEduarteScheduler();
					scheduler.triggerJob(BronExamenverzamelingenAanmakenJob.class, datamap);
				}
				catch (SchedulerException e)
				{
					log.error(e.toString(), e);
					error("Taak kon niet opgestart worden.");
					error(e.getLocalizedMessage());
				}
				info("De taak is op de achtergrond gestart");
				setResponsePage(returnPage);
			}
		};
		return link;
	}
}
