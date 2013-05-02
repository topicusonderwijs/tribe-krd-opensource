package nl.topicus.eduarte.krdparticipatie.web.pages.beheer.importeren;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.krdparticipatie.entities.AbsentiemeldingenImporterenJobRun;
import nl.topicus.eduarte.krdparticipatie.jobs.AbsentiemeldingenImporterenDataMap;
import nl.topicus.eduarte.krdparticipatie.jobs.AbsentiemeldingenImporterenJob;
import nl.topicus.eduarte.krdparticipatie.principals.beheer.AbsentiemeldingenImporteren;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieBeheerMenuItem;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

/**
 * Pagina voor het importeren van absentiemeldingen vanuit een csv-bestand.
 * 
 * @author loite
 */
@PageInfo(title = "Absentiemeldingen importeren", menu = "Beheer > Aanwezigheid > Absentiemeldingen importeren")
@InPrincipal(AbsentiemeldingenImporteren.class)
public class AbsentiemeldingenImporterenPage extends
		AbstractJobBeheerPage<AbsentiemeldingenImporterenJobRun>
{
	private static final long serialVersionUID = 1L;

	public AbsentiemeldingenImporterenPage()
	{
		super(CoreMainMenuItem.Beheer, AbsentiemeldingenImporterenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new AbsentiemeldingenImporterenDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, ParticipatieBeheerMenuItem.AbsentiemeldingenImporteren);
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Bestand importeren";
	}

}
