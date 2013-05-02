package nl.topicus.eduarte.web.components.factory;

import nl.topicus.cobra.modules.AbstractModuleComponentFactory;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class CoreZorgvierkantPresentiePanelFactoryImpl extends AbstractModuleComponentFactory
		implements ZorgvierkantPresentiePanelFactory
{
	public CoreZorgvierkantPresentiePanelFactoryImpl()
	{
		super(0);
	}

	@Override
	public Panel create(String id, IModel<Deelnemer> deelnemerModel)
	{
		return new EmptyPanel(id);
	}

}
