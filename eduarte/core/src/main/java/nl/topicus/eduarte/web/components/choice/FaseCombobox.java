package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.FaseDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.Fase;
import nl.topicus.eduarte.zoekfilters.FaseZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Locatiecombobox met security check.
 * 
 * @author loite
 */
public class FaseCombobox extends AbstractAjaxDropDownChoice<Fase>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Fase>>
	{
		private static final long serialVersionUID = 1L;

		public ListModel()
		{
		}

		@Override
		protected List<Fase> load()
		{
			FaseZoekFilter zoekFilter = new FaseZoekFilter();
			return DataAccessRegistry.getHelper(FaseDataAccessHelper.class).list(zoekFilter);
		}
	}

	public FaseCombobox(String id, IModel<Fase> model)
	{
		super(id, model, new ListModel());
	}

	@Override
	protected void onError(AjaxRequestTarget target, RuntimeException e)
	{
		super.onError(target, e);
		setModelObject(null);
	}

}
