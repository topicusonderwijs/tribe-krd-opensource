package nl.topicus.eduarte.tester;

import nl.topicus.cobra.web.pages.FeedbackComponent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class FeedbackAwareTestPage extends WebPage implements FeedbackComponent
{
	private FeedbackPanel feedback;

	public FeedbackAwareTestPage()
	{
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
	}

	@Override
	public void refreshFeedback(AjaxRequestTarget target)
	{
		target.addComponent(feedback);
	}
}
