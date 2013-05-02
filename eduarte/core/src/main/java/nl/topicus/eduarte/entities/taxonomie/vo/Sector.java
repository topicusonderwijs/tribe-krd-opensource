package nl.topicus.eduarte.entities.taxonomie.vo;

/**
 * Sector voor bovenbouw vmbo.
 * 
 * @author loite
 */
public enum Sector
{
	Techniek, ZorgEnWelzijn
	{
		@Override
		public String toString()
		{
			return "Zorg en Welzijn";
		}
	},
	Economie, Landbouw;

}
