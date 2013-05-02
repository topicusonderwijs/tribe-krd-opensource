package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.entities.IdObject;

import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;

public abstract class AbstractSelectieTarget<R, S extends IdObject> implements SelectieTarget<R, S>
{
	private static final long serialVersionUID = 1L;

	private Class< ? > target;

	private String label;

	public AbstractSelectieTarget(Class< ? > target, String label)
	{
		this.target = target;
		this.label = label;
	}

	@Override
	public ISecurityCheck getSecurityCheck()
	{
		return new ClassSecurityCheck(target);
	}

	@Override
	public String getLinkLabel()
	{
		return label;
	}

	@Override
	public void detach()
	{
	}
}
