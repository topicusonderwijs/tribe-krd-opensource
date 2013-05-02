package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.NationaliteitDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class NationaliteitMultipleChoice extends ListMultipleChoice<Nationaliteit>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Nationaliteit>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Nationaliteit> load()
		{
			LandelijkCodeNaamZoekFilter<Nationaliteit> filter =
				LandelijkCodeNaamZoekFilter.of(Nationaliteit.class);
			filter.addOrderByProperty("code");
			filter.addOrderByProperty("naam");
			return DataAccessRegistry.getHelper(NationaliteitDataAccessHelper.class).list(filter);
		}
	}

	public NationaliteitMultipleChoice(String id)
	{
		this(id, null);
	}

	public NationaliteitMultipleChoice(String id, IModel<Collection<Nationaliteit>> model)
	{
		super(id, model, new ListModel(), new ToStringRenderer());
	}
}
