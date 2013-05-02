package nl.topicus.cobra.web.components.form.table;

import nl.topicus.cobra.web.components.form.AbstractFieldContainer;
import nl.topicus.cobra.web.components.form.FieldProperties;

public class EduArteTableWithoutLabelMarkupRender extends EduArteTableMarkupRenderer
{
	@SuppressWarnings("hiding")
	public static final String NAME = "EduArteTableWithoutLabelMarkupRender";

	@Override
	public <X, Y, Z> AbstractFieldContainer<X, Y, Z> createContainer(String id,
			FieldProperties<X, Y, Z> properties)
	{
		return ((AbstractTableFieldContainer<X, Y, Z>) super.createContainer(id, properties))
			.setRenderLabel(false);
	}
}
