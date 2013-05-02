/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.OpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.OpleidingZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author hoeve
 */
public class OpleidingCombobox extends AbstractAjaxDropDownChoice<Opleiding>
{
	private static final long serialVersionUID = 1L;

	public OpleidingCombobox(String id)
	{
		this(id, null, new OpleidingZoekFilter());
	}

	public OpleidingCombobox(String id, IModel<Opleiding> model, OpleidingZoekFilter filter)
	{
		super(id, model, new ChoicesModel(filter));
	}

	public OpleidingCombobox(String id, IModel<Opleiding> model,
			IModel< ? extends List< ? extends Opleiding>> choices)
	{
		super(id, model, choices, new EntiteitToStringRenderer());
	}

	public OpleidingCombobox(String id, OpleidingZoekFilter filter)
	{
		this(id, null, new ChoicesModel(filter));
	}

	private final static class ChoicesModel extends LoadableDetachableModel<List<Opleiding>>
	{
		private static final long serialVersionUID = 1L;

		private OpleidingZoekFilter filter;

		private ChoicesModel(OpleidingZoekFilter filter)
		{
			this.filter = filter == null ? new OpleidingZoekFilter() : filter;
		}

		@Override
		protected List<Opleiding> load()
		{
			return DataAccessRegistry.getHelper(OpleidingDataAccessHelper.class).list(filter);
		}
	}

	public void addUpdateComponent(Component component)
	{
		getComponents().add(component);
	}

}
