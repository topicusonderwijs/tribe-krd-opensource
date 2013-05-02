package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.participatie.helpers.BasisroosterDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.Basisrooster;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Dropdown waarin een basisrooster geselecteerd kan worden. Default zonder ajaxhandler.
 * 
 * @author loite
 */
public class BasisroosterCombobox extends AbstractAjaxDropDownChoice<Basisrooster>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Basisrooster>>
	{
		private static final long serialVersionUID = 1L;

		public ListModel()
		{
		}

		@Override
		protected List<Basisrooster> load()
		{
			return DataAccessRegistry.getHelper(BasisroosterDataAccessHelper.class).list();
		}
	}

	/**
	 * Constructor voor combobox zonder ajaxhandler en eigen model.
	 * 
	 * @param id
	 */
	public BasisroosterCombobox(String id, IModel<Basisrooster> model)
	{
		super(id, model, new ListModel(), new ToStringRenderer(), false);
	}

}
