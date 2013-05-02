package nl.topicus.cobra.web.components.form.modifier;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Deze modifier maakt het mogelijk om velden (conditioneel) te enabelen.
 * 
 * @author papegaaij
 */
public class EnableModifier extends MultiFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private IModel<Boolean> enabledModel;

	private EnableBehavior behavior;

	/**
	 * Maakt een nieuwe EnableModifier met waarde die bij elke render opnieuw uit het
	 * model gehaald wordt.
	 * 
	 * 
	 * @param propertyName
	 * @param enabledModel
	 */
	public EnableModifier(String propertyName, IModel<Boolean> enabledModel)
	{
		this(enabledModel, propertyName);
	}

	/**
	 * Maakt een nieuwe EnableModifier met een vaste waarde
	 * 
	 * @param enabled
	 * @param propertyNames
	 */
	public EnableModifier(boolean enabled, String... propertyNames)
	{
		this(new Model<Boolean>(enabled), propertyNames);
	}

	/**
	 * Maakt een nieuwe EnableModifier met waarde die bij elke render opnieuw uit het
	 * model gehaald wordt.
	 * 
	 * @param propertyNames
	 * @param enabledModel
	 */
	public EnableModifier(IModel<Boolean> enabledModel, String... propertyNames)
	{
		super(Action.POST_PROCESS, propertyNames);
		this.enabledModel = enabledModel;
	}

	protected static class EnableBehavior extends AbstractBehavior
	{
		private static final long serialVersionUID = 1L;

		private List<IModel<Boolean>> models = new ArrayList<IModel<Boolean>>();

		protected EnableBehavior(IModel<Boolean> firstModel)
		{
			models.add(firstModel);
		}

		private void addModel(IModel<Boolean> model)
		{
			models.add(model);
		}

		@Override
		public void beforeRender(Component component)
		{
			if (!Label.class.isAssignableFrom(component.getClass()))
				component.setEnabled(calcEnabled());
		}

		public boolean calcEnabled()
		{
			boolean enabled = true;
			for (IModel<Boolean> curModel : models)
				enabled &= curModel.getObject();
			return enabled;
		}
	}

	/**
	 * Hangt de modifier aan het veld.
	 * 
	 * @param fieldSet
	 * @param field
	 * @param fieldProperties
	 */
	@Override
	public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		for (Object curBehavior : field.getBehaviors())
		{
			if (curBehavior instanceof EnableBehavior)
			{
				behavior = (EnableBehavior) curBehavior;
				behavior.addModel(enabledModel);
				return;
			}
		}
		field.add(behavior = new EnableBehavior(enabledModel));
	}

	protected EnableBehavior getBehavior()
	{
		return behavior;
	}

	@Override
	public void detach()
	{
		enabledModel.detach();
		super.detach();
	}
}
