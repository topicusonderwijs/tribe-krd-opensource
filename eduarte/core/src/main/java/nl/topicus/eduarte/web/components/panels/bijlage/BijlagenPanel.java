package nl.topicus.eduarte.web.components.panels.bijlage;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.web.components.modalwindow.BijlageLink;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class BijlagenPanel<E extends BijlageEntiteit, T extends IBijlageKoppelEntiteit<E>> extends
		TypedPanel<T>
{
	private static final long serialVersionUID = 1L;

	private static final PopupSettings POPUP =
		new PopupSettings(PopupSettings.LOCATION_BAR | PopupSettings.MENU_BAR
			| PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS | PopupSettings.STATUS_BAR
			| PopupSettings.TOOL_BAR);

	public BijlagenPanel(String id, IModel<T> model)
	{
		this(id, model, "Bijlagen");
	}

	public BijlagenPanel(String id, IModel<T> model, String caption)
	{
		super(id, model);

		add(new Label("caption", caption));

		final ListView<BijlageEntiteit> bijlagen =
			new ListView<BijlageEntiteit>("bijlagen", new PropertyModel<List<BijlageEntiteit>>(
				model, "bijlagen"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final ListItem<BijlageEntiteit> item)
				{
					Label bijlageOmschrijving =
						new Label("omschrijving", new PropertyModel<String>(item.getModel(),
							"bijlage.omschrijving"));
					item.add(bijlageOmschrijving);

					Bijlage bijlage = (item.getModelObject()).getBijlage();
					if (bijlage.getBestand() != null)
					{
						BijlageLink<Bijlage> link =
							new BijlageLink<Bijlage>("link", new PropertyModel<Bijlage>(item
								.getModel(), "bijlage"));
						item.add(link);
					}
					else if (bijlage.getLink() != null && !bijlage.getLink().isEmpty())
					{
						ExternalLink el = new ExternalLink("link", bijlage.getLink());
						el.add(new Label("label", bijlage.getLink()));
						el.setPopupSettings(POPUP);
						el.add(new SimpleAttributeModifier("title",
							"Link openen in een nieuw venster"));
						item.add(el);
					}
				}
			};
		add(bijlagen);

		WebMarkupContainer geenBijlagen = new WebMarkupContainer("geenBijlagen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				Collection< ? > bijlagenCol = bijlagen.getModelObject();
				return super.isVisible() && bijlagenCol.isEmpty();
			}
		};
		add(geenBijlagen);
	}

	@Override
	public boolean isVisible()
	{
		return super.isVisible() && getModelObject() != null;
	}
}
