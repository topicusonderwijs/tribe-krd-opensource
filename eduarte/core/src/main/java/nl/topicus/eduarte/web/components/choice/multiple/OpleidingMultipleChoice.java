package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class OpleidingMultipleChoice extends UitgebreidZoekMultipleChoice<Opleiding>
{
	private static final long serialVersionUID = 1L;

	private final class ListModel extends LoadableDetachableModel<List<Opleiding>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Opleiding> load()
		{
			OpleidingZoekFilter filter = OpleidingZoekFilter.createDefaultFilter();
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				OpleidingMultipleChoice.this));
			return DataAccessRegistry.getHelper(OpleidingDataAccessHelper.class).list(filter);
		}
	}

	public OpleidingMultipleChoice(String id, IModel<Collection<Opleiding>> model)
	{
		super(id, model, null, new ToStringRenderer());
		setChoices(new ListModel());
	}
}
