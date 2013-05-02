/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.checks;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.models.OrganisatieEenheidLocatieKoppeling;
import nl.topicus.eduarte.dao.helpers.LocatieDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;

import org.apache.wicket.Component;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;

/**
 * Securitycheck die controleerd of de gebruiker voldoende rechten voor een
 * Organisatie-eenheid-locatie heeft.
 * 
 * @author marrink
 */
@Actions( {Instelling.class, nl.topicus.eduarte.app.security.actions.OrganisatieEenheid.class})
public class OrganisatieEenheidLocatieSecurityCheck extends EduArteSecurityCheck implements
		OrganisatieEenheidLocatieProvider
{
	private static final long serialVersionUID = 1L;

	private Long orgEhdId;

	private Long locatieId;

	public OrganisatieEenheidLocatieSecurityCheck(ISecurityCheck wrapped,
			OrganisatieEenheidLocatieProvider provider)
	{
		this(wrapped, provider.getOrganisatieEenheid(), provider.getLocatie());
	}

	@SuppressWarnings("unchecked")
	public OrganisatieEenheidLocatieSecurityCheck(ISecurityCheck wrapped,
			OrganisatieEenheid eenheid, Locatie locatie)
	{
		super(wrapped, Instelling.class,
			nl.topicus.eduarte.app.security.actions.OrganisatieEenheid.class);
		orgEhdId = eenheid == null ? null : eenheid.getId();
		locatieId = locatie == null ? null : locatie.getId();
	}

	@Override
	public final OrganisatieEenheid getOrganisatieEenheid()
	{
		return DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class).get(
			OrganisatieEenheid.class, orgEhdId);
	}

	@Override
	public final Locatie getLocatie()
	{
		return locatieId == null ? null : DataAccessRegistry.getHelper(
			LocatieDataAccessHelper.class).get(Locatie.class, locatieId);
	}

	@Override
	protected boolean isEntitySet()
	{
		return orgEhdId != null;
	}

	@Override
	protected boolean verify(WaspAction action)
	{
		if (action.implies(getAction(Instelling.class)))
			return true;

		if (action
			.implies(getAction(nl.topicus.eduarte.app.security.actions.OrganisatieEenheid.class)))
		{
			IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > account =
				getOrganisatieEenheidLocatieVanAccount();
			if (account == null)
				return false;

			List<OrganisatieEenheidLocatieKoppeling> flatList =
				OrganisatieEenheidLocatieUtil.flatten(account
					.getOrganisatieEenheidLocatieKoppelingen());

			if (OrganisatieEenheidLocatieUtil.hoortBij(flatList, this))
				return true;

			if (!isEditTarget())
				return OrganisatieEenheidLocatieUtil.isGekoppeldAanParent(flatList, this);
		}
		return false;
	}

	/**
	 * Vervangt de huidige {@link ISecurityCheck} van de component door een nieuwe
	 */
	public static Component replaceSecurityCheck(Component target, OrganisatieEenheid eenheid,
			Locatie locatie)
	{
		return ComponentUtil.setSecurityCheck(target, new OrganisatieEenheidLocatieSecurityCheck(
			ComponentUtil.getSecurityCheck(target), eenheid, locatie));
	}
}
