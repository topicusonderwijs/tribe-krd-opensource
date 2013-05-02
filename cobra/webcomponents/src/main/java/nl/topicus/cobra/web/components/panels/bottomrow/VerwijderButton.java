package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.link.ConfirmationLink;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Standaard verwijderbutton. Let op: Dit is geen SecureComponent. Gebruik de link alleen
 * op pagina's waar de gebruiker alleen op mag komen als hij/zij voldoende rechten heeft
 * voor het verwijderen. Oftewel: Plaats deze link op de Edit-pagina van het object.
 * 
 * @author loite
 */
public abstract class VerwijderButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private String confirmMessage;

	public VerwijderButton(BottomRowPanel bottomRow)
	{
		this(bottomRow, "Verwijderen");
	}

	public VerwijderButton(BottomRowPanel bottomRow, String label)
	{
		this(bottomRow, label, "Weet u zeker dat u dit object wilt verwijderen?");
	}

	public VerwijderButton(BottomRowPanel bottomRow, String label, String confirmMessage)
	{
		this(bottomRow, label, confirmMessage, CobraKeyAction.VERWIJDEREN);
	}

	public VerwijderButton(BottomRowPanel bottomRow, String label, String confirmMessage,
			CobraKeyAction actionKey)
	{
		super(bottomRow, label, actionKey, ButtonAlignment.LEFT);
		this.confirmMessage = confirmMessage;
	}

	public String getConfirmMessage()
	{
		return confirmMessage;
	}

	public void setConfirmMessage(String confirmMessage)
	{
		this.confirmMessage = confirmMessage;
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		return new ConfirmationLink<Object>(linkId, getConfirmMessage())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				VerwijderButton.this.onClick();
			}
		};
	}

	@Override()
	public abstract boolean isVisible();

	protected abstract void onClick();

}
