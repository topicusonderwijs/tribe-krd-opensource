package nl.topicus.eduarte.app;

import nl.topicus.cobra.modules.CobraModule;
import nl.topicus.eduarte.entities.signalering.Signaal;

/**
 * Modules die signalen gebruiken kunnen deze interface implementeren om het afhandelen
 * van de signalen mogelijk te maken. {@link AbstractEduArteModule} biedt een basis
 * implementatie voor deze interface.
 * 
 * @author papegaaij
 */
public interface EduArteSignaalModule extends CobraModule
{
	/**
	 * Geeft de {@link SignaalHandler} voor het gegeven signaal.
	 * 
	 * @param <T>
	 *            Het type van het signaal.
	 * @param signaal
	 *            Het signaal waar een handler voor gezocht wordt.
	 * @return De handler, of null als er geen beschikbaar is.
	 */
	public <T extends Signaal> SignaalHandler<T> getSignaalHandler(T signaal);
}
