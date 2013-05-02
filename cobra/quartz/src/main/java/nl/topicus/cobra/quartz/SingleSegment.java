package nl.topicus.cobra.quartz;

public enum SingleSegment implements JobSegment
{
	ENTIRE_JOB;

	@Override
	public int getPercent()
	{
		return 100;
	}
}
