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
import nl.topicus.eduarte.app.security.actions.Uitvoerend;
import nl.topicus.eduarte.app.security.actions.Verantwoordelijk;
import nl.topicus.eduarte.dao.helpers.DBSMedewerkerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.DeelnemerDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;

import org.apache.wicket.Component;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;

/**
 * @author marrink
 */
@Actions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class, Docent.class,
	Verantwoordelijk.class, Uitvoerend.class})
public class DeelnemerSecurityCheck extends EduArteSecurityCheck
{
	private static final long serialVersionUID = 1L;

	private Long id;

	public DeelnemerSecurityCheck(ISecurityCheck wrapped, DeelnemerProvider deelnemer)
	{
		this(wrapped, deelnemer.getDeelnemer());
	}

	public DeelnemerSecurityCheck(ISecurityCheck wrapped, Deelnemer deelnemer)
	{
		super(EduArteApp.get().getActionFactory().getEduArteInstellingsActions(), wrapped);
		this.id = deelnemer == null ? null : deelnemer.getId();
	}

	/**
	 * De deelnemer waar deze check op werkt.
	 * 
	 * @return deelnemer
	 */
	protected final Deelnemer getDeelnemer()
	{
		return DataAccessRegistry.getHelper(DeelnemerDataAccessHelper.class).get(id);
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
		Deelnemer deelnemer = getDeelnemer();
		Medewerker medewerker = getMedewerker();

		if (action.implies(getAction(Instelling.class)))
			return true;

		if (action.implies(getAction(OrganisatieEenheid.class)) && medewerker != null)
			return OrganisatieEenheidLocatieUtil.gelijkeOrganisatieEenheidLocatie(medewerker,
				deelnemer);

		if (action.implies(getAction(Begeleider.class)) && medewerker != null)
			return DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).isBegeleiderVan(
				medewerker, deelnemer, TimeUtil.getInstance().currentDate());

		if (action.implies(getAction(Docent.class)) && medewerker != null)
			return DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).isDocentVan(
				medewerker, deelnemer, TimeUtil.getInstance().currentDate());

		if (action.implies(getAction(Verantwoordelijk.class)) && medewerker != null)
			return DataAccessRegistry
				.getHelper(DBSMedewerkerDataAccessHelper.class)
				.isVerantwoordelijkeVan(medewerker, deelnemer, TimeUtil.getInstance().currentDate());

		if (action.implies(getAction(Uitvoerend.class)) && medewerker != null)
			return DataAccessRegistry.getHelper(DBSMedewerkerDataAccessHelper.class)
				.isUitvoerendeVan(medewerker, deelnemer, TimeUtil.getInstance().currentDate());

		return false;
	}

	/**
	 * Vervangt de huidige securitycheck van de component door een nieuwe
	 * deelnemersecuritycheck.
	 * 
	 * @param component
	 * @param deelnemer
	 * @return de component
	 */
	public static final Component replaceSecurityCheck(Component component, Deelnemer deelnemer)
	{
		return ComponentUtil.setSecurityCheck(component, new DeelnemerSecurityCheck(ComponentUtil
			.getSecurityCheck(component), deelnemer));
	}

	/**
	 * Vervangt de huidige securitycheck van de component door een nieuwe
	 * deelnemersecuritycheck.
	 * 
	 * @param component
	 * @param deelnemer
	 * @param useAlternativeRendering
	 * @return de component
	 */
	public static final Component replaceSecurityCheck(Component component, Deelnemer deelnemer,
			boolean useAlternativeRendering)
	{
		DeelnemerSecurityCheck check =
			new DeelnemerSecurityCheck(ComponentUtil.getSecurityCheck(component), deelnemer);
		check.setUseAlternativeRenderCheck(useAlternativeRendering);
		return ComponentUtil.setSecurityCheck(component, check);
	}
}
