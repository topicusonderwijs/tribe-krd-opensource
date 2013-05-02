package nl.topicus.eduarte.web.components.panels.sidebar;

import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.web.components.datapanel.CollapsableGroupRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxButtonColumn;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.app.sidebar.datastores.AbstractSideBarDataStore;
import nl.topicus.eduarte.dao.helpers.BookmarkDataAccessHelper;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.Bookmark.SoortBookmark;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BookmarkTable;
import nl.topicus.eduarte.web.components.panels.sidebar.BookmarkUtil.GotoBookmarkException;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.home.HomePage;
import nl.topicus.eduarte.zoekfilters.BookmarkZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * Sidebar component waarin persoonlijke bookmarks getoond worden.
 * 
 * @author loite
 */
public class BookmarksSideBar extends AbstractSideBar
{
	private static final long serialVersionUID = 1L;

	private final BookmarkToevoegenModalWindow toevoegenWindow;

	public BookmarksSideBar(String id, final SecurePage page)
	{
		super(id, page);
		toevoegenWindow = new BookmarkToevoegenModalWindow("toevoegenWindow", page, null);

		add(createDataPanel(createProvider(), createTable(), page));

		add(toevoegenWindow);
		AjaxLink<Void> toevoegen = new AjaxLink<Void>("bookmarkToevoegen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				toevoegenWindow.setFolder(null);
				toevoegenWindow.show(target);
			}

		};
		toevoegen.setVisible(page.supportsBookmarks());
		add(toevoegen);

		final BookmarkFoldersBeherenModalWindow foldersBeherenWindow =
			new BookmarkFoldersBeherenModalWindow("foldersBeherenWindow", page);
		add(foldersBeherenWindow);
		AjaxLink<Void> beherenLink = new AjaxLink<Void>("foldersBeherenLink")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				foldersBeherenWindow.show(target);
			}

		};
		add(beherenLink);

	}

	private IDataProvider<Bookmark> createProvider()
	{
		BookmarkZoekFilter filter = new BookmarkZoekFilter();
		filter.setAccount(EduArteContext.get().getAccount());
		filter.setSoort(SoortBookmark.Bookmark);
		return GeneralFilteredSortableDataProvider.of(filter, BookmarkDataAccessHelper.class);
	}

	private BookmarkTable createTable()
	{

		BookmarkTable bookmarkTable = new BookmarkTable()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public GroupProperty<Bookmark> getDefaultGroupProperty()
			{
				GroupProperty<Bookmark> gp =
					new GroupProperty<Bookmark>("bookmarkFolder", "Map", "bookmarkFolder.volgorde");
				gp.addColumn(new AjaxButtonColumn<Bookmark>("Favoriet aan deze map toevoegen", "")
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
						return "newItem_grey";
					}

					@Override
					protected String getCssEnabled()
					{
						return "newItem";
					}

					@Override
					public void onClick(WebMarkupContainer item, IModel<Bookmark> rowModel,
							AjaxRequestTarget target)
					{
						Bookmark bookmark = rowModel.getObject();
						if (bookmark != null)
						{
							BookmarkFolder folder = bookmark.getBookmarkFolder();
							toevoegenWindow.setFolder(folder);
							toevoegenWindow.show(target);
						}
					}
				});
				gp.setAscending(false);
				return gp;
			}
		};
		bookmarkTable.addColumn(new AjaxButtonColumn<Bookmark>("Map verwijderen", "")
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
			public void onClick(WebMarkupContainer item, IModel<Bookmark> rowModel,
					AjaxRequestTarget target)
			{
				EduArteRequestCycle.get().openAndPushHibernateSession();
				try
				{
					Bookmark bookmark = rowModel.getObject();
					bookmark.delete();
					bookmark.commit();
				}
				finally
				{
					EduArteRequestCycle.get().closeAndPopHibernateSession();
				}
				target.addComponent(BookmarksSideBar.this.getParent().getParent());
			}
		});
		return bookmarkTable;
	}

	private EduArteDataPanel<Bookmark> createDataPanel(IDataProvider<Bookmark> dataProvider,
			BookmarkTable bookmarkTable, final SecurePage page)
	{
		final EduArteDataPanel<Bookmark> datapanel =
			new EduArteDataPanel<Bookmark>("favorieten", dataProvider, bookmarkTable)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onAfterPopulateItem(Item<Bookmark> item)
				{
					WebMarkupContainer wmc = ((WebMarkupContainer) item.get("groepHeaderRow"));
					if (wmc != null)
					{
						Label l = (Label) wmc.get("columnHeaderCaption");
						if (l != null && l.getDefaultModelObject() != null)
						{
							String label = l.getDefaultModelObjectAsString();
							l.setDefaultModelObject(label.substring(0, label.lastIndexOf("(")));
						}
					}
				}
			};
		datapanel.setButtonsVisible(false);
		datapanel.setTitleVisible(false);
		CollapsableGroupRowFactoryDecorator<Bookmark> decorator =
			new CollapsableGroupRowFactoryDecorator<Bookmark>(
				new CustomDataPanelPageLinkRowFactory<Bookmark>(HomePage.class)
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onClick(Item<Bookmark> item)
					{
						Bookmark bookmark = item.getModelObject();
						try
						{
							setResponsePage(new BookmarkUtil().goToBookmark(bookmark,
								(SecurePage) getPage()));
						}
						catch (GotoBookmarkException e)
						{
							error(e.getMessage());
						}
					}

					@Override
					public boolean isEnabled(Item<Bookmark> item)
					{
						return super.isEnabled(item) && !page.isEditable();
					}
				});
		decorator.setDefaultExpanded(false);
		datapanel.setRowFactory(decorator);
		return datapanel;
	}

	@Override
	protected Class< ? extends AbstractSideBarDataStore> getDataStoreClass()
	{
		return null;
	}
}
