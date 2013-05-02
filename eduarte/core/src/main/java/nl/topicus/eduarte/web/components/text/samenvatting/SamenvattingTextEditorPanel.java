package nl.topicus.eduarte.web.components.text.samenvatting;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.modelsv2.HibernateModel;
import nl.topicus.cobra.web.components.wiquery.TextEditorPanel;
import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSamenvattingTemplate;
import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSamenvattingZin;
import nl.topicus.eduarte.web.components.choice.GesprekSamenvattingTemplateCombobox;
import nl.topicus.eduarte.web.components.choice.GesprekSamenvattingZinCombobox;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplate;
import org.odlabs.wiquery.core.javascript.JsStatement;

/**
 * @author N. Henzen
 */
public class SamenvattingTextEditorPanel extends TextEditorPanel
{
	private static final long serialVersionUID = 1L;

	private TextTemplate scriptTemplate;

	private GesprekSamenvattingZinCombobox gesprekSamenvattingZin;

	private GesprekSamenvattingTemplateCombobox gesprekSamenvattingTemplate;

	/**
	 * @param id
	 * @param textModel
	 */

	public SamenvattingTextEditorPanel(String id, IModel<String> textModel)
	{
		super(id, textModel);

		setOutputMarkupId(true);
		gesprekSamenvattingZin =
			new GesprekSamenvattingZinCombobox("GesprekSamenvattingZin",
				new HibernateModel<GesprekSamenvattingZin>(null));
		gesprekSamenvattingZin.setOutputMarkupId(true);

		gesprekSamenvattingTemplate =
			new GesprekSamenvattingTemplateCombobox("GesprekSamenvattingTemplate",
				new HibernateModel<GesprekSamenvattingTemplate>(null));
		gesprekSamenvattingTemplate.setOutputMarkupId(true);

		AjaxLink<Void> addLink = new AjaxLink<Void>("toevoegen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				area.clearInput();
				GesprekSamenvattingTemplate template = gesprekSamenvattingTemplate.getModelObject();
				if (template != null)
					area.setModelObject(template.getTemplate());
				target.addComponent(SamenvattingTextEditorPanel.this);
			}
		};

		add(addLink);
		add(gesprekSamenvattingTemplate);
		add(gesprekSamenvattingZin);

	}

	/**
	 * @return javascript
	 */
	public String getJavascriptExtraControls()
	{
		scriptTemplate =
			new PackagedTextTemplate(SamenvattingTextEditorPanel.class, "ExtControls.js");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("comboboxid", gesprekSamenvattingZin.getMarkupId());
		return scriptTemplate.asString(params);
	}

	@Override
	protected String getExtraControls()
	{
		return getJavascriptExtraControls();
	}

	@Override
	public JsStatement statement()
	{
		return super.statement().append(
			";$('<li></li>').appendTo($('#" + getMarkupId() + " ul.panel')).append($('#"
				+ gesprekSamenvattingZin.getMarkupId() + "'))");
	}
}
