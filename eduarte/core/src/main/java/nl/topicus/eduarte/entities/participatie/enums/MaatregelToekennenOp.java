package nl.topicus.eduarte.entities.participatie.enums;

import nl.topicus.cobra.util.StringUtil;

/**
 * Geeft aan wat op welke dag de maatregel moet worden toegekend
 * 
 * @author vandekamp
 */
public enum MaatregelToekennenOp
{
	/**
	 * Maatregel moet op dezelfde dag als de absentiemelding toegekend worden
	 */
	Dezelfde_Dag
	{
		@Override
		public String toString()
		{
			return "Dezelfde dag als de absentiemelding";
		}

	},
	/**
	 * De volgende schooldag van de deelnemer. Hierbij wordt rekening gehouden met het
	 * rooster van de deelnemer. Als er geen rooster voor de deelnemer gevonden kan
	 * worden, wordt de volgende schooldag gebruikt.
	 */
	Volgende_Schooldag
	{
		@Override
		public String toString()
		{
			return "Volgende schooldag";
		}
	},
	/**
	 * Niet een speciale datum
	 */
	Ongedefinieerd
	{
		@Override
		public String toString()
		{
			return "ongedefinieerd";
		}
	};

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return StringUtil.firstCharUppercase(name());
	}
}
