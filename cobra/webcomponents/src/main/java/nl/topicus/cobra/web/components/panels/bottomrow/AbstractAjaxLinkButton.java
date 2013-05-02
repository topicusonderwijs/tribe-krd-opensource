package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.ActionKey;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;

public abstract class AbstractAjaxLinkButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public AbstractAjaxLinkButton(BottomRowPanel panel, String label, ActionKey action)
	{
		this(panel, label, action, ButtonAlignment.RIGHT);
	}

	public AbstractAjaxLinkButton(BottomRowPanel bottomRow, String label, ActionKey action,
			ButtonAlignment alignment)
	{
		super(bottomRow, label, action, alignment);
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		return new AjaxLink<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				AbstractAjaxLinkButton.this.onClick(target);
			}
		};
	}

	protected abstract void onClick(AjaxRequestTarget target);

}
