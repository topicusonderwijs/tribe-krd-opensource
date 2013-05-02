package nl.topicus.eduarte.krd.web.pages.beheer.mutatielog;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkenJobRun;
import nl.topicus.eduarte.krd.jobs.MutatieLogVerwerkenJob;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.MutatieLogVerwerkenPrincipal;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

@PageInfo(title = "Mutatielogverwerkers beheren", menu = {"Beheer > Systeem"})
@InPrincipal(MutatieLogVerwerkenPrincipal.class)
public class MutatieLogJobPage extends AbstractJobBeheerPage<MutatieLogVerwerkenJobRun>
{
	public MutatieLogJobPage()
	{
		super(CoreMainMenuItem.Beheer, MutatieLogVerwerkenJob.class, "");
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, BeheerMenuItem.MutatieLogVerwerkers);
	}
}
