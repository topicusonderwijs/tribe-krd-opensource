package nl.topicus.eduarte.web.components.panels.bijlage;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.web.components.modalwindow.BijlageLink;

import org.apache.wicket.model.IModel;

public class BijlageLinkPanel extends TypedPanel<Bijlage>
{
	private static final long serialVersionUID = 1L;

	public BijlageLinkPanel(String id, IModel<Bijlage> model)
	{
		super(id, model);
		add(new BijlageLink<Bijlage>("link", model));
	}
}
