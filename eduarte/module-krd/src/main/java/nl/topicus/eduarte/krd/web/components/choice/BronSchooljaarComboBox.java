/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.krd.dao.helpers.BronSchooljaarStatusDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.zoekfilters.BronSchooljaarStatusZoekFilter;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class BronSchooljaarComboBox extends AbstractAjaxDropDownChoice<BronSchooljaarStatus>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends
			LoadableDetachableModel<List<BronSchooljaarStatus>>
	{
		private static final long serialVersionUID = 1L;

		private BronSchooljaarStatusZoekFilter filter;

		public ListModel(BronSchooljaarStatusZoekFilter filter)
		{
			this.filter = filter;
		}

		@Override
		protected List<BronSchooljaarStatus> load()
		{
			return DataAccessRegistry.getHelper(BronSchooljaarStatusDataAccessHelper.class).list(
				filter);
		}
	}

	public BronSchooljaarComboBox(String id, BronSchooljaarStatusZoekFilter filter)
	{
		this(id, null, filter);
	}

	public BronSchooljaarComboBox(String id, IModel<BronSchooljaarStatus> model,
			BronSchooljaarStatusZoekFilter filter)
	{
		super(id, model, new ListModel(filter), new EntiteitPropertyRenderer("schooljaar"));
	}
}
