package nl.topicus.eduarte.web.components.panels;

import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.zoekfilters.VrijVeldZoekFilter;

import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;

/**
 * Abstracte classe om de toon en edit panel te taggen. Dit omdat de edit versie in het
 * krd zit en we dit in core niet kennen.
 * 
 * @author hoeve
 */
public abstract class AbstractVrijVeldEntiteitPanel<T extends VrijVeldable< ? >> extends
		FormComponentPanel<T>
{
	private static final long serialVersionUID = 1L;

	public AbstractVrijVeldEntiteitPanel(String id, IModel<T> model)
	{
		super(id, model);
	}

	public abstract VrijVeldZoekFilter getVrijVeldZoekFilter();

}
