package nl.topicus.eduarte.web.components.link;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.web.pages.groep.GroepRapportagesPage;
import nl.topicus.eduarte.web.pages.shared.SelectieTarget;

/**
 * Interface welke aangeroepen wordt nadat men een rapportage heeft gestart.
 * 
 * Deze interface wordt gebruikt door pagina's welke een rapportage hebben gestart en zijn
 * aangeroepen door pagina's welke van deze interface erven. Bv
 * {@link GroepRapportagesPage} roept een deelnemer selectie pagina aan een deze start de
 * rapportage. Vervolgens gaat men terug naar de {@link GroepRapportagesPage}.
 * 
 * @author hoeve
 */
public interface IRapportageReturnPage<R extends IdObject, S extends IdObject, ZF extends DetachableZoekFilter<S>>
{
	/**
	 * Deze functie aan wanneer een rapportage is aangemaakt en men naar deze pagina wilt
	 * terug gaan en een actie nav de rapportage start wilt uitvoeren.
	 */
	void rapportageInvoked();

	/**
	 * @return Geeft de default filter van deze pagina.
	 */
	DetachableZoekFilter<S> getDefaultFilter();

	DatabaseSelection<R, S> createSelection();

	/**
	 * Geeft de pagina waarnaar toe moet worden genavigeerd om een selectie te doen om zo
	 * een rapportage te genereren.
	 * 
	 * @param selectionModel
	 * @param target
	 */
	void setResponsePage(DatabaseSelection<R, S> selection, SelectieTarget<R, S> target);

	Class< ? extends ZoekFilterDataAccessHelper<S, ZF>> getDataAccessHelperClass();

}
