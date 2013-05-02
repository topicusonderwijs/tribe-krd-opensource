package nl.topicus.cobra.web.components.modal;

import nl.topicus.cobra.web.components.shortcut.ShortcutEnabledComponent;
import nl.topicus.cobra.web.components.shortcut.ShortcutRegister;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.odlabs.wiquery.core.commons.CoreJavaScriptHeaderContributor;

public class ModalWindowContainer extends Panel implements ShortcutEnabledComponent
{
	private static final long serialVersionUID = 1L;

	private RepeatingView windowPanels;

	public ModalWindowContainer(String id, Page page)
	{
		super(id);
		setOutputMarkupId(true);
		add(windowPanels = new RepeatingView("windowPanels"));
		page.setMetaData(new ModalWindowContainerLocator(), this);
		add(new HeaderContributor(new CoreJavaScriptHeaderContributor()));
		add(JavascriptPackageResource.getHeaderContribution(ModalWindowContainer.class,
			"modalWindowContainer.js"));
	}

	public void addModalWindow(CobraModalWindow< ? > cobraModalWindow)
	{
		ModalWindow window = cobraModalWindow.createModalWindow(windowPanels.newChildId());
		windowPanels.add(window);
		IRequestTarget requestTarget = RequestCycle.get().getRequestTarget();
		if (requestTarget instanceof AjaxRequestTarget)
		{
			AjaxRequestTarget target = (AjaxRequestTarget) requestTarget;
			String params =
				"'" + getMarkupId() + "', '" + window.getMarkupId() + "', '"
					+ window.get(window.getContentId()).getMarkupId() + "'";
			target.appendJavascript("verifyModalWindow(" + params + ")");
		}
	}

	@Override
	public final void registerShortcuts(ShortcutRegister register)
	{
		// doe niets, shortcuts werken niet in modal windows
	}
}
