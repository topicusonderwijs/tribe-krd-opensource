/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.SchooladviesDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Schooladvies;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.SchooladviesZoekFilter;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author hop
 */
public class SchooladviesComboBox extends DropDownChoice<Schooladvies>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Schooladvies>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Schooladvies> load()
		{
			return DataAccessRegistry.getHelper(SchooladviesDataAccessHelper.class).list(
				new SchooladviesZoekFilter());
		}
	}

	public SchooladviesComboBox(String id)
	{
		this(id, null);
	}

	public SchooladviesComboBox(String id, IModel<Schooladvies> model)
	{
		super(id, model, new ListModel(), new EntiteitToStringRenderer());
	}
}
