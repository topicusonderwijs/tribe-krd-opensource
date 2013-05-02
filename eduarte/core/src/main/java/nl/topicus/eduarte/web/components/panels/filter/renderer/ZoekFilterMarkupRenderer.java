package nl.topicus.eduarte.web.components.panels.filter.renderer;

import nl.topicus.cobra.web.components.form.AbstractFieldContainer;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldContainerType;
import nl.topicus.cobra.web.components.form.FieldMarkupRenderer;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.markup.html.panel.Panel;

public class ZoekFilterMarkupRenderer implements FieldMarkupRenderer
{
	public static final String NAME = "ZoekFilterMarkupRenderer";

	@Override
	public <X> Panel createFieldSetMarkup(String id, AutoFieldSet<X> fieldSet)
	{
		return new ZoekFilterFieldSetMarkupPanel(id, fieldSet);
	}

	@Override
	public <X, Y, Z> AbstractFieldContainer<X, Y, Z> createContainer(String id,
			FieldProperties<X, Y, Z> properties)
	{
		if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_TEXT))
		{
			return new ZoekFilterInputTextEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_PASSWORD))
		{
			return new ZoekFilterInputPasswordEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_CHECKBOX))
		{
			return new ZoekFilterInputCheckboxEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.INPUT_FILE))
		{
			return new ZoekFilterInputFileEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.TEXTAREA))
		{
			return new ZoekFilterTextAreaEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.SELECT))
		{
			return new ZoekFilterSelectEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.DIV))
		{
			return new ZoekFilterDivEditorContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(FieldContainerType.LABEL))
		{
			return new ZoekFilterLabelDisplayContainer<X, Y, Z>(id, properties);
		}
		else if (properties.getFieldContainerType().equals(
			ZoekFilterMultiSelectEditorContainer.TYPE))
		{
			return new ZoekFilterMultiSelectEditorContainer<X, Y, Z>(id, properties);
		}
		throw new IllegalArgumentException("Unsupported editor container type: " + properties);
	}
}
