package nl.topicus.eduarte.app.security.checks;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.security.AlternativeRenderCheckSupported;
import nl.topicus.eduarte.dao.helpers.PersoonDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.providers.PersoonProvider;

import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;

public class NietOverledenSecurityCheck extends EduArteSecurityCheck implements
		AlternativeRenderCheckSupported
{
	private static final long serialVersionUID = 1L;

	private Long id;

	public NietOverledenSecurityCheck(ISecurityCheck wrapped, PersoonProvider provider)
	{
		this(wrapped, provider.getPersoon());
	}

	public NietOverledenSecurityCheck(ISecurityCheck wrapped, Persoon persoon)
	{
		super(wrapped);

		if (persoon == null)
			id = null;
		else
			id = persoon.getId();
	}

	protected Persoon getPersoon()
	{
		if (id == null || id < 0)
			return null;
		return DataAccessRegistry.getHelper(PersoonDataAccessHelper.class).get(id);
	}

	@Override
	protected boolean isEntitySet()
	{
		return id != null && id >= 0;
	}

	@Override
	protected boolean verify(WaspAction action)
	{
		Persoon persoon = getPersoon();
		return persoon == null || !persoon.isOverleden();
	}
}
