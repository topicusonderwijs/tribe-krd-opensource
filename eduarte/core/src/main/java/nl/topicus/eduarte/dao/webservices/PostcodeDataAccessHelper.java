package nl.topicus.eduarte.dao.webservices;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Provincie;

/**
 * Service voor het ophalen van correcte adressen bij een postcode/huisnummer combinatie.
 */
public interface PostcodeDataAccessHelper extends DataAccessHelper<Entiteit>
{
	/**
	 * Adres interface voor het teruggeven van een adres query.
	 */
	public interface Adres
	{
		/**
		 * @return Returns the straatnaam.
		 */
		public abstract String getStraatnaam();

		/**
		 * @return Returns the huisnummer.
		 */
		public abstract String getHuisnummer();

		/**
		 * @return Returns the postcode.
		 */
		public abstract String getPostcode();

		/**
		 * @return Returns the plaatsnaam.
		 */
		public abstract String getPlaatsnaam();

		/**
		 * @return Returns the gemeente.
		 */
		public abstract Gemeente getGemeente();

		/**
		 * @return Returns the land.
		 */
		public abstract Land getLand();

		/**
		 * @return Returns the provincie.
		 */
		public abstract Provincie getProvincie();

		public abstract String getHuisnummerToevoeging();
	}

	/**
	 * Geeft een Adres terug met daarin de velden die gevonden zijn. Geeft een Adres terug
	 * zonder zoekresultaten, maar met zoek termen, wanneer er een fout is opgetreden.
	 * 
	 * @param postcode
	 *            de postcode
	 * @param huisnummer
	 *            het huisnummer
	 * @return een gevuld Adres als de call successvol was, anders <code>null</code>
	 */
	public Adres fillAdresByPostcodeHuisnummer(String postcode, String huisnummer,
			String huisnummerToevoeging);
}
