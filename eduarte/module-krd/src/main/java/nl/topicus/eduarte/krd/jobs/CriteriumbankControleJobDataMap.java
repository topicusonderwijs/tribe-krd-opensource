package nl.topicus.eduarte.krd.jobs;

import java.util.List;

import nl.topicus.cobra.quartz.DetachableJobDataMap;

public class CriteriumbankControleJobDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public CriteriumbankControleJobDataMap(List<Long> examendeelnameIds, List<Long> verbintenisIds,
			Long toegestaneExamenstatusOvergangId)
	{
		setExamendeelnameIds(examendeelnameIds);
		setVerbintenisIds(verbintenisIds);
		setToegestaneExamenstatusOvergangId(toegestaneExamenstatusOvergangId);
	}

	public void setExamendeelnameIds(List<Long> examendeelnameIds)
	{
		put("examendeelnameIds", examendeelnameIds);
	}

	@SuppressWarnings("unchecked")
	public List<Long> getExamendeelnameIds()
	{
		return (List<Long>) get("examendeelnameIds");
	}

	public void setVerbintenisIds(List<Long> verbintenisIds)
	{
		put("verbintenisIds", verbintenisIds);
	}

	@SuppressWarnings("unchecked")
	public List<Long> getVerbintenisIds()
	{
		return (List<Long>) get("verbintenisIds");
	}

	public void setToegestaneExamenstatusOvergangId(Long toegestaneExamenstatusOvergangId)
	{
		put("toegestaneExamenstatusOvergangId", toegestaneExamenstatusOvergangId);
	}

	public Long getToegestaneExamenstatusOvergangId()
	{
		return (Long) get("toegestaneExamenstatusOvergangId");
	}
}
