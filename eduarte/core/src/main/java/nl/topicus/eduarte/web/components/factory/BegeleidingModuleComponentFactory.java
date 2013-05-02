package nl.topicus.eduarte.web.components.factory;

import java.util.List;

import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.wiquery.CollapsablePanel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;

public interface BegeleidingModuleComponentFactory extends ModuleComponentFactory
{
	public CollapsablePanel< ? > getDeelnemerkaartPanel(String id, IModel<Deelnemer> deelnemerModel);

	public ContextImage getDeelnemerTitelImage(String id, IModel<Deelnemer> deelnemerModel);

	public List<AbstractCustomColumn<Verbintenis>> getDeelnemerTableColumns();
}
