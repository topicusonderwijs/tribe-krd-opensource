package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class DocumentCategorieMultipleChoice extends
		UitgebreidZoekMultipleChoice<DocumentCategorie>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<DocumentCategorie>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<DocumentCategorie> load()
		{
			CodeNaamActiefZoekFilter<DocumentCategorie> filter =
				new CodeNaamActiefZoekFilter<DocumentCategorie>(DocumentCategorie.class);
			filter.addOrderByProperty("naam");
			return DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(filter);

		}
	}

	public DocumentCategorieMultipleChoice(String id, IModel<Collection<DocumentCategorie>> model)
	{
		super(id, model, new ListModel(), new ToStringRenderer());
	}
}
