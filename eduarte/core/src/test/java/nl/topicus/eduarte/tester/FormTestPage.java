package nl.topicus.eduarte.tester;

import nl.topicus.cobra.web.components.modal.ModalWindowContainer;
import nl.topicus.cobra.web.pages.FeedbackComponent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.TestPanelSource;

/**
 * Test pagina die een form component bevat waar een form-panel gedropt kan worden zonder
 * dat er een specifieke pagina gebouwd hoeft te worden. Hiermee kan je dus
 * startFormPanel(new TestPanelSource() {}) gebruiken.
 */
public class FormTestPage extends WebPage implements FeedbackComponent
{
	private FeedbackPanel feedback;

	private boolean refreshFeedbackCalled = false;

	private Form<Void> form;

	private WebMarkupContainer panel;

	public FormTestPage(TestPanelSource panelFactory)
	{
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		setOutputMarkupId(true);
		add(feedback);

		form = new Form<Void>("form");
		add(form);
		panel = panelFactory.getTestPanel(DummyPanelPage.TEST_PANEL_ID);
		form.add(panel);

		add(new ModalWindowContainer("modalWindowContainer", this));
	}

	public FormTestPage(TestFormComponentPanelSource panelFactory)
	{
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		setOutputMarkupId(true);
		add(feedback);

		form = new Form<Void>("form");
		add(form);
		panel = panelFactory.getTestPanel(DummyPanelPage.TEST_PANEL_ID);
		form.add(panel);

		add(new ModalWindowContainer("modalWindowContainer", this));
	}

	public String getPanelComponentPath()
	{
		return panel.getPath();
	}

	@Override
	public void refreshFeedback(AjaxRequestTarget target)
	{
		refreshFeedbackCalled = true;
		target.addComponent(feedback);
	}

	public boolean isRefreshFeedbackCalled()
	{
		return refreshFeedbackCalled;
	}
}
