/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.checks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.CobraApplication;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.web.security.AlternativeRenderCheckSupported;
import nl.topicus.cobra.web.security.EditTargetAwareSecurityCheck;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.DeelnemerAccount;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;

import org.apache.wicket.security.actions.ActionFactory;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.AbstractSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.swarm.strategies.SwarmStrategy;
import org.hibernate.Hibernate;

/**
 * Securitycheck die de verschillende extra acties toetst. Indien de alternatieve
 * rendering check gebruikt wordt zal de component altijd zichtbaar zijn (indien de
 * gewrapte check dit toestaat).
 * 
 * @author marrink
 */
public abstract class EduArteSecurityCheck extends AbstractSecurityCheck implements
		AlternativeRenderCheckSupported, EditTargetAwareSecurityCheck
{
	private static final long serialVersionUID = 1L;

	private boolean useAlternativeRenderCheck = false;

	private final List<Class< ? extends WaspAction>> actions;

	private ISecurityCheck wrapped;

	private boolean editTarget;

	private transient Account account;

	private SwarmStrategy strategy;

	public EduArteSecurityCheck(ISecurityCheck wrapped)
	{
		this(EduArteApp.get().getActionFactory().getEduArteActions(), wrapped);
	}

	public EduArteSecurityCheck(ISecurityCheck wrapped, Class< ? extends WaspAction>... actions)
	{
		Asserts.assertNotNull("actions", actions);
		Asserts.assertNotNull("wrapped", wrapped);
		this.actions = Arrays.asList(actions);
		this.wrapped = wrapped;
		this.strategy = (SwarmStrategy) CobraApplication.get().getStrategyFactory().newStrategy();
	}

	/**
	 * @param actions
	 *            een gesorteerde lijst met de acties die gecontroleerd moeten worden van
	 *            hoog naar laag
	 * @param wrapped
	 */
	public EduArteSecurityCheck(List<Class< ? extends WaspAction>> actions, ISecurityCheck wrapped)
	{
		Asserts.assertNotNull("actions", actions);
		Asserts.assertNotNull("wrapped", wrapped);
		this.actions = new ArrayList<Class< ? extends WaspAction>>(actions);
		this.wrapped = wrapped;
		this.strategy = (SwarmStrategy) CobraApplication.get().getStrategyFactory().newStrategy();
	}

	public SwarmStrategy getStrategyNietUitSessie()
	{
		return strategy;
	}

	@Override
	public void setEditTarget(boolean editTarget)
	{
		this.editTarget = editTarget;
	}

	public boolean isEditTarget()
	{
		return editTarget;
	}

	@Override
	public boolean isActionAuthorized(WaspAction action)
	{
		if (!isEntitySet())
			return true;

		WaspAction combined = null;
		WaspAction additional;
		ActionFactory factory = getActionFactory();
		for (Class< ? extends WaspAction> actionClass : actions)
		{
			additional = factory.getAction(actionClass);
			combined = action.add(additional);
			if (wrapped.isActionAuthorized(combined))
			{
				if (isUseAlternativeRenderCheck() && !combined.implies(getAction(Enable.class)))
					return true;
				if (verify(additional))
					return true;
			}
		}
		return false;
	}

	/**
	 * Verifieerd of de gebruiker idd hiervoor bevoegd is. bv bij Instelling of de
	 * gebruiker idd een medewerker is van een bepaalde organisatie.
	 * 
	 * @param action
	 * @return true als de gerbuiker geverifieerd is, anders false
	 */
	protected abstract boolean verify(WaspAction action);

	protected abstract boolean isEntitySet();

	/**
	 * @see org.apache.wicket.security.checks.ISecurityCheck#isAuthenticated()
	 */
	@Override
	public boolean isAuthenticated()
	{
		return wrapped.isAuthenticated();
	}

	/**
	 * Wrapper om {@link #getActionFactory()}. Leverd direct de actie op.
	 * 
	 * @param clazz
	 * @return actie of null als deze niet bestaat
	 */
	protected final WaspAction getAction(Class< ? extends WaspAction> clazz)
	{
		return getActionFactory().getAction(clazz);
	}

	/**
	 * De organisatie van de huidige gebruiker.
	 * 
	 * @return organisatie of null als deze niet bekend is.
	 */
	protected final Organisatie getOrganisatie()
	{
		return EduArteContext.get().getOrganisatie();
	}

	/**
	 * De ingelogde gebruiker.
	 * 
	 * @return medewerker of null als er geen medewerker gekoppeld is aan de account die
	 *         is ingelogd
	 */
	protected final IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > getOrganisatieEenheidLocatieVanAccount()
	{
		if (getAccount() instanceof MedewerkerAccount)
			return ((MedewerkerAccount) getAccount()).getMedewerker();
		if (getAccount() instanceof DeelnemerAccount)
			return ((DeelnemerAccount) getAccount()).getDeelnemer();
		return null;
	}

	/**
	 * De ingelogde gebruiker.
	 * 
	 * @return medewerker of null als er geen medewerker gekoppeld is aan de account die
	 *         is ingelogd
	 */
	protected final Medewerker getMedewerker()
	{
		if (MedewerkerAccount.class.isAssignableFrom(Hibernate.getClass(getAccount())))
			return ((MedewerkerAccount) getAccount().doUnproxy()).getMedewerker();
		return null;
	}

	/**
	 * De ingelogde gebruiker.
	 * 
	 * @return account of null als er niemand ingelogd is
	 */
	protected final Account getAccount()
	{
		if (account == null)
			account = EduArteContext.get().getAccount();
		return account;
	}

	public EduArteSecurityCheck setAccount(Account account)
	{
		this.account = account;
		return this;
	}

	public final boolean isUseAlternativeRenderCheck()
	{
		return useAlternativeRenderCheck;
	}

	@Override
	public final void setUseAlternativeRenderCheck(boolean useAlternativeRenderCheck)
	{
		this.useAlternativeRenderCheck = useAlternativeRenderCheck;
	}

	@Override
	public final boolean getUseAlternativeRenderCheck()
	{
		return useAlternativeRenderCheck;
	}
}
