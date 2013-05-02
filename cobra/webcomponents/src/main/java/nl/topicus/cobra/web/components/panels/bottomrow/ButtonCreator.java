package nl.topicus.cobra.web.components.panels.bottomrow;

import java.io.Serializable;

import nl.topicus.cobra.web.components.shortcut.ActionKey;

import org.apache.wicket.markup.html.link.Link;

public abstract class ButtonCreator implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String label;

	private ActionKey action;

	private ButtonAlignment alignment;

	public ButtonCreator(String label, ActionKey action, ButtonAlignment alignment)
	{
		this.label = label;
		this.action = action;
		this.alignment = alignment;
	}

	public String getLabel()
	{
		return label;
	}

	public ActionKey getAction()
	{
		return action;
	}

	public ButtonAlignment getAlignment()
	{
		return alignment;
	}

	abstract public Link< ? > createLink(String linkId);
}
