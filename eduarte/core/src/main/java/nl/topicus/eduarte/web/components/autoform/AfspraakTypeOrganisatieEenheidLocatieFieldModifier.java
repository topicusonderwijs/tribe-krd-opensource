package nl.topicus.eduarte.web.components.autoform;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.entities.participatie.enums.OnderwijsproductGebruik;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.AfspraakTypeCombobox;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakTypeZoekFilter;
import nl.topicus.eduarte.web.components.quicksearch.onderwijsproduct.OnderwijsproductSearchEditor;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class AfspraakTypeOrganisatieEenheidLocatieFieldModifier extends
		OrganisatieEenheidLocatieFieldModifier

{
	private static final long serialVersionUID = 1L;

	private static final String AFSPRAAK_TYPE = "afspraakType";

	private static final String ONDERWIJSPRODUCT = "onderwijsproduct";

	private OnderwijsproductSearchEditor onderwijsproductEditor;

	private AfspraakTypeCombobox afspraakTypeCombobox;

	private AfspraakTypeZoekFilter afspraakTypeFilter;

	private OnderwijsproductZoekFilter onderwijsproductFilter;

	public AfspraakTypeOrganisatieEenheidLocatieFieldModifier(
			AfspraakTypeZoekFilter afspraakTypeFilter)
	{
		this.afspraakTypeFilter = afspraakTypeFilter;
		this.onderwijsproductFilter = new OnderwijsproductZoekFilter();
	}

	@Override
	public <T> void bind(AutoFieldSet<T> fieldset)
	{
		super.bind(fieldset);
		afspraakTypeFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(
			fieldset.getModel(), getOrganisatieEenheidPropertyName()));
		afspraakTypeFilter.setLocatieModel(new PropertyModel<Locatie>(fieldset.getModel(),
			getLocatiePropertyName()));
		onderwijsproductFilter.setOrganisatieEenheidModel(new PropertyModel<OrganisatieEenheid>(
			fieldset.getModel(), getOrganisatieEenheidPropertyName()));
		onderwijsproductFilter.setLocatieModel(new PropertyModel<Locatie>(fieldset.getModel(),
			getLocatiePropertyName()));
	}

	@Override
	public <T> boolean isApplicable(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			Action action)
	{
		if (Action.CREATION.equals(action)
			&& property != null
			&& (property.getPath().equals(AFSPRAAK_TYPE) || property.getPath().equals(
				ONDERWIJSPRODUCT)))
		{
			return true;
		}
		return super.isApplicable(fieldSet, property, action);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Component createField(final AutoFieldSet<T> fieldSet, String id, IModel< ? > model,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		if (property.getPath().equals(AFSPRAAK_TYPE))
		{
			afspraakTypeCombobox =
				new AfspraakTypeCombobox(id, (IModel<AfspraakType>) model, afspraakTypeFilter)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target, AfspraakType newSelection)
					{

						refreshComponents(fieldSet, target);
						AfspraakTypeOrganisatieEenheidLocatieFieldModifier.this.onUpdate(target);
					}
				};
			afspraakTypeCombobox.setNullValid(true);
			afspraakTypeCombobox.setAddSelectedItemToChoicesWhenNotInList(false);
			afspraakTypeFilter
				.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
					afspraakTypeCombobox));
			return afspraakTypeCombobox;
		}
		else if (property.getPath().equals(ONDERWIJSPRODUCT))
		{
			onderwijsproductEditor =
				new OnderwijsproductSearchEditor(id, (IModel<Onderwijsproduct>) model,
					onderwijsproductFilter)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isEnabled()
					{
						Afspraak afspraak = (Afspraak) fieldSet.getModelObject();
						return super.isEnabled()
							&& (afspraak.getAfspraakType() == null || !OnderwijsproductGebruik.ONGEBRUIKT
								.equals(afspraak.getAfspraakType().getOnderwijsproductGebruik()));
					}

					@Override
					public boolean isRequired()
					{
						Afspraak afspraak = (Afspraak) fieldSet.getModelObject();
						return super.isRequired()
							|| (afspraak.getAfspraakType() != null && OnderwijsproductGebruik.VERPLICHT
								.equals(afspraak.getAfspraakType().getOnderwijsproductGebruik()));
					}
				};
			return onderwijsproductEditor;
		}

		return super.createField(fieldSet, id, model, property, fieldProperties);
	}

	@Override
	protected IModel< ? > getFilterEntiteitModel(AutoFieldSet< ? > fieldSet)
	{
		return new PropertyModel<AfspraakType>(fieldSet.getModel(), AFSPRAAK_TYPE);
	}

	@Override
	public <T> void postProcess(final AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		if (afspraakTypeCombobox != null)
		{
			afspraakTypeCombobox.connectListForAjaxRefresh(afspraakTypeCombobox);
			if (onderwijsproductEditor != null)
				afspraakTypeCombobox.connectListForAjaxRefresh(onderwijsproductEditor);
			if (getOrganisatieEenheidCombobox() != null)
			{
				afspraakTypeCombobox.connectListForAjaxRefresh(getOrganisatieEenheidCombobox(),
					getLocatieCombobox());
				getOrganisatieEenheidCombobox().connectListForAjaxRefresh(afspraakTypeCombobox);
				getLocatieCombobox().connectListForAjaxRefresh(afspraakTypeCombobox);
			}
		}
		if (getOrganisatieEenheidCombobox() != null)
			super.postProcess(fieldSet, field, fieldProperties);
	}

	@Override
	public void detach()
	{
		super.detach();
		afspraakTypeFilter.detach();
	}
}