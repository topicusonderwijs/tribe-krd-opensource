package nl.topicus.eduarte.resultaten.web.components.structuur;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

public class ToetsBoomPanel extends TypedPanel<Toets>
{
	private static final long serialVersionUID = 1L;

	public ToetsBoomPanel(String id, IModel<Toets> model)
	{
		super(id, model);

		Label label = new Label("code", model.getObject().getCodeVoorWeergave());
		add(label);

		WebMarkupContainer radio = new WebMarkupContainer("radio");
		radio.setOutputMarkupId(true);
		radio.setVisible(isSelectable());
		add(radio);

		if (isSelectable())
			label.add(new SimpleAttributeModifier("for", radio.getMarkupId()));
		if (isSelected())
			radio.add(new SimpleAttributeModifier("checked", "checked"));
	}

	protected boolean isSelected()
	{
		return false;
	}

	protected boolean isSelectable()
	{
		return false;
	}
}
