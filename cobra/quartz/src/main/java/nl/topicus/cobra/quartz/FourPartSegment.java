package nl.topicus.cobra.quartz;

public enum FourPartSegment implements JobSegment
{
	FIRST_PART,
	SECOND_PART,
	THIRD_PART,
	FOURTH_PART;

	@Override
	public int getPercent()
	{
		return 25;
	}
}
