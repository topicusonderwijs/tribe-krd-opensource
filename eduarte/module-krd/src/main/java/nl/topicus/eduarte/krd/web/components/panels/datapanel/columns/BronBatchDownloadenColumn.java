package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.web.components.modalwindow.DummyLink;
import nl.topicus.onderwijs.duo.bron.IBronBatch;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class BronBatchDownloadenColumn extends AbstractCustomColumn<IBronBatch>
{
	private static final long serialVersionUID = 1L;

	public BronBatchDownloadenColumn(String id, String header)
	{
		super(id, header);
	}

	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, IModel<IBronBatch> rowModel, int span)
	{

		IBronBatch batch = rowModel.getObject();
		LinkPanel container = new LinkPanel(componentId);
		container.add(new SimpleAttributeModifier("class", "spanBijlage"));
		if (batch.getBestand() != null)
		{
			container.add(new BronBatchDownloadenLink("link", rowModel));
		}
		else
		{
			// geen bestand en geen link ==> toon niets
			container.add(new DummyLink("link"));
			container.setVisible(false);
		}
		cellItem.add(container);

	}

	private static final class LinkPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		public LinkPanel(String id)
		{
			super(id);
		}
	}
}
