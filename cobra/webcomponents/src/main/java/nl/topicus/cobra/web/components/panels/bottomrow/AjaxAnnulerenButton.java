package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;

public abstract class AjaxAnnulerenButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public AjaxAnnulerenButton(BottomRowPanel bottomRow)
	{
		super(bottomRow, "Annuleren", CobraKeyAction.ANNULEREN, ButtonAlignment.RIGHT);
	}

	public AjaxAnnulerenButton(BottomRowPanel bottomRow, String label)
	{
		super(bottomRow, label, CobraKeyAction.ANNULEREN, ButtonAlignment.RIGHT);
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
				AjaxAnnulerenButton.this.onClick(target);
			}
		};
	}

	protected abstract void onClick(AjaxRequestTarget target);
}
