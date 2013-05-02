package nl.topicus.cobra.web.components.modal;

import org.apache.wicket.markup.html.panel.Panel;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class CobraModalWindowBasePanel<T> extends Panel implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private CobraModalWindow<T> modalWindow;

	public CobraModalWindowBasePanel(String id, CobraModalWindow<T> modalWindow)
	{
		super(id);
		this.modalWindow = modalWindow;
	}

	public CobraModalWindow<T> getModalWindow()
	{
		return modalWindow;
	}

	public JsStatement onShowStatement()
	{
		JsQuery query = new JsQuery(this);
		JsStatement ret = query.$("[class]:input:visible:eq(0)").chain("focus");
		return ret;
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(CobraModalWindowBasePanel.class,
			"modalwindowpanel.js");
	}

	@Override
	public JsStatement statement()
	{
		return new JsQuery(this).$().chain("modalWindowPanel");
	}

	@Override
	public boolean isVisible()
	{
		return super.isVisible() && modalWindow.isShown();
	}
}
