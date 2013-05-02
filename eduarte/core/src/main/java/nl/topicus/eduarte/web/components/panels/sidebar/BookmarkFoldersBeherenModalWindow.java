package nl.topicus.eduarte.web.components.panels.sidebar;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;

/**
 * Modal window voor het beheren van bookmarkfolders.
 * 
 * @author niesink
 */
public class BookmarkFoldersBeherenModalWindow extends CobraModalWindow<BookmarkFolder>
{
	private static final long serialVersionUID = 1L;

	public BookmarkFoldersBeherenModalWindow(String id, final SecurePage page)
	{
		super(id);
		setTitle("Mappen voor favorieten beheren");
		setInitialHeight(400);
		setInitialWidth(500);
		setResizable(false);

		setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.addComponent(page.get("laySide"));
			}
		});
	}

	@Override
	protected CobraModalWindowBasePanel<BookmarkFolder> createContents(String id)
	{
		return new BookmarkFoldersBeherenPanel(id, BookmarkFoldersBeherenModalWindow.this);
	}
}
