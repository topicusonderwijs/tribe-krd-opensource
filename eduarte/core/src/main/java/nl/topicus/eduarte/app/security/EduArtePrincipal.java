package nl.topicus.eduarte.app.security;

import nl.topicus.cobra.security.IPrincipalSource;
import nl.topicus.cobra.security.impl.AbstractPrincipalImpl;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;

import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.actions.WaspActionFactory;

public class EduArtePrincipal extends AbstractPrincipalImpl<EduArtePrincipal>
{
	private static final long serialVersionUID = 1L;

	public EduArtePrincipal(IPrincipalSource<EduArtePrincipal> source,
			Class< ? extends WaspAction> action)
	{
		super(source, action);
	}

	@Override
	protected WaspActionFactory getActionFactory()
	{
		return EduArteApp.get().getActionFactory();
	}

	public EduArteModuleKey[] getModules()
	{
		if (getSourceClass().isAnnotationPresent(Module.class))
			return getSourceClass().getAnnotation(Module.class).value();
		return new EduArteModuleKey[] {EduArteModuleKey.BASISFUNCTIONALITEIT};
	}

	public AuthorisatieNiveau getAuthorisatieNiveau()
	{
		if (getSourceClass().isAnnotationPresent(Niveau.class))
			return getSourceClass().getAnnotation(Niveau.class).value();
		return AuthorisatieNiveau.REST;
	}
}
