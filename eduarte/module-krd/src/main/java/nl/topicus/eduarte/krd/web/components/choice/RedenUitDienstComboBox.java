/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.RedenUitDienstDataAccessHelper;
import nl.topicus.eduarte.entities.personen.RedenUitDienst;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.RedenUitDienstZoekFilter;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author hoeve
 */
public class RedenUitDienstComboBox extends DropDownChoice<RedenUitDienst>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<RedenUitDienst>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<RedenUitDienst> load()
		{
			return DataAccessRegistry.getHelper(RedenUitDienstDataAccessHelper.class).list(
				new RedenUitDienstZoekFilter());
		}

	}

	public RedenUitDienstComboBox(String id)
	{
		this(id, null);
	}

	public RedenUitDienstComboBox(String id, IModel<RedenUitDienst> model)
	{
		super(id, model, new ListModel(), new EntiteitToStringRenderer());
	}
}
