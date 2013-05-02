package nl.topicus.eduarte.entities.participatie;

/**
 * Externe systemen waarmee de participatiemodule kan koppelen.
 * 
 * @author loite
 */
public enum ExternSysteem
{

	/**
	 * Onbekend/overige systemen.
	 */
	Overig,
	/**
	 * Het roosterpakket gp-Untis.
	 */
	gpUntis
	{
		@Override
		public String getOmschrijving()
		{
			return "gp-Untis";
		}
	};

	/**
	 * @return De omschrijving van het systeem.
	 */
	public String getOmschrijving()
	{
		return toString();
	}

}
