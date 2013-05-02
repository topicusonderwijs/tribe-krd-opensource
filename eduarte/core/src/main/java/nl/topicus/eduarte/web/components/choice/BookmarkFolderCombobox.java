/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.BookmarkFolderDataAccessHelper;
import nl.topicus.eduarte.entities.sidebar.BookmarkFolder;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author niesink
 */
public class BookmarkFolderCombobox extends AbstractAjaxDropDownChoice<BookmarkFolder>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<BookmarkFolder>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<BookmarkFolder> load()
		{

			return DataAccessRegistry.getHelper(BookmarkFolderDataAccessHelper.class).list(
				EduArteContext.get().getAccount());
		}
	}

	public BookmarkFolderCombobox(String id)
	{
		this(id, null);
	}

	public BookmarkFolderCombobox(String id, IModel<BookmarkFolder> model)
	{
		super(id, model, new ListModel(), new EntiteitToStringRenderer());
	}

}
