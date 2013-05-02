package nl.topicus.cobra.web.components.wiquery.resources;

import org.apache.wicket.ResourceReference;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.ui.mouse.MouseJavascriptResourceReference;
import org.odlabs.wiquery.ui.sortable.SortableJavaScriptResourceReference;
import org.odlabs.wiquery.ui.widget.WidgetJavascriptResourceReference;

public final class ResourceRefUtil
{
	private ResourceRefUtil()
	{
	}

	public static void addJHotKeys(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(ResourceRefUtil.class, "jquery.hotkeys.js");
		resourceManager.addJavaScriptResource(ResourceRefUtil.class,
			"jquery.hotkeys.execFunction.js");
	}

	public static void addClueTip(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(ResourceRefUtil.class, "cluetip/jquery.cluetip.js");
		resourceManager.addCssResource(ResourceRefUtil.class, "cluetip/jquery.cluetip.css");
	}

	public static void addColorpicker(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(ResourceRefUtil.class, "color/js/colorpicker.js");
		resourceManager.addCssResource(ResourceRefUtil.class, "color/css/colorpicker.css");
	}

	public static void addDropDownCheckList(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(ResourceRefUtil.class,
			"dropdownchecklist/ui.dropdownchecklist.js");
		resourceManager.addCssResource(ResourceRefUtil.class,
			"dropdownchecklist/ui.dropdownchecklist.css");
	}

	public static void addOrderSelect(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(WidgetJavascriptResourceReference.get());
		resourceManager.addJavaScriptResource(MouseJavascriptResourceReference.get());
		resourceManager.addJavaScriptResource(SortableJavaScriptResourceReference.get());

		resourceManager.addJavaScriptResource(ResourceRefUtil.class, "order/orderselect.js");
		resourceManager.addCssResource(ResourceRefUtil.class, "order/orderselect.css");
	}

	public static void addTimers(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(ResourceRefUtil.class, "jquery.timers-1.1.3.js");
	}

	public static void addYAIEF(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(ResourceRefUtil.class, "yaief/jquery.yaief.js");
		resourceManager.addCssResource(ResourceRefUtil.class, "yaief/yaief.css");
	}

	public static void addMaskMoney(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(ResourceRefUtil.class,
			"maskmoney/jquery.maskMoney.js");
		resourceManager.addCssResource(ResourceRefUtil.class, "maskmoney/maskmoney.css");
	}

	public static void addCarousel(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(ResourceRefUtil.class,
			"carousel/jquery.jcarousel.min.js");
		resourceManager.addCssResource(ResourceRefUtil.class, "carousel/skin.css");
	}

	public static ResourceReference getThemeResource()
	{
		return new ResourceReference(ResourceRefUtil.class, "theme/ui.all.css");
	}

	public static void addPopupFeedbackPanel(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(ResourceRefUtil.class,
			"popupfeedbackpanel/popupfeedbackpanel.js");
	}

	public static void addMediaQueries(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(ResourceRefUtil.class, "jquery.mediaqueries.js");
	}
}
