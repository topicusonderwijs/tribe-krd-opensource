package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieContactPersoonRolDataAccessHelper;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoonRol;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonRolZoekFilter;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox voor rollen van contactpersonen van externe organisaties.
 * 
 * @author hoeve
 */
public class ExterneOrganisatieContactPersoonRolCombobox extends
		DropDownChoice<ExterneOrganisatieContactPersoonRol>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends
			LoadableDetachableModel<List<ExterneOrganisatieContactPersoonRol>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<ExterneOrganisatieContactPersoonRol> load()
		{
			ExterneOrganisatieContactPersoonRolZoekFilter zoekFilter =
				new ExterneOrganisatieContactPersoonRolZoekFilter();
			zoekFilter.setActief(true);
			return DataAccessRegistry.getHelper(
				ExterneOrganisatieContactPersoonRolDataAccessHelper.class).list(zoekFilter);
		}

	}

	public ExterneOrganisatieContactPersoonRolCombobox(String id)
	{
		this(id, null);
	}

	public ExterneOrganisatieContactPersoonRolCombobox(String id,
			IModel<ExterneOrganisatieContactPersoonRol> model)
	{
		super(id, model, new ListModel(), new EntiteitToStringRenderer());
	}
}
