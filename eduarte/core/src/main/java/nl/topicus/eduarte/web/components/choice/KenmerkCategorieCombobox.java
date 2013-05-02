/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.kenmerk.KenmerkCategorie;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.CodeNaamActiefZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author loite
 */
public class KenmerkCategorieCombobox extends AbstractAjaxDropDownChoice<KenmerkCategorie>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<KenmerkCategorie>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<KenmerkCategorie> load()
		{
			CodeNaamActiefZoekFilter<KenmerkCategorie> filter =
				new CodeNaamActiefZoekFilter<KenmerkCategorie>(KenmerkCategorie.class);
			filter.setActief(Boolean.TRUE);
			filter.addOrderByProperty("naam");

			return DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(filter);
		}
	}

	public KenmerkCategorieCombobox(String id)
	{
		this(id, null);
	}

	public KenmerkCategorieCombobox(String id, IModel<KenmerkCategorie> model)
	{
		super(id, model, new ListModel(), new EntiteitToStringRenderer());
	}

}
