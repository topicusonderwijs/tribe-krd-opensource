package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.Aggregatieniveau;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author loite
 */
public class AggregatieniveauCombobox extends DropDownChoice<Aggregatieniveau>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Aggregatieniveau>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<Aggregatieniveau> load()
		{
			return DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(
				Aggregatieniveau.class, "niveau");
		}
	}

	public AggregatieniveauCombobox(String id)
	{
		super(id, new ListModel(), new ChoiceRenderer<Aggregatieniveau>("naam", "id"));
	}

	public AggregatieniveauCombobox(String id, IModel<Aggregatieniveau> model)
	{
		super(id, model, new ListModel(), new ChoiceRenderer<Aggregatieniveau>("naam", "id"));
	}
}
