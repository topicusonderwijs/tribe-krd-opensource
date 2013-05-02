package nl.topicus.cobra.web.security;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.reflection.InvocationFailedException;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.pages.IEditPage;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.strategies.SecurityException;

public class ModelBasedSecurityCheckFactory implements ISecurityCheckFactory
{
	public ISecurityCheck createSecurityCheck(Class< ? extends ISecurityCheck> checkClass,
			ISecurityCheck wrapped, Component component, Class< ? > target)
	{
		Set<Class< ? >> possibleTypes = collectPossibleTypes(checkClass);
		if (possibleTypes.isEmpty())
			return instantiate(checkClass, wrapped);
		Component curComponent = component;
		List<Object> objectsFound = new ArrayList<Object>();
		while (curComponent != null)
		{
			if (DisableSecurityCheckMarker.isSecurityCheckDisabled(curComponent, checkClass))
				return null;
			IModel< ? > curModel = curComponent.getDefaultModel();
			if (curModel != null)
			{
				try
				{
					Object curObject = curModel.getObject();
					if (matches(possibleTypes, curObject))
					{
						ISecurityCheck ret = instantiate(checkClass, wrapped, curObject);
						if (ret instanceof EditTargetAwareSecurityCheck)
						{
							((EditTargetAwareSecurityCheck) ret).setEditTarget(IEditPage.class
								.isAssignableFrom(target));
						}
						return ret;
					}
					if (curObject != null)
						objectsFound.add(curObject);
				}
				catch (WicketRuntimeException propertyException)
				{
					// catch this exception and ignore it, because it is probably caused
					// by a CompoundPropertyModel
				}
			}
			curComponent = curComponent.getParent();
		}
		throw new SecurityException("Cannot find a parent of "
			+ component.getPage().getClass().getSimpleName() + "." + component.getPath()
			+ " with one of the following model objects " + possibleTypes + " for " + target
			+ ", which is required to create an instance of " + checkClass.getName()
			+ ". The following entities were found: " + objectsFound
			+ ". Make sure that the link, or one of its parents provides the required "
			+ "model object.");
	}

	private ISecurityCheck instantiate(Class< ? extends ISecurityCheck> checkClass,
			ISecurityCheck wrapped)
	{
		if (wrapped != null)
		{
			try
			{
				return ReflectionUtil.invokeConstructor(checkClass, wrapped);
			}
			catch (InvocationFailedException e)
			{
			}
		}
		return ReflectionUtil.invokeConstructor(checkClass);
	}

	private ISecurityCheck instantiate(Class< ? extends ISecurityCheck> checkClass,
			ISecurityCheck wrapped, Object object)
	{
		if (wrapped != null)
		{
			try
			{
				return ReflectionUtil.invokeConstructor(checkClass, wrapped, object);
			}
			catch (InvocationFailedException e)
			{
				if (!e.isCausedByMethodNotFound())
					throw e;
			}
		}
		return ReflectionUtil.invokeConstructor(checkClass, object);
	}

	private Set<Class< ? >> collectPossibleTypes(Class< ? extends ISecurityCheck> checkClass)
	{
		Set<Class< ? >> ret = new HashSet<Class< ? >>();
		for (Constructor< ? > curConstructor : checkClass.getConstructors())
		{
			if (curConstructor.getParameterTypes().length == 1
				&& !ISecurityCheck.class.isAssignableFrom(curConstructor.getParameterTypes()[0]))
			{
				ret.add(curConstructor.getParameterTypes()[0]);
			}
			else if (curConstructor.getParameterTypes().length == 2)
			{
				ret.add(curConstructor.getParameterTypes()[1]);
			}
		}
		return ret;
	}

	private boolean matches(Set<Class< ? >> possibleTypes, Object object)
	{
		if (object == null)
			return false;
		Class< ? > checkClass = object.getClass();
		for (Class< ? > curClass : possibleTypes)
		{
			if (curClass.isAssignableFrom(checkClass))
				return true;
		}
		return false;
	}
}
