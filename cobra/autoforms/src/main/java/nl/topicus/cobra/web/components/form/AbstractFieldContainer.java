package nl.topicus.cobra.web.components.form;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * {@code AbstractFieldContainer} is de basis voor field containers. Een field container
 * is verantwoordelijk voor het renderen van de markup voor een veld.
 * 
 * @author papegaaij
 */
public abstract class AbstractFieldContainer<X, Y, Z> extends Panel
{
	private static final long serialVersionUID = 1L;

	private FieldProperties<X, Y, Z> properties;

	/**
	 * Maakt een nieuwe field container.
	 * 
	 * @param id
	 *            Het id van de container.
	 * @param properties
	 *            De properties van het veld.
	 */
	public AbstractFieldContainer(String id, FieldProperties<X, Y, Z> properties)
	{
		super(id);
		this.properties = properties;
	}

	@Override
	public boolean isVisible()
	{
		return super.isVisible() && properties.isVisible();
	}

	/**
	 * Stelt de field container in staat om extra bewerkingen uit te voeren op het veld,
	 * zoals het toevoegen van een maxlength attribuut aan text velden.
	 * 
	 * @param field
	 *            Het veld dat zich in deze field container bevind.
	 * @param fieldSet
	 *            De field set waar deze container zich in bevind.
	 */
	public void postProcessFormField(Component field, AutoFieldSet<X> fieldSet)
	{
	}

	/**
	 * Geeft de properties van het field.
	 * 
	 * @return De properties van het field.
	 */
	public FieldProperties<X, Y, Z> getFieldProperties()
	{
		return properties;
	}

	/**
	 * Wrapper functie om het field aan de container toe te voegen. Default roepen we de
	 * {@link MarkupContainer#add(Component...)} add.
	 */
	public void add(Component formField)
	{
		super.add(formField);
	}
}
