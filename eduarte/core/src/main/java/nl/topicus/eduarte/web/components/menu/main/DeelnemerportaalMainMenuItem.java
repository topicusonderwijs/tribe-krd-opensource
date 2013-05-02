package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.web.components.menu.main.MainMenuItem;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.eduarte.web.pages.KeyActionEnum;

/**
 * De verschillende items van het menu.
 * 
 * @author marrink
 */
public enum DeelnemerportaalMainMenuItem implements MainMenuItem
{
	// TODO Paul: rechten verwijderen
	/**
	 * De homepage van een gebruiker van het systeem.
	 */
	Home(KeyActionEnum.HOOFDMENU_HOME),
	/**
	 * Schermen die van toepassing zijn op de verschillende groepen die gedefinieerd zijn.
	 */
	Dossier(KeyActionEnum.HOOFDMENU_DOSSIER),
	/**
	 * Schermen die van toepassing zijn op de verschillende groepen die gedefinieerd zijn.
	 */
	Help(KeyActionEnum.HOOFDMENU_HELP/* , RechtenSoort.DEELNEMER */);

	private ActionKey shortcut;

	DeelnemerportaalMainMenuItem(ActionKey shortcut)
	{
		this.shortcut = shortcut;
	}

	@Override
	public String getLabel()
	{
		return toString();
	}

	@Override
	public int getIndex()
	{
		return ordinal() * 100;
	}

	@Override
	public ActionKey getShortcut()
	{
		return shortcut;
	}
}
