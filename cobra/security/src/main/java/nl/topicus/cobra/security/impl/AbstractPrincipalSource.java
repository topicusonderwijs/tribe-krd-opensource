package nl.topicus.cobra.security.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Everybody;
import nl.topicus.cobra.security.ExtraPermission;
import nl.topicus.cobra.security.ExtraPermissions;
import nl.topicus.cobra.security.IPrincipalSource;
import nl.topicus.cobra.security.InPrincipal;

import org.apache.wicket.Component;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.actions.WaspActionFactory;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.apache.wicket.security.hive.BasicHive;
import org.apache.wicket.security.hive.authorization.EverybodyPrincipal;
import org.apache.wicket.security.hive.authorization.Permission;
import org.apache.wicket.security.hive.authorization.Principal;

public abstract class AbstractPrincipalSource<T extends AbstractPrincipalImpl<T>> implements
		IPrincipalSource<T>
{
	private static final long serialVersionUID = 1L;

	public AbstractPrincipalSource()
	{
	}

	@Override
	public List<T> addPrincipalsToHive(BasicHive hive, WaspActionFactory actionFactory,
			List<Class< ? extends Component>> componentClasses)
	{
		if (isEverybody())
		{
			List<Class< ? extends WaspAction>> actions =
				new ArrayList<Class< ? extends WaspAction>>(getLoopActions());
			if (actions.isEmpty())
				actions.add(null);
			for (Class< ? extends WaspAction> curAction : actions)
			{
				List<Permission> permissions =
					createPermissions(actionFactory, componentClasses, curAction);
				Principal principal = new EverybodyPrincipal();
				hive.addPrincipal(principal, permissions);
			}
			return Collections.emptyList();
		}
		else
		{
			List<T> ret = new ArrayList<T>();
			for (Class< ? extends WaspAction> curAction : getLoopActions())
			{
				List<Permission> permissions =
					createPermissions(actionFactory, componentClasses, curAction);
				T principal = createPrincipal(curAction);
				hive.addPrincipal(principal, permissions);
				ret.add(principal);
			}
			return ret;
		}
	}

	public boolean isImplied(T base, T possibleImplied)
	{
		@SuppressWarnings("unchecked")
		Class< ? extends IPrincipalSource<T>> thisClass =
			(Class< ? extends IPrincipalSource<T>>) getClass();

		List<Class< ? extends IPrincipalSource<T>>> implies = SecurityUtil.getImplies(thisClass);
		if (implies.isEmpty())
			return false;

		for (Class< ? extends IPrincipalSource< ? extends Principal>> curImplies : implies)
		{
			if (curImplies.equals(possibleImplied.getSourceClass()))
			{
				return base.getAction().implies(possibleImplied.getAction());
			}
		}
		for (Class< ? extends IPrincipalSource< ? extends Principal>> curImplies : implies)
		{
			@SuppressWarnings("unchecked")
			IPrincipalSource<T> source =
				(IPrincipalSource<T>) ReflectionUtil.invokeConstructor(curImplies);
			if (source.isImplied(source.createPrincipal(base.getActionClass()), possibleImplied))
			{
				return true;
			}
		}
		return false;
	}

	private List<Permission> createPermissions(WaspActionFactory actionFactory,
			List<Class< ? extends Component>> componentClasses, Class< ? extends WaspAction> action)
	{
		List<Permission> permissions = new ArrayList<Permission>();
		permissions.addAll(getExtraPermissions(actionFactory, action));
		for (Class< ? extends Component> curComponent : componentClasses)
		{
			InPrincipal declaration = getDeclaration(curComponent);
			for (Actions curActions : declaration.actions())
			{
				List<Class< ? extends WaspAction>> actions =
					new ArrayList<Class< ? extends WaspAction>>();
				actions.addAll(Arrays.asList(curActions.value()));
				if (action != null)
					actions.add(action);
				permissions.add(ReflectionUtil.invokeConstructor(declaration.permissionClass(),
					curComponent.getName(), SecurityUtil.mergeActions(actions, actionFactory)));
			}
		}
		return permissions;
	}

	public List<Class< ? extends WaspAction>> getLoopActions()
	{
		Actions actions = getClass().getAnnotation(Actions.class);
		if (actions == null)
			return Collections.emptyList();
		return Arrays.asList(actions.value());
	}

	private InPrincipal getDeclaration(Class< ? extends Component> componentClass)
	{
		List<InPrincipal> inPrincipals = SecurityUtil.getInPrincipals(componentClass);
		for (InPrincipal curInPrincipals : inPrincipals)
		{
			if (Arrays.asList(curInPrincipals.value()).contains(getClass()))
			{
				return curInPrincipals;
			}
		}
		throw new IllegalArgumentException(getClass().getSimpleName() + " is not set on "
			+ componentClass);
	}

	public List<Permission> getExtraPermissions(WaspActionFactory actionFactory,
			Class< ? extends WaspAction> action)
	{
		ExtraPermissions extraPermissions = getClass().getAnnotation(ExtraPermissions.class);
		if (extraPermissions == null)
			return Collections.emptyList();
		List<Permission> ret = new ArrayList<Permission>();
		for (ExtraPermission curPermission : extraPermissions.value())
		{
			List<Class< ? extends WaspAction>> totalActions =
				new ArrayList<Class< ? extends WaspAction>>();
			totalActions.addAll(Arrays.asList(curPermission.actions()));
			if (action != null)
				totalActions.add(action);
			String name =
				curPermission.componentPrefix().equals(Component.class) ? curPermission.name()
					: SecureComponentHelper.alias(curPermission.componentPrefix())
						+ curPermission.name();
			ret.add(ReflectionUtil.invokeConstructor(curPermission.type(), name, SecurityUtil
				.mergeActions(totalActions, actionFactory)));
		}
		return ret;
	}

	public boolean isEverybody()
	{
		return getClass().getAnnotation(Everybody.class) != null;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj != null && obj.getClass().equals(getClass());
	}

	@Override
	public int hashCode()
	{
		return getClass().hashCode();
	}
}
