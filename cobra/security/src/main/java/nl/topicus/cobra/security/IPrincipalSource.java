package nl.topicus.cobra.security;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.actions.WaspActionFactory;
import org.apache.wicket.security.hive.BasicHive;
import org.apache.wicket.security.hive.authorization.Principal;

public interface IPrincipalSource<T extends Principal> extends Serializable
{
	public List<T> addPrincipalsToHive(BasicHive hive, WaspActionFactory actionFactory,
			List<Class< ? extends Component>> componentClasses);

	public T createPrincipal(Class< ? extends WaspAction> action);

	public boolean isImplied(T base, T possibleImplied);
}
