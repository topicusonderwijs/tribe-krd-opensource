package nl.topicus.eduarte.onderwijscatalogus.web.pages.onderwijsproduct.importeren;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.onderwijscatalogus.entities.ToegestaanOndProdImportJobRun;
import nl.topicus.eduarte.onderwijscatalogus.jobs.ToegestaanOndProdImportDataMap;
import nl.topicus.eduarte.onderwijscatalogus.jobs.ToegestaanOndProdImportJob;
import nl.topicus.eduarte.onderwijscatalogus.principals.onderwijs.ToegestaneOnderwijsproductenImporteren;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

/**
 * @author vandekamp
 */
@PageInfo(title = "Toegestane onderwijsproducten importeren", menu = "Onderwijs > Onderwijsproducten > Toegestane onderwijsproducten importeren")
@InPrincipal(ToegestaneOnderwijsproductenImporteren.class)
public class ToegestaanOndProdImportOverzichtPage extends
		AbstractJobBeheerPage<ToegestaanOndProdImportJobRun>
{
	public ToegestaanOndProdImportOverzichtPage()
	{
		super(CoreMainMenuItem.Onderwijs, ToegestaanOndProdImportJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new ToegestaanOndProdImportDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id,
			OnderwijsCollectiefMenuItem.ToegestaneOnderwijsproductenImporteren);
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Bestand importeren";
	}

}
