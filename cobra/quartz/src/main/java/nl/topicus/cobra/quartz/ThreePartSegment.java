package nl.topicus.cobra.quartz;

public enum ThreePartSegment implements JobSegment
{
	FIRST_PART(33),
	SECOND_PART(33),
	THIRD_PART(34);

	private int percent;

	private ThreePartSegment(int percent)
	{
		this.percent = percent;
	}

	@Override
	public int getPercent()
	{
		return percent;
	}
}
