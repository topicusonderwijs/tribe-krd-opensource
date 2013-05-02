package nl.topicus.eduarte.web.components.panels.sidebar;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.dao.helpers.BookmarkFolderDataAccessHelper;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Page voor BookmarkToevoegenModalWindow
 * 
 * @author loite
 */
public class BookmarkFolderToevoegenPanel extends ModalWindowBasePanel<BookmarkFolder>
{
	private static final long serialVersionUID = 1L;

	private Form<BookmarkFolder> form;

	private IModel<BookmarkFolder> folderModel;

	public BookmarkFolderToevoegenPanel(String id, BookmarkFolderToevoegenModalWindow modalWindow,
			IChangeRecordingModel<BookmarkFolder> folder)
	{
		super(id, modalWindow);

		BookmarkFolder newBookmarkFolder;
		if (folder != null)
			newBookmarkFolder = folder.getObject();
		else
			newBookmarkFolder = new BookmarkFolder();

		folderModel =
			ModelFactory.getCompoundChangeRecordingModel(newBookmarkFolder, folder != null ? folder
				.getManager() : new DefaultModelManager(BookmarkFolder.class));

		form = new Form<BookmarkFolder>("form", folderModel);
		RequiredTextField<String> naam =
			new RequiredTextField<String>("naam", new PropertyModel<String>(folderModel, "naam"));
		ComponentUtil.fixLength(naam, BookmarkFolder.class);
		form.add(naam);
		add(form);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxOpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > aform)
			{
				refreshFeedback(target);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > aform)
			{
				try
				{
					BookmarkFolder bookmarkfolder = form.getModelObject();

					if (!bookmarkfolder.isSaved())
					{
						bookmarkfolder.setAccount(EduArteContext.get().getAccount());
						bookmarkfolder.setVolgorde(DataAccessRegistry.getHelper(
							BookmarkFolderDataAccessHelper.class).list(
							EduArteContext.get().getAccount()).size() + 1);
					}

					bookmarkfolder.saveOrUpdate();

					bookmarkfolder.commit();
				}
				finally
				{
					EduArteRequestCycle.get().closeAndPopHibernateSession();
				}
				getModalWindow().close(target);
			}
		}.setMakeDefault(true));
		panel.addButton(new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				getModalWindow().close(target);
			}
		});
	}
}
