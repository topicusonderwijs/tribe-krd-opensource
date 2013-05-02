package nl.topicus.eduarte.dao.helpers;

import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;
import nl.topicus.eduarte.zoekfilters.BookmarkFolderZoekFilter;

/**
 * @author niesink
 */
public interface BookmarkFolderDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<BookmarkFolder, BookmarkFolderZoekFilter>
{

	/**
	 * Geeft alle bookmarkfolders van de gegeven gebruiker.
	 * 
	 * @param account
	 *            Het account waarvoor de bookmarkfolders opgevraagd moet worden
	 * @return De bookmarkfolders van het gegeven account.
	 */
	public List<BookmarkFolder> list(Account account);

	BookmarkFolder getVolgorde(Integer volgorde);
}
