package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.ExterneOrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class ExterneOrganisatieMultipleChoice extends ListMultipleChoice<ExterneOrganisatie>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<ExterneOrganisatie>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<ExterneOrganisatie> load()
		{
			ExterneOrganisatieZoekFilter filter = new ExterneOrganisatieZoekFilter();
			filter.addOrderByProperty("naam");
			return DataAccessRegistry.getHelper(ExterneOrganisatieDataAccessHelper.class).list(
				filter);
		}
	}

	public ExterneOrganisatieMultipleChoice(String id)
	{
		this(id, null);
	}

	public ExterneOrganisatieMultipleChoice(String id, IModel<Collection<ExterneOrganisatie>> model)
	{
		super(id, model, new ListModel(), new IChoiceRenderer<ExterneOrganisatie>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(ExterneOrganisatie object)
			{
				return object.getNaam();
			}

			@Override
			public String getIdValue(ExterneOrganisatie object, int index)
			{
				return object.getId().toString();
			}

		});
	}
}
