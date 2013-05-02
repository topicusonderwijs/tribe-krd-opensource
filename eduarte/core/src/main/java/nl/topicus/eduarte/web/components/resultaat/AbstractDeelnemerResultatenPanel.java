package nl.topicus.eduarte.web.components.resultaat;

import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.model.IModel;

public abstract class AbstractDeelnemerResultatenPanel<T, M extends ResultatenModel> extends
		AbstractResultatenPanel<T, M>
{
	private static final long serialVersionUID = 1L;

	protected IModel<Deelnemer> deelnemerModel;

	public AbstractDeelnemerResultatenPanel(String id, ResultatenUIFactory<T, M> uiFactory,
			ToetsZoekFilter toetsFilter, Deelnemer deelnemer, Cohort cohort)
	{
		super(id, uiFactory, toetsFilter, true, null);
		deelnemerModel = ModelFactory.getModel(deelnemer);
		getResultaatstructuurFilter().setDeelnemers(Arrays.asList(deelnemer));
		if (cohort != null)
			getResultaatstructuurFilter().setCohort(cohort);

		createComponents();
	}

	@Override
	public List<Deelnemer> getDeelnemers()
	{
		return Arrays.asList(deelnemerModel.getObject());
	}

	@Override
	public List<Toets> getToetsen()
	{
		return DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).list(getToetsFilter());
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		deelnemerModel.detach();
	}
}
