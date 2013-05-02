package nl.topicus.eduarte.krd.bron;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BronChanges
{
	private static final Logger log = LoggerFactory.getLogger(BronChanges.class);

	private LinkedHashMap<Entiteit, List<BronStateChange>> entiteitChanges =
		new LinkedHashMap<Entiteit, List<BronStateChange>>();

	public void addChange(Entiteit sleutel, BronStateChange change)
	{
		List<BronStateChange> list = entiteitChanges.get(sleutel);
		if (list == null)
		{
			list = new ArrayList<BronStateChange>();
			entiteitChanges.put(sleutel, list);
		}
		list.add(change);
		log.debug("Change recorded: {}", change);
	}

	/**
	 * @return een kopie van de lijst van gewijzigde verbintenissen.
	 */
	public List<Verbintenis> getGewijzigdeVerbintenissen()
	{
		ArrayList<Verbintenis> verbintenissen = new ArrayList<Verbintenis>();
		for (Entiteit entiteit : entiteitChanges.keySet())
		{
			if (entiteit instanceof Verbintenis)
			{
				verbintenissen.add((Verbintenis) entiteit);
			}
		}
		return verbintenissen;
	}

	public BronVerbintenisChanges getWijzigingen(Verbintenis verbintenis)
	{
		List<BronStateChange> changes = entiteitChanges.get(verbintenis);
		if (changes == null)
		{
			changes = Collections.emptyList();
		}
		return new BronVerbintenisChanges(verbintenis, changes);
	}

	/**
	 * @return een kopie van de lijst van gewijzigde deelnemers.
	 */
	public List<Deelnemer> getGewijzigdeDeelnemers()
	{
		ArrayList<Deelnemer> deelnemers = new ArrayList<Deelnemer>();
		for (Entiteit entiteit : entiteitChanges.keySet())
		{
			if (entiteit instanceof Deelnemer)
			{
				deelnemers.add((Deelnemer) entiteit);
			}
		}
		return deelnemers;
	}

	public BronDeelnemerChanges getWijzigingen(Deelnemer deelnemer)
	{
		List<BronStateChange> changes = entiteitChanges.get(deelnemer);
		if (changes == null)
		{
			changes = Collections.emptyList();
		}
		return new BronDeelnemerChanges(deelnemer, changes);
	}

	public boolean isEmpty()
	{
		return entiteitChanges.isEmpty();
	}
}
