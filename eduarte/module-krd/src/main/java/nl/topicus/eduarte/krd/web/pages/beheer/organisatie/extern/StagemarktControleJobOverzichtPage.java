package nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.krd.entities.StagemarktOrganisatieControleJobRun;
import nl.topicus.eduarte.krd.jobs.StagemarktOrganisatieControleJob;
import nl.topicus.eduarte.krd.principals.organisatie.ExterneOrganisatiesWrite;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenu;
import nl.topicus.eduarte.web.components.menu.RelatieBeheerMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.quartz.JobDataMap;

@PageInfo(title = "Stagemarkt controle", menu = "Relatie > Externe organisaties")
@InPrincipal(ExterneOrganisatiesWrite.class)
@RechtenSoorten( {RechtenSoort.INSTELLING, RechtenSoort.BEHEER})
public class StagemarktControleJobOverzichtPage extends
		AbstractJobBeheerPage<StagemarktOrganisatieControleJobRun>
{
	public StagemarktControleJobOverzichtPage()
	{
		super(CoreMainMenuItem.Relatie, StagemarktOrganisatieControleJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	protected AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel)
	{
		return new AbstractAjaxLinkButton(panel, "Controleren", CobraKeyAction.GEEN,
			ButtonAlignment.LEFT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
			}
		};
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new RelatieBeheerMenu(id, RelatieBeheerMenuItem.ExterneOrganisaties);
	}
}
