package nl.topicus.cobra.web.components.wiquery;

/**
 * @author henzen
 */
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplate;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

/**
 * Panel met rich text editor
 * 
 * @author Henzen
 */
public class TextEditorPanel extends FormComponentPanel<String> implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	protected TextArea<String> area;

	private TextTemplate scriptTemplate;

	public TextEditorPanel(String id, IModel<String> model)
	{
		super(id, model);
		setOutputMarkupId(true);
		createTextAreaComponent();
		area.setOutputMarkupId(true);
		area.setType(String.class);
		add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return isRequired() ? "wysiwyg_required" : "";
			}
		}, " "));
		add(area);
	}

	private void createTextAreaComponent()
	{
		area = new TextArea<String>("textEditor", getModel())
		{
			private static final long serialVersionUID = 1L;

			// Zorgt dat er geen script tags worden opgeslagen zodat er geen javascript
			// kan worden geinjecteerd
			@Override
			@SuppressWarnings("unchecked")
			public final IConverter getConverter(Class type)
			{
				return new IConverter()
				{
					private static final long serialVersionUID = 1L;

					public Object convertToObject(String value, Locale locale)
					{
						return escJavasriptTags(value);
					}

					public String convertToString(Object value, Locale locale)
					{
						return (String) value;
					}

					@SuppressWarnings("all")
					private String escJavasriptTags(String value)
					{
						value =
							value.replaceAll("</[sS][cC][rR][iI][pP][tT]>",
								"  | <- !SCRIPTTAG close | ");
						value =
							value.replaceAll("<[sS][cC][rR][iI][pP][tT][^>]*>",
								" | !SCRIPTTAG open -> | ");
						return value;
					}
				};
			}

			@Override
			public boolean isEnabled()
			{
				return TextEditorPanel.this.isEnabled();
			}

			@Override
			public boolean isRequired()
			{
				return TextEditorPanel.this.isRequired();
			}

			@Override
			public IModel<String> getLabel()
			{
				return TextEditorPanel.this.getLabel();
			}
		};
	}

	/**
	 * @return javscript as String
	 */
	public String getJavascript()
	{
		scriptTemplate = new PackagedTextTemplate(TextEditorPanel.class, "TextEditorPanel.js");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("textAreaId", area.getMarkupId());
		params.put("textEditorContentId", this.getMarkupId());
		params.put("extraControls", getExtraControls());
		return scriptTemplate.asString(params);
	}

	/**
	 * Overschreven om bij een ajax request het javascript up te daten.
	 * 
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender()
	{
		if (RequestCycle.get().getRequestTarget() instanceof AjaxRequestTarget)
		{
			AjaxRequestTarget target = (AjaxRequestTarget) RequestCycle.get().getRequestTarget();
			target.appendJavascript("updateJQuery" + area.getMarkupId() + "();");
		}
		super.onBeforeRender();
	}

	protected String getExtraControls()
	{
		return "";
	}

	public String getAreaMarkupID()
	{
		return area.getMarkupId();
	}

	@Override
	protected void convertInput()
	{
		setConvertedInput(area.getConvertedInput());
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(TextEditorPanel.class, "jquery.wysiwyg.js");
		resourceManager.addCssResource(TextEditorPanel.class, "jquery.wysiwyg.css");
	}

	@Override
	public JsStatement statement()
	{
		return new JsQuery(area).$().chain("wysiwyg", getJavascript());
	}
}
