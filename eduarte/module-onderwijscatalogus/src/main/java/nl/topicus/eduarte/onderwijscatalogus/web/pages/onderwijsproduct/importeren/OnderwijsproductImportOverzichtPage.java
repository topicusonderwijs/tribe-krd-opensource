package nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct.importeren;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.onderwijscatalogus.entities.OnderwijsproductImportJobRun;
import nl.topicus.eduarte.onderwijscatalogus.jobs.OnderwijsproductImportDataMap;
import nl.topicus.eduarte.onderwijscatalogus.jobs.OnderwijsproductImportJob;
import nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs.OnderwijsproductenImporteren;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

/**
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproducten importeren", menu = "Onderwijs > Onderwijsproducten > Onderwijsproducten importeren")
@InPrincipal(OnderwijsproductenImporteren.class)
public class OnderwijsproductImportOverzichtPage extends
		AbstractJobBeheerPage<OnderwijsproductImportJobRun>
{
	public OnderwijsproductImportOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs, OnderwijsproductImportJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new OnderwijsproductImportDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id,
			OnderwijsCollectiefMenuItem.OnderwijsproductenImporteren);
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Bestand importeren";
	}

}
