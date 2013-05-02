package nl.topicus.eduarte.resultaten.web.pages.deelnemer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.resultaten.entities.ResultatenImporterenJobRun;
import nl.topicus.eduarte.resultaten.jobs.ResultatenImporterenDataMap;
import nl.topicus.eduarte.resultaten.jobs.ResultatenImporterenJob;
import nl.topicus.eduarte.resultaten.principals.deelnemer.ResultatenImporteren;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

/**
 * @author papegaaij
 */
@PageInfo(title = "Resultaten importeren", menu = "Deelnemer > Resultaten > Resultaten importeren")
@InPrincipal(ResultatenImporteren.class)
public class ResultatenImporterenPage extends AbstractJobBeheerPage<ResultatenImporterenJobRun>
{
	private static final long serialVersionUID = 1L;

	public ResultatenImporterenPage()
	{
		super(CoreMainMenuItem.Deelnemer, ResultatenImporterenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new ResultatenImporterenDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.ResultatenImporteren);
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Resultaten importeren";
	}
}
