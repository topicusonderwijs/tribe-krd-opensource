package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.web.components.panels.filter.ResultaatZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.markup.repeater.data.IDataProvider;

public class ResultatenmatrixPanel<M extends ResultatenModel> extends
		AbstractDeelnemerResultatenPanel<Resultaatstructuur, M>
{
	private static final long serialVersionUID = 1L;

	public ResultatenmatrixPanel(String id, ResultatenUIFactory<Resultaatstructuur, M> uiFactory,
			ToetsZoekFilter toetsFilter, Deelnemer deelnemer, Cohort cohort)
	{
		super(id, uiFactory, toetsFilter, deelnemer, cohort);
	}

	@Override
	protected AbstractResultatenTable<Resultaatstructuur, M> createTable(
			ResultaatColumnCreator<Resultaatstructuur, M> columnCreator)
	{
		return new ResultatenmatrixTable<M>(getResultatenModel(), deelnemerModel, columnCreator,
			getToetsFilter());
	}

	@Override
	protected AutoZoekFilterPanel<ToetsZoekFilter> createZoekFilterPanel(String id,
			EduArteDataPanel<Resultaatstructuur> datapanel)
	{
		return new ResultaatZoekFilterPanel(id, getToetsFilter(), datapanel, false, true, null);
	}

	@Override
	protected IDataProvider<Resultaatstructuur> createDataProvder()
	{
		return GeneralFilteredSortableDataProvider.of(getResultaatstructuurFilter(),
			ResultaatstructuurDataAccessHelper.class);
	}

	@Override
	protected void postProcessDatapanel(EduArteDataPanel<Resultaatstructuur> datapanel)
	{
		datapanel.addHeaderToolbar(new PogingHeaderToolbar<Resultaatstructuur>(datapanel));
	}

	@Override
	protected boolean isMultiToets()
	{
		return true;
	}
}
