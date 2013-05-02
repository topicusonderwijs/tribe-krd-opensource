package nl.topicus.cobra.web.components.panels.bottomrow;

import nl.topicus.cobra.web.components.link.ConfirmationAjaxLink;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.security.checks.ISecurityCheck;

/**
 * Ajax button met confirmation. Let op: Dit is geen SecureComponent. Gebruik de link
 * alleen op pagina's waar de gebruiker alleen op mag komen als hij/zij voldoende rechten
 * heeft voor deze actie.
 * 
 * @author loite
 */
public abstract class AbstractAjaxConfirmationButton extends AbstractBottomRowButton
{
	private static final long serialVersionUID = 1L;

	private final String confirmMessage;

	private final ISecurityCheck securityCheck;

	public AbstractAjaxConfirmationButton(BottomRowPanel bottomRow, String label,
			String confirmMessage, CobraKeyAction action, ButtonAlignment alignment,
			ISecurityCheck securityCheck)
	{
		super(bottomRow, label, action, alignment);
		this.confirmMessage = confirmMessage;
		this.securityCheck = securityCheck;
	}

	@Override
	protected WebMarkupContainer getLink(String linkId)
	{
		ConfirmationAjaxLink<Object> link =
			new nl.topicus.cobra.web.components.link.ConfirmationAjaxLink<Object>(linkId,
				confirmMessage)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					AbstractAjaxConfirmationButton.this.onClick(target);
				}

			};
		if (securityCheck != null)
			link.setSecurityCheck(securityCheck);
		return link;
	}

	protected abstract void onClick(AjaxRequestTarget target);

}
