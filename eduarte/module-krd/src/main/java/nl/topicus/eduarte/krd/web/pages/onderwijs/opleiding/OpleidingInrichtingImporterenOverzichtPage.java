package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.krd.entities.OpleidingInrichtingImporterenJobRun;
import nl.topicus.eduarte.krd.jobs.OpleidingInrichtingImporterenJob;
import nl.topicus.eduarte.krd.jobs.OpleidingInrichtingImporterenJobDataMap;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingInrichtingImporteren;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

/**
 * @author papegaaij
 */
@PageInfo(title = "Opleidingsinrichting importeren", menu = "Onderwijs > Opleiding > Inrichting importeren")
@InPrincipal(OpleidingInrichtingImporteren.class)
public class OpleidingInrichtingImporterenOverzichtPage extends
		AbstractJobBeheerPage<OpleidingInrichtingImporterenJobRun>
{
	public OpleidingInrichtingImporterenOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs, OpleidingInrichtingImporterenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new OpleidingInrichtingImporterenJobDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.InrichtingImporteren);
	}
}
