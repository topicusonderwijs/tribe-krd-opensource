package nl.topicus.cobra.web.components.shortcut;

/**
 * In deze enum zijn een aantal toetscombinaties gedefineerd die aan een component
 * gekoppeld worden door de ShortcutEnabledComponent interface te implementeren.
 * <p>
 * let wel op: IE voert ook zijn default functie uit dus let goed op welke toetsen je
 * gebruikt.
 * 
 * @author Nick Henzen
 */
public enum CobraKeyAction implements ActionKey
{
	GEEN(""),
	ANNULEREN("Ctrl+Backspace"),
	SLUITEN("Ctrl+Backspace"),
	OPSLAAN("Ctrl+Return"),
	VOLGENDE("Alt+Ctrl+Return"),
	VERWIJDEREN("Ctrl+Delete"),
	TOEVOEGEN("Ctrl+Insert"),
	LINKKNOP1("Ctrl+Right"),
	TERUG("Ctrl+Left"),
	BEWERKEN("Ctrl+e"),
	VOLGEND_ZOEKRESULTAAT("Ctrl+Shift+Right"),
	VORIG_ZOEKRESULTAAT("Ctrl+Shift+Left"),
	QUICKLIST_ZOEKRESULTAAT("Ctrl+Shift+Down"),
	NAAR_ZOEKRESULTAAT("Ctrl+Shift+Up");

	private final String shortKeys;

	CobraKeyAction(String shortKeys)
	{
		this.shortKeys = shortKeys;
	}

	public String getShortKeys()
	{
		return shortKeys;
	}
}
