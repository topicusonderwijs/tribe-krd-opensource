package nl.topicus.eduarte.app.signalering.handler;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.items.LinkItem;
import nl.topicus.cobra.web.security.TargetBasedSecurityCheck;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.app.signalering.EventHandler;
import nl.topicus.eduarte.entities.signalering.Event;
import nl.topicus.eduarte.entities.signalering.Signaal;
import nl.topicus.eduarte.entities.signalering.events.IObjectGekoppeldEvent;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.security.actions.Enable;

public abstract class PageLinkEventHandler<T extends Event & IObjectGekoppeldEvent< ? >> implements
		EventHandler<T>
{
	private Class< ? extends Page> target;

	public PageLinkEventHandler(Class< ? extends Page> target)
	{
		this.target = target;
	}

	@Override
	public void handleEvent(T event, Signaal signaal)
	{
		EduArteRequestCycle.get().setResponsePage(
			ReflectionUtil.invokeConstructor(target, event.getObject()));
	}

	@Override
	public void postProcessLinkItem(LinkItem<Signaal> item, T event, Signaal signaal)
	{
		boolean linkEnabled;
		if (event.getObject() == null)
		{
			linkEnabled = false;
		}
		else
		{
			WebMarkupContainer tmp = new WebMarkupContainer("tmp");
			tmp.setDefaultModel(ModelFactory.getModel(event.getObject()));
			TargetBasedSecurityCheck check = new TargetBasedSecurityCheck(tmp, target);
			linkEnabled = check.isActionAuthorized(Enable.class);
		}
		item.setLinkEnabled(linkEnabled);
		if (!linkEnabled)
			item.add(new AppendingAttributeModifier("class", "inactive arrow"));
	}
}
