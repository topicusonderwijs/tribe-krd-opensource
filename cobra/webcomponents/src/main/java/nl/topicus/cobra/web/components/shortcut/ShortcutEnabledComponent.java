package nl.topicus.cobra.web.components.shortcut;

/**
 * Doordeze interface aanteroepen is het mogelijk een toetsencombinatie te binden aan een
 * component. Deze toetsencombinaties zijn gedefineerd in KeyActionEnum.java.
 * <p>
 * Mogelijke componenten zijn: textfield, links en buttons.
 * <p>
 * <b>Let wel op:</b> werkt niet met butons die een popupgenereren</br> omdat de return
 * waarde wordt genegeerd.
 * </p>
 * <p>
 * <b>Voorbeeld:</b></br> page.registerShortcut(get("submit"), KeyActionEnum.OPSLAAN);
 * </p>
 * 
 * @author Nick Henzen
 */
public interface ShortcutEnabledComponent
{
	public void registerShortcuts(ShortcutRegister register);
}
