package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.zoekfilters.RolZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox met alle rollen.
 * 
 * @author papegaaij
 */
public class RolCombobox extends AbstractAjaxDropDownChoice<Rol>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Rol>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Rol> load()
		{
			RolZoekFilter filter = new RolZoekFilter();
			filter.setAuthorisatieNiveau(AuthorisatieNiveau.REST);
			return DataAccessRegistry.getHelper(RolDataAccessHelper.class).list(filter);
		}
	}

	public RolCombobox(String id)
	{
		this(id, null);
	}

	public RolCombobox(String id, IModel<Rol> model)
	{
		super(id, model, new ListModel(), new EntiteitPropertyRenderer("naam"));
	}
}
