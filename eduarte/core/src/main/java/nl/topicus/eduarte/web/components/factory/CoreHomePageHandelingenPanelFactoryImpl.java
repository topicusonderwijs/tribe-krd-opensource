package nl.topicus.eduarte.web.components.factory;

import nl.topicus.cobra.modules.AbstractModuleComponentFactory;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

public class CoreHomePageHandelingenPanelFactoryImpl extends AbstractModuleComponentFactory
		implements HomePageHandelingenPanelFactory
{
	public CoreHomePageHandelingenPanelFactoryImpl()
	{
		super(1);
	}

	@Override
	public Panel create(String id)
	{
		return new EmptyPanel(id);
	}

}
