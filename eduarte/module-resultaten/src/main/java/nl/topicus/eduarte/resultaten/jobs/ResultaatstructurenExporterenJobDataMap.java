package nl.topicus.eduarte.resultaten.jobs;

import java.util.Set;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;

public class ResultaatstructurenExporterenJobDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public ResultaatstructurenExporterenJobDataMap(Set<Onderwijsproduct> onderwijsproducten)
	{
		setOnderwijsproducten(onderwijsproducten);
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
