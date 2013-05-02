package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortPraktijklokaal;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class SoortPraktijklokaalCombobox extends DropDownChoice<SoortPraktijklokaal>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<SoortPraktijklokaal>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<SoortPraktijklokaal> load()
		{
			return DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(
				SoortPraktijklokaal.class);
		}
	}

	public SoortPraktijklokaalCombobox(String id)
	{
		super(id, new ListModel(), new ChoiceRenderer<SoortPraktijklokaal>("naam", "id"));
	}

	public SoortPraktijklokaalCombobox(String id, IModel<SoortPraktijklokaal> model)
	{
		super(id, model, new ListModel(), new ChoiceRenderer<SoortPraktijklokaal>("naam", "id"));
	}
}
