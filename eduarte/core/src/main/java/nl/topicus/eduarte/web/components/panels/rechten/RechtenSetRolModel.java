package nl.topicus.eduarte.web.components.panels.rechten;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.wiquery.tristate.TriState;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.entities.security.authorization.Rol;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class RechtenSetRolModel implements IDetachable
{
	private static final long serialVersionUID = 1L;

	private EduArtePrincipal principal;

	private IModel<Rol> rolModel;

	private int impliedCount = 0;

	public RechtenSetRolModel(EduArtePrincipal principal, IModel<Rol> rolModel, int impliedCount)
	{
		this.principal = principal;
		this.rolModel = rolModel;
		this.impliedCount = impliedCount;
	}

	public TriState getSelection()
	{
		if (getRol().containsRecht(principal))
			return TriState.On;
		else if (impliedCount > 0)
			return TriState.Partial;
		else
			return TriState.Off;
	}

	public void setSelection(TriState selection)
	{
		if (TriState.On.equals(selection))
			getRol().addRecht(principal);
		else
			getRol().removeRecht(principal);
	}

	private Rol getRol()
	{
		return rolModel.getObject();
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(rolModel);
	}

	public void setImpliedCount(int impliedCount)
	{
		this.impliedCount = impliedCount;
	}

	public int getImpliedCount()
	{
		return impliedCount;
	}
}
