package nl.topicus.eduarte.web.components.quicksearch.deelnemermedewerkergroep;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.IdObjectRenderer;
import nl.topicus.cobra.web.components.quicksearch.QuickSearchModel;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.eduarte.dao.participatie.helpers.DeelnemerMedewerkerGroepDataAccesHelper;
import nl.topicus.eduarte.entities.participatie.DeelnemerMedewerkerGroep;
import nl.topicus.eduarte.zoekfilters.DeelnemerMedewerkerGroepZoekFilter;

import org.apache.wicket.model.IModel;

public class DeelnemerMedewerkerGroepQuicksearchField extends
		QuickSearchField<DeelnemerMedewerkerGroep>
{
	private static final long serialVersionUID = 1L;

	private DeelnemerMedewerkerGroepZoekFilter filter;

	public DeelnemerMedewerkerGroepQuicksearchField(String id,
			IModel<DeelnemerMedewerkerGroep> model, DeelnemerMedewerkerGroepZoekFilter filter)
	{
		super(id, model, QuickSearchModel.of(DeelnemerMedewerkerGroepDataAccesHelper.class,
			filter), new IdObjectRenderer<DeelnemerMedewerkerGroep>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected DeelnemerMedewerkerGroep getObject(String className, String entityId)
			{
				// standaard werkt niet omdat id een string is
				return DataAccessRegistry.getHelper(DeelnemerMedewerkerGroepDataAccesHelper.class)
					.getByFullID(entityId);
			}
		});
		this.filter = filter;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}

	@Override
	public Integer getWidth()
	{
		return 300;
	}
}
