package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.PanelColumn;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.BijBestKeuzeColumnPanel;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.ProductDatumColumnPanel;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.columns.ProductKeuzeColumnPanel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.DeelnemerCollectieveAfnameContext;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class CollectieveAfnameContextInvulTable extends
		CustomDataPanelContentDescription<DeelnemerCollectieveAfnameContext>
{
	private static final long serialVersionUID = 1L;

	public CollectieveAfnameContextInvulTable()
	{
		super("Productregels invullen");
		addColumn(new CustomPropertyColumn<DeelnemerCollectieveAfnameContext>("Naam", "Naam",
			"productregel.naam", "productregel.naam"));
		addColumn(new PanelColumn<DeelnemerCollectieveAfnameContext>("Keuze", "Keuze")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getPanel(String componentId, WebMarkupContainer row,
					IModel<DeelnemerCollectieveAfnameContext> model)
			{
				return new ProductKeuzeColumnPanel(componentId, model, row);
			}

		});
		addColumn(new PanelColumn<DeelnemerCollectieveAfnameContext>("Datum", "Datum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getPanel(String componentId, WebMarkupContainer row,
					IModel<DeelnemerCollectieveAfnameContext> model)
			{
				return new ProductDatumColumnPanel(componentId, model);
			}

		});
		addColumn(new PanelColumn<DeelnemerCollectieveAfnameContext>("BijBestaandeKeuze",
			"Bij Bestaande Keuze")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getPanel(String componentId, WebMarkupContainer row,
					IModel<DeelnemerCollectieveAfnameContext> model)
			{
				return new BijBestKeuzeColumnPanel(componentId, model);
			}
		});
	}
}
