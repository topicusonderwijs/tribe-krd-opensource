/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.checks;

import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.models.OrganisatieEenheidLocatieKoppeling;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.providers.MedewerkerProvider;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;

/**
 * Securitycheck die controleerd of de gebruiker voldoende rechten voor een andere
 * {@link Medewerker} heeft.
 * 
 * @author marrink
 */
@Actions( {Instelling.class, OrganisatieEenheid.class})
public class OrganisatieEenheidLocatieKoppelbaarSecurityCheck extends EduArteSecurityCheck
{
	private static final long serialVersionUID = 1L;

	private IModel< ? extends IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? >> entiteitModel;

	public OrganisatieEenheidLocatieKoppelbaarSecurityCheck(ISecurityCheck wrapped,
			MedewerkerProvider medewerker)
	{
		this(wrapped, (IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? >) medewerker
			.getMedewerker());
	}

	public OrganisatieEenheidLocatieKoppelbaarSecurityCheck(ISecurityCheck wrapped,
			CurriculumOnderwijsproduct curriculum)
	{
		this(wrapped, curriculum.getCurriculum().getOpleiding());
	}

	public OrganisatieEenheidLocatieKoppelbaarSecurityCheck(ISecurityCheck wrapped,
			Medewerker medewerker)
	{
		this(wrapped, (IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? >) medewerker);
	}

	public OrganisatieEenheidLocatieKoppelbaarSecurityCheck(ISecurityCheck wrapped,
			IOrganisatieEenheidLocatieKoppelEntiteit< ? > koppeling)
	{
		this(wrapped, koppeling.getEntiteit());
	}

	public OrganisatieEenheidLocatieKoppelbaarSecurityCheck(ISecurityCheck wrapped,
			IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > entiteit)
	{
		super(EduArteApp.get().getActionFactory().getEduArteInstellingsActions(), wrapped);
		if (!(entiteit instanceof Entiteit) || ((Entiteit) entiteit).isSaved())
		{
			entiteitModel = ModelFactory.getModel(entiteit);
			entiteitModel.detach();
		}
	}

	private IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > getEntiteit()
	{
		if (entiteitModel == null)
			return null;
		IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > ret = entiteitModel.getObject();
		entiteitModel.detach();
		return ret;
	}

	@Override
	protected boolean isEntitySet()
	{
		return getEntiteit() != null;
	}

	@Override
	protected final boolean verify(WaspAction action)
	{
		if (action.implies(getAction(Instelling.class)))
			return true;

		if (action.implies(getAction(OrganisatieEenheid.class)))
		{
			IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > account =
				getOrganisatieEenheidLocatieVanAccount();
			if (account == null)
				return false;

			List<OrganisatieEenheidLocatieKoppeling> flatList =
				OrganisatieEenheidLocatieUtil.flatten(account
					.getOrganisatieEenheidLocatieKoppelingen());

			if (OrganisatieEenheidLocatieUtil.gelijkeOrganisatieEenheidLocatie(flatList,
				getEntiteit()))
				return true;

			if (!isEditTarget())
			{
				for (IOrganisatieEenheidLocatieKoppelEntiteit< ? > curEntiteitKoppeling : getEntiteit()
					.getOrganisatieEenheidLocatieKoppelingen())
				{
					if (OrganisatieEenheidLocatieUtil.isGekoppeldAanParent(flatList,
						curEntiteitKoppeling))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Vervangt de huidige {@link ISecurityCheck} van de component door een nieuwe
	 * {@link OrganisatieEenheidLocatieKoppelbaarSecurityCheck}.
	 * 
	 * @param target
	 * @param medewerker
	 * @return de component
	 */
	public static Component replaceSecurityCheck(Component target, Medewerker medewerker)
	{
		return ComponentUtil.setSecurityCheck(target,
			new OrganisatieEenheidLocatieKoppelbaarSecurityCheck(ComponentUtil
				.getSecurityCheck(target), medewerker));
	}

}
