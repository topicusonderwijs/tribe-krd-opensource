package nl.topicus.eduarte.web.components.choice.renderer;

import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Renderer voor {@link OrganisatieEenheid}.
 * 
 * @author marrink
 */
public final class OrganisatieEenheidRenderer implements IChoiceRenderer<OrganisatieEenheid>
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	@Override
	public Object getDisplayValue(OrganisatieEenheid object)
	{
		return object.getNaam();
	}

	/**
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object,
	 *      int)
	 */
	@Override
	public String getIdValue(OrganisatieEenheid object, int index)
	{
		return object.getId().toString();
	}

}
