package nl.topicus.cobra.web.security;

import java.util.ArrayList;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.security.checks.ISecurityCheck;

public class DisableSecurityCheckMarker extends
		MetaDataKey<ArrayList<Class< ? extends ISecurityCheck>>>
{
	private static final long serialVersionUID = 1L;

	public static <T extends Component> T place(T component,
			Class< ? extends ISecurityCheck> disabledCheck)
	{
		DisableSecurityCheckMarker marker = new DisableSecurityCheckMarker();
		ArrayList<Class< ? extends ISecurityCheck>> curData = component.getMetaData(marker);
		if (curData == null)
		{
			curData = new ArrayList<Class< ? extends ISecurityCheck>>();
			component.setMetaData(marker, curData);
		}
		curData.add(disabledCheck);
		return component;
	}

	public static boolean isSecurityCheckDisabled(Component component,
			Class< ? extends ISecurityCheck> check)
	{
		ArrayList<Class< ? extends ISecurityCheck>> securityChecks =
			component.getMetaData(new DisableSecurityCheckMarker());
		if (securityChecks == null)
			return false;
		for (Class< ? extends ISecurityCheck> curCheck : securityChecks)
		{
			if (curCheck.isAssignableFrom(check))
				return true;
		}
		return false;
	}
}
