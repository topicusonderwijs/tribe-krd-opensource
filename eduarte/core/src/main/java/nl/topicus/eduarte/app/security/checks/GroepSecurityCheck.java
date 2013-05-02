/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.checks;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.GroepProvider;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;

import org.apache.wicket.Component;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;

/**
 * @author hoeve
 */
@Actions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class, Docent.class})
public class GroepSecurityCheck extends EduArteSecurityCheck
{
	private static final long serialVersionUID = 1L;

	private Long id;

	public GroepSecurityCheck(ISecurityCheck wrapped, GroepProvider provider)
	{
		this(wrapped, provider.getGroep());
	}

	/**
	 * @param wrapped
	 * @param groep
	 */
	public GroepSecurityCheck(ISecurityCheck wrapped, Groep groep)
	{
		super(EduArteApp.get().getActionFactory().getEduArteInstellingsActions(), wrapped);
		this.id = groep == null ? null : groep.getId();
	}

	/**
	 * De groep waar deze check op werkt.
	 * 
	 * @return groep
	 */
	protected final Groep getGroep()
	{
		return DataAccessRegistry.getHelper(GroepDataAccessHelper.class).get(id);
	}

	@Override
	protected boolean isEntitySet()
	{
		return id != null;
	}

	/**
	 * @see nl.topicus.eduarte.app.security.checks.EduArteSecurityCheck#verify(org.apache.wicket.security.actions.WaspAction)
	 */
	@Override
	protected final boolean verify(WaspAction action)
	{
		Groep groep = getGroep();
		if (action.implies(getAction(Instelling.class)))
			return true;
		if (action.implies(getAction(OrganisatieEenheid.class)))
		{
			return OrganisatieEenheidLocatieUtil.hoortBij(OrganisatieEenheidLocatieUtil
				.flatten(getMedewerker().getOrganisatieEenheidLocatieKoppelingen()), groep);
		}
		if (action.implies(getAction(Begeleider.class)))
		{
			Medewerker medewerker = getMedewerker();
			if (medewerker == null)
				return false;
			return DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).isBegeleiderVan(
				medewerker, null, TimeUtil.getInstance().currentDate());
		}
		if (action.implies(getAction(Docent.class)))
		{
			Medewerker medewerker = getMedewerker();
			if (medewerker == null)
				return false;
			return DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).isDocentVan(
				medewerker, null, TimeUtil.getInstance().currentDate());
		}
		return false;
	}

	/**
	 * Vervangt de huidige securitycheck van de component door een nieuwe
	 * groepsecuritycheck.
	 * 
	 * @param component
	 * @param groep
	 * @return de component
	 */
	public static final Component replaceSecurityCheck(Component component, Groep groep)
	{
		return ComponentUtil.setSecurityCheck(component, new GroepSecurityCheck(ComponentUtil
			.getSecurityCheck(component), groep));
	}
}
