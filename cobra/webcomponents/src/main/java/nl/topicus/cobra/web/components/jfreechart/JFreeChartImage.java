package nl.topicus.cobra.web.components.jfreechart;

import org.apache.wicket.Resource;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.jfree.chart.JFreeChart;

/**
 * Image van JFreeChart charts voor gebruik in Wicket-applicaties. Gebaseerd op
 * gelijknamig component uit de Competentiemeter.
 * 
 * @author loite
 * @author papegaaij
 */
public class JFreeChartImage extends NonCachingImage
{
	private static final long serialVersionUID = 1L;

	private int width;

	private int height;

	public JFreeChartImage(String id, IModel<JFreeChart> chartModel, int width, int height)
	{
		super(id, chartModel);

		this.width = width;
		this.height = height;
	}

	@Override
	protected Resource getImageResource()
	{
		return new DynamicImageResource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected byte[] getImageData()
			{
				JFreeChart chart = (JFreeChart) getDefaultModelObject();

				return toImageData(chart.createBufferedImage(getWidth(), getHeight()));
			}

			@Override
			protected void setHeaders(WebResponse response)
			{
				if (isCacheable())
				{
					super.setHeaders(response);
				}
				else
				{
					response.setHeader("Pragma", "no-cache");
					response.setHeader("Cache-Control", "no-cache");
					response.setDateHeader("Expires", 0);
				}
			}
		};
	}

	protected int getHeight()
	{
		return height;
	}

	protected int getWidth()
	{
		return width;
	}
}
