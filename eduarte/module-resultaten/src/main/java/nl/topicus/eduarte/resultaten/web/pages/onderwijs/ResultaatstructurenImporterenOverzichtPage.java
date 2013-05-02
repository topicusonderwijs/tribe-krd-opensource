package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.resultaten.entities.ResultaatstructurenImporterenJobRun;
import nl.topicus.eduarte.resultaten.jobs.ResultaatstructurenImporterenJob;
import nl.topicus.eduarte.resultaten.jobs.ResultaatstructurenImporterenJobDataMap;
import nl.topicus.eduarte.resultaten.principals.onderwijs.OnderwijsproductResultaatstructuurImporteren;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

/**
 * @author papegaaij
 */
@PageInfo(title = "Resultaatstructuren importeren", menu = "Onderwijs > Onderwijsproducten > Importeren resultaatstructuren")
@InPrincipal(OnderwijsproductResultaatstructuurImporteren.class)
public class ResultaatstructurenImporterenOverzichtPage extends
		AbstractJobBeheerPage<ResultaatstructurenImporterenJobRun>
{
	public ResultaatstructurenImporterenOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs, ResultaatstructurenImporterenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new ResultaatstructurenImporterenJobDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id,
			OnderwijsCollectiefMenuItem.ResultaatstructurenImporteren);
	}
}
