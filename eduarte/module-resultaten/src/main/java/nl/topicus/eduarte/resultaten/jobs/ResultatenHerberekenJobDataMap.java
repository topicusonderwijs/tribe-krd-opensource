package nl.topicus.eduarte.resultaten.jobs;

import java.util.List;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

public class ResultatenHerberekenJobDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public ResultatenHerberekenJobDataMap(Resultaatstructuur structuur, List<Toets> toetsen,
			boolean maakBeschikbaar)
	{
		setResultaatstructuur(structuur);
		setToetsen(toetsen);
		setMaakBeschikbaar(maakBeschikbaar);
	}

	public Resultaatstructuur getResultaatstructuur()
	{
		return (Resultaatstructuur) get("resultaatstructuur");
	}

	public void setResultaatstructuur(Resultaatstructuur resultaatstructuur)
	{
		put("resultaatstructuur", resultaatstructuur);
	}

	@SuppressWarnings("unchecked")
	public List<Toets> getToetsen()
	{
		return (List<Toets>) get("toetsen");
	}

	public void setToetsen(List<Toets> toetsen)
	{
		put("toetsen", toetsen);
	}

	public Boolean getMaakBeschikbaar()
	{
		return (Boolean) get("maakBeschikbaar");
	}

	public void setMaakBeschikbaar(Boolean maakBeschikbaar)
	{
		put("maakBeschikbaar", maakBeschikbaar);
	}
}
