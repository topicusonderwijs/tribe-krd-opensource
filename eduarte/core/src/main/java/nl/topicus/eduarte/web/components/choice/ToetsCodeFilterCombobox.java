package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.ToetsCodeFilterDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilter;
import nl.topicus.eduarte.zoekfilters.ToetsCodeFilterZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class ToetsCodeFilterCombobox extends AbstractAjaxDropDownChoice<ToetsCodeFilter>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<ToetsCodeFilter>>
	{
		private static final long serialVersionUID = 1L;

		private ToetsCodeFilterZoekFilter filter;

		public ListModel(ToetsCodeFilterZoekFilter filter)
		{
			this.filter = filter;
		}

		@Override
		protected List<ToetsCodeFilter> load()
		{
			return DataAccessRegistry.getHelper(ToetsCodeFilterDataAccessHelper.class).list(filter);
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			filter.detach();
		}
	}

	public ToetsCodeFilterCombobox(String id, IModel<ToetsCodeFilter> model,
			ToetsCodeFilterZoekFilter filter)
	{
		super(id, model, new ListModel(filter), new ToStringRenderer());
	}
}
