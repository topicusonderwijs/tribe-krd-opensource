package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.BijBestaandeKeuzeEnum;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.DeelnemerCollectieveAfnameContext;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class BijBestKeuzeColumnPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public BijBestKeuzeColumnPanel(String id, IModel<DeelnemerCollectieveAfnameContext> model)
	{
		super(id);
		EnumCombobox<BijBestaandeKeuzeEnum> combo =
			new EnumCombobox<BijBestaandeKeuzeEnum>("bijBestaandeKeuzeEnum",
				new PropertyModel<BijBestaandeKeuzeEnum>(model, "bijBestaandeKeuzeEnum"),
				BijBestaandeKeuzeEnum.values());
		add(combo);
		setRenderBodyOnly(true);
	}
}
