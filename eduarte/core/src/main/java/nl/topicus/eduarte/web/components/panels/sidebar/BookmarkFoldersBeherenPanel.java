package nl.topicus.eduarte.web.components.panels.sidebar;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxClickableRowFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxButtonColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.dao.helpers.BookmarkFolderDataAccessHelper;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.zoekfilters.BookmarkFolderZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * Page voor BookmarkToevoegenModalWindow
 * 
 * @author loite
 */
public class BookmarkFoldersBeherenPanel extends ModalWindowBasePanel<BookmarkFolder>
{
	private static final long serialVersionUID = 1L;

	private final BookmarkFolderToevoegenModalWindow folderToevoegenWindow;

	// private Form form;

	public BookmarkFoldersBeherenPanel(String id, BookmarkFoldersBeherenModalWindow modalWindow)
	{
		super(id, modalWindow);

		folderToevoegenWindow =
			new BookmarkFolderToevoegenModalWindow("foldertoevoegenWindow", null);
		add(folderToevoegenWindow);

		BookmarkFolderZoekFilter filter = new BookmarkFolderZoekFilter();
		filter.setAccount(EduArteContext.get().getAccount());

		IDataProvider<BookmarkFolder> provider =
			GeneralFilteredSortableDataProvider.of(filter, BookmarkFolderDataAccessHelper.class);

		EduArteDataPanel<BookmarkFolder> datapanel =
			new EduArteDataPanel<BookmarkFolder>("favorietenmappen", provider, createTable());
		datapanel.setButtonsVisible(false);
		datapanel.setRowFactory(new CustomDataPanelAjaxClickableRowFactory<BookmarkFolder>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target, Item<BookmarkFolder> item)
			{
				folderToevoegenWindow.setFolder(item.getModelObject());
				folderToevoegenWindow.show(target);
			}
		});

		add(datapanel);

		createComponents();
	}

	public CustomDataPanelContentDescription<BookmarkFolder> createTable()
	{
		CustomDataPanelContentDescription<BookmarkFolder> table =
			new CustomDataPanelContentDescription<BookmarkFolder>("Favorietenmappen");
		table.addColumn(new CustomPropertyColumn<BookmarkFolder>("Naam", "Naam", "naam"));
		table.addColumn(new CustomPropertyColumn<BookmarkFolder>("Aantal favorieten",
			"Aantal favorieten", "bookmarks.size")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass()
			{
				return "unit_50";
			}
		});
		table.addColumn(new AjaxButtonColumn<BookmarkFolder>("Map omhoog", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass()
			{
				return "unit_20";
			}

			@Override
			protected String getCssDisabled()
			{
				return "";
			}

			@Override
			protected String getCssEnabled()
			{
				return "ui-icon ui-icon-circle-arrow-s";
			}

			@Override
			public void onClick(WebMarkupContainer item, IModel<BookmarkFolder> rowModel,
					AjaxRequestTarget target)
			{
				BookmarkFolder folder = rowModel.getObject();
				BookmarkFolder otherFolder =
					DataAccessRegistry.getHelper(BookmarkFolderDataAccessHelper.class).getVolgorde(
						folder.getVolgorde() - 1);

				int volg = folder.getVolgorde();
				folder.setVolgorde(volg - 1);
				otherFolder.setVolgorde(volg);

				folder.saveOrUpdate();
				folder.commit();
				otherFolder.saveOrUpdate();
				otherFolder.commit();
				target.addComponent(BookmarkFoldersBeherenPanel.this);
			}

			@Override
			public boolean isContentsEnabled(IModel<BookmarkFolder> rowModel)
			{
				BookmarkFolder folder = rowModel.getObject();
				return folder.getVolgorde() != 1;
			}
		});
		table.addColumn(new AjaxButtonColumn<BookmarkFolder>("Map omlaag", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass()
			{
				return "unit_20";
			}

			@Override
			protected String getCssDisabled()
			{
				return "";
			}

			@Override
			protected String getCssEnabled()
			{
				return "ui-icon ui-icon-circle-arrow-n";
			}

			@Override
			public void onClick(WebMarkupContainer item, IModel<BookmarkFolder> rowModel,
					AjaxRequestTarget target)
			{
				BookmarkFolder folder = rowModel.getObject();
				BookmarkFolder otherFolder =
					DataAccessRegistry.getHelper(BookmarkFolderDataAccessHelper.class).getVolgorde(
						folder.getVolgorde() + 1);

				int volg = folder.getVolgorde();
				folder.setVolgorde(volg + 1);
				otherFolder.setVolgorde(volg);

				folder.saveOrUpdate();
				folder.commit();
				otherFolder.saveOrUpdate();
				otherFolder.commit();
				target.addComponent(BookmarkFoldersBeherenPanel.this);
			}

			@Override
			public boolean isContentsEnabled(IModel<BookmarkFolder> rowModel)
			{
				BookmarkFolder folder = rowModel.getObject();
				return folder.getVolgorde() != DataAccessRegistry
					.getHelper(BookmarkFolderDataAccessHelper.class)
					.list(EduArteContext.get().getAccount()).size();
			}
		});

		table.addColumn(new AjaxButtonColumn<BookmarkFolder>("Map verwijderen", "")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getCssClass()
			{
				return "unit_20";
			}

			@Override
			protected String getCssDisabled()
			{
				return "deleteItem_grey";
			}

			@Override
			protected String getCssEnabled()
			{
				return "deleteItem";
			}

			@Override
			public void onClick(WebMarkupContainer item, IModel<BookmarkFolder> rowModel,
					AjaxRequestTarget target)
			{
				EduArteRequestCycle.get().openAndPushHibernateSession();
				try
				{
					BookmarkFolder folder = rowModel.getObject();
					for (Bookmark bookmark : folder.getBookmarks())
						bookmark.setBookmarkFolder(null);
					for (BookmarkFolder otherFolder : DataAccessRegistry.getHelper(
						BookmarkFolderDataAccessHelper.class).list(
						EduArteContext.get().getAccount()))
					{
						if (otherFolder.getVolgorde() > folder.getVolgorde())
							otherFolder.setVolgorde(otherFolder.getVolgorde() - 1);
						otherFolder.saveOrUpdate();
					}
					folder.delete();
					folder.commit();
				}
				finally
				{
					EduArteRequestCycle.get().closeAndPopHibernateSession();
				}
				target.addComponent(BookmarkFoldersBeherenPanel.this);
			}
		});

		return table;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		AjaxLink<Void> objectToevoegen = new AjaxLink<Void>("objectToevoegen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				folderToevoegenWindow.setFolder(null);
				folderToevoegenWindow.show(target);
			}
		};
		objectToevoegen.add(new Label("objectToevoegenLabel", "Map toevoegen"));
		add(objectToevoegen);

		panel.addButton(new AbstractAjaxLinkButton(panel, "Sluiten", CobraKeyAction.GEEN,
			ButtonAlignment.RIGHT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				ModalWindow.closeCurrent(target);
			}

		});
	}
}
