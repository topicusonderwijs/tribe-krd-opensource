package nl.topicus.eduarte.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;

/**
 * Standaard voltooien. Let op: Dit is geen SecureComponent.
 * 
 * @author loite
 */
public abstract class VoltooienButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	public VoltooienButton(BottomRowPanel bottomRow)
	{
		this(bottomRow, "Voltooien");
	}

	public VoltooienButton(BottomRowPanel bottomRow, String label)
	{
		super(bottomRow, label, CobraKeyAction.LINKKNOP1, ButtonAlignment.RIGHT);
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
				VoltooienButton.this.onClick();
			}

		};
	}

	/**
	 * Methode die aangeroepen wordt bij het klikken op de link na het bevestigen van de
	 * actie.
	 */
	protected abstract void onClick();

}
