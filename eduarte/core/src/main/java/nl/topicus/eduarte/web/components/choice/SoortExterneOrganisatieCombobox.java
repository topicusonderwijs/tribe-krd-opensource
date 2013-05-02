package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.SoortExterneOrganisatie;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class SoortExterneOrganisatieCombobox extends DropDownChoice<SoortExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends
			LoadableDetachableModel<List<SoortExterneOrganisatie>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<SoortExterneOrganisatie> load()
		{
			return DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(
				SoortExterneOrganisatie.class);
		}
	}

	public SoortExterneOrganisatieCombobox(String id)
	{
		super(id, new ListModel(), new ChoiceRenderer<SoortExterneOrganisatie>("naam", "id"));
	}

	public SoortExterneOrganisatieCombobox(String id, IModel<SoortExterneOrganisatie> model)
	{
		super(id, model, new ListModel(), new ChoiceRenderer<SoortExterneOrganisatie>("naam", "id"));
	}
}
