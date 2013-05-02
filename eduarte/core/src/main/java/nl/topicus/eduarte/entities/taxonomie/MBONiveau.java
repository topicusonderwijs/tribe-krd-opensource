package nl.topicus.eduarte.entities.taxonomie;

import nl.topicus.cobra.util.StringUtil;

public enum MBONiveau
{
	Niveau1("Niveau 1"),
	Niveau2("Niveau 2"),
	Niveau3("Niveau 3"),
	Niveau4("Niveau 4");

	private final String omschrijving;

	private MBONiveau(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	@Override
	public String toString()
	{
		return omschrijving;
	}

	/**
	 * @param niveau
	 *            Het niveau in de vorm 1, 2, 3 of 4.
	 * @return Het niveau dat overeenkomt met de gegeven string.
	 */
	public static MBONiveau parse(String niveau)
	{
		if (StringUtil.isNotEmpty(niveau) && StringUtil.isNumeric(niveau))
		{
			int nr = Integer.valueOf(niveau).intValue();
			if (nr >= 1 && nr <= MBONiveau.values().length)
				return MBONiveau.values()[nr - 1];
		}
		return null;
	}

}
