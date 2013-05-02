package nl.topicus.eduarte.web.components.choice.renderer;

import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.Meeteenheid;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Renderer voor meeteenheden
 * 
 * @author vanharen
 */
public class MeeteenheidRenderer implements IChoiceRenderer<Meeteenheid>
{
	private static final long serialVersionUID = 1L;

	public Object getDisplayValue(Meeteenheid meeteenheid)
	{
		if (meeteenheid == null)
			return null;
		return meeteenheid.getNaam();
	}

	public String getIdValue(Meeteenheid meeteenheid, int index)
	{
		if (meeteenheid == null)
			return null;
		return meeteenheid.getId().toString();
	}

}
