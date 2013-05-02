package nl.topicus.cobra.web.components.form.modifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;

public class AjaxFormSubmitRefreshModifier extends SingleFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private boolean refreshContainers;

	private List<String> refreshFields;

	private List<Component> refreshComponents;

	private Form< ? > form;

	public AjaxFormSubmitRefreshModifier(String propertyName, Form< ? > form,
			Serializable... refreshComponentsAndFields)
	{
		super(propertyName, Action.POST_PROCESS);
		this.form = form;
		refreshFields = new ArrayList<String>();
		refreshComponents = new ArrayList<Component>();
		for (Serializable curComponent : refreshComponentsAndFields)
		{
			if (curComponent instanceof String)
				refreshFields.add((String) curComponent);
			else if (curComponent instanceof Component)
				refreshComponents.add((Component) curComponent);
			else
				throw new IllegalArgumentException("Kan alleen strings en components refreshen");
		}
	}

	@Override
	public <T> boolean isApplicable(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			Action action)
	{
		if (Action.POST_PROCESS.equals(action) && property == null)
			return true;
		return super.isApplicable(fieldSet, property, action);
	}

	public boolean isRefreshContainers()
	{
		return refreshContainers;
	}

	public AjaxFormSubmitRefreshModifier setRefreshContainers(boolean refreshContainers)
	{
		this.refreshContainers = refreshContainers;
		return this;
	}

	@Override
	public <T> void postProcess(final AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		if (field == null)
		{
			if (refreshContainers)
			{
				for (String curField : refreshFields)
					fieldSet.findFieldComponent(curField).getParent().setOutputMarkupId(true);
			}
		}
		else
		{
			field.add(new AjaxFormSubmitBehavior(form, "onchange")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target)
				{
					AjaxFormSubmitRefreshModifier.this.onError(target);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target)
				{
					AjaxFormSubmitRefreshModifier.this.onSubmit(target);
					refreshComponents(fieldSet, target);
				}
			});
		}
	}

	@SuppressWarnings("unused")
	protected void onError(AjaxRequestTarget target)
	{

	}

	@SuppressWarnings("unused")
	protected void onSubmit(AjaxRequestTarget target)
	{

	}

	public void refreshComponents(final AutoFieldSet< ? > fieldSet, AjaxRequestTarget target)
	{
		for (String curField : refreshFields)
		{
			Component curComponent = fieldSet.findFieldComponent(curField);
			if (refreshContainers)
				curComponent = curComponent.getParent();
			target.addComponent(curComponent);
		}

		for (Component curComponent : refreshComponents)
			target.addComponent(curComponent);
	}

	public void addComponents(List<Component> components)
	{
		refreshComponents.addAll(components);
	}
}
