package nl.topicus.eduarte.entities.participatie.enums;

/**
 * @author vandekamp
 */
public enum AbsentiePresentieEnum
{
	Absentie,
	/**
	 * Alleen presentie
	 */
	Presentie,
	/**
	 * Absentie en Presentie
	 */
	Absentie_en_Presentie
	{
		@Override
		public String toString()
		{
			return "Absentie en presentie";
		}
	};
}
