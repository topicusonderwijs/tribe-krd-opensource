package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class GroepMultipleChoice extends UitgebreidZoekMultipleChoice<Groep>
{
	private static final long serialVersionUID = 1L;

	private final class ListModel extends LoadableDetachableModel<List<Groep>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Groep> load()
		{
			GroepZoekFilter filter = GroepZoekFilter.createDefaultFilter();
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				GroepMultipleChoice.this));
			return DataAccessRegistry.getHelper(GroepDataAccessHelper.class).list(filter);
		}
	}

	public GroepMultipleChoice(String id, IModel<Collection<Groep>> model)
	{
		super(id, model, null, new ToStringRenderer());
		setChoices(new ListModel());
	}
}
