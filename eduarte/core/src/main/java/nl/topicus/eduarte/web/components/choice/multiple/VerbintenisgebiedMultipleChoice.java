package nl.topicus.eduarte.web.components.choice.multiple;

import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.zoekfilters.TaxonomieElementZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class VerbintenisgebiedMultipleChoice extends
		UitgebreidZoekMultipleChoice<Verbintenisgebied>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Verbintenisgebied>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		@SuppressWarnings("unchecked")
		protected List<Verbintenisgebied> load()
		{
			List< ? extends TaxonomieElement> ret =
				DataAccessRegistry.getHelper(TaxonomieElementDataAccessHelper.class).list(
					new TaxonomieElementZoekFilter(Verbintenisgebied.class));
			return (List<Verbintenisgebied>) ret;
		}
	}

	public VerbintenisgebiedMultipleChoice(String id, IModel<Collection<Verbintenisgebied>> model)
	{
		super(id, model, new ListModel(), new ToStringRenderer());
	}

}
