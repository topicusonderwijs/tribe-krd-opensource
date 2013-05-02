package nl.topicus.cobra.web.components.form.table;

import nl.topicus.cobra.web.components.form.AbstractFieldContainer;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldContainerType;
import nl.topicus.cobra.web.components.form.FieldMarkupRenderer;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * {@code TableMarkupRenderer} is een {@link FieldMarkupRenderer} die de form velden onder
 * elkaar in een table plaatst. Dit is de standaard renderer binnen DBS.
 * 
 * @author papegaaij
 */
public class TableMarkupRenderer implements FieldMarkupRenderer
{
	public static final String NAME = "TableMarkupRenderer";

	@Override
	public <T> Panel createFieldSetMarkup(String id, AutoFieldSet<T> fieldSet)
	{
		return new TableFieldSetMarkupPanel(id, fieldSet);
	}

	@Override
	public <X, Y, Z> AbstractFieldContainer<X, Y, Z> createContainer(String id,
			FieldProperties<X, Y, Z> properties)
	{
		if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_TEXT))
		{
			return new TableInputTextEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_PASSWORD))
		{
			return new TableInputPasswordEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_CHECKBOX))
		{
			return new TableInputCheckboxEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_FILE))
		{
			return new TableInputFileEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.TEXTAREA))
		{
			return new TableTextAreaEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.SELECT))
		{
			return new TableSelectEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.DIV))
		{
			return new TableDivEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.LABEL))
		{
			return new TableLabelDisplayContainer<X, Y, Z>(id, properties);
		}
		throw new IllegalArgumentException("Unsupported editor container type: " + properties);
	}
}
