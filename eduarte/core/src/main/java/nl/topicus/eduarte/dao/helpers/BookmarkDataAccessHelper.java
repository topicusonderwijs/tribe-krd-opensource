package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.Bookmark.SoortBookmark;
import nl.topicus.eduarte.zoekfilters.BookmarkZoekFilter;

/**
 * @author loite
 */
public interface BookmarkDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<Bookmark, BookmarkZoekFilter>
{

	/**
	 * Geeft alle bookmarks van de gegeven gebruiker die horen bij de gegeven pagina, of
	 * die globaal gedefinieerd zijn.
	 * 
	 * @param account
	 *            Het account waarvoor de bookmarks opgevraagd moet worden
	 * @param pageClass
	 *            De pagina waarop de gebruiker zit.
	 * @param soort
	 *            Het soort bookmark, een ToDo of een gewone Bookmark
	 * @return De bookmarks van het gegeven account.
	 */
	public List<Bookmark> list(Account account, String pageClass, SoortBookmark soort);

}
