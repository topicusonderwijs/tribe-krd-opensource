package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.ActionKey;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;

public abstract class AbstractLinkButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public AbstractLinkButton(BottomRowPanel bottomRow, String label, ActionKey action,
			ButtonAlignment alignment)
	{
		super(bottomRow, label, action, alignment);
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				AbstractLinkButton.this.onClick();
			}
		};
	}

	protected abstract void onClick();
}
