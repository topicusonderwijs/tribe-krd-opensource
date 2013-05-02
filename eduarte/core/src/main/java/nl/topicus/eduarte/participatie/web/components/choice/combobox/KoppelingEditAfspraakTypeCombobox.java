package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import java.util.EnumSet;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakTypeDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.entities.participatie.ExterneAgendaKoppeling;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakTypeZoekFilter;
import nl.topicus.eduarte.web.components.choice.OrganisatieEenheidCombobox;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox voor afspraaktypes binnen het bewerken van een agendakoppeling.
 */
public class KoppelingEditAfspraakTypeCombobox extends AbstractAjaxDropDownChoice<AfspraakType>
{
	private static final long serialVersionUID = 1L;

	private static class ChoicesModel extends LoadableDetachableModel<List<AfspraakType>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<OrganisatieEenheid> organisatieEenheid;

		private AfspraakTypeZoekFilter filter;

		public ChoicesModel(OrganisatieEenheidLocatieAuthorizationContext authContext,
				Component component, OrganisatieEenheid organisatieEenheid)
		{
			setOrganisatieEenheid(organisatieEenheid);
			filter = new AfspraakTypeZoekFilter();
			EnumSet<AfspraakTypeCategory> cats = EnumSet.of(AfspraakTypeCategory.EXTERN);
			filter.setCategories(cats);
			filter.setActief(true);
			filter.setOrganisatieEenheid(organisatieEenheid);
			if (authContext == null)
				filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
					component));
			if (authContext != null)
				filter.setAuthorizationContext(authContext);
		}

		@Override
		protected List<AfspraakType> load()
		{
			return DataAccessRegistry.getHelper(AfspraakTypeDataAccessHelper.class).list(filter);
		}

		public void setOrganisatieEenheid(OrganisatieEenheid eenheid)
		{
			organisatieEenheid = ModelFactory.getModel(eenheid);
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			organisatieEenheid.detach();
		}
	}

	private AutoFieldSet<ExterneAgendaKoppeling> fieldSet;

	public KoppelingEditAfspraakTypeCombobox(String id, IModel<AfspraakType> model,
			AutoFieldSet<ExterneAgendaKoppeling> fieldSet)
	{
		this(id, fieldSet, model, (ChoicesModel) null);
		setChoices(new ChoicesModel(null, this, fieldSet.getModelObject().getOrganisatieEenheid()));
	}

	private KoppelingEditAfspraakTypeCombobox(String id,
			AutoFieldSet<ExterneAgendaKoppeling> fieldSet, IModel<AfspraakType> model,
			ChoicesModel choices)
	{
		super(id, model, choices);
		this.fieldSet = fieldSet;
	}

	@Override
	protected void onBeforeRender()
	{
		if (!hasBeenRendered())
			((OrganisatieEenheidCombobox) fieldSet.findFieldComponent("organisatieEenheid"))
				.addUpdateComponent(this);
		super.onBeforeRender();
	}

}
