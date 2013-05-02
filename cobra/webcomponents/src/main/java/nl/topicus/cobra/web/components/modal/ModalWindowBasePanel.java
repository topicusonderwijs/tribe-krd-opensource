package nl.topicus.cobra.web.components.modal;

import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.wiquery.SlidingFeedbackPanel;
import nl.topicus.cobra.web.pages.FeedbackComponent;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.odlabs.wiquery.ui.commons.WiQueryUIPlugin;

/**
 * Basepanel voor modal windows
 * 
 * @author papegaaij
 */
@WiQueryUIPlugin
public abstract class ModalWindowBasePanel<T> extends CobraModalWindowBasePanel<T> implements
		FeedbackComponent
{
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer layLeft;

	public ModalWindowBasePanel(String id, CobraModalWindow<T> modalWindow)
	{
		super(id, modalWindow);
		add(layLeft = new WebMarkupContainer("layLeft")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isTransparentResolver()
			{
				return true;
			}
		});
		add(new SlidingFeedbackPanel("feedback").setOutputMarkupId(true));
	}

	public WebMarkupContainer getLayLeft()
	{
		return layLeft;
	}

	protected void createComponents()
	{
		BottomRowPanel bottomRow = new BottomRowPanel("bottom");
		add(bottomRow);
		fillBottomRow(bottomRow);
	}

	@SuppressWarnings("unused")
	protected void fillBottomRow(BottomRowPanel panel)
	{
	}

	@Override
	public void refreshFeedback(AjaxRequestTarget target)
	{
		target.addComponent(get("feedback"));
	}
}
