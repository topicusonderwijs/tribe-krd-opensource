/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.beheer;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.hibernate.cache.CacheStatistics;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.core.principals.beheer.systeem.Caches;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author loite
 */
@PageInfo(title = "Caches", menu = {"Beheer > Systeem > Caches"})
@InPrincipal(Caches.class)
public class CachesPage extends AbstractBeheerPage<Void>
{
	private static final long serialVersionUID = 1L;

	private static final class CachesModel extends
			LoadableDetachableModel<List< ? extends CacheStatistics>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List< ? extends CacheStatistics> load()
		{
			return EduArteApp.get().getCacheImplStatistics();
		}

	}

	public CachesPage()
	{
		super(BeheerMenuItem.Caches);
		add(new ListView<CacheStatistics>("cache", new CachesModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<CacheStatistics> item)
			{
				CacheStatistics stats = item.getModelObject();
				if (stats != null)
				{
					item.add(ComponentFactory.getDataLabel("naam", stats.getName()));
					item.add(ComponentFactory.getDataLabel("elementsInMemory", String.valueOf(stats
						.getMemoryStoreCount())));
					item.add(ComponentFactory.getDataLabel("sizeInMemory", String.valueOf(stats
						.getMemoryStoreSize())));
					item.add(ComponentFactory.getDataLabel("hitCount", String.valueOf(stats
						.getCacheHits())));
					item.add(ComponentFactory.getDataLabel("missCount", String.valueOf(stats
						.getCacheMisses())));
				}
				else
				{
					item.add(ComponentFactory.getDataLabel("naam", ""));
					item.add(ComponentFactory.getDataLabel("elementsInMemory", ""));
					item.add(ComponentFactory.getDataLabel("sizeInMemory", ""));
					item.add(ComponentFactory.getDataLabel("hitCount", ""));
					item.add(ComponentFactory.getDataLabel("missCount", ""));
				}
			}
		});
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AbstractLinkButton(panel, "Caches leegmaken", CobraKeyAction.GEEN,
			ButtonAlignment.RIGHT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				EduArteApp.get().clearCacheImplSecondLevelCache();
			}

		});
	}
}
