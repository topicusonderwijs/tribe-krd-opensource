package nl.topicus.cobra.web.components.choice;

import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.ClientInfo;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.StateEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;

/**
 * Een behaviour welke een niet bestaande class op het opgegeven {@link Component} plaatst
 * samen met een {@link MouseEvent#MOUSEOVER} en een {@link MouseEvent#MOUSEOUT} welke een
 * bestaande css class toevoegen resp verwijderen.
 * 
 * Dit is bedoeld om voor Internet Explorer de opties onder de dropdown toonbaar te maken,
 * het nadeel is dat het hele component meegaat. Deze behavior werkt ook alleen wanneer de
 * gebruiker Internet Explorer gebruikt.
 * 
 * @author hoeve
 */
public class IEChoiceResizeBehavior extends AbstractBehavior
{
	private static final long serialVersionUID = 1L;

	private String resizeClass;

	public IEChoiceResizeBehavior(String resizeClass)
	{
		this.resizeClass = resizeClass;
	}

	@Override
	public void bind(final Component component)
	{
		ClientInfo info = RequestCycle.get().getClientInfo();
		if (!(info instanceof WebClientInfo))
			return;

		if (((WebClientInfo) info).getProperties().isBrowserInternetExplorer())
			addResizeBehaviors(component);
	}

	private void addResizeBehaviors(final Component component)
	{
		component.add(new WiQueryEventBehavior(new Event(MouseEvent.MOUSEOVER)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public JsScope callback()
			{
				return JsScope.quickScope("if(!$(this).hasClass('" + resizeClass
					+ "')) { $(this).addClass('" + resizeClass + "'); } $(this).focus(); ");
			}
		}));

		component.add(new WiQueryEventBehavior(new Event(StateEvent.BLUR)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public JsScope callback()
			{
				return JsScope.quickScope("if($(this).hasClass('" + resizeClass
					+ "')) { $(this).removeClass('" + resizeClass + "'); } ");
			}
		}));
	}
}
