package nl.topicus.cobra.web.components.wiquery;

import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class SlidingFeedbackPanel extends FeedbackPanel implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	public SlidingFeedbackPanel(String id)
	{
		super(id);
	}

	public SlidingFeedbackPanel(String id, IFeedbackMessageFilter filter)
	{
		super(id, filter);
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		resourceManager
			.addJavaScriptResource(SlidingFeedbackPanel.class, "slidingfeedbackpanel.js");
	}

	@Override
	public JsStatement statement()
	{
		if (anyMessage())
		{
			return new JsQuery(this).$().chain("slidingFeedback");
		}
		else
			return new JsStatement();
	}
}
