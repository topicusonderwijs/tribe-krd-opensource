package nl.topicus.eduarte.entities.inschrijving;

public interface BronCommuniceerbaar
{
	/**
	 * @return <code>true</code> als de status communicatie met BRON mogelijk maakt
	 *         (tussen Volledig en Beeindigd)
	 */
	public abstract boolean isBronCommuniceerbaar();

}