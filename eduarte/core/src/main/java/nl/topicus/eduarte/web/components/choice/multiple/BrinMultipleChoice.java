package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.BrinDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.zoekfilters.BrinZoekFilter;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class BrinMultipleChoice extends ListMultipleChoice<Brin>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Brin>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Brin> load()
		{
			BrinZoekFilter filter = new BrinZoekFilter();
			filter.addOrderByProperty("code");
			return DataAccessRegistry.getHelper(BrinDataAccessHelper.class).list(filter);
		}
	}

	public BrinMultipleChoice(String id)
	{
		this(id, null);
	}

	public BrinMultipleChoice(String id, IModel<Collection<Brin>> model)
	{
		super(id, model, new ListModel(), new IChoiceRenderer<Brin>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Brin object)
			{
				return object.getNaam();
			}

			@Override
			public String getIdValue(Brin object, int index)
			{
				return object.getId().toString();
			}

		});
	}

}
