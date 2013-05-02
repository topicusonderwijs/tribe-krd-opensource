package nl.topicus.cobra.web.components.wiquery.auto;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;

public class AddHiddenFieldBehavior extends AbstractBehavior
{
	private static final long serialVersionUID = 1L;

	private QuickSearchField<Object> component;

	private int renderCount = 0;

	private String previousFieldName;

	private String curFieldName;

	public AddHiddenFieldBehavior()
	{
	}

	@Override
	@SuppressWarnings( {"hiding", "unchecked"})
	public void bind(Component component)
	{
		if (this.component != null)
		{
			throw new IllegalStateException(
				"AddHiddenFieldBehavior can only be attached to one component");
		}
		this.component = (QuickSearchField<Object>) component;
	}

	@Override
	public void onRendered(Component c)
	{
		String curValue =
			previousFieldName == null ? null : c.getRequest().getParameter(previousFieldName);
		if (component.getModelObject() != null)
		{
			curValue = component.getRenderer().getIdValue(component.getModelObject());
		}

		String value = curValue == null ? "" : " value=\"" + curValue + "\"";
		component.getResponse().write(
			"<input type=\"hidden\" id=\"" + getValueFieldName() + "\" name=\""
				+ getValueFieldName() + "\"" + value + "/>");
	}

	public String getValueFieldName()
	{
		if (curFieldName == null)
		{
			renderCount++;
			curFieldName = component.getMarkupId() + "--selected-value-" + renderCount;
		}
		return curFieldName;
	}

	public String getPreviousFieldName()
	{
		return previousFieldName;
	}

	@Override
	public void detach(Component c)
	{
		super.detach(component);
		if (curFieldName != null)
			previousFieldName = curFieldName;
		curFieldName = null;
	}
}
