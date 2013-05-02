package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.DocumentTypeDataAccessHelper;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.zoekfilters.DocumentTypeZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class DocumentTypeMultipleChoice extends UitgebreidZoekMultipleChoice<DocumentType>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<DocumentType>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<DocumentType> load()
		{
			DocumentTypeZoekFilter filter = new DocumentTypeZoekFilter();
			filter.addOrderByProperty("naam");
			return DataAccessRegistry.getHelper(DocumentTypeDataAccessHelper.class).list(filter);
		}
	}

	public DocumentTypeMultipleChoice(String id, IModel<Collection<DocumentType>> model)
	{
		super(id, model, new ListModel(), new ToStringRenderer());
	}

}
