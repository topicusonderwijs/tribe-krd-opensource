package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class ZorglijnCombobox extends AbstractAjaxDropDownChoice<Integer>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Integer>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Integer> load()
		{
			return DataAccessRegistry.getHelper(RolDataAccessHelper.class).getZorglijnen(
				EduArteContext.get().getAccount().getMaxZorglijn());
		}
	}

	public ZorglijnCombobox(String id, IModel<Integer> model)
	{
		super(id, model, new ListModel());
		setNullValid(true);
	}
}
