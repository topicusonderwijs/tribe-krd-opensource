package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.resultaten.entities.ResultaatstructurenKopierenJobRun;
import nl.topicus.eduarte.resultaten.jobs.ResultaatstructurenKopierenJob;
import nl.topicus.eduarte.resultaten.principals.onderwijs.OnderwijsproductResultaatstructuurKopieren;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.quartz.JobDataMap;

/**
 * @author papegaaij
 */
@PageInfo(title = "Resultaatstructuren kopieren", menu = "Onderwijs > Onderwijsproducten > Kopiëren resultaatstructuren")
@InPrincipal(OnderwijsproductResultaatstructuurKopieren.class)
public class ResultaatstructurenKopierenOverzichtPage extends
		AbstractJobBeheerPage<ResultaatstructurenKopierenJobRun>
{
	public ResultaatstructurenKopierenOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs, ResultaatstructurenKopierenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return null;
	}

	@Override
	protected AbstractBottomRowButton createTaakStartenButton(BottomRowPanel panel)
	{
		return new PageLinkButton(panel, "Taak nu starten", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new ResultaatstructurenKopierenStap1Page(
					ResultaatstructurenKopierenOverzichtPage.this);
			}

			@Override
			public Class<ResultaatstructurenKopierenStap1Page> getPageIdentity()
			{
				return ResultaatstructurenKopierenStap1Page.class;
			}
		});
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id,
			OnderwijsCollectiefMenuItem.ResultaatstructurenKopieren);
	}
}
