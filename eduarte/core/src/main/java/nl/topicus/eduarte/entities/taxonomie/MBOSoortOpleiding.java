package nl.topicus.eduarte.entities.taxonomie;

import nl.topicus.cobra.util.StringUtil;

/**
 * Soorten MBO-opleidingen.
 * 
 * @author loite
 */
public enum MBOSoortOpleiding
{
	Assistentopleiding,
	Basisberoepsopleiding,
	Middenkaderopleiding,
	Specialistenopleiding,
	Vakopleiding;

	public static MBOSoortOpleiding parse(String soort)
	{
		if (StringUtil.isEmpty(soort))
			return null;
		for (MBOSoortOpleiding srt : MBOSoortOpleiding.values())
		{
			if (srt.name().equalsIgnoreCase(soort))
				return srt;
		}
		return null;
	}

}
