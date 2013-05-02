package nl.topicus.eduarte.krd.jobs;

import java.util.List;
import java.util.Set;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.pages.shared.KopieerSettings;

public class CriteriaKopierenJobDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public CriteriaKopierenJobDataMap(KopieerSettings kopieerSettings, Set<Opleiding> opleidingen,
			List<Criterium> criteria)
	{
		setKopieerSettings(kopieerSettings);
		setOpleidingen(opleidingen);
		setCriteria(criteria);
	}

	public KopieerSettings getKopieerSettings()
	{
		return (KopieerSettings) get("kopieerSettings");
	}

	public void setKopieerSettings(KopieerSettings kopieerSettings)
	{
		put("kopieerSettings", kopieerSettings);
	}

	@SuppressWarnings("unchecked")
	public Set<Opleiding> getOpleidingen()
	{
		return (Set<Opleiding>) get("opleidingen");
	}

	public void setOpleidingen(Set<Opleiding> opleidingen)
	{
		put("opleidingen", opleidingen);
	}

	@SuppressWarnings("unchecked")
	public List<Criterium> getCriteria()
	{
		return (List<Criterium>) get("criteria");
	}

	public void setCriteria(List<Criterium> criteria)
	{
		put("criteria", criteria);
	}
}
