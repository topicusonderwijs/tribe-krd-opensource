package nl.topicus.eduarte.resultaten.web.pages.deelnemer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.resultaten.entities.SeResultatenInlezenJobRun;
import nl.topicus.eduarte.resultaten.jobs.SeResultatenInlezenDataMap;
import nl.topicus.eduarte.resultaten.jobs.SeResultatenInlezenJob;
import nl.topicus.eduarte.resultaten.principals.deelnemer.SeResultatenImporteren;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

/**
 * @author vandekamp
 */
@PageInfo(title = "SE resultaten inlezen", menu = "Deelnemer > Resultaten > SE-Resultaten importeren")
@InPrincipal(SeResultatenImporteren.class)
public class SeResultatenImporterenPage extends AbstractJobBeheerPage<SeResultatenInlezenJobRun>
{
	private static final long serialVersionUID = 1L;

	public SeResultatenImporterenPage()
	{
		super(CoreMainMenuItem.Deelnemer, SeResultatenInlezenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new SeResultatenInlezenDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerCollectiefMenu(id, DeelnemerCollectiefMenuItem.SeResultatenInlezen);
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Bestand inlezen";
	}
}
