package nl.topicus.cobra.web.components.wiquery.tristate;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

/**
 * @author papegaaij
 */
public class TriStateCheckBox extends FormComponentPanel<TriState> implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer tristate;

	private HiddenField<String> hidden;

	@SuppressWarnings("unused")
	private String hiddenValue;

	private boolean dualMode;

	public TriStateCheckBox(String id, boolean dualMode)
	{
		this(id, null, dualMode);
	}

	public TriStateCheckBox(String id, IModel<TriState> model, boolean dualMode)
	{
		super(id, model);

		this.dualMode = dualMode;

		tristate = new WebMarkupContainer("tristate");
		add(tristate);

		hidden =
			new HiddenField<String>("cbox", new PropertyModel<String>(this, "hiddenValue"),
				String.class);
		hidden.setOutputMarkupId(true);
		tristate.add(hidden);
	}

	@Override
	protected void onBeforeRender()
	{
		TriState state = getModelObject();
		if (state != null)
		{
			tristate.add(new AppendingAttributeModifier("class", state.toString().toLowerCase(),
				" "));
			if (TriState.Partial.equals(state))
				tristate.add(new AppendingAttributeModifier("class", "startpartial", " "));
		}
		if (dualMode)
			tristate.add(new AppendingAttributeModifier("class", "dualmode", " "));
		tristate.add(new AppendingAttributeModifier("class", "tristate", " "));
		if (!isEnabled())
			tristate.add(new AppendingAttributeModifier("class", "tristate_disabled", " "));
		hiddenValue = getDefaultModelObjectAsString();
		super.onBeforeRender();
	}

	protected WebMarkupContainer getTristateContainer()
	{
		return tristate;
	}

	public HiddenField<String> getValueField()
	{
		return hidden;
	}

	@Override
	protected void convertInput()
	{
		try
		{
			setConvertedInput(TriState.valueOf(hidden.getConvertedInput()));
		}
		catch (IllegalArgumentException e)
		{
			setConvertedInput(null);
		}
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(TriStateCheckBox.class, "TriStateCheckBox.js");
		resourceManager.addCssResource(TriStateCheckBox.class, "TriStateCheckBox.css");
	}

	@Override
	public JsStatement statement()
	{
		if (isEnabled())
			return new JsQuery(tristate).$().chain("click", "clickTriStateCheckBox");
		return new JsStatement();
	}

	@Override
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		tag.remove("name");
	}

	@Override
	protected void onDisabled(ComponentTag tag)
	{
	}
}
