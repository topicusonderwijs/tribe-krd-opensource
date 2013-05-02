package nl.topicus.eduarte.web.components.autoform;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.web.components.quicksearch.opleiding.OpleidingSearchEditor;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class OpleidingOrganisatieEenheidLocatieFieldModifier extends
		OrganisatieEenheidLocatieFieldModifier

{
	private static final long serialVersionUID = 1L;

	private String opleidingPropertyName = "opleiding";

	private OpleidingZoekFilter opleidingFilter;

	public OpleidingOrganisatieEenheidLocatieFieldModifier()
	{
		opleidingFilter = OpleidingZoekFilter.createDefaultFilter();
	}

	@Override
	public <T> void bind(AutoFieldSet<T> fieldset)
	{
		super.bind(fieldset);
		opleidingFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(fieldset
			.getModel(), getOrganisatieEenheidPropertyName()));
		opleidingFilter.setLocatieModel(new PropertyModel<Locatie>(fieldset.getModel(),
			getLocatiePropertyName()));
	}

	@Override
	public <T> boolean isApplicable(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			Action action)
	{
		if (Action.CREATION.equals(action) && property != null
			&& property.getPath().equals(getOpleidingPropertyName()))
		{
			return true;
		}
		if (Action.POST_PROCESS.equals(action) && property != null
			&& property.getPath().equals(getOpleidingPropertyName()))
		{
			return true;
		}
		return super.isApplicable(fieldSet, property, action);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Component createField(AutoFieldSet<T> fieldSet, String id, IModel< ? > model,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		if (property.getPath().equals(getOpleidingPropertyName()))
		{
			OpleidingSearchEditor field =
				new OpleidingSearchEditor(id, (IModel<Opleiding>) model, opleidingFilter);
			opleidingFilter
				.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(field));
			return field;
		}
		return super.createField(fieldSet, id, model, property, fieldProperties);
	}

	public String getOpleidingPropertyName()
	{
		return opleidingPropertyName;
	}

	public void setOpleidingPropertyName(String opleidingPropertyName)
	{
		this.opleidingPropertyName = opleidingPropertyName;
	}

	@Override
	protected IModel<Opleiding> getFilterEntiteitModel(AutoFieldSet< ? > fieldSet)
	{
		return new PropertyModel<Opleiding>(fieldSet.getModel(), getOpleidingPropertyName());
	}

	@Override
	public <T> void postProcess(final AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		if (field != null
			&& fieldProperties.getProperty().getPath().equals(getOpleidingPropertyName()))
		{
			((AbstractSearchEditor< ? >) field).addListener(new ISelectListener()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onUpdate(AjaxRequestTarget target)
				{
					target.addComponent(getOrganisatieEenheidCombobox());
					target.addComponent(getLocatieCombobox());
					refreshComponents(fieldSet, target);
					OpleidingOrganisatieEenheidLocatieFieldModifier.this.onUpdate(target);
				}
			});
		}
		else
			super.postProcess(fieldSet, field, fieldProperties);
	}

	@Override
	public void detach()
	{
		super.detach();
		opleidingFilter.detach();
	}
}