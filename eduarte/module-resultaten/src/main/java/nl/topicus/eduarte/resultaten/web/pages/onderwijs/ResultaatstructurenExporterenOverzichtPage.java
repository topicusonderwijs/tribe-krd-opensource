package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.resultaten.entities.ResultaatstructurenExporterenJobRun;
import nl.topicus.eduarte.resultaten.jobs.ResultaatstructurenExporterenJob;
import nl.topicus.eduarte.resultaten.principals.onderwijs.OnderwijsproductResultaatstructuurExporteren;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.DownloadableLinkColumn;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.quartz.JobDataMap;

/**
 * @author papegaaij
 */
@PageInfo(title = "Resultaatstructuren exporteren", menu = "Onderwijs > Onderwijsproducten > Exporteren resultaatstructuren")
@InPrincipal(OnderwijsproductResultaatstructuurExporteren.class)
public class ResultaatstructurenExporterenOverzichtPage extends
		AbstractJobBeheerPage<ResultaatstructurenExporterenJobRun>
{
	public ResultaatstructurenExporterenOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs, ResultaatstructurenExporterenJob.class, null);
		getJobPanel().getJobRunTable()
			.addColumn(
				new DownloadableLinkColumn<ResultaatstructurenExporterenJobRun>("Download",
					"Download"));
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
				return new ResultaatstructurenExporterenSelectiePage(
					ResultaatstructurenExporterenOverzichtPage.this);
			}

			@Override
			public Class<ResultaatstructurenExporterenSelectiePage> getPageIdentity()
			{
				return ResultaatstructurenExporterenSelectiePage.class;
			}
		});
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id,
			OnderwijsCollectiefMenuItem.ResultaatstructurenExporteren);
	}
}
