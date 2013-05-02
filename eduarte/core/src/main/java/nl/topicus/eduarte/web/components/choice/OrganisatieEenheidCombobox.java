/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.app.security.models.OrganisatieEenhedenPageModel;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.LocatieProvider;
import nl.topicus.eduarte.providers.OrganisatieEenheidProvider;
import nl.topicus.eduarte.web.components.choice.renderer.OrganisatieEenheidRenderer;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.ComponentSecurityCheck;

public class OrganisatieEenheidCombobox extends AbstractAjaxDropDownChoice<OrganisatieEenheid>
		implements OrganisatieEenheidProvider
{
	private static final long serialVersionUID = 1L;

	public OrganisatieEenheidCombobox(String id)
	{
		this(id, null, null, null);
	}

	public OrganisatieEenheidCombobox(String id, IModel<OrganisatieEenheid> model)
	{
		this(id, model, null, null);
	}

	public OrganisatieEenheidCombobox(String id, IModel<OrganisatieEenheid> model,
			LocatieProvider locatieProvider, IModel< ? > filterEntiteitModel)
	{
		super(id, model, (IModel<List<OrganisatieEenheid>>) null, new OrganisatieEenheidRenderer());
		setChoices(new OrganisatieEenhedenPageModel(new ComponentSecurityCheck(this),
			locatieProvider, filterEntiteitModel));
	}

	@Override
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return getModelObject();
	}

	public void addUpdateComponent(Component component)
	{
		getComponents().add(component);
	}

	@Override
	protected void onError(AjaxRequestTarget target, RuntimeException e)
	{
		super.onError(target, e);
		setModelObject(null);
	}
}