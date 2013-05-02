package nl.topicus.eduarte.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.core.principals.onderwijs.OpleidingInzien;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;

/**
 * @author idserda
 */
@PageInfo(title = "Opleidingkaart (opleiding herstellen)", menu = {"Onderwijs > Opleidingen > Opleidingen Herstellen > [opleiding]"})
@InPrincipal(OpleidingInzien.class)
public class OpleidingkaartHerstellenPage extends OpleidingkaartPage
{
	private static final long serialVersionUID = 1L;

	public OpleidingkaartHerstellenPage(Opleiding opleiding)
	{
		super(opleiding);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new ModuleEditPageButton<Opleiding>(panel, "Bewerken",
			CobraKeyAction.BEWERKEN, Opleiding.class, OnderwijsCollectiefMenuItem.Herstellen,
			OpleidingkaartHerstellenPage.this, getContextOpleidingModel()));
		panel.addButton(new TerugButton(panel, OpleidingHerstellenPage.class));
	}
}
