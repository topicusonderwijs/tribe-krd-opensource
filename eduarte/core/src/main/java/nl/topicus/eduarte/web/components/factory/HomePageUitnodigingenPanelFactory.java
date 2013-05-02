package nl.topicus.eduarte.web.components.factory;

import nl.topicus.cobra.modules.ModuleComponentFactory;

import org.apache.wicket.markup.html.panel.Panel;

public interface HomePageUitnodigingenPanelFactory extends ModuleComponentFactory
{
	public Panel create(String id);
}
