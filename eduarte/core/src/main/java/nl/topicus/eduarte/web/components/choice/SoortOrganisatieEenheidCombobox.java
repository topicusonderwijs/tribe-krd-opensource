/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.SoortOrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.SoortOrganisatieEenheid;
import nl.topicus.eduarte.providers.SoortOrganisatieEenheidProvider;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author hoeve
 */
public class SoortOrganisatieEenheidCombobox extends
		AbstractAjaxDropDownChoice<SoortOrganisatieEenheid> implements
		SoortOrganisatieEenheidProvider
{
	private static final long serialVersionUID = 1L;

	public SoortOrganisatieEenheidCombobox(String id, IModel<SoortOrganisatieEenheid> model)
	{
		super(id, model, new ChoicesModel(), new EntiteitToStringRenderer());
	}

	private static final class ChoicesModel extends
			LoadableDetachableModel<List<SoortOrganisatieEenheid>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<SoortOrganisatieEenheid> load()
		{
			return DataAccessRegistry.getHelper(SoortOrganisatieEenheidDataAccessHelper.class)
				.list(true);
		}
	}

	@Override
	public SoortOrganisatieEenheid getSoortOrganisatieEenheid()
	{
		return getModelObject();
	}

	public void addUpdateComponent(Component component)
	{
		getComponents().add(component);
	}
}
