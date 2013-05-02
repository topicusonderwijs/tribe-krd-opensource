package nl.topicus.eduarte.web.components.panels.rechten;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.wiquery.tristate.TriState;
import nl.topicus.cobra.web.components.wiquery.tristate.TriStateCheckBox;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplate;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsStatement;

public abstract class RechtenTriStateCheckBox extends TriStateCheckBox
{
	private static final long serialVersionUID = 1L;

	private HiddenField<Integer> impliedCount;

	private RechtenSetRolModel model;

	public RechtenTriStateCheckBox(String id, RechtenSetRolModel model)
	{
		super(id, new PropertyModel<TriState>(model, "selection"), false);
		this.model = model;

		impliedCount =
			new HiddenField<Integer>("impliedCount", new PropertyModel<Integer>(model,
				"impliedCount"));
		impliedCount.setOutputMarkupId(true);
		getTristateContainer().add(impliedCount);
		getTristateContainer().setOutputMarkupId(true);
	}

	private CharSequence getJavascript()
	{
		TextTemplate scriptTemplate =
			new PackagedTextTemplate(RechtenTriStateCheckBox.class,
				"RechtenTriStateCheckBox-dyn.js");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("checkboxid", getCheckboxId());
		params.put("implies", getImpliedCheckboxes().toString());
		return scriptTemplate.asString(params);
	}

	protected abstract List<String> getImpliedCheckboxes();

	public String getCheckboxId()
	{
		return getTristateContainer().getMarkupId();
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		resourceManager.addJavaScriptResource(new JavascriptResourceReference(
			RechtenTriStateCheckBox.class, "RechtenTriStateCheckBox.js"));
		resourceManager.addCssResource(new ResourceReference(TriStateCheckBox.class,
			"TriStateCheckBox.css"));
	}

	@Override
	public JsStatement statement()
	{
		if (isEnabled())
			return new JsStatement().append(getJavascript());
		return new JsStatement();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(model);
	}
}
