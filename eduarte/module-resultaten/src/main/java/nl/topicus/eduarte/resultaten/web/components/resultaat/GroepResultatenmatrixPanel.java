package nl.topicus.eduarte.resultaten.web.components.resultaat;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.factory.InleverenLinkFactory;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.filter.AutoZoekFilterPanel;
import nl.topicus.eduarte.web.components.panels.filter.ResultaatZoekFilterPanel;
import nl.topicus.eduarte.web.components.resultaat.AbstractResultatenPanel;
import nl.topicus.eduarte.web.components.resultaat.AbstractResultatenTable;
import nl.topicus.eduarte.web.components.resultaat.PogingHeaderToolbar;
import nl.topicus.eduarte.web.components.resultaat.ResultaatColumnCreator;
import nl.topicus.eduarte.web.components.resultaat.ResultatenModel;
import nl.topicus.eduarte.web.components.resultaat.ResultatenUIFactory;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class GroepResultatenmatrixPanel<M extends ResultatenModel> extends
		AbstractResultatenPanel<Deelnemer, M>
{
	private static final long serialVersionUID = 1L;

	private IModel<List<Deelnemer>> deelnemersModel;

	private IModel<Groep> groepModel;

	public GroepResultatenmatrixPanel(String id, ResultatenUIFactory<Deelnemer, M> uiFactory,
			ToetsZoekFilter toetsFilter, Groep groep)
	{
		this(id, uiFactory, toetsFilter, groep.getDeelnemersOpPeildatumAsDeelnemer(), groep);
	}

	public GroepResultatenmatrixPanel(String id, ResultatenUIFactory<Deelnemer, M> uiFactory,
			ToetsZoekFilter toetsFilter, List<Deelnemer> deelnemers)
	{
		this(id, uiFactory, toetsFilter, deelnemers, null);
	}

	private GroepResultatenmatrixPanel(String id, ResultatenUIFactory<Deelnemer, M> uiFactory,
			ToetsZoekFilter toetsFilter, List<Deelnemer> deelnemers, Groep groep)
	{
		super(id, uiFactory, toetsFilter, false, groep);
		deelnemersModel = ModelFactory.getListModel(deelnemers);
		groepModel = ModelFactory.getModel(groep);
		getResultaatstructuurFilter().setDeelnemers(getDeelnemers());
		createComponents();
	}

	@Override
	protected IDataProvider<Deelnemer> createDataProvder()
	{
		return new ListModelDataProvider<Deelnemer>(new LoadableDetachableModel<List<Deelnemer>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Deelnemer> load()
			{
				return getDeelnemers();
			}
		});
	}

	@Override
	protected void postProcessDatapanel(EduArteDataPanel<Deelnemer> datapanel)
	{
		datapanel.addHeaderToolbar(new PogingHeaderToolbar<Deelnemer>(datapanel));
		if (!getResultatenModel().isEditable())
			EduArteApp.get().getFirstPanelFactory(InleverenLinkFactory.class,
				EduArteContext.get().getOrganisatie()).postProcessDatapanel(datapanel);
	}

	@Override
	protected AbstractResultatenTable<Deelnemer, M> createTable(
			ResultaatColumnCreator<Deelnemer, M> columnCreator)
	{
		IModel<List<Resultaatstructuur>> resultaatstructurenModel =
			new LoadableDetachableModel<List<Resultaatstructuur>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<Resultaatstructuur> load()
				{
					Onderwijsproduct product =
						getToetsFilter().getResultaatstructuurFilter().getOnderwijsproduct();
					Cohort cohort = getToetsFilter().getResultaatstructuurFilter().getCohort();
					if (product == null || cohort == null)
						return null;

					return DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class)
						.list(getToetsFilter().getResultaatstructuurFilter());
				}
			};

		return new GroepResultatenmatrixTable<M>(getResultatenModel(), resultaatstructurenModel,
			columnCreator, getToetsFilter());
	}

	@Override
	protected AutoZoekFilterPanel<ToetsZoekFilter> createZoekFilterPanel(String id,
			EduArteDataPanel<Deelnemer> datapanel)
	{
		return new ResultaatZoekFilterPanel(id, getToetsFilter(), datapanel, true, false,
			groepModel.getObject());
	}

	@Override
	public List<Toets> getToetsen()
	{
		return DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).list(getToetsFilter());
	}

	@Override
	public List<Deelnemer> getDeelnemers()
	{
		return deelnemersModel.getObject();
	}

	@Override
	protected boolean isMultiToets()
	{
		return true;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		deelnemersModel.detach();
		ComponentUtil.detachQuietly(groepModel);
	}
}
