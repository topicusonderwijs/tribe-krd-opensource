package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.OnderwijsproductAfnameContextListModel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class DeelnemerNaamColumnPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public DeelnemerNaamColumnPanel(String id, IModel<OnderwijsproductAfnameContextListModel> model)
	{
		super(id);
		Verbintenis verbintenis = model.getObject().getVerbintenis();
		add(new Label("label", verbintenis.getDeelnemer().getPersoon().getVolledigeNaam()));
		setRenderBodyOnly(true);
	}
}
