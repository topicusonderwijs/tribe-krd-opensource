package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author vandekamp
 */
public class DeelnemernummerClickableColumn extends AbstractCustomColumn<IBronSignaal>
{
	private static final long serialVersionUID = 1L;

	public DeelnemernummerClickableColumn(String id, String header)
	{
		super(id, header);
	}

	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, final IModel<IBronSignaal> rowModel, int span)
	{
		LinkPanel container = new LinkPanel(componentId);
		Link<Void> link = new Link<Void>("link")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				IBronSignaal signaal = rowModel.getObject();
				setResponsePage(new DeelnemerkaartPage(signaal.getAanleverMelding().getDeelnemer()));
			}

		};
		container.add(link);
		link.add(new Label("deelnemernummer", new PropertyModel<Integer>(rowModel,
			"aanleverMelding.deelnemer.deelnemernummer")));
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
