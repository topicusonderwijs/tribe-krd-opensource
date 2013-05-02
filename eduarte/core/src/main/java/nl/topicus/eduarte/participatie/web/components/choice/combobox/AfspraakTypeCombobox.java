package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import java.util.EnumSet;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakTypeDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.participatie.zoekfilters.AfspraakTypeZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox voor afspraaktypes.
 * 
 * @author loite
 */
public class AfspraakTypeCombobox extends AbstractAjaxDropDownChoice<AfspraakType>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<AfspraakType>>
	{
		private static final long serialVersionUID = 1L;

		private AfspraakTypeZoekFilter zoekFilter;

		public ListModel(AfspraakTypeCategory categorie,
				OrganisatieEenheidLocatieAuthorizationContext authContext, Component component)
		{
			zoekFilter = new AfspraakTypeZoekFilter();
			if (authContext == null)
				zoekFilter
					.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
						component));
			if (categorie != null)
				zoekFilter.setCategories(EnumSet.of(categorie));
			if (authContext != null)
				zoekFilter.setAuthorizationContext(authContext);
		}

		public ListModel(AfspraakTypeZoekFilter zoekFilter)
		{
			this.zoekFilter = zoekFilter;
		}

		@Override
		protected List<AfspraakType> load()
		{
			return DataAccessRegistry.getHelper(AfspraakTypeDataAccessHelper.class)
				.list(zoekFilter);
		}

		@Override
		protected void onDetach()
		{
			zoekFilter.detach();
		}
	}

	public AfspraakTypeCombobox(String id, IModel<AfspraakType> model)
	{
		super(id, model, (IModel<List<AfspraakType>>) null);
		this.setChoices(new ListModel(null, null, this));
	}

	public AfspraakTypeCombobox(String id, IModel<AfspraakType> model,
			AfspraakTypeZoekFilter zoekFilter)
	{
		super(id, model, new ListModel(zoekFilter));
	}

	public AfspraakTypeCombobox(String id, IModel<AfspraakType> model,
			AfspraakTypeCategory categorie)
	{
		super(id, model, (IModel<List<AfspraakType>>) null, new ToStringRenderer());
		this.setChoices(new ListModel(categorie, null, this));
	}
}
