package nl.topicus.eduarte.krd.web.pages.beheer.bron.foto;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.krd.bron.jobs.BronFotobestandInlezenDataMap;
import nl.topicus.eduarte.krd.bron.jobs.BronFotobestandInlezenJob;
import nl.topicus.eduarte.krd.entities.BronFotobestandInlezenJobRun;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.DeelnemerBronFotoInlezen;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BronMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

/**
 * 
 * 
 * @author loite
 */
@PageInfo(title = "BRON Fotobestand inlezen", menu = "Deelnemer")
@InPrincipal(DeelnemerBronFotoInlezen.class)
public class BronFotobestandInlezenPage extends AbstractJobBeheerPage<BronFotobestandInlezenJobRun>
{
	private static final long serialVersionUID = 1L;

	public BronFotobestandInlezenPage()
	{
		super(CoreMainMenuItem.Beheer, BronFotobestandInlezenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new BronFotobestandInlezenDataMap();
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
		panel.addButton(new TerugButton(panel, BronFotobestandenPage.class));
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Bestand inlezen";
	}
}
