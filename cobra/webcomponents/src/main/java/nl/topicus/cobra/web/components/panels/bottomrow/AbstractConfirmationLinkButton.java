package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.link.ConfirmationLink;
import nl.topicus.cobra.web.components.shortcut.ActionKey;

import org.apache.wicket.markup.html.WebMarkupContainer;

public abstract class AbstractConfirmationLinkButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private String confirmationMessage;

	private ConfirmationLink<Object> link = null;

	public AbstractConfirmationLinkButton(BottomRowPanel bottomRow, String label, ActionKey action,
			ButtonAlignment alignment, String confirmationMessage)
	{
		super(bottomRow, label, action, alignment);
		this.confirmationMessage = confirmationMessage;
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		link = new ConfirmationLink<Object>(linkId, getConfirmationMessage())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				AbstractConfirmationLinkButton.this.onClick();
			}
		};
		return link;
	}

	abstract protected void onClick();

	public String getConfirmationMessage()
	{
		return confirmationMessage;
	}

	public void setConfirmationMessage(String confirmationMessage)
	{
		this.confirmationMessage = confirmationMessage;
		if (link != null)
			link.setConfirmation(confirmationMessage);
	}
}
