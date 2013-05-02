package nl.topicus.eduarte.app.resultaat;

import java.io.Serializable;
import java.util.*;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.DeelnemerResultaatVersie;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

public class ResultaatVersionCollection implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Map<ResultaatVersionKey, Long> versions = new HashMap<ResultaatVersionKey, Long>();

	public ResultaatVersionCollection(Resultaatstructuur resultaatstructuur)
	{
		versions.put(null, resultaatstructuur.getVersion());
		addKeys(resultaatstructuur);
	}

	public ResultaatVersionCollection(ToetsZoekFilter toetsFilter)
	{
		Set<Resultaatstructuur> structuren = new HashSet<Resultaatstructuur>();
		for (Toets curToets : DataAccessRegistry.getHelper(ToetsDataAccessHelper.class).list(
			toetsFilter))
		{
			structuren.add(curToets.getResultaatstructuur());
		}
		for (Resultaatstructuur curStructuur : structuren)
		{
			addKeys(curStructuur);
		}
	}

	private void addKeys(Resultaatstructuur structuur)
	{
		if (!structuur.isSaved())
			return;

		Set<Deelnemer> deelnemers =
			new HashSet<Deelnemer>(getHelper().getDeelnemersMetResultaten(structuur));
		for (DeelnemerResultaatVersie curVersie : structuur.getVersies())
		{
			versions.put(new ResultaatVersionKey(curVersie), curVersie.getVersie());
			deelnemers.remove(curVersie.getDeelnemer());
		}
		for (Deelnemer curDeelnemer : deelnemers)
		{
			versions.put(new ResultaatVersionKey(curDeelnemer, structuur), null);
		}
	}

	public ResultaatVersionCollection(List<Deelnemer> deelnemers)
	{
		for (Deelnemer curDeelnemer : deelnemers)
		{
			List<Resultaatstructuur> structuren =
				getHelper().getStructuren(Arrays.asList(curDeelnemer));
			Set<Resultaatstructuur> structurenSet = new HashSet<Resultaatstructuur>(structuren);
			List<DeelnemerResultaatVersie> versies =
				readVersions(Arrays.asList(curDeelnemer), structuren);
			for (DeelnemerResultaatVersie curVersie : versies)
			{
				versions.put(new ResultaatVersionKey(curVersie), curVersie.getVersie());
				structurenSet.remove(curVersie.getResultaatstructuur());
			}
			for (Resultaatstructuur curStructuur : structurenSet)
			{
				versions.put(new ResultaatVersionKey(curDeelnemer, curStructuur), null);
			}
		}
	}

	public long getVersion(ResultaatVersionKey key)
	{
		Long ret = versions.get(key);
		return ret == null ? -1 : ret;
	}

	private Set<ResultaatVersionKey> getKeys()
	{
		return versions.keySet();
	}

	public Set<ResultaatVersionKey> getLockKeys()
	{
		Set<ResultaatVersionKey> ret = new HashSet<ResultaatVersionKey>(getKeys());
		ret.remove(null);
		return ret;
	}

	public boolean verifyVersions(ResultaatVersionCollection other)
	{
		Set<ResultaatVersionKey> keysToCheck = new HashSet<ResultaatVersionKey>(getKeys());
		keysToCheck.addAll(other.getKeys());
		for (ResultaatVersionKey curKey : keysToCheck)
			if (getVersion(curKey) != other.getVersion(curKey))
				return false;
		return true;
	}

	public void incrementAndSave()
	{
		@SuppressWarnings("unchecked")
		BatchDataAccessHelper<IdObject> helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		Map<Resultaatstructuur, List<Deelnemer>> deelnemersPerStructuur =
			new HashMap<Resultaatstructuur, List<Deelnemer>>();
		for (Map.Entry<ResultaatVersionKey, Long> curEntry : versions.entrySet())
		{
			ResultaatVersionKey key = curEntry.getKey();
			if (curEntry.getKey() != null)
			{
				Deelnemer curDeelnemer = helper.load(Deelnemer.class, key.getDeelnemerId());
				Resultaatstructuur curStructuur =
					helper.load(Resultaatstructuur.class, key.getStructuurId());
				List<Deelnemer> curDeelnemers = deelnemersPerStructuur.get(curStructuur);
				if (curDeelnemers == null)
				{
					curDeelnemers = new ArrayList<Deelnemer>();
					deelnemersPerStructuur.put(curStructuur, curDeelnemers);
				}
				curDeelnemers.add(curDeelnemer);
			}
		}
		List<DeelnemerResultaatVersie> resultaatVersions =
			new ArrayList<DeelnemerResultaatVersie>();
		if (!deelnemersPerStructuur.isEmpty())
		{
			resultaatVersions.addAll(getOrCreateVersions(deelnemersPerStructuur));
			getHelper().incrementVersies(resultaatVersions);
		}
	}

	private List<DeelnemerResultaatVersie> getOrCreateVersions(
			Map<Resultaatstructuur, List<Deelnemer>> deelnemersPerStructuur)
	{
		List<DeelnemerResultaatVersie> ret = new ArrayList<DeelnemerResultaatVersie>();
		for (Map.Entry<Resultaatstructuur, List<Deelnemer>> curDeelnemers : deelnemersPerStructuur
			.entrySet())
		{
			List<DeelnemerResultaatVersie> inDb =
				readVersions(curDeelnemers.getValue(), Collections.singletonList(curDeelnemers
					.getKey()));
			Set<Deelnemer> deelnemersInDb = new HashSet<Deelnemer>();
			for (DeelnemerResultaatVersie curVersie : inDb)
				deelnemersInDb.add(curVersie.getDeelnemer());

			ret.addAll(inDb);
			for (Deelnemer curDeelnemer : curDeelnemers.getValue())
			{
				if (!deelnemersInDb.contains(curDeelnemer))
				{
					DeelnemerResultaatVersie newVersie =
						new DeelnemerResultaatVersie(curDeelnemer, curDeelnemers.getKey());
					newVersie.save();
					ret.add(newVersie);
				}
			}
		}
		return ret;
	}

	private List<DeelnemerResultaatVersie> readVersions(List<Deelnemer> deelnemers,
			List<Resultaatstructuur> structuren)
	{
		return getHelper().getVersies(deelnemers, structuren);
	}

	private ResultaatstructuurDataAccessHelper getHelper()
	{
		return DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class);
	}
}
