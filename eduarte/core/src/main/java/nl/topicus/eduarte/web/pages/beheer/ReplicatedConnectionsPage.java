/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.beheer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.core.principals.beheer.systeem.Databaseconnecties;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author loite
 */
@PageInfo(title = "Databaseconnecties", menu = {"Beheer > Systeem > Databaseconnecties"})
@InPrincipal(Databaseconnecties.class)
public class ReplicatedConnectionsPage extends AbstractBeheerPage<Void>
{
	private static final long serialVersionUID = 1L;

	private static final class ConnectionDataPanelContentDescription extends
			CustomDataPanelContentDescription<Connection>
	{
		private static final long serialVersionUID = 1L;

		private ConnectionDataPanelContentDescription()
		{
			super("Databaseconnecties");
			addColumn(new CustomPropertyColumn<Connection>("Type", "Type", "class.name"));
			addColumn(new CustomPropertyColumn<Connection>("In queue", "In queue", "queueLengths"));
		}
	}

	/**
	 * Constructor
	 */
	public ReplicatedConnectionsPage()
	{
		super(BeheerMenuItem.Databaseconnecties);
		EduArteDataPanel<Connection> datapanel =
			new EduArteDataPanel<Connection>("connections", new CollectionDataProvider<Connection>(
				new LoadableDetachableModel<List<Connection>>()
				{
					private static final long serialVersionUID = 1L;

					@SuppressWarnings("unchecked")
					@Override
					public List<Connection> load()
					{
						List<Connection> connections = new ArrayList<Connection>();
						Class< ? > clazz;
						try
						{
							clazz = Class.forName("nl.topicus.grimstad.ReplicatedOracleDriver");
							Field field = clazz.getField("INSTANCE");
							Object instance = field.get(null);
							Method connectionsMethod =
								instance.getClass().getMethod("getConnections");
							connections = (List<Connection>) connectionsMethod.invoke(instance);
						}
						catch (ClassNotFoundException e)
						{
						}
						catch (SecurityException e)
						{
						}
						catch (NoSuchFieldException e)
						{
						}
						catch (IllegalArgumentException e)
						{
						}
						catch (IllegalAccessException e)
						{
						}
						catch (NoSuchMethodException e)
						{
						}
						catch (InvocationTargetException e)
						{
						}
						return connections;
					}

				}, true), new ConnectionDataPanelContentDescription());
		add(datapanel);
		createComponents();
	}
}
