package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.LandDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.zoekfilters.LandelijkCodeNaamZoekFilter;

import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class LandMultipleChoice extends ListMultipleChoice<Land>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Land>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Land> load()
		{
			LandelijkCodeNaamZoekFilter<Land> filter = LandelijkCodeNaamZoekFilter.of(Land.class);
			filter.addOrderByProperty("code");
			filter.addOrderByProperty("naam");
			return DataAccessRegistry.getHelper(LandDataAccessHelper.class).list(filter);
		}
	}

	public LandMultipleChoice(String id)
	{
		this(id, null);
	}

	public LandMultipleChoice(String id, IModel<Collection<Land>> model)
	{
		super(id, model, new ListModel(), new ToStringRenderer());
	}

}
