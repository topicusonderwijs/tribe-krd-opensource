package nl.topicus.eduarte.web.components.choice.dropdownchecklist;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.wiquery.DropDownCheckList;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.SoortExterneOrganisatie;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class SoortExterneOrganisatieDropDownCheckList extends
		DropDownCheckList<SoortExterneOrganisatie>
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

	public SoortExterneOrganisatieDropDownCheckList(String id,
			IModel<Collection<SoortExterneOrganisatie>> model)
	{
		super(id, model, new ListModel(), true);
	}
}
