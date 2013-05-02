/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.models;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.security.actions.ActionFactory;
import org.apache.wicket.security.checks.ISecurityCheck;

/**
 * Een model om de lijst met organisatie-eenheden en locaties op te leveren waar de
 * gebruiker recht op heeft.
 * 
 * @author papegaaij
 */
public class OrganisatieEenhedenLocatiesModel extends
		LoadableDetachableModel<List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >>>
		implements IOrganisatieEenhedenLocatiesModel
{
	private static final long serialVersionUID = 1L;

	private Boolean instellingClearance;

	private Boolean organisatieEenheidClearance;

	private ISecurityCheck securityCheck;

	public OrganisatieEenhedenLocatiesModel(ISecurityCheck securityCheck)
	{
		this.securityCheck = securityCheck;
	}

	@Override
	protected List< ? extends IOrganisatieEenheidLocatieKoppelEntiteit< ? >> load()
	{
		if (!isInstellingClearance())
		{
			return EduArteContext.get().getOrganisatieEenheidLocatieVanAccount()
				.getOrganisatieEenheidLocatieKoppelingen();
		}

		List<OrganisatieEenheidLocatieKoppeling> res =
			new ArrayList<OrganisatieEenheidLocatieKoppeling>();
		res.add(new OrganisatieEenheidLocatieKoppeling(DataAccessRegistry.getHelper(
			OrganisatieEenheidDataAccessHelper.class).getRootOrganisatieEenheid(), null));
		return res;
	}

	public boolean isInstellingClearance()
	{
		ActionFactory actionFactory = EduArteApp.get().getActionFactory();
		if (instellingClearance == null)
			instellingClearance =
				securityCheck.isActionAuthorized(actionFactory.getAction(Instelling.class));
		return instellingClearance;
	}

	public boolean isOrganisatieEenheidClearance()
	{
		ActionFactory actionFactory = EduArteApp.get().getActionFactory();
		if (organisatieEenheidClearance == null)
			organisatieEenheidClearance =
				securityCheck.isActionAuthorized(actionFactory
					.getAction(nl.topicus.eduarte.app.security.actions.OrganisatieEenheid.class));
		return organisatieEenheidClearance;
	}
}
