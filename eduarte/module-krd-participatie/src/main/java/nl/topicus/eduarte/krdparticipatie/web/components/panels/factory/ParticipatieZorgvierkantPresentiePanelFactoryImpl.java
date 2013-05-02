package nl.topicus.eduarte.krdparticipatie.web.components.panels.factory;

import nl.topicus.cobra.modules.AbstractModuleComponentFactory;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.factory.ZorgvierkantPresentiePanelFactory;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ParticipatieZorgvierkantPresentiePanelFactoryImpl extends
		AbstractModuleComponentFactory implements ZorgvierkantPresentiePanelFactory
{
	public ParticipatieZorgvierkantPresentiePanelFactoryImpl()
	{
		super(10);
	}

	@Override
	public Panel create(String id, IModel<Deelnemer> deelnemerModel)
	{
		return new ZorgvierkantPresentiePanel(id, deelnemerModel);
	}

}
