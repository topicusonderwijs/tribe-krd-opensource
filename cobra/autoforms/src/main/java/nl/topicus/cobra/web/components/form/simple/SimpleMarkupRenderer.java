package nl.topicus.cobra.web.components.form.simple;

import nl.topicus.cobra.web.components.form.AbstractFieldContainer;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldContainerType;
import nl.topicus.cobra.web.components.form.FieldMarkupRenderer;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.markup.html.panel.Panel;

public class SimpleMarkupRenderer implements FieldMarkupRenderer
{
	public static final String NAME = "SimpleMarkupRenderer";

	@Override
	public <T> Panel createFieldSetMarkup(String id, AutoFieldSet<T> fieldSet)
	{
		return new SimpleFieldSetMarkupPanel(id, fieldSet);
	}

	@Override
	public <X, Y, Z> AbstractFieldContainer<X, Y, Z> createContainer(String id,
			FieldProperties<X, Y, Z> properties)
	{
		if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_TEXT))
		{
			return new SimpleInputTextEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_PASSWORD))
		{
			return new SimpleInputPasswordEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_CHECKBOX))
		{
			return new SimpleInputCheckboxEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_FILE))
		{
			return new SimpleInputFileEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.TEXTAREA))
		{
			return new SimpleTextAreaEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.SELECT))
		{
			return new SimpleSelectEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.DIV))
		{
			return new SimpleDivEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.LABEL))
		{
			return new SimpleLabelDisplayContainer<X, Y, Z>(id, properties);
		}
		throw new IllegalArgumentException("Unsupported editor container type: " + properties);
	}
}
