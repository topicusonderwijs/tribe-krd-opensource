package nl.topicus.eduarte.web.components.panels.templates;

import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;

/**
 * Implementaties moeten een constructor hebben met:
 * 
 * String id, RapportageConfiguratiePage page
 * 
 * @author papegaaij
 * @param <T>
 */
public interface RapportageConfiguratiePanel<T extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>>
{
	public RapportageConfiguratieFactory<T> getConfiguratie();
}
