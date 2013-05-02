package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.IJkpuntDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.IJkpunt;
import nl.topicus.eduarte.zoekfilters.IJkpuntZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandenbrink
 */
public class IJkpuntCombobox extends AbstractAjaxDropDownChoice<IJkpunt>
{
	public IJkpuntCombobox(String id, IModel<IJkpunt> model)
	{
		super(id, model, new IJkpuntListModel(new IJkpuntZoekFilter()), new IJkpuntRenderer());
	}

	public IJkpuntCombobox(String id, IModel<IJkpunt> model, IJkpuntZoekFilter filter)
	{
		super(id, model, new IJkpuntListModel(filter), new IJkpuntRenderer());
	}

	private static final long serialVersionUID = 1L;

	private static final class IJkpuntListModel extends LoadableDetachableModel<List<IJkpunt>>
	{
		private static final long serialVersionUID = 1L;

		private IJkpuntZoekFilter filter;

		public IJkpuntListModel(IJkpuntZoekFilter filter)
		{
			this.filter = filter;
			this.filter.addOrderByProperty("datum");
		}

		@Override
		protected List<IJkpunt> load()
		{
			return DataAccessRegistry.getHelper(IJkpuntDataAccessHelper.class).list(filter);
		}
	}
}
