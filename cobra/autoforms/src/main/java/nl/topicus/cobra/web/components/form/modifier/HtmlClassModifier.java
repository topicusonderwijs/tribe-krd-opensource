package nl.topicus.cobra.web.components.form.modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class HtmlClassModifier extends MultiFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private IModel<ArrayList<String>> htmlClasses;

	public HtmlClassModifier(String htmlClass, String... propertyNames)
	{
		this(Arrays.asList(htmlClass), propertyNames);
	}

	public HtmlClassModifier(List<String> htmlClasses, String... propertyNames)
	{
		this(new Model<ArrayList<String>>(new ArrayList<String>(htmlClasses)), propertyNames);
	}

	public HtmlClassModifier(IModel<ArrayList<String>> htmlClasses, String... propertyNames)
	{
		super(Action.HTML_CLASSES, propertyNames);
		this.htmlClasses = htmlClasses;
	}

	@Override
	public <T> List<String> getHtmlClasses(AutoFieldSet<T> fieldSet, Property<T, ? , ? > property,
			FieldProperties<T, ? , ? > fieldProperties)
	{
		return htmlClasses.getObject();
	}
}
