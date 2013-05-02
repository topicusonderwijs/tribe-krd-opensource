package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Leerstijl;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class LeerstijlCombobox extends DropDownChoice<Leerstijl>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Leerstijl>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<Leerstijl> load()
		{
			return DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(
				Leerstijl.class);
		}
	}

	public LeerstijlCombobox(String id)
	{
		super(id, new ListModel(), new ChoiceRenderer<Leerstijl>("naam", "id"));
	}

	public LeerstijlCombobox(String id, IModel<Leerstijl> model)
	{
		super(id, model, new ListModel(), new ChoiceRenderer<Leerstijl>("naam", "id"));
	}
}
