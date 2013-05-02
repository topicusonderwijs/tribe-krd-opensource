package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.RedenUitschrijvingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter.SoortRedenUitschrijvingTonen;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class RedenUitschrijvingMultipleChoice extends
		UitgebreidZoekMultipleChoice<RedenUitschrijving>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<RedenUitschrijving>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<RedenUitschrijving> load()
		{
			RedenUitschrijvingZoekFilter filter = new RedenUitschrijvingZoekFilter();
			filter.addOrderByProperty("code");
			filter.setActief(true);
			filter.setSoort(SoortRedenUitschrijvingTonen.Verbintenis);

			return DataAccessRegistry.getHelper(RedenUitschrijvingDataAccessHelper.class).list(
				filter);
		}
	}

	public RedenUitschrijvingMultipleChoice(String id, IModel<Collection<RedenUitschrijving>> model)
	{
		super(id, model, new ListModel(), new ToStringRenderer());
	}

}
