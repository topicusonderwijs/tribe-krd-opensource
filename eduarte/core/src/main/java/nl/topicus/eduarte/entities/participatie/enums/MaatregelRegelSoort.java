package nl.topicus.eduarte.entities.participatie.enums;

import nl.topicus.cobra.util.StringUtil;

/**
 * Geeft aan wat voor absentiesoort een absentiereden is.
 * 
 * @author vandekamp
 */
public enum MaatregelRegelSoort
{
	/**
	 * Het aantal meldingen moet gelijk zijn aan een constante.
	 */
	Gelijk_Aan
	{
		@Override
		public String toString()
		{
			return "Aantal meldingen gelijk aan";
		}

	},
	/**
	 * Het aantal meldingen moet gelijk zijn aan een veelvoud van een constante, eventueel
	 * na aftrek van een aantal vrije meldingen.
	 */
	Elke_X_Meldingen
	{
		@Override
		public String toString()
		{
			return "Elke x-aantal meldingen";
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
