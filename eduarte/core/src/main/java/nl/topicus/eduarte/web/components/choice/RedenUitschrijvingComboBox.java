/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.RedenUitschrijvingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter;
import nl.topicus.eduarte.zoekfilters.RedenUitschrijvingZoekFilter.SoortRedenUitschrijvingTonen;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author hoeve
 */
public class RedenUitschrijvingComboBox extends DropDownChoice<RedenUitschrijving>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<RedenUitschrijving>>
	{
		private static final long serialVersionUID = 1L;

		private SoortRedenUitschrijvingTonen soort;

		public ListModel(SoortRedenUitschrijvingTonen soort)
		{
			this.soort = soort;
		}

		@Override
		protected List<RedenUitschrijving> load()
		{
			RedenUitschrijvingZoekFilter filter = new RedenUitschrijvingZoekFilter();
			filter.addOrderByProperty("code");
			filter.setActief(true);
			filter.setSoort(soort);

			return DataAccessRegistry.getHelper(RedenUitschrijvingDataAccessHelper.class).list(
				filter);
		}
	}

	public RedenUitschrijvingComboBox(String id)
	{
		this(id, SoortRedenUitschrijvingTonen.Alle);
	}

	public RedenUitschrijvingComboBox(String id, SoortRedenUitschrijvingTonen soort)
	{
		this(id, null, soort);
	}

	public RedenUitschrijvingComboBox(String id, IModel<RedenUitschrijving> model,
			SoortRedenUitschrijvingTonen soort)
	{
		super(id, model, new ListModel(soort), new EntiteitToStringRenderer());
	}
}
