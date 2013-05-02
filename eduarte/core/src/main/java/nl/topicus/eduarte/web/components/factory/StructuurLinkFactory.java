package nl.topicus.eduarte.web.components.factory;

import nl.topicus.cobra.modules.ModuleComponentFactory;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.Link;

public interface StructuurLinkFactory extends ModuleComponentFactory
{
	public Link< ? > createStructuurLink(String id, Resultaatstructuur structuur, Component parent);
}
