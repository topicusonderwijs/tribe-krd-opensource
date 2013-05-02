package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.web.components.modalwindow.BijlageLink;
import nl.topicus.eduarte.web.components.modalwindow.DummyLink;

import org.apache.wicket.authorization.Action;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.checks.ISecurityCheck;

/**
 * @author loite
 */
public class BijlageLinkColumn<T extends BijlageEntiteit> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	private static final PopupSettings POPUP =
		new PopupSettings(PopupSettings.LOCATION_BAR | PopupSettings.MENU_BAR
			| PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS | PopupSettings.STATUS_BAR
			| PopupSettings.TOOL_BAR);

	private final ISecurityCheck securityCheck;

	public BijlageLinkColumn(String id, String header)
	{
		this(id, header, null);
	}

	public BijlageLinkColumn(String id, String header, ISecurityCheck securityCheck)
	{
		super(id, header);
		this.securityCheck = securityCheck;
	}

	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, IModel<T> rowModel, int span)
	{
		BijlageEntiteit bijlageEntiteit = rowModel.getObject();
		Bijlage bijlage = bijlageEntiteit.getBijlage();

		LinkPanel container = new LinkPanel(componentId);
		container.add(new SimpleAttributeModifier("class", "spanBijlage"));
		if (bijlage.getBestand() != null)
		{
			BijlageLink<Bijlage> link =
				new BijlageLink<Bijlage>("link", new PropertyModel<Bijlage>(rowModel, "bijlage"));
			if (securityCheck != null)
			{
				link.setSecurityCheck(securityCheck);
				container.setVisibilityAllowed(link.isActionAuthorized(Action.RENDER));
			}
			container.add(link);
		}
		else if (bijlage.getLink() != null && !bijlage.getLink().isEmpty())
		{
			ExternalLink el = new ExternalLink("link", bijlage.getLink());
			el.add(new Label("label", bijlage.getLink()));
			el.setPopupSettings(POPUP);
			el.add(new SimpleAttributeModifier("title", "Link openen in een nieuw venster"));
			container.add(el);
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

		/**
		 * @param id
		 */
		public LinkPanel(String id)
		{
			super(id);
		}
	}

}
