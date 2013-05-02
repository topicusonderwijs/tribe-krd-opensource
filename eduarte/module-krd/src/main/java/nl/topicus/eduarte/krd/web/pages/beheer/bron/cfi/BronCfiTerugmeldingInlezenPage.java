package nl.topicus.eduarte.krd.web.pages.beheer.bron.cfi;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.krd.bron.jobs.BronCfiTerugmeldingInlezenDataMap;
import nl.topicus.eduarte.krd.bron.jobs.BronCfiTerugmeldingInlezenJob;
import nl.topicus.eduarte.krd.entities.BronCfiTerugmInlezenJobRun;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.DeelnemerBronCfiTerugmeldingInlezen;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BronMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

/**
 * @author vandekamp
 */
@PageInfo(title = "CFI-terugmelding inlezen", menu = "Deelnemer")
@InPrincipal(DeelnemerBronCfiTerugmeldingInlezen.class)
public class BronCfiTerugmeldingInlezenPage extends
		AbstractJobBeheerPage<BronCfiTerugmInlezenJobRun>
{
	private static final long serialVersionUID = 1L;

	public BronCfiTerugmeldingInlezenPage()
	{
		super(CoreMainMenuItem.Beheer, BronCfiTerugmeldingInlezenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new BronCfiTerugmeldingInlezenDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, BronMenuItem.BRON);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new TerugButton(panel, BronAlgemeenPage.class));
		panel.addButton(new PageLinkButton(panel, "CFI-terugmelding overzicht",
			BronCfiTerugmeldingenPage.class));
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Bestand inlezen";
	}
}
