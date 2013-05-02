package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.TypeToets;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class TypeToetsCombobox extends DropDownChoice<TypeToets>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<TypeToets>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<TypeToets> load()
		{
			return DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(
				TypeToets.class);
		}
	}

	public TypeToetsCombobox(String id)
	{
		super(id, new ListModel(), new ChoiceRenderer<TypeToets>("naam", "id"));
	}

	public TypeToetsCombobox(String id, IModel<TypeToets> model)
	{
		super(id, model, new ListModel(), new ChoiceRenderer<TypeToets>("naam", "id"));
	}
}
