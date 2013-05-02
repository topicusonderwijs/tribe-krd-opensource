package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurCategorieDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatstructuurCategorie;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurCategorieZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class ResultaatstructuurCategorieCombobox extends
		AbstractAjaxDropDownChoice<ResultaatstructuurCategorie>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends
			LoadableDetachableModel<List<ResultaatstructuurCategorie>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<ResultaatstructuurCategorie> load()
		{
			ResultaatstructuurCategorieZoekFilter filter =
				new ResultaatstructuurCategorieZoekFilter();
			filter.setActief(true);
			filter.addOrderByProperty("naam");
			return DataAccessRegistry.getHelper(ResultaatstructuurCategorieDataAccessHelper.class)
				.list(filter);
		}
	}

	public ResultaatstructuurCategorieCombobox(String id)
	{
		this(id, null);
	}

	public ResultaatstructuurCategorieCombobox(String id, IModel<ResultaatstructuurCategorie> model)
	{
		super(id, model, new ListModel());
	}
}
