package nl.topicus.eduarte.entities.taxonomie;

/**
 * Taxonomien bestaan uit drie hoofddelen; Taxonomie, Verbintenisgebied, Deelgebied
 * 
 * @author loite
 */
public enum SoortTaxonomieElement
{
	/**
	 * Taxonomie
	 */
	Taxonomie
	{
		@Override
		public boolean isAanmaakbaar()
		{
			return false;
		}
	},
	/**
	 * Verbintenisgebied
	 */
	Verbintenisgebied,
	/**
	 * Deelgebied
	 */
	Deelgebied;

	/**
	 * @return true als taxonomieelementtypes van dit soort via de applicatie aan te maken
	 *         zijn.
	 */
	public boolean isAanmaakbaar()
	{
		return true;
	}

}
