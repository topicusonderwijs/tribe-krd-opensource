package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.ModerneTaal;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.TaalType;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Taalkeuze;
import nl.topicus.eduarte.zoekfilters.TaalkeuzeZoekFilter;

/**
 * @author vandenbrink
 */
public interface TaalkeuzeDataAccessHelper extends
		ZoekFilterDataAccessHelper<Taalkeuze, TaalkeuzeZoekFilter>
{
	/**
	 * Convenience functie om te kijken of de taal in gebruik (i.e. er is een
	 * {@link Taalkeuze} voor die taal).
	 * 
	 * @param taal
	 * @return true als taal gekozen is door een deelnemer
	 */
	public boolean isTaalInGebruik(ModerneTaal taal);

	/**
	 * De talen die een deelnemer gekozen heeft bij een inschrijving
	 * 
	 * @param verbintenis
	 * @return lijst met taalkeuzes
	 */
	public List<Taalkeuze> getTaalkeuzes(Verbintenis verbintenis);

	public List<ModerneTaal> getTaalkeuzes(Verbintenis verbintenis, TaalType taaltype);
}
