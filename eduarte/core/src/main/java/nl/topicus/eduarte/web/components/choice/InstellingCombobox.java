package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class InstellingCombobox extends DropDownChoice<Instelling>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Instelling>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Instelling> load()
		{
			return DataAccessRegistry.getHelper(OrganisatieDataAccessHelper.class)
				.getInstellingen();
		}

	}

	public InstellingCombobox(String id)
	{
		this(id, null);
	}

	public InstellingCombobox(String id, IModel<Instelling> model)
	{
		super(id, model, new ListModel(), new EntiteitToStringRenderer());
	}
}
