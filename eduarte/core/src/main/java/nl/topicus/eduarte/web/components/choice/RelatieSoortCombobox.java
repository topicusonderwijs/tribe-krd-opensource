package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.RelatieSoortDataAccesHelper;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.RelatieSoortZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * 
 * 
 * @author vanharen
 */
public class RelatieSoortCombobox extends AbstractAjaxDropDownChoice<RelatieSoort>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<RelatieSoort>>
	{
		private static final long serialVersionUID = 1L;

		private RelatieSoortZoekFilter filter;

		public ListModel(RelatieSoortZoekFilter filter)
		{
			filter.setActief(true);
			this.filter = filter;
		}

		@Override
		protected List<RelatieSoort> load()
		{
			return DataAccessRegistry.getHelper(RelatieSoortDataAccesHelper.class).list(filter);
		}
	}

	public RelatieSoortCombobox(String id)
	{
		this(id, null);
	}

	public RelatieSoortCombobox(String id, IModel<RelatieSoort> model)
	{
		this(id, model, new RelatieSoortZoekFilter());
	}

	public RelatieSoortCombobox(String id, IModel<RelatieSoort> model, RelatieSoortZoekFilter filter)
	{
		super(id, model, new ListModel(filter), new EntiteitToStringRenderer());
	}
}
