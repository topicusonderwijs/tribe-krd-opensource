package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.link.ConfirmationAjaxLink;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Ajax verwijderbutton. Let op: Dit is geen SecureComponent. Gebruik de link alleen op
 * pagina's waar de gebruiker alleen op mag komen als hij/zij voldoende rechten heeft voor
 * het verwijderen. Oftewel: Plaats deze link op de Edit-pagina van het object.
 * 
 * @author loite
 */
public abstract class AjaxVerwijderButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private final String confirmMessage;

	public AjaxVerwijderButton(BottomRowPanel bottomRow)
	{
		this(bottomRow, "Verwijderen");
	}

	public AjaxVerwijderButton(BottomRowPanel bottomRow, String label)
	{
		this(bottomRow, label, "Weet u zeker dat u dit object wilt verwijderen?");
	}

	public AjaxVerwijderButton(BottomRowPanel bottomRow, String label, String confirmMessage)
	{
		super(bottomRow, label, CobraKeyAction.VERWIJDEREN, ButtonAlignment.LEFT);
		this.confirmMessage = confirmMessage;
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		return new ConfirmationAjaxLink<Void>(linkId, confirmMessage)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				AjaxVerwijderButton.this.onClick(target);
			}

		};
	}

	@Override()
	public abstract boolean isVisible();

	protected abstract void onClick(AjaxRequestTarget target);

}
