package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.components.shortcut.ShortcutEnabledComponent;
import nl.topicus.cobra.web.components.shortcut.ShortcutRegister;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * Basis voor alle knoppen die aan de onderkant van de pagina komen te staan. Deze knoppen
 * zijn veelal actieknoppen zoals opslaan, bewerken, verwijderen en annuleren.
 */
public abstract class AbstractBottomRowButton extends Panel implements ShortcutEnabledComponent
{
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer link;

	private String label;

	private ActionKey action;

	private ButtonAlignment alignment;

	public AbstractBottomRowButton(BottomRowPanel bottomRow, String label, ActionKey action,
			ButtonAlignment alignment)
	{
		super(bottomRow.getNextButtonId());
		this.label = label;
		this.action = action;
		this.alignment = alignment;
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		if (!hasBeenRendered())
		{
			addOrReplace(link = getLink("link"));
			link.add(getTitleModifier());
			link.add(new Label("label", new AbstractReadOnlyModel<String>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject()
				{
					return getLabel();
				}
			}));
		}
		link.setEnabled(isEnabled());
	}

	protected AttributeModifier getTitleModifier()
	{
		return new AttributeModifier("title", true, new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				String returnString = getLabel();
				if (getAction() != null && StringUtil.isNotEmpty(getAction().getShortKeys()))
					returnString += " (" + getAction().getShortKeys() + ")";
				return returnString;
			}
		});
	}

	protected abstract WebMarkupContainer getLink(String linkId);

	public String getLabel()
	{
		return label;
	}

	public AbstractBottomRowButton setLabel(String label)
	{
		this.label = label;
		return this;
	}

	public ActionKey getAction()
	{
		return action;
	}

	public AbstractBottomRowButton setAction(ActionKey action)
	{
		this.action = action;
		return this;
	}

	public ButtonAlignment getAlignment()
	{
		return alignment;
	}

	public AbstractBottomRowButton setAlignment(ButtonAlignment alignment)
	{
		this.alignment = alignment;
		return this;
	}

	@Override
	public void registerShortcuts(ShortcutRegister register)
	{
		ActionKey actionKey = getAction();
		if (actionKey != null && !CobraKeyAction.GEEN.equals(actionKey) && link != null)
			register.registerShortcut(link, actionKey);
	}
}
