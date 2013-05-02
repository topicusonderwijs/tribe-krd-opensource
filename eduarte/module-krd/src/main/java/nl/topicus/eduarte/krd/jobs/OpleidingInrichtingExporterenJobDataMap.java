package nl.topicus.eduarte.krd.jobs;

import java.util.Set;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.eduarte.entities.opleiding.Opleiding;

public class OpleidingInrichtingExporterenJobDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public OpleidingInrichtingExporterenJobDataMap(Set<Opleiding> opleidingen)
	{
		setOpleidingen(opleidingen);
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
}
