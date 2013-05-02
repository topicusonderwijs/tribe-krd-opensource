/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.listview;

import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.security.TargetBasedSecurityCheck;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public abstract class ClickableIdObjectListView<T extends IdObject> extends IdObjectListView<T>
		implements IClickableListView<T>
{
	private static final long serialVersionUID = 1L;

	public ClickableIdObjectListView(String id)
	{
		super(id);
	}

	public ClickableIdObjectListView(String id, IModel<List<T>> listModel)
	{
		super(id, listModel);
	}

	public abstract void onClick(ListItem<T> item);

	@Override
	protected ListItem<T> newItem(int index)
	{
		return new LinkItem<T>(index, getListItemModel(getModel(), index))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return super.isEnabled() && ClickableIdObjectListView.this.isEnabled();
			}
		};
	}

	public static class SecureClickableAlwaysVisibleItem<T extends IdObject> extends LinkItem<T>
	{
		private static final long serialVersionUID = 1L;

		public SecureClickableAlwaysVisibleItem(int index, IModel<T> model,
				Class< ? extends Page> clickTarget)
		{
			super(index, model);
			TargetBasedSecurityCheck check = new TargetBasedSecurityCheck(this, clickTarget);
			check.setUseAlternativeRenderCheck(true);
			setSecurityCheck(check);
		}
	}

}
