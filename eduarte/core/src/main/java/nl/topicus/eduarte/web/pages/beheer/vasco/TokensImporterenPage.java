package nl.topicus.eduarte.web.pages.beheer.vasco;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.core.principals.beheer.systeem.VascoTokensBeheren;
import nl.topicus.eduarte.entities.vasco.VascoTokensImporterenJobRun;
import nl.topicus.eduarte.jobs.vasco.VascoTokensImporterenDataMap;
import nl.topicus.eduarte.jobs.vasco.VascoTokensImporterenJob;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.quartz.JobDataMap;

@PageInfo(title = "Vasco tokens importeren", menu = "Beheer > Accounts > Importeren")
@InPrincipal(VascoTokensBeheren.class)
public class TokensImporterenPage extends AbstractJobBeheerPage<VascoTokensImporterenJobRun>
{
	public TokensImporterenPage()
	{
		super(CoreMainMenuItem.Beheer, VascoTokensImporterenJob.class, null);
	}

	@Override
	protected JobDataMap createDataMap()
	{
		return new VascoTokensImporterenDataMap();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new BeheerMenu(id, BeheerMenuItem.TokensImporteren);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		// panel.addButton(new TerugButton(panel, BronFotobestandenPage.class));
	}

	@Override
	protected String getTaakStartenButtonTekst()
	{
		return "Bestand inlezen";
	}
}
