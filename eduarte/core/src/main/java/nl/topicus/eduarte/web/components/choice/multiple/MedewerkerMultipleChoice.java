package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class MedewerkerMultipleChoice extends UitgebreidZoekMultipleChoice<Medewerker>
{
	private static final long serialVersionUID = 1L;

	private class ListModel extends LoadableDetachableModel<List<Medewerker>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Medewerker> load()
		{
			MedewerkerZoekFilter filter = new MedewerkerZoekFilter();
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				MedewerkerMultipleChoice.this));
			filter.addOrderByProperty("persoon.achternaam");
			return DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).list(filter);
		}
	}

	public MedewerkerMultipleChoice(String id, IModel<Collection<Medewerker>> model)
	{
		super(id, model, (IModel<List<Medewerker>>) null, new ToStringRenderer());
		setChoices(new ListModel());
	}

}
