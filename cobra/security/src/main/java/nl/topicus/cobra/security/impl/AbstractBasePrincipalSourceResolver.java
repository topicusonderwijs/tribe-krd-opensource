package nl.topicus.cobra.security.impl;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.security.IPrincipalSource;
import nl.topicus.cobra.security.IPrincipalSourceResolver;
import nl.topicus.cobra.security.InPrincipal;

import org.apache.wicket.Component;

public abstract class AbstractBasePrincipalSourceResolver<T extends AbstractPrincipalImpl<T>>
		implements IPrincipalSourceResolver<T>
{
	private String principalPackage;

	private String componentPackage;

	public AbstractBasePrincipalSourceResolver(String principalPackage, String componentPackage)
	{
		this.principalPackage = principalPackage;
		this.componentPackage = componentPackage;
	}

	@Override
	public List<Class< ? extends Component>> getAnnotatedComponents()
	{
		return scanForClasses(Component.class, InPrincipal.class, componentPackage);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<IPrincipalSource<T>> getPrinicipalSources()
	{
		List<IPrincipalSource<T>> ret = new ArrayList<IPrincipalSource<T>>();
		for (Class< ? extends IPrincipalSource> curValue : scanForClasses(IPrincipalSource.class,
			null, principalPackage))
		{
			ret.add(ReflectionUtil.invokeConstructor(curValue));
		}
		return ret;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Class< ? extends IPrincipalSource<T>>> getPrincipalSources(
			Class< ? extends Component> componentClass)
	{
		List<InPrincipal> inPrincipalList = SecurityUtil.getInPrincipals(componentClass);
		List<Class< ? extends IPrincipalSource<T>>> ret =
			new ArrayList<Class< ? extends IPrincipalSource<T>>>();
		for (InPrincipal curInPrincipal : inPrincipalList)
		{
			for (Class< ? extends IPrincipalSource> curValue : curInPrincipal.value())
			{
				ret.add((Class< ? extends IPrincipalSource<T>>) curValue);
			}
		}
		return ret;
	}

	@Override
	public String toString()
	{
		return componentPackage;
	}

	abstract protected <C> List<Class< ? extends C>> scanForClasses(Class<C> baseClass,
			Class< ? extends Annotation> annotationClass, String packageName);
}
