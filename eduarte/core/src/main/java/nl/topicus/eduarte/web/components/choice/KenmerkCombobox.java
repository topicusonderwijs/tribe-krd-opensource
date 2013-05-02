/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.KenmerkDataAccessHelper;
import nl.topicus.eduarte.entities.kenmerk.Kenmerk;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.KenmerkZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author loite
 */
public class KenmerkCombobox extends AbstractAjaxDropDownChoice<Kenmerk>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Kenmerk>>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<KenmerkCategorie> categorieModel;

		public ListModel(IModel<KenmerkCategorie> categorieModel)
		{
			this.categorieModel = categorieModel;
		}

		@Override
		protected List<Kenmerk> load()
		{
			KenmerkZoekFilter filter = new KenmerkZoekFilter();
			filter.setActief(Boolean.TRUE);
			filter.setCategorie(categorieModel.getObject());
			filter.addOrderByProperty("naam");
			return DataAccessRegistry.getHelper(KenmerkDataAccessHelper.class).list(filter);
		}
	}

	public KenmerkCombobox(String id, IModel<KenmerkCategorie> categorieModel)
	{
		this(id, null, categorieModel);
	}

	public KenmerkCombobox(String id, IModel<Kenmerk> model, IModel<KenmerkCategorie> categorieModel)
	{
		super(id, model, new ListModel(categorieModel), new EntiteitToStringRenderer());
	}
}
