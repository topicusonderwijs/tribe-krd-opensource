package nl.topicus.cobra.web.components.shortcut;

import org.apache.wicket.Component;

public interface ShortcutRegister
{
	public void registerShortcut(Component c, ActionKey action);
}
