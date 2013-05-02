package nl.topicus.eduarte.web.pages.deelnemerportaal.dossier;

import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.menu.deelnemerPortaal.DeelnemerportaalDossierMenu;
import nl.topicus.eduarte.web.components.menu.main.DeelnemerportaalMainMenuItem;
import nl.topicus.eduarte.web.pages.SelfServiceSecurePage;

/**
 * Abstracte pagina voor alle home pagina's. Let op subclasses dienen nog wel
 * {@link #createComponents()} aan te roepen.
 * 
 * @author marrink
 */
public abstract class AbstractDeelnemerportaalDossierPage extends SelfServiceSecurePage
{
	private final MenuItemKey selectedTab;

	/**
	 * Keuze welke tab geselecteerd is.
	 */
	protected AbstractDeelnemerportaalDossierPage(MenuItemKey selectedTab, Verbintenis verbintenis)
	{
		super(DeelnemerportaalMainMenuItem.Dossier, verbintenis);
		this.selectedTab = selectedTab;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new DeelnemerportaalDossierMenu(id, selectedTab, getContextVerbintenisModel());
	}
}
