package nl.topicus.cobra.web.components.wiquery;

import java.awt.Color;

import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class Colordisplay extends TypedPanel<Color>
{
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer display;

	public Colordisplay(String id, IModel<Color> colorModel)
	{
		super(id, colorModel);
		display = new WebMarkupContainer("colordisplay");
		add(display);
		display.add(new AttributeAppender("style", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				Color color = getModelObject();
				return "background-color: " + getHtmlColorCode(color.darker());
			}
		}, ";"));
	}

	public static String getHtmlColorCode(Color color)
	{
		return "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
	}
}
