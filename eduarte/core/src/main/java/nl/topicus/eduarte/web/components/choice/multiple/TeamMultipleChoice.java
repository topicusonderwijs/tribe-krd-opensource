package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Team;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class TeamMultipleChoice extends UitgebreidZoekMultipleChoice<Team>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Team>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<Team> load()
		{
			return DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(Team.class,
				"naam");
		}
	}

	public TeamMultipleChoice(String id, IModel<Collection<Team>> model)
	{
		super(id, model, new ListModel(), new ToStringRenderer());
	}

}
