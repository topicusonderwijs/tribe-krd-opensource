package nl.topicus.cobra.web.components.form;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * {@code FieldMarkupRenderer} is verantwoordelijk voor het genereren van de markup van
 * een {@link AutoFieldSet} component. De markup bestaat uit een top-level markup en een
 * per-field markup. De per-field markup wordt geleverd door subclasses van
 * {@link AbstractFieldContainer}.
 * 
 * @author papegaaij
 */
public interface FieldMarkupRenderer
{
	/**
	 * Geeft de top-level markup voor de {@link AutoFieldSet}.
	 * 
	 * @param id
	 *            Het id van het panel.
	 * @param fieldSet
	 *            De fieldset die gerendered moet worden.
	 * @return De top-level markup voor de {@link AutoFieldSet}.
	 */
	public <T> Panel createFieldSetMarkup(String id, AutoFieldSet<T> fieldSet);

	/**
	 * Maakt de {@link AbstractFieldContainer} voor het gegeven field. Een implementatie
	 * van deze interface moet in ieder geval overweg kunnen met de types die gedefinieerd
	 * zijn in {@link FieldContainerType}.
	 * 
	 * @param id
	 *            Het id van de de field container.
	 * @param properties
	 *            Bevat de properties van het veld. Voor {@code FieldMarkupRenderer} is
	 *            {@link FieldProperties#getFieldContainerType()} waarschijnlijk de meest
	 *            belangrijke.
	 * @return De container voor het veld.
	 */
	public <X, Y, Z> AbstractFieldContainer<X, Y, Z> createContainer(String id,
			FieldProperties<X, Y, Z> properties);
}
