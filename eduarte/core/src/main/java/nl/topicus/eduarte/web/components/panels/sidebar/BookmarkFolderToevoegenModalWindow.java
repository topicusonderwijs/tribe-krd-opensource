package nl.topicus.eduarte.web.components.panels.sidebar;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;

/**
 * Modal window voor het toevoegen van een nieuwe bookmarkfolder.
 * 
 * @author niesink
 */
public class BookmarkFolderToevoegenModalWindow extends CobraModalWindow<BookmarkFolder>
{
	private static final long serialVersionUID = 1L;

	private IChangeRecordingModel<BookmarkFolder> folder;

	public BookmarkFolderToevoegenModalWindow(String id, BookmarkFolder folder)
	{
		super(id);
		setTitle("Map voor favorieten toevoegen");
		if (folder != null)
			setFolder(folder);
		setInitialHeight(200);
		setInitialWidth(500);
		setResizable(false);

		setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.addComponent(BookmarkFolderToevoegenModalWindow.this.getParent());
			}

		});
	}

	@Override
	protected CobraModalWindowBasePanel<BookmarkFolder> createContents(String id)
	{
		return new BookmarkFolderToevoegenPanel(id, BookmarkFolderToevoegenModalWindow.this, folder);
	}

	public void setFolder(BookmarkFolder folder)
	{
		if (folder != null)
			this.folder =
				ModelFactory.getCompoundChangeRecordingModel(folder, new DefaultModelManager(
					BookmarkFolder.class));
		else
			this.folder = null;
	}

	public IChangeRecordingModel<BookmarkFolder> getFolder()
	{
		return folder;
	}
}
