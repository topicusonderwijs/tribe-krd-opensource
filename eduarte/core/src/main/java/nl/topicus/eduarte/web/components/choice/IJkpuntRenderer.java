package nl.topicus.eduarte.web.components.choice;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.IJkpunt;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Renderer voor ijkpunten
 * 
 * @author vandenbrink
 */
public class IJkpuntRenderer implements IChoiceRenderer<IJkpunt>
{
	private static final long serialVersionUID = 1L;

	public Object getDisplayValue(IJkpunt object)
	{
		if (object == null)
			return null;
		IJkpunt ijkpunt = object;
		String display =
			ijkpunt.getDatum() != null ? TimeUtil.getInstance().formatDate(ijkpunt.getDatum())
				+ " - " : "";
		display += ijkpunt.getNaam();
		if (ijkpunt.getDeelnemer() == null)
		{
			if (ijkpunt.getOpleiding() == null)
				display += " (globale vrije matrix)";

			else
				display += " (opleiding)";
		}
		else
			display += " (individueel)";
		return display;
	}

	public String getIdValue(IJkpunt object, int index)
	{
		if (object == null)
			return null;
		return (object).getId().toString();
	}

}
