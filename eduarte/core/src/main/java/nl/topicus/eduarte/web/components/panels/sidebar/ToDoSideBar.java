package nl.topicus.eduarte.web.components.panels.sidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.link.ConfirmationAjaxLink;
import nl.topicus.cobra.web.components.listview.CompoundIdObjectListView;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.app.sidebar.datastores.AbstractSideBarDataStore;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.BookmarkConstructorArgument;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.entities.sidebar.Bookmark.SoortBookmark;
import nl.topicus.eduarte.util.XmlSerializer;
import nl.topicus.eduarte.web.components.panels.sidebar.BookmarkUtil.GotoBookmarkException;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;

/**
 * Sidebar component waarin persoonlijke 'Te doen's' getoond worden.
 * 
 * @author vandekamp
 */
public class ToDoSideBar extends AbstractSideBar
{
	private static final long serialVersionUID = 1L;

	public ToDoSideBar(String id, final SecurePage page)
	{
		super(id, page);
		final CompoundIdObjectListView<Bookmark> listview =
			new CompoundIdObjectListView<Bookmark>("listview", new BookmarksModel(page,
				SoortBookmark.ToDo))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(final ListItem<Bookmark> item)
				{
					Link<Void> link = new Link<Void>("link")
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick()
						{
							Bookmark bookmark = item.getModelObject();
							try
							{
								setResponsePage(new BookmarkUtil().goToBookmark(bookmark, page));
							}
							catch (GotoBookmarkException e)
							{
								error(e.getMessage());
							}
						}

						@Override
						public boolean isEnabled()
						{
							return super.isEnabled() && !page.isEditable();
						}
					};
					link.add(ComponentFactory.getDataLabel("omschrijving"));
					item.add(link);
					ConfirmationAjaxLink<Void> verwijderen =
						new ConfirmationAjaxLink<Void>("verwijderen",
							"Weet u zeker dat u deze Te doen wilt verwijderen?")
						{
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target)
							{
								EduArteRequestCycle.get().openAndPushHibernateSession();
								try
								{
									Bookmark bookmark = item.getModelObject();
									bookmark.delete();
									bookmark.commit();
								}
								finally
								{
									EduArteRequestCycle.get().closeAndPopHibernateSession();
								}
								target.addComponent(ToDoSideBar.this.getParent());
							}

						};
					item.add(verwijderen);
				}
			};
		add(listview);

		GeenElementenPanel geenElementen =
			new GeenElementenPanel("geen", "Geen 'Te doen' toegevoegd", 2)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return listview.size() == 0;
				}
			};
		add(geenElementen);

		AjaxLink<Void> toevoegen = new AjaxLink<Void>("todoToevoegen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				createBookmark(page, target);

			}
		};
		toevoegen.setVisible(page.supportsBookmarks());
		add(toevoegen);
	}

	protected void createBookmark(SecurePage page, AjaxRequestTarget target)
	{
		EduArteRequestCycle.get().openAndPushHibernateSession();
		try
		{
			List<Class< ? >> types = new ArrayList<Class< ? >>();
			List<Object> values = new ArrayList<Object>();
			page.getBookmarkConstructorArguments(types, values);
			List<Class< ? extends IContextInfoObject>> contextParameterTypes =
				Collections.emptyList();
			List<String> serializedValues = XmlSerializer.serializeValues(values);

			Bookmark todo = new Bookmark();
			todo.setSoort(SoortBookmark.ToDo);
			todo.setPageClass(page.getClass().getName());
			todo.setPagePrivate(false);
			todo.setOmschrijving(getOmschrijving(page));
			todo.setAccount(EduArteContext.get().getAccount());
			List<BookmarkConstructorArgument> arguments =
				todo.createBookmarkConstructorArguments(types, serializedValues,
					contextParameterTypes);
			todo.setArguments(arguments);
			todo.save();
			for (BookmarkConstructorArgument arg : arguments)
			{
				arg.save();
			}
			todo.commit();
			target.addComponent(page.get(SecurePage.ID_LAYSIDE));
		}
		finally
		{
			EduArteRequestCycle.get().closeAndPopHibernateSession();
		}

	}

	private String getOmschrijving(SecurePage page)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(page.getClass().getSimpleName());
		builder.append(" - ");
		for (Class< ? extends IContextInfoObject> clazz : page.getContextParameterTypes())
		{
			IContextInfoObject obj = page.getContextValue(clazz);
			String value = obj == null ? null : obj.getContextInfoOmschrijving();
			if (value != null)
			{
				builder.append(value);
				builder.append(" - ");
			}
		}
		if (builder.length() > 0)
			builder.delete(builder.lastIndexOf(" - "), builder.length());
		String omschrijving = "";
		if (builder.length() > 100)
			omschrijving = builder.substring(0, 100);
		else
			omschrijving = builder.toString();
		return omschrijving;
	}

	@Override
	protected Class< ? extends AbstractSideBarDataStore> getDataStoreClass()
	{
		return null;
	}
}
