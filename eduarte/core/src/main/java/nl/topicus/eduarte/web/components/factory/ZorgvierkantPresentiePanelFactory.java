package nl.topicus.eduarte.web.components.factory;

import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public interface ZorgvierkantPresentiePanelFactory extends ModuleComponentFactory
{
	public Panel create(String id, IModel<Deelnemer> deelnemerModel);
}
