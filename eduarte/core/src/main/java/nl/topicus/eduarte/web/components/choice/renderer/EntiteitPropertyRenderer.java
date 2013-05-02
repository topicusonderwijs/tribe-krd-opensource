package nl.topicus.eduarte.web.components.choice.renderer;

import java.io.Serializable;

import nl.topicus.cobra.entities.TransientIdObject;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.util.lang.PropertyResolver;

/**
 * Renderer voor entiteiten waarbij een gegeven property van de entiteit als display value
 * gebruikt wordt.
 * 
 * @author loite
 */
public class EntiteitPropertyRenderer implements IChoiceRenderer<TransientIdObject>
{
	private static final long serialVersionUID = 1L;

	private final String property;

	/**
	 * Constructor
	 * 
	 * @param property
	 *            Het property dat gebruikt moet worden als display value.
	 */
	public EntiteitPropertyRenderer(String property)
	{
		this.property = property;
	}

	@Override
	public Object getDisplayValue(TransientIdObject object)
	{
		return PropertyResolver.getValue(property, object);
	}

	@Override
	public String getIdValue(TransientIdObject object, int index)
	{
		TransientIdObject entiteit = object;
		Serializable id = entiteit.getIdAsSerializable();
		if (id != null)
			return id.toString();
		if (entiteit.getTemporaryId() != null)
			return entiteit.getTemporaryId().toString();
		return "i" + String.valueOf(index);
	}
}
