package nl.topicus.eduarte.web.components.panels;

import nl.topicus.eduarte.entities.groep.Groep;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/**
 * @author loite
 */
public class GroepTitel extends Label
{
	private static final long serialVersionUID = 1L;

	public GroepTitel(String id, Groep groep)
	{
		super(id);
		StringBuilder builder = new StringBuilder(30);
		if (groep != null)
		{
			builder.append(groep.getGroepstype().getNaam()).append(": ").append(
				groep.getContextInfoOmschrijving());
			if (groep.getOrganisatieEenheid() != null)
			{
				builder.append(" (").append(groep.getOrganisatieEenheid().getNaam()).append(")");
			}
		}
		setDefaultModel(new Model<String>(builder.toString()));
	}

}
