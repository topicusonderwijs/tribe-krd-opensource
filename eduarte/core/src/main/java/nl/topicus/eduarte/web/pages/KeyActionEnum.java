package nl.topicus.eduarte.web.pages;

import nl.topicus.cobra.web.components.shortcut.ActionKey;

/**
 * In deze enum zijn een aantal toetsen combinaties gedefineerd.</br> Deze toesen
 * combinaties kunnen aan een component gekoppeld </br> worden door de
 * ShortcutEnabledComponent interface te implementeren.
 * <p>
 * let wel op: IE voert ook zijn default functie uit dus let goed op welke toetsen je
 * gebruikt.
 * 
 * @author Nick Henzen
 */
public enum KeyActionEnum implements ActionKey
{
	HOOFDMENU_HOME("Alt+Ctrl+h"),
	HOOFDMENU_DEELNEMER("Alt+Ctrl+d"),
	HOOFDMENU_GROEP("Alt+Ctrl+g"),
	HOOFDMENU_MEDEWERKER("Alt+Ctrl+m"),
	HOOFDMENU_ONDERWIJS("Alt+Ctrl+o"),
	HOOFDMENU_BEHEER("Alt+Ctrl+b"),
	HOOFDMENU_RELATIE("Alt+Ctrl+r"),
	HOOFDMENU_HELP("Alt+Ctrl+f1"),
	HOOFDMENU_ZOEKEN("Alt+Ctrl+f"),
	// financieel
	HOOFDMENU_FINANCIEEL("Alt+Ctrl+i"),
	// deelnemerportaal
	HOOFDMENU_DOSSIER("Alt+Ctrl+d"),
	HOOFDMENU_ABSENTIE("Alt+Ctrl+a"),
	// digitaal aanmelden
	HOOFDMENU_DIGITAAL_AANMELDEN("Alt+Ctrl+p");

	private final String shortKeys;

	KeyActionEnum(String shortKeys)
	{
		this.shortKeys = shortKeys;
	}

	public String getShortKeys()
	{
		return shortKeys;
	}
}
