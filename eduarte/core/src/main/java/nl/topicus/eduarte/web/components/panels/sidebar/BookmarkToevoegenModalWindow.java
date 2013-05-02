package nl.topicus.eduarte.web.components.panels.sidebar;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;

/**
 * Modal window voor het toevoegen van een nieuwe bookmark.
 * 
 * @author loite
 */
public class BookmarkToevoegenModalWindow extends CobraModalWindow<Bookmark>
{
	private static final long serialVersionUID = 1L;

	private SecurePage page;

	private IChangeRecordingModel<BookmarkFolder> folder;

	public BookmarkToevoegenModalWindow(String id, final SecurePage page, BookmarkFolder folder)
	{
		super(id);
		this.page = page;
		if (folder != null)
			setFolder(folder);
		setTitle("Huidige pagina toevoegen als nieuwe favoriet");
		setInitialHeight(400);
		setInitialWidth(700);
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
	protected CobraModalWindowBasePanel<Bookmark> createContents(String id)
	{
		return new BookmarkToevoegenPanel(id, page, BookmarkToevoegenModalWindow.this, this.folder);
	}

	public void setFolder(BookmarkFolder folder)
	{
		if (folder != null)
			this.folder =
				ModelFactory.getCompoundChangeRecordingModel(folder, new DefaultModelManager(
					Bookmark.class, BookmarkFolder.class));
		else
			this.folder = null;
	}
}
