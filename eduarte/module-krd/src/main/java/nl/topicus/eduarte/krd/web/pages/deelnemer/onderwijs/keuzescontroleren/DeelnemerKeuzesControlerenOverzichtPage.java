package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.keuzescontroleren;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.krd.principals.deelnemer.onderwijsproduct.DeelnemersOnderwijsproductenKeuzeControleren;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.quartz.JobDataMap;

@PageInfo(title = "Deelnemer keuzes controleren", menu = "Deelnemer > Onderwijs > Keuzes controleren")
@InPrincipal(DeelnemersOnderwijsproductenKeuzeControleren.class)
public class DeelnemerKeuzesControlerenOverzichtPage extends
		AbstractJobBeheerPage<DeelnemerKeuzesControlerenJobRun>
{
	public DeelnemerKeuzesControlerenOverzichtPage()
	{
		super(CoreMainMenuItem.Deelnemer, DeelnemerKeuzesControlerenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.KeuzesControleren);
	}

	@Override
	protected AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel)
	{
		return new PageLinkButton(panel, "Nieuwe controle", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerKeuzesControlerenPage();
			}

			@Override
			public Class<DeelnemerKeuzesControlerenPage> getPageIdentity()
			{
				return DeelnemerKeuzesControlerenPage.class;
			}
		});
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}