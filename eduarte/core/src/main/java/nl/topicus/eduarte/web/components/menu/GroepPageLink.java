package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author vanharen
 */
public final class GroepPageLink implements IPageLink
{
	private static final long serialVersionUID = 1L;

	private final Class< ? extends SecurePage> pageClass;

	private IModel<Groep> groep;

	public GroepPageLink(Class< ? extends SecurePage> pageClass, IModel<Groep> groep)
	{
		this.pageClass = pageClass;
		this.groep = groep;
	}

	@Override
	public Page getPage()
	{
		return ReflectionUtil.invokeConstructor(pageClass, groep.getObject());
	}

	@Override
	public Class< ? extends Page> getPageIdentity()
	{
		return pageClass;
	}

	public Groep getGroep()
	{
		return groep.getObject();
	}
}