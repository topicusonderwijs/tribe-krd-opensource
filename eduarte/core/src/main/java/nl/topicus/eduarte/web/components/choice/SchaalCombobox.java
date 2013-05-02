/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.SchaalDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;
import nl.topicus.eduarte.zoekfilters.NaamActiefZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author papegaaij
 */
public class SchaalCombobox extends AbstractAjaxDropDownChoice<Schaal>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Schaal>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Schaal> load()
		{
			NaamActiefZoekFilter<Schaal> filter = new NaamActiefZoekFilter<Schaal>(Schaal.class);
			filter.addOrderByProperty("naam");
			filter.setActief(true);
			return DataAccessRegistry.getHelper(SchaalDataAccessHelper.class).list(filter);
		}

	}

	public SchaalCombobox(String id, IModel<Schaal> model)
	{
		super(id, model, new ListModel(), new EntiteitPropertyRenderer("naam"));
	}
}
