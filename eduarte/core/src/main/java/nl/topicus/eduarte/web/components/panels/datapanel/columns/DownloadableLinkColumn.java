package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.bijlage.IDownloadable;
import nl.topicus.eduarte.web.components.modalwindow.BijlageLink;
import nl.topicus.eduarte.web.components.modalwindow.DummyLink;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class DownloadableLinkColumn<T extends IDownloadable> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	public DownloadableLinkColumn(String id, String header)
	{
		super(id, header);
	}

	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, IModel<T> rowModel, int span)
	{
		IDownloadable bijlage = rowModel.getObject();

		LinkPanel container = new LinkPanel(componentId);
		container.add(new SimpleAttributeModifier("class", "spanBijlage"));
		if (bijlage.getBestand() != null)
		{
			BijlageLink<T> link = new BijlageLink<T>("link", rowModel);
			container.add(link);
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
