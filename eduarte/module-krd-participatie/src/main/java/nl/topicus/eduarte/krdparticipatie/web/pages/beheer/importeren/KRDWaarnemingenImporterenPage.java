package nl.topicus.eduarte.krdparticipatie.web.pages.beheer.importeren;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.krdparticipatie.entities.KRDWaarnemingenImporterenJobRun;
import nl.topicus.eduarte.krdparticipatie.jobs.KRDWaarnemingenImporterenDataMap;
import nl.topicus.eduarte.krdparticipatie.jobs.KRDWaarnemingenImporterenJob;
import nl.topicus.eduarte.krdparticipatie.principals.beheer.KRDWaarnemingenImporteren;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieBeheerMenuItem;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

/**
 * Pagina voor het importeren van waarnemingen vanuit een csv-bestand.
 * 
 * @author loite
 */
@PageInfo(title = "Waarnemingen importeren", menu = "Beheer > Aanwezigheid > Waarnemingen importeren")
@InPrincipal(KRDWaarnemingenImporteren.class)
public class KRDWaarnemingenImporterenPage extends
		AbstractJobBeheerPage<KRDWaarnemingenImporterenJobRun>
{
	private static final long serialVersionUID = 1L;

	public KRDWaarnemingenImporterenPage()
	{
		super(CoreMainMenuItem.Beheer, KRDWaarnemingenImporterenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new KRDWaarnemingenImporterenDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, ParticipatieBeheerMenuItem.KRDWaarnemingenImporteren);
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Bestand importeren";
	}

}
