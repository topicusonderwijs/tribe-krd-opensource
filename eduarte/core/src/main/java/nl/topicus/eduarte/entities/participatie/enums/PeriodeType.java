package nl.topicus.eduarte.entities.participatie.enums;

/**
 * @author vandekamp
 */
public enum PeriodeType
{
	/**
	 * Het lopende schooljaar
	 */
	Schooljaar
	{
		@Override
		public String toString()
		{
			return "Het lopende schooljaar";
		}
	},
	/**
	 * De laatste X weken
	 */
	Laatste_x_weken
	{
		@Override
		public String toString()
		{
			return "Laatste x-aantal weken";
		}
	},
	/**
	 * In het systeem gedefineerde Periode
	 */
	Periode
	{
		@Override
		public String toString()
		{
			return "Gedefineerde periode";
		}
	};
}
