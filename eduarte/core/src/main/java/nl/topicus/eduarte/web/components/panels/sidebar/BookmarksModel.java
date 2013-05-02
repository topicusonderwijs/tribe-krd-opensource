package nl.topicus.eduarte.web.components.panels.sidebar;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.BookmarkDataAccessHelper;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.sidebar.Bookmark.SoortBookmark;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookmarksModel extends LoadableDetachableModel<List<Bookmark>>
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(BookmarksModel.class);

	private final SecurePage page;

	private SoortBookmark soort;

	public BookmarksModel(SecurePage page, SoortBookmark soort)
	{
		this.page = page;
		this.soort = soort;
	}

	@Override
	protected List<Bookmark> load()
	{
		List<Bookmark> list =
			DataAccessRegistry.getHelper(BookmarkDataAccessHelper.class).list(
				EduArteContext.get().getAccount(), page.getClass().getName(), soort);
		// Verwijder alle contextgevoelige bookmarks waarvoor niet alle argumenten
		// beschikbaar zijn.
		int index = 0;
		while (index < list.size())
		{
			boolean verwijderd = false;
			Bookmark bookmark = list.get(index);
			if (bookmark.containsContextArguments())
			{
				try
				{
					List<Class< ? extends IContextInfoObject>> types =
						bookmark.getContextArgumentTypes();
					List<Class< ? extends IContextInfoObject>> available =
						page.getContextParameterTypes();
					for (Class< ? extends IContextInfoObject> type : types)
					{
						if (!available.contains(type))
						{
							list.remove(index);
							verwijderd = true;
							break;
						}
					}
				}
				catch (ClassNotFoundException e)
				{
					// Class van argument kon niet gevonden worden.
					log.error(e.toString(), e);
				}
			}
			if (!verwijderd)
			{
				index++;
			}
		}
		Collections.sort(list);
		return list;
	}
}
