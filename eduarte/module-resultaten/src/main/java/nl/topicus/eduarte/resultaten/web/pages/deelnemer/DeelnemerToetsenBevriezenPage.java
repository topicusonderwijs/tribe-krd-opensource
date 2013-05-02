package nl.topicus.eduarte.resultaten.web.pages.deelnemer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.DeelnemerToetsBevriezing;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.resultaten.principals.deelnemer.DeelnemerToetsenBevriezen;
import nl.topicus.eduarte.resultaten.web.pages.shared.AbstractToetsenBevriezenPage;
import nl.topicus.eduarte.web.components.panels.filter.ResultaatstructuurZoekFilterPanel;
import nl.topicus.eduarte.web.components.resultaat.ResultaatKey;
import nl.topicus.eduarte.web.pages.PageContext;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

@PageInfo(title = "Toetsen bevriezen", menu = "Deelnemer > Resultaten > Bevriezen")
@InPrincipal(DeelnemerToetsenBevriezen.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class,
	Docent.class})
public class DeelnemerToetsenBevriezenPage extends AbstractToetsenBevriezenPage
{
	private IModel<List<Deelnemer>> selectedDeelnemers;

	private ResultaatstructuurZoekFilter filter;

	private Map<Long, List<IChangeRecordingModel<DeelnemerToetsBevriezing>>> bevriezingen =
		new HashMap<Long, List<IChangeRecordingModel<DeelnemerToetsBevriezing>>>();

	public DeelnemerToetsenBevriezenPage(PageContext context, List<Deelnemer> selectedDeelnemers)
	{
		this(context, selectedDeelnemers, createResultaatstructuurFilter(selectedDeelnemers));
	}

	private static ResultaatstructuurZoekFilter createResultaatstructuurFilter(
			List<Deelnemer> deelnemers)
	{
		List<Resultaatstructuur> structuren =
			DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class).getStructuren(
				deelnemers);
		ResultaatstructuurZoekFilter ret = new ResultaatstructuurZoekFilter();
		ret.setDeelnemers(deelnemers);
		if (!structuren.isEmpty())
		{
			ret.setCohort(structuren.get(0).getCohort());
			ret.setOnderwijsproduct(structuren.get(0).getOnderwijsproduct());
		}
		return ret;
	}

	public DeelnemerToetsenBevriezenPage(PageContext context, List<Deelnemer> selectedDeelnemers,
			final ResultaatstructuurZoekFilter filter)
	{
		super(context, new LoadableDetachableModel<Resultaatstructuur>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Resultaatstructuur load()
			{
				List<Resultaatstructuur> structuren =
					DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class).list(
						filter);
				return structuren.size() == 1 ? structuren.get(0) : null;
			}
		});
		this.selectedDeelnemers = ModelFactory.getListModel(selectedDeelnemers);
		this.filter = filter;
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		add(new ResultaatstructuurZoekFilterPanel("filter", filter, null, false)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onZoek(IPageable pageable, AjaxRequestTarget target)
			{
				super.onZoek(pageable, target);
				updateResultaatstuctuur();
				target.addComponent(DeelnemerToetsenBevriezenPage.this);
			}
		});
		updateResultaatstuctuur();
		createComponents();
	}

	protected void updateResultaatstuctuur()
	{
		bevriezingen.clear();
		if (getResultaatstructuur() == null)
			return;

		ModelManager manager = new DefaultModelManager(DeelnemerToetsBevriezing.class);
		List<Deelnemer> deelnemers = selectedDeelnemers.getObject();
		List<Toets> toetsen = getResultaatstructuur().getToetsen();

		Map<ResultaatKey, DeelnemerToetsBevriezing> bevriezingenPerDeelnemerToets =
			new HashMap<ResultaatKey, DeelnemerToetsBevriezing>();
		for (DeelnemerToetsBevriezing curBevriezing : DataAccessRegistry.getHelper(
			ToetsDataAccessHelper.class).getBevriezingen(toetsen, deelnemers))
		{
			bevriezingenPerDeelnemerToets.put(new ResultaatKey(curBevriezing.getToets(),
				curBevriezing.getDeelnemer()), curBevriezing);
		}

		for (Toets curToets : toetsen)
		{
			List<IChangeRecordingModel<DeelnemerToetsBevriezing>> modelList =
				new ArrayList<IChangeRecordingModel<DeelnemerToetsBevriezing>>();
			bevriezingen.put(curToets.getId(), modelList);
			for (Deelnemer curDeelnemer : deelnemers)
			{
				DeelnemerToetsBevriezing curBevriezing =
					bevriezingenPerDeelnemerToets.get(new ResultaatKey(curToets, curDeelnemer));
				if (curBevriezing == null)
					curBevriezing = new DeelnemerToetsBevriezing(curDeelnemer, curToets);
				modelList.add(ModelFactory.getCompoundChangeRecordingModel(curBevriezing, manager));
			}
		}
	}

	@Override
	public List<DeelnemerToetsBevriezing> getBevriezingen(Toets toets)
	{
		List<DeelnemerToetsBevriezing> ret = new ArrayList<DeelnemerToetsBevriezing>();
		List<IChangeRecordingModel<DeelnemerToetsBevriezing>> retBevriezingen =
			bevriezingen.get(toets.getId());
		for (IChangeRecordingModel<DeelnemerToetsBevriezing> curModel : retBevriezingen)
		{
			ret.add(curModel.getObject());
		}
		return ret;
	}

	@Override
	public boolean getDisableBevrorenToetsen()
	{
		return true;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, null)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isFormSubmittingButton()
			{
				return false;
			}

			@Override
			protected void onSubmit()
			{
				for (List<IChangeRecordingModel<DeelnemerToetsBevriezing>> curModelList : bevriezingen
					.values())
					for (IChangeRecordingModel<DeelnemerToetsBevriezing> curModel : curModelList)
						curModel.saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(DeelnemerZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, DeelnemerZoekenPage.class));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		selectedDeelnemers.detach();
		filter.detach();
		ComponentUtil.detachQuietly(bevriezingen);
	}
}
