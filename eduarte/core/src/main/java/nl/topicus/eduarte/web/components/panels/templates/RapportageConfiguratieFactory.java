package nl.topicus.eduarte.web.components.panels.templates;

import nl.topicus.cobra.templates.annotations.Exportable;

import org.apache.wicket.model.IDetachable;

@Exportable
public interface RapportageConfiguratieFactory<T> extends IDetachable
{
	public Object createConfiguratie(T contextObject);
}
