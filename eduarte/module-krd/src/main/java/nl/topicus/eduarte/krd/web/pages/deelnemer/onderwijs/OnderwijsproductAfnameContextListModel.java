package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameContextZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class OnderwijsproductAfnameContextListModel extends
		LoadableDetachableModel<List<OnderwijsproductAfnameContext>>
{
	private static final long serialVersionUID = 1L;

	private List<IChangeRecordingModel<OnderwijsproductAfnameContext>> contextListModel;

	private ModelManager manager = getManager();

	private IModel<Verbintenis> verbintenis;

	public OnderwijsproductAfnameContextListModel(IModel<List<Productregel>> productregelsModel,
			Verbintenis verbintenis)
	{
		setVerbintenis(verbintenis);
		contextListModel = new ArrayList<IChangeRecordingModel<OnderwijsproductAfnameContext>>();
		List<Productregel> regels = productregelsModel.getObject();
		for (Productregel regel : regels)
		{
			OnderwijsproductAfnameContextZoekFilter filter =
				new OnderwijsproductAfnameContextZoekFilter();
			filter.setProductregel(regel);
			filter.setVerbintenis(verbintenis);
			OnderwijsproductAfnameContextDataAccessHelper helper =
				DataAccessRegistry.getHelper(OnderwijsproductAfnameContextDataAccessHelper.class);
			List<OnderwijsproductAfnameContext> contextList = helper.list(filter);
			if (contextList.size() > 0)
				contextListModel.add(ModelFactory.getCompoundChangeRecordingModel(contextList
					.get(0), manager));
			else
			{
				contextListModel.add(ModelFactory.getCompoundChangeRecordingModel(
					createEmptyContext(verbintenis, regel), manager));
			}
		}
	}

	public OnderwijsproductAfnameContext createEmptyContext(Verbintenis verb, Productregel regel)
	{
		OnderwijsproductAfnameContext context = new OnderwijsproductAfnameContext();
		context.setVerbintenis(verb);
		context.setProductregel(regel);
		OnderwijsproductAfname afname = new OnderwijsproductAfname();
		afname.setBegindatum(verb.getBegindatum());
		afname.setDeelnemer(verb.getDeelnemer());
		afname.setCohort(regel.getCohort());
		context.setOnderwijsproductAfname(afname);
		afname.getAfnameContexten().add(context);

		return context;
	}

	@Override
	protected List<OnderwijsproductAfnameContext> load()
	{
		List<OnderwijsproductAfnameContext> contextList =
			new ArrayList<OnderwijsproductAfnameContext>();
		for (IChangeRecordingModel<OnderwijsproductAfnameContext> model : contextListModel)
		{
			contextList.add(model.getObject());
		}
		return contextList;
	}

	public IChangeRecordingModel<OnderwijsproductAfnameContext> get(
			OnderwijsproductAfnameContext context)
	{
		for (IChangeRecordingModel<OnderwijsproductAfnameContext> model : contextListModel)
		{
			OnderwijsproductAfnameContext curContext = model.getObject();
			if (curContext.equals(context))
			{
				return model;
			}
		}
		throw new IllegalArgumentException("context niet gevonden: " + context);
	}

	public IModel<OnderwijsproductAfnameContext> getContextWithRegel(Productregel regel)
	{
		for (IModel<OnderwijsproductAfnameContext> model : contextListModel)
		{
			OnderwijsproductAfnameContext curContext = model.getObject();
			if (curContext.getProductregel().equals(regel))
			{
				return model;
			}
		}
		throw new IllegalArgumentException("Geen context gevonden voor: " + regel);
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(verbintenis);
		for (IChangeRecordingModel<OnderwijsproductAfnameContext> model : contextListModel)
		{
			ComponentUtil.detachQuietly(model);
		}
		super.onDetach();
	}

	/**
	 * Slaat de wijzigingen op. Er wordt geen commit uitgevoerd.
	 * 
	 * @param begindatum
	 *            als de begindatum null is wordt er niets veranderd, anders wordt de
	 *            meegeven begindatum ingevuld als begindatum van de
	 *            onderwijsproductafname
	 */
	public void batchSaveModel(Date begindatum)
	{
		for (IChangeRecordingModel<OnderwijsproductAfnameContext> model : contextListModel)
		{
			OnderwijsproductAfnameContext context = model.getObject();
			if (context.getOnderwijsproductAfname().getOnderwijsproduct() != null)
			{
				if (begindatum != null
					&& context.getOnderwijsproductAfname().getBegindatum() == null)
					context.getOnderwijsproductAfname().setBegindatum(begindatum);
				model.saveObject();
			}
		}
	}

	public ModelManager getManager()
	{
		return new DefaultModelManager(OnderwijsproductAfnameContext.class,
			OnderwijsproductAfname.class);
	}

	public Verbintenis getVerbintenis()
	{
		if (verbintenis != null)
			return verbintenis.getObject();
		return null;
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		if (verbintenis != null)
			this.verbintenis = ModelFactory.getModel(verbintenis);
		else
			this.verbintenis = null;
	}
}
