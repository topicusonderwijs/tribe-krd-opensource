/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.eduarte.app.security.GuiCategory;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.security.actions.Access;
import org.apache.wicket.security.actions.ActionFactory;
import org.apache.wicket.security.actions.RegistrationException;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.swarm.actions.SwarmAction;
import org.apache.wicket.security.swarm.actions.SwarmActionFactory;

public class EduArteActionFactory extends SwarmActionFactory
{
	private List<Class< ? extends WaspAction>> eduarteActions;

	@SuppressWarnings("unchecked")
	public EduArteActionFactory(Object key)
	{
		super(key);
		try
		{
			checkValue(register(Docent.class, "docent"), GuiCategory.DOCENT); // base=8
			checkValue(register(Begeleider.class, "begeleider"), GuiCategory.BEGELEIDER); // base=16
			checkValue(register(Uitvoerend.class, "uitvoerend"), GuiCategory.UITVOEREND); // base=32
			checkValue(register(Verantwoordelijk.class, "verantwoordelijk"),
				GuiCategory.VERANTWOORDELIJK); // base=64
			checkValue(register(OrganisatieEenheid.class, new ImpliesOtherAction(
				"organisatie.eenheid", this, Verantwoordelijk.class, Uitvoerend.class,
				Docent.class, Begeleider.class)), GuiCategory.ORGANISATIE_EENHEID); // base=128
			checkValue(register(Instelling.class, new ImpliesOtherAction("instelling", this,
				OrganisatieEenheid.class)), GuiCategory.INSTELLING); // base=256
			checkValue(register(Beheer.class, "beheer"), GuiCategory.BEHEER);
			checkValue(register(Deelnemer.class, "deelnemer"), GuiCategory.DEELNEMER);

			eduarteActions = new ArrayList<Class< ? extends WaspAction>>();
			eduarteActions.add(Deelnemer.class);
			eduarteActions.add(Beheer.class);
			eduarteActions.add(Instelling.class);
			eduarteActions.add(OrganisatieEenheid.class);
			eduarteActions.add(Verantwoordelijk.class);
			eduarteActions.add(Uitvoerend.class);
			eduarteActions.add(Begeleider.class);
			eduarteActions.add(Docent.class);
			eduarteActions = Collections.unmodifiableList(eduarteActions);

		}
		catch (RegistrationException e)
		{
			throw new WicketRuntimeException(e);
		}
	}

	/**
	 * Checks actions for an expected internal value. This is an early warning for when
	 * new actions are inserted and someone forgot to update the database values of the
	 * existing actions.
	 * 
	 * @param action
	 * @param expectedValue
	 */
	private void checkValue(SwarmAction action, int expectedValue)
	{
		if (action.actions() != expectedValue)
			throw new IllegalStateException("The internal value for " + action.getName()
				+ " must be " + expectedValue + " but is " + action.actions());
		// If this happens you probably forgot to update the database you ...., if you did
		// update the database then you forgot to update the values here

	}

	/**
	 * De acties die binnen EduArte een speciale betekenis hebben.
	 * 
	 * @return lijst met acties
	 */
	public List<Class< ? extends WaspAction>> getEduArteActions()
	{
		return eduarteActions;
	}

	/**
	 * De acties die binnen EduArte een speciale betekenis hebben binnen een instelling.
	 * Dit is een subset van {@link #getEduArteActions()}
	 * 
	 * @return lijst met acties
	 */
	public List<Class< ? extends WaspAction>> getEduArteInstellingsActions()
	{
		return getEduArteActions(RechtenSoort.INSTELLING);
	}

	public List<Class< ? extends WaspAction>> getEduArteActions(RechtenSoort soort)
	{
		List<Class< ? extends WaspAction>> ret = new ArrayList<Class< ? extends WaspAction>>();
		for (Class< ? extends WaspAction> curAction : getEduArteActions())
		{
			RechtenSoorten soorten = curAction.getAnnotation(RechtenSoorten.class);
			if (soorten != null && Arrays.asList(soorten.value()).contains(soort))
				ret.add(curAction);
		}
		return ret;
	}

	/**
	 * Gets the action that implies all these actions.
	 * 
	 * @param actions
	 * @return the action or null if none exists
	 */
	public WaspAction getAction(Class< ? extends WaspAction>... actions)
	{
		if (actions == null)
			return null;
		WaspAction myAction = getAction(Access.class);
		for (Class< ? extends WaspAction> clazz : actions)
			myAction.add(getAction(clazz));
		return myAction;
	}

	/**
	 * Overridden to return the actual action type, i.e. SwarmAction.
	 * 
	 * @see org.apache.wicket.security.swarm.actions.SwarmActionFactory#register(java.lang.Class,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final synchronized SwarmAction register(Class waspActionClass, String name)
			throws RegistrationException
	{
		return super.register(waspActionClass, name);
	}

	protected static class SerializableSwarmAction extends SwarmAction implements Serializable
	{
		private static final long serialVersionUID = 1L;

		protected SerializableSwarmAction(int action, String name, Object key)
		{
			super(action, name, key);
		}
	}

	/**
	 * Any class that implies another action.
	 * 
	 * @author marrink
	 */
	protected static final class ImpliesOtherAction extends SwarmAction
	{
		private static final long serialVersionUID = 1L;

		/**
		 * the base action value
		 * 
		 * @param name
		 *            name of the new action
		 * @param factory
		 *            factory where this class will be registered
		 * @param otherAction
		 *            a single action class to implie, not null
		 */
		public ImpliesOtherAction(String name, EduArteActionFactory factory,
				Class< ? extends WaspAction> otherAction)
		{
			super(factory.nextPowerOf2() | (factory.getAction(otherAction)).actions(), name,
				factory.getFactoryKey());
		}

		/**
		 * the base action value
		 * 
		 * @param name
		 *            name of the new action
		 * @param factory
		 *            factory where this class will be registered
		 * @param otherActions
		 *            any number of action classes to implie
		 */
		public ImpliesOtherAction(String name, EduArteActionFactory factory,
				Class< ? extends WaspAction>... otherActions)
		{
			super(factory.nextPowerOf2() | bitwiseOr(factory, otherActions), name, factory
				.getFactoryKey());
		}

		/**
		 * Creates a bitwise or of all the actions supplied. Note that all these classes
		 * must already be registered.
		 * 
		 * @param factory
		 * @param otherActions
		 *            any number of action classes to implie
		 * @return
		 */
		private static final int bitwiseOr(ActionFactory factory,
				Class< ? extends WaspAction>... otherActions)
		{
			int result = 0;
			if (otherActions != null)
			{
				for (Class< ? extends WaspAction> action : otherActions)
					result = result | ((SwarmAction) factory.getAction(action)).actions();
			}
			return result;
		}
	}
}
