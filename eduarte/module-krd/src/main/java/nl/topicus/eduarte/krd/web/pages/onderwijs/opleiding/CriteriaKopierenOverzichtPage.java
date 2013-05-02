package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.krd.entities.CriteriaKopierenJobRun;
import nl.topicus.eduarte.krd.jobs.CriteriaKopierenJob;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingCriteriaKopieren;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.quartz.JobDataMap;

/**
 * @author loite
 */
@PageInfo(title = "Criteria kopiëren", menu = "Onderwijs > Kopiëren criteria")
@InPrincipal(OpleidingCriteriaKopieren.class)
public class CriteriaKopierenOverzichtPage extends AbstractJobBeheerPage<CriteriaKopierenJobRun>
{
	public CriteriaKopierenOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs, CriteriaKopierenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	protected AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel)
	{
		return new PageLinkButton(panel, "Kopiëren", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new CriteriaKopierenStap2Page(CriteriaKopierenOverzichtPage.this);
			}

			@Override
			public Class<CriteriaKopierenStap2Page> getPageIdentity()
			{
				return CriteriaKopierenStap2Page.class;
			}
		});
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.CriteriaKopieren);
	}
}
