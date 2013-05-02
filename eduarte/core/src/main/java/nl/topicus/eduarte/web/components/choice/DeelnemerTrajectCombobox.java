package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.dbs.TrajectDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.zoekfilters.dbs.TrajectZoekFilter;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class DeelnemerTrajectCombobox extends AbstractAjaxDropDownChoice<Traject>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Traject>>
	{
		private static final long serialVersionUID = 1L;

		private TrajectZoekFilter zoekFilter;

		public ListModel(TrajectZoekFilter zoekFilter)
		{
			this.zoekFilter = zoekFilter;
		}

		@Override
		protected List<Traject> load()
		{
			return DataAccessRegistry.getHelper(TrajectDataAccessHelper.class).list(zoekFilter);
		}

		@Override
		protected void onDetach()
		{
			zoekFilter.detach();
		}
	}

	public DeelnemerTrajectCombobox(String id, IModel<Traject> model, TrajectZoekFilter zoekFilter)
	{
		super(id, model, new ListModel(zoekFilter), new DeelnemerTrajectRenderer());
	}

	private static class DeelnemerTrajectRenderer implements IChoiceRenderer<Traject>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(Traject object)
		{
			return object.getTitel();
		}

		@Override
		public String getIdValue(Traject object, int index)
		{
			return object.getId().toString();
		}

	}
}
