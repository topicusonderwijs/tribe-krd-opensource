package nl.topicus.cobra.web.components.form.modifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.CheckBox;

public class AjaxRefreshModifier extends SingleFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private boolean refreshContainers;

	private List<String> refreshFields;

	private List<Component> refreshComponents;

	private Map<AutoFieldSet< ? >, List<AjaxRefreshModifier>> conntectedModifiers =
		new LinkedHashMap<AutoFieldSet< ? >, List<AjaxRefreshModifier>>();

	public AjaxRefreshModifier(String propertyName, Serializable... refreshComponentsAndFields)
	{
		super(propertyName, Action.POST_PROCESS);
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

	public AjaxRefreshModifier setRefreshContainers(boolean refreshContainers)
	{
		this.refreshContainers = refreshContainers;
		return this;
	}

	public AjaxRefreshModifier connectRefreshModifier(AutoFieldSet< ? > fieldset,
			AjaxRefreshModifier modifier)
	{
		List<AjaxRefreshModifier> connected = conntectedModifiers.get(fieldset);
		if (connected == null)
		{
			connected = new ArrayList<AjaxRefreshModifier>();
			conntectedModifiers.put(fieldset, connected);
		}
		connected.add(modifier);
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
			String event = "onchange";
			if (field instanceof CheckBox)
				event = "onclick"; // IE

			field.add(new AjaxFormComponentUpdatingBehavior(event)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					performOnUpdate(fieldSet, target);
				}

				@Override
				protected void onError(AjaxRequestTarget target, RuntimeException e)
				{
					performOnError(target);
				}
			});
		}
	}

	protected final <T> void performOnUpdate(final AutoFieldSet<T> fieldSet,
			AjaxRequestTarget target)
	{
		AjaxRefreshModifier.this.onUpdate(target);
		refreshComponents(fieldSet, target);
	}

	protected final void performOnError(AjaxRequestTarget target)
	{
		AjaxRefreshModifier.this.onError(target);
		for (Map.Entry<AutoFieldSet< ? >, List<AjaxRefreshModifier>> curConnected : conntectedModifiers
			.entrySet())
			for (AjaxRefreshModifier curModifier : curConnected.getValue())
				curModifier.onError(target);
	}

	@SuppressWarnings("unused")
	protected void onUpdate(AjaxRequestTarget target)
	{
	}

	@SuppressWarnings("unused")
	protected void onError(AjaxRequestTarget target)
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

		for (Map.Entry<AutoFieldSet< ? >, List<AjaxRefreshModifier>> curConnected : conntectedModifiers
			.entrySet())
			for (AjaxRefreshModifier curModifier : curConnected.getValue())
			{
				curModifier.onUpdate(target);
				curModifier.refreshComponents(curConnected.getKey(), target);
			}
	}

	public void addFields(List<String> fields)
	{
		refreshFields.addAll(fields);
	}

	public void addComponents(List<Component> components)
	{
		refreshComponents.addAll(components);
	}
}
