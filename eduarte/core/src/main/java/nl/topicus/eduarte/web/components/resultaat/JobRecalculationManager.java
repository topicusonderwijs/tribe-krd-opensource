package nl.topicus.eduarte.web.components.resultaat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class JobRecalculationManager extends RecalculationManager
{
	private static final long serialVersionUID = 1L;

	private class ToetsDeelnemerResultaatModel extends
			LoadableDetachableModel<List<List<Resultaat>>>
	{
		private static final long serialVersionUID = 1L;

		private ResultaatKey key;

		public ToetsDeelnemerResultaatModel(ResultaatKey key)
		{
			this.key = key;
		}

		@Override
		protected List<List<Resultaat>> load()
		{
			List<Resultaat> retResultaten =
				DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class).getActueleResultaten(
					key.getToets(), key.getDeelnemer());
			List<List<Resultaat>> resultatenVoorToets =
				convertToResultatenPerPoging(key, retResultaten);
			ResultatenModel.sortResultatenVoorToets(resultatenVoorToets);
			return resultatenVoorToets;
		}
	}

	private Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten =
		new HashMap<ResultaatKey, IModel<List<List<Resultaat>>>>();

	private Long deelnemerId = null;

	public JobRecalculationManager()
	{
	}

	@Override
	@SuppressWarnings("hiding")
	protected List<List<Resultaat>> getFromMap(ResultaatKey key,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		if (!resultaten.containsKey(key))
			resultaten.put(key, new ToetsDeelnemerResultaatModel(key));
		return super.getFromMap(key, resultaten);
	}

	public void recalculate()
	{
		resultatenModel = new ResultatenModel(null, new LoadableDetachableModel<List<Toets>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Toets> load()
			{
				List<Toets> ret = new ArrayList<Toets>();
				for (ResultaatKey curKey : getRecalculationKeys())
					ret.add(curKey.getToets());
				return ret;
			}
		}, new LoadableDetachableModel<List<Deelnemer>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Deelnemer> load()
			{
				List<Deelnemer> ret = new ArrayList<Deelnemer>();
				for (ResultaatKey curKey : getRecalculationKeys())
					ret.add(curKey.getDeelnemer());
				return ret;
			}
		});

		recalculate(resultaten);
		flush();
	}

	@Override
	@SuppressWarnings("hiding")
	protected void recalculate(ResultaatKey recalcKey,
			Map<ResultaatKey, IModel<List<List<Resultaat>>>> resultaten)
	{
		if (deelnemerId == null)
			deelnemerId = recalcKey.getDeelnemer().getId();
		super.recalculate(recalcKey, resultaten);

		if (recalcKey.getDeelnemer().getId() != deelnemerId)
		{
			deelnemerId = recalcKey.getDeelnemer().getId();
			flush();
		}
	}

	public void flush()
	{
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).flush();
		DataAccessRegistry.getHelper(SessionDataAccessHelper.class).clearSession();
		detach();
	}

	@Override
	public void detach()
	{
		super.detach();
		for (Map.Entry<ResultaatKey, IModel<List<List<Resultaat>>>> curEntry : resultaten
			.entrySet())
		{
			curEntry.getKey().detach();
			curEntry.getValue().detach();
		}
	}
}
