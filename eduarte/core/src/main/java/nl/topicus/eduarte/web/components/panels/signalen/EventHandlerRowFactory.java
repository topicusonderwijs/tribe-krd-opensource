package nl.topicus.eduarte.web.components.panels.signalen;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.cobra.web.components.datapanel.items.LinkItem;
import nl.topicus.eduarte.app.signalering.EventDescription;
import nl.topicus.eduarte.app.signalering.EventHandler;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.Signaal;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.hibernate.Hibernate;

public class EventHandlerRowFactory extends CustomDataPanelRowFactory<Signaal>
{
	private static final long serialVersionUID = 1L;

	public EventHandlerRowFactory()
	{
	}

	@Override
	public void bind(CustomDataPanel<Signaal> panel)
	{
		panel.addBehaviorToTable(new AppendingAttributeModifier("class", "tblClick", " "));
	}

	@Override
	public final WebMarkupContainer createRow(String id, CustomDataPanel<Signaal> panel,
			final Item<Signaal> item)
	{
		final EventDescription eventDescription = getEventDescription(getEvent(item));
		LinkItem<Signaal> ret = new LinkItem<Signaal>(id, item.getIndex(), item.getModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onLinkClicked()
			{
				Signaal signaal = getSignaal(item);
				signaal.setDatumGelezen(TimeUtil.getInstance().currentDate());
				signaal.commit();
				createHandler(eventDescription).handleEvent(getEvent(item), signaal);
			}
		};
		createHandler(eventDescription).postProcessLinkItem(ret, getEvent(item), getSignaal(item));
		return ret;
	}

	private EventDescription getEventDescription(Event event)
	{
		Class< ? > eventClass = Hibernate.getClass(event);
		return eventClass.getAnnotation(EventDescription.class);
	}

	private Signaal getSignaal(Item<Signaal> item)
	{
		return item.getModelObject();
	}

	private Event getEvent(Item<Signaal> item)
	{
		return (Event) getSignaal(item).getEvent().doUnproxy();
	}

	@SuppressWarnings("unchecked")
	private EventHandler<Event> createHandler(final EventDescription eventDescription)
	{
		return (EventHandler<Event>) ReflectionUtil.invokeConstructor(eventDescription.handler());
	}
}
