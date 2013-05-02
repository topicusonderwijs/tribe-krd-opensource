package nl.topicus.eduarte.dao.helpers.vasco;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.vasco.VascoStatus;
import nl.topicus.vasco.VascoValidationResponse;

/**
 * DataAccessHelper voor het interacteren met een Vasco server voor het valideren en
 * importeren van tokens.
 */
public interface VascoDataAccessHelper extends DataAccessHelper<Entiteit>
{
	/**
	 * Bepaalt de status van de vasco server.
	 * 
	 * @return <code>null</code> als er een fout optreedt tijdens het opvragen van de
	 *         status.
	 */
	public VascoStatus getStatus();

	/**
	 * Verifieert het wachtwoord van de token geidentificeerd door het vascoSerienummer
	 * gegeven de digipassData.
	 */
	VascoValidationResponse verifieer(String vascoSerienummer, String digipassData, String password);

	void importFile(String dpxkey, byte[] contents) throws VascoImportException;

	/**
	 * Exceptie die kan optreden bij het importeren van vasco tokens (bijvoorbeeld als de
	 * RMI server niet bereikbaar is).
	 */
	public static class VascoImportException extends Exception
	{
		private static final long serialVersionUID = 1L;

		public VascoImportException(Exception e)
		{
			super(e);
		}

		public VascoImportException(String message, Exception e)
		{
			super(message, e);
		}
	}
}
