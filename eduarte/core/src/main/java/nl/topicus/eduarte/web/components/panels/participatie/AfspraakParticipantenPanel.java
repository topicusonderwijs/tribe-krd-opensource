package nl.topicus.eduarte.web.components.panels.participatie;

import java.util.List;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.web.components.panels.afspraak.EntiteitTypeImage;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class AfspraakParticipantenPanel extends TypedPanel<Afspraak>
{
	private static final long serialVersionUID = 1L;

	public AfspraakParticipantenPanel(String id, IModel<Afspraak> model)
	{
		super(id, model);

		setOutputMarkupPlaceholderTag(true);

		add(new ListView<AfspraakParticipant>("participanten",
			new PropertyModel<List<AfspraakParticipant>>(model, "participanten"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<AfspraakParticipant> item)
			{
				item.add(new EntiteitTypeImage("image", item.getModel()));
				item.add(new Label("participant", item.getModel()).add(new AttributeAppender(
					"class", new AbstractReadOnlyModel<String>()
					{
						private static final long serialVersionUID = 1L;

						@Override
						public String getObject()
						{
							AfspraakParticipant participant = item.getModelObject();
							String ret = "";

							switch (participant.getUitnodigingStatus())
							{
								case GEACCEPTEERD:
									return ret + "lblGeaccepteerd";
								case GEWEIGERD:
									return ret + "lblGeweigerd";
								case UITGENODIGD:
									return ret + "lblUitgenodigd";
								default:
									return ret;
							}
						}
					}, " ")));
			}
		});
		add(createGeenGegevensGevondenMarker());
	}

	private boolean heeftParticipanten(IModel<Afspraak> model)
	{
		if (model.getObject() == null)
			return true;

		return (model.getObject()).getParticipanten().isEmpty();
	}

	private WebMarkupContainer createGeenGegevensGevondenMarker()
	{
		WebMarkupContainer geenGegevensGevonden =
			new WebMarkupContainer("geenGegevensGevonden", getDefaultModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return heeftParticipanten(getAfspraakModel());
				}
			};
		geenGegevensGevonden.setRenderBodyOnly(true);
		Label geenGegevensMsg = new Label("geenGegevensMsg", "Er zijn geen participanten gevonden");
		geenGegevensMsg.add(new AttributeModifier("colspan", new Model<Integer>(2)));
		geenGegevensGevonden.add(geenGegevensMsg);
		return geenGegevensGevonden;
	}

	public IModel<Afspraak> getAfspraakModel()
	{
		return getModel();
	}
}
