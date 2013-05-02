package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;

import org.apache.wicket.markup.html.panel.Panel;

public class CollectiefInfoPanel<T extends Enum<T>> extends Panel
{
	private static final long serialVersionUID = 1L;

	public CollectiefInfoPanel(String id, CollectieveStatusovergangEditModel<T> model)
	{
		super(id);
		AutoFieldSet<CollectieveStatusovergangEditModel<T>> infoAutoForm =
			new AutoFieldSet<CollectieveStatusovergangEditModel<T>>("infoAutoForm", model);
		infoAutoForm.setRenderMode(RenderMode.DISPLAY);
		infoAutoForm.setPropertyNames("beginstatus", "eindstatus", "einddatum",
			"redenUitschrijving", "taxonomie");

		infoAutoForm.addFieldModifier(new VisibilityModifier(model.getEindstatus() != null,
			"beginstatus"));
		infoAutoForm.addFieldModifier(new VisibilityModifier(model.getBeginstatus() != null,
			"eindstatus"));

		infoAutoForm.addFieldModifier(new VisibilityModifier(model.getEinddatum() != null,
			"einddatum"));

		infoAutoForm.addFieldModifier(new VisibilityModifier(model.getRedenUitschrijving() != null,
			"redenUitschrijving"));

		infoAutoForm.addFieldModifier(new VisibilityModifier(model.getTaxonomie() != null,
			"taxonomie"));

		add(infoAutoForm);
	}
}
