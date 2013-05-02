package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten.BronAlgemeenOverzichtenPanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class BronAlgemeenDetailPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public BronAlgemeenDetailPanel(String id, IModel<BronSchooljaarStatus> schooljaarStatusModel)
	{
		super(id);
		add(new BronAlgemeenStatusPanel("status", schooljaarStatusModel));
		add(new BronAlgemeenOverzichtenPanel("overzichten", schooljaarStatusModel));
	}
}
