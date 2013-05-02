package nl.topicus.cobra.web.components.wiquery;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplate;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class Colorpicker extends TypedPanel<Color> implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private TextTemplate scriptTemplate;

	private HiddenField<String> selected;

	private WebMarkupContainer display;

	private WebMarkupContainer picker;

	public Colorpicker(String id, IModel<Color> colorModel)
	{
		super(id, colorModel);
		scriptTemplate = new PackagedTextTemplate(Colorpicker.class, "colorpicker.js");
		selected = new HiddenField<String>("selectedColor", new IModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				Color colorObj = getModelObject();
				if (colorObj == null)
					return "#ffffff";
				return String.format("#%1$02x%2$02x%3$02x", colorObj.getRed(), colorObj.getGreen(),
					colorObj.getBlue());
			}

			@Override
			public void setObject(String object)
			{
				String colorStr = object.toString();
				Colorpicker.this.setDefaultModelObject(new Color(Integer.parseInt(colorStr
					.substring(1), 16)));
			}

			@Override
			public void detach()
			{
			}
		});
		display = new WebMarkupContainer("colordisplay");
		picker = new WebMarkupContainer("colorpicker");
		selected.setOutputMarkupId(true);
		display.setOutputMarkupId(true);
		picker.setOutputMarkupId(true);
		add(selected);
		add(picker);
		picker.add(display);
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		ResourceRefUtil.addColorpicker(resourceManager);
	}

	@Override
	public JsStatement statement()
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("selectedid", selected.getMarkupId());
		params.put("displayid", display.getMarkupId());
		params.put("pickerid", picker.getMarkupId());
		params.put("color", "#123456");
		return new JsStatement().append(scriptTemplate.asString(params));
	}

}
