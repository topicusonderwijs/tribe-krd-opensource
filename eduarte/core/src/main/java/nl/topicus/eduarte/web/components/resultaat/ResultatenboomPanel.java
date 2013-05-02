package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.factory.InleverenLinkFactory;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.web.components.panels.filter.ResultaatZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.markup.repeater.data.IDataProvider;

public class ResultatenboomPanel<M extends ResultatenModel> extends
		AbstractDeelnemerResultatenPanel<Toets, M>
{
	private static final long serialVersionUID = 1L;

	private CollapsableRowFactoryDecorator<Toets> rowFactory;

	public ResultatenboomPanel(String id, ResultatenUIFactory<Toets, M> uiFactory,
			ToetsZoekFilter toetsFilter, Deelnemer deelnemer, Cohort cohort)
	{
		super(id, uiFactory, toetsFilter, deelnemer, cohort);
		getToetsFilter().addOrderByProperty("boom");
		getToetsFilter().setAscending(true);
	}

	@Override
	protected AbstractResultatenTable<Toets, M> createTable(
			ResultaatColumnCreator<Toets, M> columnCreator)
	{
		rowFactory =
			new CollapsableRowFactoryDecorator<Toets>(
				new ResultatenboomRowFactory(getToetsFilter()));
		ResultatenboomTable<M> ret =
			new ResultatenboomTable<M>(getResultatenModel(), deelnemerModel, rowFactory,
				columnCreator);
		if (!getResultatenModel().isEditable())
			EduArteApp
				.get()
				.getFirstPanelFactory(InleverenLinkFactory.class,
					EduArteContext.get().getOrganisatie())
				.addInleverenColumn(ret, getResultatenModel().getDeelnemers());
		return ret;
	}

	@Override
	protected AutoZoekFilterPanel<ToetsZoekFilter> createZoekFilterPanel(String id,
			EduArteDataPanel<Toets> datapanel)
	{
		return new ResultaatZoekFilterPanel(id, getToetsFilter(), datapanel, true, true, null);
	}

	@Override
	protected IDataProvider<Toets> createDataProvder()
	{
		return GeneralFilteredSortableDataProvider.of(getToetsFilter(),
			ToetsDataAccessHelper.class);
	}

	@Override
	protected void postProcessDatapanel(EduArteDataPanel<Toets> datapanel)
	{
		datapanel.setRowFactory(rowFactory);
	}

	@Override
	protected boolean isMultiToets()
	{
		return false;
	}
}
