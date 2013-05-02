package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;

/**
 * @author maatman
 */
public enum TijdEenheid
{
	Dagen
	{
		@Override
		public Date add(Date start, int eenheden)
		{
			return TimeUtil.getInstance().addDays(start, eenheden);
		}
	},
	Weken
	{
		@Override
		public Date add(Date start, int eenheden)
		{
			return TimeUtil.getInstance().addWeeks(start, eenheden);
		}
	},
	Maanden
	{
		@Override
		public Date add(Date start, int eenheden)
		{
			return TimeUtil.getInstance().addMonths(start, eenheden);
		}
	},
	Jaren
	{
		@Override
		public Date add(Date start, int eenheden)
		{
			return TimeUtil.getInstance().addYears(start, eenheden);
		}
	};

	public abstract Date add(Date start, int eenheden);
}
