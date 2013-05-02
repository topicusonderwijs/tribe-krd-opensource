package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.TypeLocatie;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class TypeLocatieCombobox extends DropDownChoice<TypeLocatie>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<TypeLocatie>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<TypeLocatie> load()
		{
			return DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(
				TypeLocatie.class);
		}
	}

	public TypeLocatieCombobox(String id)
	{
		super(id, new ListModel(), new ChoiceRenderer<TypeLocatie>("naam", "id"));
	}

	public TypeLocatieCombobox(String id, IModel<TypeLocatie> model)
	{
		super(id, model, new ListModel(), new ChoiceRenderer<TypeLocatie>("naam", "id"));
	}
}
