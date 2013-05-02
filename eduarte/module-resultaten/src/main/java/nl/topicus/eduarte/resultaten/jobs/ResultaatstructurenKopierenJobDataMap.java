package nl.topicus.eduarte.resultaten.jobs;

import java.util.Set;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;

public class ResultaatstructurenKopierenJobDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public ResultaatstructurenKopierenJobDataMap(ResultaatstructuurKopieerSettings kopieerSettings,
			Set<Onderwijsproduct> onderwijsproducten, Resultaatstructuur bronStructuur)
	{
		setKopieerSettings(kopieerSettings);
		setOnderwijsproducten(onderwijsproducten);
		setBronStructuur(bronStructuur);
	}

	public ResultaatstructuurKopieerSettings getKopieerSettings()
	{
		return (ResultaatstructuurKopieerSettings) get("kopieerSettings");
	}

	public void setKopieerSettings(ResultaatstructuurKopieerSettings kopieerSettings)
	{
		put("kopieerSettings", kopieerSettings);
	}

	public Resultaatstructuur getBronStructuur()
	{
		return (Resultaatstructuur) get("bronStructuur");
	}

	public void setBronStructuur(Resultaatstructuur bronStructuur)
	{
		put("bronStructuur", bronStructuur);
	}

	@SuppressWarnings("unchecked")
	public Set<Onderwijsproduct> getOnderwijsproducten()
	{
		return (Set<Onderwijsproduct>) get("onderwijsproducten");
	}

	public void setOnderwijsproducten(Set<Onderwijsproduct> onderwijsproducten)
	{
		put("onderwijsproducten", onderwijsproducten);
	}
}
