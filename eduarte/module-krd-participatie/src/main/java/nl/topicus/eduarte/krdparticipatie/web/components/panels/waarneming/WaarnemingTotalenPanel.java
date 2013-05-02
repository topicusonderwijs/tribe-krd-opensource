package nl.topicus.eduarte.krdparticipatie.web.components.panels.waarneming;

import java.util.List;

import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.listview.ExportableListView;
import nl.topicus.eduarte.krdparticipatie.web.components.models.TotalenModel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.WaarnemingOverzichtInterface;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author vanderkamp
 */
public class WaarnemingTotalenPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private WaarnemingOverzichtZoekFilter filter;

	public WaarnemingTotalenPanel(String id, final WaarnemingOverzichtInterface page)
	{
		super(id);
		filter = page.getFilterModel().getObject();
		ExportableListView<WaarnemingTotaalOverzicht> totalen =
			new ExportableListView<WaarnemingTotaalOverzicht>("totalen", new TotalenModel(filter))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<WaarnemingTotaalOverzicht> item)
				{
					WaarnemingTotaalOverzicht totaalOverzicht = item.getModelObject();
					Label kleur = new Label("kleur", new Model<String>());
					kleur.add(new AttributeAppender("class", new Model<String>("td"
						+ totaalOverzicht.getCollor()), " "));
					kleur.add(new AttributeAppender("class", new Model<String>("tdKlein"), " "));
					item.add(kleur);
					item.add(ComponentFactory.getDataLabel("type"));
					item.add(ComponentFactory.getDataLabel("klokuren"));
					item.add(ComponentFactory.getDataLabel("lesuren"));
				}

				@Override
				protected IModel<WaarnemingTotaalOverzicht> getListItemModel(
						IModel< ? extends List<WaarnemingTotaalOverzicht>> listViewModel, int index)
				{
					return new CompoundPropertyModel<WaarnemingTotaalOverzicht>(super
						.getListItemModel(listViewModel, index));
				}

				@Override
				public boolean allowSkipColumns()
				{
					return true;
				}
			};
		add(totalen);

	}
}
