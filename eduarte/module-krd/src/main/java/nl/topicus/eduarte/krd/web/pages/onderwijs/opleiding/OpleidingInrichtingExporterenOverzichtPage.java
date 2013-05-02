package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.krd.entities.OpleidingInrichtingExporterenJobRun;
import nl.topicus.eduarte.krd.jobs.OpleidingInrichtingExporterenJob;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingInrichtingExporteren;
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
@PageInfo(title = "Opleidingsinrichting exporteren", menu = "Onderwijs > Opleiding > Inrichting exporteren")
@InPrincipal(OpleidingInrichtingExporteren.class)
public class OpleidingInrichtingExporterenOverzichtPage extends
		AbstractJobBeheerPage<OpleidingInrichtingExporterenJobRun>
{
	public OpleidingInrichtingExporterenOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs, OpleidingInrichtingExporterenJob.class, null);
		getJobPanel().getJobRunTable()
			.addColumn(
				new DownloadableLinkColumn<OpleidingInrichtingExporterenJobRun>("Download",
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
				return new OpleidingInrichtingExporterenSelectiePage(
					OpleidingInrichtingExporterenOverzichtPage.this);
			}

			@Override
			public Class<OpleidingInrichtingExporterenSelectiePage> getPageIdentity()
			{
				return OpleidingInrichtingExporterenSelectiePage.class;
			}
		});
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.InrichtingExporteren);
	}
}
