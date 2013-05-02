package nl.topicus.eduarte.web.components.autoform;

import java.io.Serializable;
import java.util.Date;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.modifier.FieldAdapter;
import nl.topicus.eduarte.web.components.text.SettableDatumTijdField;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public class BegindatumWordtEinddatumFieldModifier extends FieldAdapter
{
	private static final long serialVersionUID = 1L;

	private AutoFieldSet< ? > fieldset;

	private String beginDatumTijdPropertyName;

	private String eindDatumTijdPropertyName;

	private String prefix;

	private EduArteAjaxRefreshModifier refreshComponents;

	/**
	 * Zet de einddatum wanneer de begindatum wordt aangepast
	 */
	public BegindatumWordtEinddatumFieldModifier(String beginDatumTijdPropertyName,
			String eindDatumTijdPropertyName, String prefix)
	{
		setBeginDatumTijdPropertyName(beginDatumTijdPropertyName);
		setEindDatumTijdPropertyName(eindDatumTijdPropertyName);
		setPrefix(prefix);
	}

	public void setRefreshComponents(Serializable... refreshComponentsAndFields)
	{
		refreshComponents = new EduArteAjaxRefreshModifier("", refreshComponentsAndFields);
	}

	@Override
	@SuppressWarnings("hiding")
	public <T> void bind(AutoFieldSet<T> fieldset)
	{
		if (this.fieldset != null && this.fieldset != fieldset)
		{
			throw new IllegalStateException("Can only bind to one fieldset");
		}
		this.fieldset = fieldset;
	}

	@Override
	public <T> boolean isApplicable(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			Action action)
	{
		if (Action.CREATION.equals(action))
		{
			return property.getName().equals(getBeginDatumTijdPropertyName())
				|| property.getName().equals(getEindDatumTijdPropertyName());
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Component createField(AutoFieldSet<T> fieldSet, String id, IModel< ? > model,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		if (property.getName().equals(getBeginDatumTijdPropertyName()))

			return new SettableDatumTijdField(id, (IModel<Date>) model)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void dateChanged(AjaxRequestTarget target)
				{
					super.onUpdate(target);
					BegindatumWordtEinddatumFieldModifier.this.onUpdate(this.datum);
					refreshComponents(target);
				}
			};

		else
			return new SettableDatumTijdField(id, (IModel<Date>) model);
	}

	protected void refreshComponents(AjaxRequestTarget target)
	{
		if (refreshComponents != null)
			refreshComponents.refreshComponents(fieldset, target);
	}

	protected void onUpdate(Date datum)
	{
		getEindDatumTijdField().setDate(datum);
	}

	protected SettableDatumTijdField getBeginDatumTijdField()
	{
		return (SettableDatumTijdField) getFieldset().findFieldComponent(
			getPrefix() + getBeginDatumTijdPropertyName());
	}

	protected SettableDatumTijdField getEindDatumTijdField()
	{
		return (SettableDatumTijdField) getFieldset().findFieldComponent(
			getPrefix() + getEindDatumTijdPropertyName());
	}

	protected AutoFieldSet< ? > getFieldset()
	{
		return fieldset;
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(refreshComponents);
	}

	public String getBeginDatumTijdPropertyName()
	{
		return beginDatumTijdPropertyName;
	}

	public void setBeginDatumTijdPropertyName(String beginDatumTijdPropertyName)
	{
		this.beginDatumTijdPropertyName = beginDatumTijdPropertyName;
	}

	public String getEindDatumTijdPropertyName()
	{
		return eindDatumTijdPropertyName;
	}

	public void setEindDatumTijdPropertyName(String eindDatumTijdPropertyName)
	{
		this.eindDatumTijdPropertyName = eindDatumTijdPropertyName;
	}

	private void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	private String getPrefix()
	{
		return this.prefix;
	}

}
