package nl.topicus.eduarte.krd.jobs;

import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerker.UpdateSoort;

public class GewijzigdRecord
{
	private MutatieLogVerwerkenJob job;

	private Long rowid;

	private UpdateSoort actie;

	private String tabelnaam;

	private Long modifiedId;

	public GewijzigdRecord(MutatieLogVerwerkenJob job, Long rowid, UpdateSoort action,
			String tabelnaam, Long modifiedId)
	{
		this.job = job;
		this.rowid = rowid;
		this.actie = action;
		this.tabelnaam = tabelnaam;
		this.modifiedId = modifiedId;
	}

	public UpdateSoort getActie()
	{
		return actie;
	}

	public Long getModifiedId()
	{
		return modifiedId;
	}

	public Long getRowId()
	{
		return rowid;
	}

	public String getTabelnaam()
	{
		return tabelnaam;
	}

	public void warn(String message)
	{
		job.warn(message);
	}

	public void error(String message)
	{
		job.error(message);
	}

	public void error(String message, Throwable throwable)
	{
		job.error(message, throwable);
	}

	@Override
	public String toString()
	{
		return String.format("%s %s#%s", actie, tabelnaam.toUpperCase(), modifiedId);
	}
}