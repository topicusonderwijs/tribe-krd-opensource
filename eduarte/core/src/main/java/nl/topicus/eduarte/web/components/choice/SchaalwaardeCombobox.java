/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.SchaalwaardeDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.zoekfilters.SchaalwaardeZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author papegaaij
 */
public class SchaalwaardeCombobox extends AbstractAjaxDropDownChoice<Schaalwaarde>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Schaalwaarde>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Schaal> schaalModel;

		public ListModel(IModel<Schaal> schaalModel)
		{
			this.schaalModel = schaalModel;
		}

		@Override
		protected List<Schaalwaarde> load()
		{
			SchaalwaardeZoekFilter filter = new SchaalwaardeZoekFilter(schaalModel.getObject());
			return DataAccessRegistry.getHelper(SchaalwaardeDataAccessHelper.class).list(filter);
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			schaalModel.detach();
		}
	}

	public SchaalwaardeCombobox(String id, IModel<Schaalwaarde> model, IModel<Schaal> schaalModel)
	{
		super(id, model, new ListModel(schaalModel), new EntiteitPropertyRenderer("naam"));
	}
}
