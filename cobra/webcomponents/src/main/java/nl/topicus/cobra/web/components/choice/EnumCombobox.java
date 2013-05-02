package nl.topicus.cobra.web.components.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.web.components.ResourcePostfixProvider;
import nl.topicus.cobra.web.components.choice.render.EnumRenderer;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IObjectClassAwareModel;

/**
 * @author loite
 * @param <T>
 */
public class EnumCombobox<T extends Enum<T>> extends AbstractAjaxDropDownChoice<T> implements
		ResourcePostfixProvider
{

	private static final long serialVersionUID = 1L;

	private String resourcePostfix;

	private static <T extends Enum<T>> List<T> findChoices(IObjectClassAwareModel<T> model)
	{
		Class<T> clazz = model.getObjectClass();
		if (clazz == null)
			throw new IllegalArgumentException("Model cannot determine object class");

		return new ArrayList<T>(EnumSet.allOf(clazz));
	}

	public EnumCombobox(String id, IObjectClassAwareModel<T> model)
	{
		super(id, model, findChoices(model), new EnumRenderer<T>(null), false);
		setResourcePostfix(model.getObjectClass().getSimpleName());
	}

	public EnumCombobox(String id, IObjectClassAwareModel<T> model, boolean addAjaxHandler)
	{
		super(id, model, findChoices(model), new EnumRenderer<T>(null), addAjaxHandler);
		setResourcePostfix(model.getObjectClass().getSimpleName());
	}

	public EnumCombobox(String id, IObjectClassAwareModel<T> model, Component... components)
	{
		super(id, model, findChoices(model), new EnumRenderer<T>(null), true);
		setResourcePostfix(model.getObjectClass().getSimpleName());
		connectListForAjaxRefresh(components);
	}

	public EnumCombobox(String id, T... choices)
	{
		super(id, Arrays.asList(choices), new EnumRenderer<T>(null), false);
	}

	public EnumCombobox(boolean submitOnEnter, String id, T... choices)
	{
		super(id, Arrays.asList(choices), new EnumRenderer<T>(null), false, submitOnEnter);
	}

	public EnumCombobox(String id, boolean addAjaxHandler, T... choices)
	{
		super(id, Arrays.asList(choices), new EnumRenderer<T>(null), addAjaxHandler);
	}

	public EnumCombobox(String id, IModel<T> model, T... choices)
	{
		super(id, model, Arrays.asList(choices), new EnumRenderer<T>(null), false);
		extractResourcePrefix(model);
	}

	private void extractResourcePrefix(IModel<T> model)
	{
		if (model instanceof IObjectClassAwareModel< ? >)
		{
			IObjectClassAwareModel<T> ocaModel = (IObjectClassAwareModel<T>) model;
			if (ocaModel.getObjectClass() != null)
				setResourcePostfix(((IObjectClassAwareModel<T>) model).getObjectClass()
					.getSimpleName());
		}
	}

	public EnumCombobox(String id, IModel<T> model, IModel<List<T>> choices)
	{
		super(id, model, choices, new EnumRenderer<T>(null), false);
		extractResourcePrefix(model);
	}

	public EnumCombobox(String id, IModel<T> model, List<T> choices)
	{
		super(id, model, choices, new EnumRenderer<T>(null), false);
		extractResourcePrefix(model);
	}

	public EnumCombobox(String id, IModel<T> model, boolean addAjaxHandler, T... choices)
	{
		super(id, model, Arrays.asList(choices), new EnumRenderer<T>(null), addAjaxHandler);
		extractResourcePrefix(model);
	}

	public EnumCombobox(String id, IModel<T> model, Map<T, String> names, T... choices)
	{
		super(id, model, Arrays.asList(choices), new EnumRenderer<T>(names), false);
		extractResourcePrefix(model);
	}

	public void setResourcePostfix(String resourcePostfix)
	{
		this.resourcePostfix = resourcePostfix;
	}

	public String getResourcePostfix()
	{
		return resourcePostfix;
	}
}
