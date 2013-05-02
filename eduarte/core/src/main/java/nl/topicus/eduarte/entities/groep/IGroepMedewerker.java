package nl.topicus.eduarte.entities.groep;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.GroepProvider;
import nl.topicus.eduarte.providers.MedewerkerProvider;

/**
 * Interface om aan te geven dat het om een koppel object gaat tussen een groep en een
 * medewerker
 * 
 * @author hoeve
 */
public interface IGroepMedewerker extends IdObject, MedewerkerProvider, GroepProvider
{
	public void setGroep(Groep groep);

	public void setMedewerker(Medewerker medewerker);
}
