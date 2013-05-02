package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import java.util.Date;

import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.DeelnemerCollectieveAfnameContext;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ProductDatumColumnPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public ProductDatumColumnPanel(String id, final IModel<DeelnemerCollectieveAfnameContext> model)
	{
		super(id);

		add(new DatumField("beginDatum", new PropertyModel<Date>(model, "beginDatum"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired()
			{
				DeelnemerCollectieveAfnameContext context = model.getObject();
				return context.getOnderwijsproduct() != null;
			}
		});
		add(new DatumField("eindDatum", new PropertyModel<Date>(model, "eindDatum")));
		setRenderBodyOnly(true);
	}
}
