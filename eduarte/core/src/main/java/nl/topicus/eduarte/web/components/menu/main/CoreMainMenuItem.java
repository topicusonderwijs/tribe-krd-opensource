package nl.topicus.eduarte.web.components.menu.main;

import nl.topicus.cobra.web.components.menu.main.MainMenuItem;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.web.pages.KeyActionEnum;

/**
 * De verschillende items van het menu.
 * 
 * @author marrink
 */
public enum CoreMainMenuItem implements MainMenuItem
{
	/**
	 * De homepage van een gebruiker van het systeem.
	 */
	Home(KeyActionEnum.HOOFDMENU_HOME),
	/**
	 * Schermen die van toepassing zijn op de leerlingen.
	 */
	Deelnemer(KeyActionEnum.HOOFDMENU_DEELNEMER)
	{
		@Override
		public String getLabel()
		{
			return EduArteApp.get().getDeelnemerTerm();
		}
	},
	/**
	 * Schermen die van toepassing zijn op de verschillende groepen die gedefinieerd zijn.
	 */
	Groep(KeyActionEnum.HOOFDMENU_GROEP),
	/**
	 * Schermen om de medewerkers van de organisatie te beheren.
	 */
	Medewerker(KeyActionEnum.HOOFDMENU_MEDEWERKER),
	/**
	 * Beheer schermen voor de organisatie.
	 */
	Onderwijs(KeyActionEnum.HOOFDMENU_ONDERWIJS),

	/**
	 * Beheer schermen voor relatiebeheer
	 */
	Relatie(KeyActionEnum.HOOFDMENU_RELATIE),

	/**
	 * Schermen om onderwijsinrichting vast te leggen.
	 */
	Beheer(KeyActionEnum.HOOFDMENU_BEHEER),
	/**
	 * Een link naar de wikipagina
	 */
	Help(KeyActionEnum.HOOFDMENU_HELP);

	private ActionKey shortcut;

	CoreMainMenuItem(ActionKey shortcut)
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
