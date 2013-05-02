/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels;

import java.util.List;

import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.IContactgegevenEntiteit;
import nl.topicus.eduarte.web.components.datapanel.ContactgegevenWaardePropertyColumn;

import org.apache.wicket.model.IModel;

/**
 * Panel welke een standaard datapanel toont met de gekoppelde
 * {@link IContactgegevenEntiteit}en
 * 
 * @author hoeve
 */
public class ContactgegevenEntiteitPanel<T extends IContactgegevenEntiteit> extends
		TypedPanel<List<T>>
{
	private static final long serialVersionUID = 1L;

	public ContactgegevenEntiteitPanel(String id, IModel<List<T>> contactgegevensModel,
			boolean verbergGeheim)
	{
		super(id, contactgegevensModel);

		CustomDataPanel<T> data =
			new EduArteDataPanel<T>("gegevens", new ListModelDataProvider<T>(contactgegevensModel),
				new ContactgevenEntiteitTable("Contactgegevens", verbergGeheim));

		add(data);
	}

	private class ContactgevenEntiteitTable extends CustomDataPanelContentDescription<T>
	{
		private static final long serialVersionUID = 1L;

		public ContactgevenEntiteitTable(String title, boolean verbergGeheim)
		{
			super(title);

			addColumn(new CustomPropertyColumn<T>("Omschrijving", "Omschrijving",
				"soortContactgegeven.naam", "soortContactgegeven.naam"));
			addColumn(new ContactgegevenWaardePropertyColumn<T>("Waarde", "Waarde",
				"contactgegeven", "contactgegeven", verbergGeheim));
			addColumn(new BooleanPropertyColumn<T>("Geheim", "Geheim", "geheim", "geheim"));
		}
	}

	public List<T> getContactGegevens()
	{
		return getModelObject();
	}
}
