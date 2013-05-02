package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.eduarte.krd.entities.PersoonFotosInlezenJobRun;
import nl.topicus.eduarte.krd.jobs.PersoonFotosInlezenJob;
import nl.topicus.eduarte.krd.jobs.PersoonFotosInlezenJobDataMap;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.FotosUploadenPrincipal;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.util.lang.Bytes;
import org.quartz.JobDataMap;

/**
 * Pagina voor het inlezen en verwerken van deelnemer en medewerker fotos. Deze fotos zijn
 * de pasfotos die je ziet bij de deelnemerkaart en de medewerkerkaart. De fotos worden
 * verkleind naar maximaal 240x320 (B x H). Zie de PersoonFotosInlezenJob class voor meer
 * informatie.
 */
@PageInfo(title = "Foto's uploaden", menu = "Beheer > Systeem > Foto's uploaden")
@InPrincipal(FotosUploadenPrincipal.class)
public class UploadFotosPage extends AbstractJobBeheerPage<PersoonFotosInlezenJobRun>
{
	public UploadFotosPage()
	{
		super(CoreMainMenuItem.Beheer, PersoonFotosInlezenJob.class, "");

		// mantis 53485
		setMaxSize(Bytes.megabytes(100));
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new PersoonFotosInlezenJobDataMap();
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Fotos uploaden";
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, BeheerMenuItem.FotosUploaden);
	}
}
