package nl.topicus.eduarte.krdparticipatie.web.components.combobox;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class LesweekIndelingCombobox extends DropDownChoice<LesweekIndeling>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<LesweekIndeling>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<LesweekIndeling> load()
		{
			return DataAccessRegistry.getHelper(LesweekIndelingDataAccessHelper.class).list();
		}
	}

	public LesweekIndelingCombobox(String id, IModel<LesweekIndeling> model)
	{
		super(id, model, new ListModel(), new ToStringRenderer());
	}

}
