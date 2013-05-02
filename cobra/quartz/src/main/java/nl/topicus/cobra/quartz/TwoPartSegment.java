package nl.topicus.cobra.quartz;

public enum TwoPartSegment implements JobSegment
{
	FIRST_HALF,
	SECOND_HALF;

	@Override
	public int getPercent()
	{
		return 50;
	}
}
