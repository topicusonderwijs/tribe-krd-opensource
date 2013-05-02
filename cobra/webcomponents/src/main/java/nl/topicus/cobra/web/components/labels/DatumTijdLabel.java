package nl.topicus.cobra.web.components.labels;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class DatumTijdLabel extends Label
{
	private static final long serialVersionUID = 1L;

	public DatumTijdLabel(String id)
	{
		super(id);
	}

	public DatumTijdLabel(String id, IModel<Date> model)
	{
		super(id, model);
	}

	@Override
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		replaceComponentTagBody(markupStream, openTag, TimeUtil.getInstance()
			.formatDateTimeNoSeconds(getModelObject()));
	}

	public Date getModelObject()
	{
		return (Date) getDefaultModelObject();
	}

}
