package nl.topicus.cobra.security;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.security.hive.authorization.Principal;

public interface IPrincipalSourceResolver<T extends Principal>
{
	public List<IPrincipalSource<T>> getPrinicipalSources();

	public List<Class< ? extends Component>> getAnnotatedComponents();

	public List<Class< ? extends IPrincipalSource<T>>> getPrincipalSources(
			Class< ? extends Component> componentClass);
}
