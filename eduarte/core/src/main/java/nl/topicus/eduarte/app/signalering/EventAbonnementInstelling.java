package nl.topicus.eduarte.app.signalering;

/**
 * @author loite
 */
public enum EventAbonnementInstelling
{
	/**
	 * Signalering staat uit
	 */
	Uit,
	/**
	 * Signalering staat aan
	 */
	Aan,
	/**
	 * Signalering is verplicht (alleen te gebruiken bij InstellingSignaleringSetting,
	 * niet voor MedewerkerSignaleringSetting)
	 */
	Verplicht;

}
