package nl.topicus.cobra.web.components.form.modifier;

import nl.topicus.cobra.transformers.IdTransformer;
import nl.topicus.cobra.transformers.Transformer;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Modifier die de waarde van het bron veld transformeert en pusht naar een ander veld, en
 * beide velden opnieuw rendert.
 */
public class MultiFieldValueTransformModifier extends SingleFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private String event = "onblur";

	private String bronProperty;

	private String doelProperty;

	private Transformer< ? > transformer;

	private IModel<Boolean> copyValueEnabledModel;

	public MultiFieldValueTransformModifier(String bronProperty, String doelProperty)
	{
		this(bronProperty, doelProperty, new IdTransformer());
	}

	public MultiFieldValueTransformModifier(String bronProperty, String doelProperty,
			Transformer< ? > transformer)
	{
		this(bronProperty, doelProperty, transformer, new Model<Boolean>(true));
	}

	public MultiFieldValueTransformModifier(String bronProperty, String doelProperty,
			Transformer< ? > transformer, IModel<Boolean> copyValueEnabledModel)
	{
		super(bronProperty, Action.POST_PROCESS);
		this.bronProperty = bronProperty;
		this.doelProperty = doelProperty;
		this.transformer = transformer;
		this.copyValueEnabledModel = copyValueEnabledModel;
	}

	public void setEvent(String event)
	{
		this.event = event;
	}

	@Override
	public <T> void postProcess(final AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		field.setOutputMarkupId(true);

		field.add(new AjaxFormComponentUpdatingBehavior(event)
		{
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings("unchecked")
			protected void onUpdate(AjaxRequestTarget ajaxTarget)
			{
				Boolean enabled = copyValueEnabledModel.getObject();

				Component bron = fieldSet.findFieldComponent(bronProperty);
				Component doel = fieldSet.findFieldComponent(doelProperty);

				Object transformedValueBron =
					((Transformer<Object>) transformer).transform(bron.getDefaultModelObject());
				bron.setDefaultModelObject(transformedValueBron);

				if (enabled)
				{
					doel.setDefaultModelObject(transformedValueBron);
				}
				else
				{
					Object transformedValueDoel =
						((Transformer<Object>) transformer).transform(doel.getDefaultModelObject());
					doel.setDefaultModelObject(transformedValueDoel);
				}

				ajaxTarget.addComponent(doel);
				ajaxTarget.addComponent(bron);

			}
		});
	}

}
