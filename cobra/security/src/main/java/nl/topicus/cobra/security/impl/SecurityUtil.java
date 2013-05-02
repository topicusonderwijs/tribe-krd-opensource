/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.security.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.security.IPrincipalSource;
import nl.topicus.cobra.security.Implies;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.InPrincipals;
import nl.topicus.cobra.security.NoReadPrincipalSource;
import nl.topicus.cobra.security.Write;

import org.apache.wicket.Component;
import org.apache.wicket.security.actions.ActionFactory;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.actions.WaspActionFactory;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.ISecureComponent;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.apache.wicket.security.hive.authorization.Principal;
import org.apache.wicket.security.swarm.actions.SwarmAction;

/**
 * Utillity class om snel te werken met de custom actions en objecten die daar verband mee
 * houden.
 * 
 * @author marrink
 */
public final class SecurityUtil
{

	public static boolean isActionImplied(WaspActionFactory factory,
			Class< ? extends WaspAction> actionClass, int actions)
	{
		SwarmAction action = (SwarmAction) factory.getAction(actionClass);
		int actionBits = action.actions();
		return (actions & actionBits) == actionBits;
	}

	public static boolean isHighestAction(WaspActionFactory factory,
			Class< ? extends WaspAction> actionClass, int actions)
	{
		SwarmAction action = (SwarmAction) factory.getAction(actionClass);
		int actionBits = action.actions();
		return (actions & actionBits) == actionBits && (actions & ~actionBits) < actionBits;
	}

	public static WaspAction mergeActions(List<Class< ? extends WaspAction>> actionList,
			WaspActionFactory actionFactory)
	{
		WaspAction ret = null;
		for (Class< ? extends WaspAction> curAction : actionList)
		{
			if (ret == null)
				ret = actionFactory.getAction(curAction);
			else
				ret = ret.add(actionFactory.getAction(curAction));
		}
		return ret;
	}

	/**
	 * De enable actie zonder dat deze ook render impliceerd. Handig in sommige gevallen
	 * waarbij acties van elkaar afgetrokken worden.
	 * 
	 * @param factory
	 * @return pure enable actie.
	 */
	public static WaspAction getEnableWithoutRender(ActionFactory factory)
	{
		return factory.getAction(Enable.class).remove(factory.getAction(Render.class));
	}

	public static List<InPrincipal> getInPrincipals(Class< ? extends Component> componentClass)
	{
		List<InPrincipal> ret = new ArrayList<InPrincipal>();
		if (componentClass.isAnnotationPresent(InPrincipal.class))
			ret.add(componentClass.getAnnotation(InPrincipal.class));
		if (componentClass.isAnnotationPresent(InPrincipals.class))
			ret.addAll(Arrays.asList(componentClass.getAnnotation(InPrincipals.class).value()));
		return ret;
	}

	public static ISecurityCheck saveGetSecurityCheck(Component component)
	{
		if (component instanceof ISecureComponent)
			return ((ISecureComponent) component).getSecurityCheck();
		return SecureComponentHelper.getSecurityCheck(component);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Principal> List<Class< ? extends IPrincipalSource<T>>> getImplies(
			Class< ? extends IPrincipalSource<T>> sourceClass)
	{
		List<Class< ? extends IPrincipalSource<T>>> ret =
			new ArrayList<Class< ? extends IPrincipalSource<T>>>();
		Implies implies = sourceClass.getAnnotation(Implies.class);
		if (implies != null)
		{
			for (Class< ? extends IPrincipalSource< ? extends Principal>> curImplies : implies
				.value())
			{
				ret.add((Class< ? extends IPrincipalSource<T>>) curImplies);
			}
		}

		Write write = sourceClass.getAnnotation(Write.class);
		if (write != null && !write.read().equals(NoReadPrincipalSource.class))
			ret.add((Class< ? extends IPrincipalSource<T>>) write.read());
		return ret;
	}
}
