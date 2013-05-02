package nl.topicus.eduarte.resultaten.web.components.security;

import java.io.Serializable;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.Actions;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.checks.EduArteSecurityCheck;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieKoppelbaarSecurityCheck;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;

@Actions( {Instelling.class, OrganisatieEenheid.class})
public class ResultaatstructuurSecurityCheck extends EduArteSecurityCheck
{
	private static final long serialVersionUID = 1L;

	private Serializable id;

	private String securityIdPostfix = "";

	public ResultaatstructuurSecurityCheck(ISecurityCheck wrapped, Resultaatstructuur object)
	{
		super(wrapped);
		if (object != null)
			id = object.getIdAsSerializable();
	}

	public ResultaatstructuurSecurityCheck setSecurityIdPostfix(String securityIdPostfix)
	{
		this.securityIdPostfix = securityIdPostfix;
		return this;
	}

	@Override
	protected boolean isEntitySet()
	{
		return id != null;
	}

	protected Resultaatstructuur getResultaatstructuur()
	{
		ResultaatstructuurDataAccessHelper helper =
			DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class);
		return helper.get(Resultaatstructuur.class, id);
	}

	@Override
	protected boolean verify(WaspAction action)
	{
		Resultaatstructuur object = getResultaatstructuur();

		if (object.getType() == Type.FORMATIEF && !getMedewerker().equals(object.getAuteur()))
		{
			if (!isAllowed(Render.class, Resultaatstructuur.ANDERMANS_STRUCTUREN))
			{
				if (isEditTarget())
					return false;

				if (!object.isGekoppeld(getMedewerker()))
					return false;
			}
		}

		Class< ? extends WaspAction> check = isEditTarget() ? Enable.class : Render.class;
		String securityId = object.getType().getSecurityId() + securityIdPostfix;
		return isAllowed(object, check, securityId);
	}

	private boolean isAllowed(Resultaatstructuur object, Class< ? extends WaspAction> action,
			String securityId)
	{
		return new OrganisatieEenheidLocatieKoppelbaarSecurityCheck(new DataSecurityCheck(
			securityId), object.getOnderwijsproduct()).isActionAuthorized(getAction(action));
	}

	private boolean isAllowed(Class< ? extends WaspAction> action, String securityId)
	{
		return new DataSecurityCheck(securityId).isActionAuthorized(getAction(action));
	}
}
