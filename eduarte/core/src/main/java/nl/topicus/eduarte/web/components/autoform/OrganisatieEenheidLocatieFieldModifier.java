package nl.topicus.eduarte.web.components.autoform;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.LocatieProvider;
import nl.topicus.eduarte.providers.OrganisatieEenheidProvider;
import nl.topicus.eduarte.web.components.choice.LocatieCombobox;
import nl.topicus.eduarte.web.components.choice.OrganisatieEenheidCombobox;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public class OrganisatieEenheidLocatieFieldModifier extends EduArteAjaxRefreshModifier implements
		OrganisatieEenheidProvider, LocatieProvider
{
	private static final long serialVersionUID = 1L;

	private String organisatieEenheidPropertyName = "organisatieEenheid";

	private String locatiePropertyName = "locatie";

	private OrganisatieEenheidCombobox organisatieEenheidCombobox;

	private LocatieCombobox locatieCombobox;

	public OrganisatieEenheidLocatieFieldModifier()
	{
		super(null);
	}

	public String getOrganisatieEenheidPropertyName()
	{
		return organisatieEenheidPropertyName;
	}

	public void setOrganisatieEenheidPropertyName(String organisatieEenheidPropertyName)
	{
		this.organisatieEenheidPropertyName = organisatieEenheidPropertyName;
	}

	public String getLocatiePropertyName()
	{
		return locatiePropertyName;
	}

	public void setLocatiePropertyName(String locatiePropertyName)
	{
		this.locatiePropertyName = locatiePropertyName;
	}

	@Override
	public <T> boolean isApplicable(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			Action action)
	{
		if (Action.CREATION.equals(action))
		{
			return property.getPath().equals(getOrganisatieEenheidPropertyName())
				|| property.getPath().equals(getLocatiePropertyName());
		}
		if (Action.POST_PROCESS.equals(action) && property == null)
		{
			return true;
		}
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Component createField(final AutoFieldSet<T> fieldSet, String id, IModel< ? > model,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		AbstractAjaxDropDownChoice< ? > ret;
		if (property.getPath().equals(getOrganisatieEenheidPropertyName()))
		{
			organisatieEenheidCombobox =
				new OrganisatieEenheidCombobox(id, (IModel<OrganisatieEenheid>) model, this,
					getFilterEntiteitModel(fieldSet))
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target,
							OrganisatieEenheid newSelection)
					{
						super.onUpdate(target, newSelection);
						refreshComponents(fieldSet, target);
						OrganisatieEenheidLocatieFieldModifier.this.onUpdate(target);
					}
				};
			ret = organisatieEenheidCombobox;
		}
		else
		{
			locatieCombobox =
				new LocatieCombobox(id, (IModel<Locatie>) model, this,
					getFilterEntiteitModel(fieldSet))
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target, Locatie newSelection)
					{
						super.onUpdate(target, newSelection);
						refreshComponents(fieldSet, target);
						OrganisatieEenheidLocatieFieldModifier.this.onUpdate(target);
					}
				};
			ret = locatieCombobox;
		}
		ret.setAddSelectedItemToChoicesWhenNotInList(false);
		ret.setNullValid(true);
		return ret;
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target)
	{
	}

	@Override
	public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		LocatieCombobox locCombobox = getLocatieCombobox();
		OrganisatieEenheidCombobox orgEhdCombobox = getOrganisatieEenheidCombobox();
		locCombobox.connectListForAjaxRefresh(orgEhdCombobox);
		orgEhdCombobox.connectListForAjaxRefresh(locCombobox);
	}

	@SuppressWarnings("unused")
	protected IModel< ? > getFilterEntiteitModel(AutoFieldSet< ? > fieldSet)
	{
		return null;
	}

	@Override
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return getOrganisatieEenheidCombobox().getOrganisatieEenheid();
	}

	@Override
	public Locatie getLocatie()
	{
		return getLocatieCombobox().getLocatie();
	}

	protected OrganisatieEenheidCombobox getOrganisatieEenheidCombobox()
	{
		return organisatieEenheidCombobox;
	}

	protected LocatieCombobox getLocatieCombobox()
	{
		return locatieCombobox;
	}
}
