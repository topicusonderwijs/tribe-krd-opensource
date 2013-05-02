package nl.topicus.eduarte.web.components.panels;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.modifier.SingleFieldAdapter;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Geeft aan de AutoFieldSet door dat voor deze property een andere Class moet worden
 * gebruikt als component.
 * 
 * @author hoeve
 */
public class DocumentTemplateFieldAdapter extends SingleFieldAdapter
{
	private static final long serialVersionUID = 1L;

	public DocumentTemplateFieldAdapter(String propertyName, Action action)
	{
		super(propertyName, action);
	}

	@Override
	public <T> Class< ? extends Component> getFieldType(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		return DocumentTemplateLinkPanel.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Component createField(AutoFieldSet<T> fieldSet, String id, IModel< ? > model,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		return new DocumentTemplateLinkPanel<DocumentTemplate>(id,
			(IModel<DocumentTemplate>) fieldSet.getModel());
	}

	@Override
	public <T> IModel<String> getLabel(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		return new Model<String>("Bestand");
	}
}
