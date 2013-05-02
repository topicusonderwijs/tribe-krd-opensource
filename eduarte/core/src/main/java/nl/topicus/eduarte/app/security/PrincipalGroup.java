package nl.topicus.eduarte.app.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.security.IPrincipalSource;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;

import org.apache.wicket.security.actions.WaspAction;

public class PrincipalGroup implements Serializable, Comparable<PrincipalGroup>
{
	private static final long serialVersionUID = 1L;

	private Class< ? extends IPrincipalSource<EduArtePrincipal>> groupClass;

	private List<EduArtePrincipal> principals = new ArrayList<EduArtePrincipal>();

	private transient Display displayCache;

	private EduArteModuleKey moduleCache;

	public PrincipalGroup(Class< ? extends IPrincipalSource<EduArtePrincipal>> groupClass)
	{
		this.groupClass = groupClass;
	}

	public List<EduArtePrincipal> getPrincipals()
	{
		return principals;
	}

	public void addPrincipal(EduArtePrincipal principal)
	{
		principals.add(principal);
	}

	public EduArtePrincipal getPrincipal(Class< ? extends WaspAction> action, boolean write)
	{
		for (EduArtePrincipal curPrincipal : principals)
		{
			if (curPrincipal.getActionClass().equals(action) && curPrincipal.isWrite() == write)
				return curPrincipal;
		}
		return null;
	}

	private Display getDisplay()
	{
		if (displayCache == null)
			displayCache = groupClass.getAnnotation(Display.class);
		return displayCache;
	}

	public String getParent()
	{
		return getDisplay().parent();
	}

	public String getOmschrijving()
	{
		return getDisplay().label();
	}

	private EduArteModuleKey calcModule()
	{
		// optimize common case
		if (getDisplay().module().length == 1)
			return getDisplay().module()[0];
		for (EduArteModuleKey curModule : getDisplay().module())
		{
			if (EduArteApp.get().isModuleActive(curModule))
				return curModule;
		}
		return getDisplay().module()[0];
	}

	public EduArteModuleKey getModule()
	{
		if (moduleCache == null)
			moduleCache = calcModule();
		return moduleCache;
	}

	public String getGroup()
	{
		return getDisplay().group();
	}

	@Override
	public int compareTo(PrincipalGroup o)
	{
		int ret = getModule().compareTo(o.getModule());
		if (ret != 0)
			return ret;
		ret = getParent().compareTo(o.getParent());
		if (ret != 0)
			return ret;
		ret = getGroup().compareTo(o.getGroup());
		return ret == 0 ? getOmschrijving().compareTo(o.getOmschrijving()) : ret;
	}
}
