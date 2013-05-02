package nl.topicus.eduarte.entities.participatie.enums;

/**
 * WaarnemingSoort geeft aan wat voor waarneming iets is.
 * 
 * @author loite
 */
public enum WaarnemingSoort
{
	/**
	 * Deelnemer is aanwezig.
	 */
	Aanwezig
	{
		@Override
		public String toString()
		{
			return "P - Aanwezig";
		}

		@Override
		public String getCssClassName()
		{
			return "";
		}
	},
	/**
	 * Deelnemer is afwezig.
	 */
	Afwezig
	{
		@Override
		public String toString()
		{
			return "A - Afwezig";
		}

		@Override
		public String getCssClassName()
		{
			return "tblPink";
		}
	},
	/**
	 * Deelnemer was weliswaar ingepland, maar hoefde uiteindelijk niet aanwezig te zijn.
	 * Waarnemingen van dit soort moeten *NIET* meegenomen worden in overzichten, maar
	 * dienen alleen als indicator dat de aanwezigheid van iemand bij een afspraak wel
	 * gemeten is.
	 */
	Nvt
	{
		@Override
		public String toString()
		{
			return "N - Nvt";
		}

		@Override
		public String getCssClassName()
		{
			return "tblBlue";
		}
	},
	/**
	 * Een deelnemer kan deels afwezig zijn tijdens een les, bijvoorbeeld als deze eerder
	 * de les moet verlaten wegens een andere afspraak, of als deze de les pas wat later
	 * binnenkomt als gevolg van een andere afspraak. Deze waarnemingsoort is *NIET*
	 * bedoeld voor opslaan in de database, maar wordt alleen in de applicatie gebruikt
	 * zodat de gebruiker aan kan geven dat een deelnemer deels afwezig is. De applicatie
	 * zet dit zelf automatisch om in aanwezig- en afwezigheidswaarnemingen.
	 */
	DeelsAfwezig
	{
		@Override
		public String toString()
		{
			return "D - Deels afwezig";
		}

		@Override
		public String getCssClassName()
		{
			return "tblPink";
		}

		@Override
		public String getOmschrijving()
		{
			return "Deels afwezig";
		}
	};

	/**
	 * @return Class name voor de css wanneer deze waarneming getoond wordt.
	 */
	public abstract String getCssClassName();

	/**
	 * @return Omschrijving van de waarnemingsoort
	 */
	public String getOmschrijving()
	{
		return name();
	}

	public static WaarnemingSoort parse(String soort)
	{
		for (WaarnemingSoort value : values())
		{
			if (value.name().equalsIgnoreCase(soort))
			{
				return value;
			}
		}
		return null;
	}

}
