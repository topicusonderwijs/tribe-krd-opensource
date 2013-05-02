package nl.topicus.cobra.web.components.wiquery.popupfeedbackpanel;

import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;

import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class PopupFeedbackPanel extends FeedbackPanel implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	public PopupFeedbackPanel(String id)
	{
		super(id);
	}

	public PopupFeedbackPanel(String id, IFeedbackMessageFilter filter)
	{
		super(id, filter);
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		ResourceRefUtil.addPopupFeedbackPanel(resourceManager);
	}

	@Override
	public JsStatement statement()
	{
		if (anyMessage())
		{
			return new JsQuery(this).$().chain("popupFeedback");
		}
		else
			return new JsStatement();
	}
}
