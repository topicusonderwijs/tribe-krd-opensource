package nl.topicus.eduarte.entities.participatie.settings;

/**
 * De verschillende manieren waarop absentie dan wel presentie bijgehouden worden.
 * 
 * @author marrink
 */
public enum AbsentiePresentie
{
	/**
	 * Er worden geen gegevens over absentie of presentie bijgehouden.
	 */
	Geen
	{

		@Override
		public String getHeader()
		{
			return "Presentie";
		}
	},
	/**
	 * Er worden alleen absentie gegevens bijgehouden.
	 */
	Absentie
	{

		@Override
		public String getHeader()
		{
			return "Absentie";
		}
	},
	/**
	 * Er worden alleen presentie gegevens bijgehouden.
	 */
	Presentie
	{

		@Override
		public String getHeader()
		{
			return "Presentie";
		}
	},
	/**
	 * Er worden zowel absentie als presentie gegevens bijgehouden.
	 */
	AbsentiePresentie
	{
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString()
		{
			return "Absentie en presentie";
		}

		@Override
		public String getHeader()
		{
			return "Presentie";
		}
	};

	/**
	 * @return Header voor gebruik op bijvoorbeeld de zorgvierkantpagina.
	 */
	public abstract String getHeader();
}
