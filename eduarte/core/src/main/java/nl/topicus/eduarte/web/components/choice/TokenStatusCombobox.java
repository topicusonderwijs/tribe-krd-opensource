package nl.topicus.eduarte.web.components.choice;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.eduarte.entities.security.authentication.vasco.TokenStatus;

import org.apache.wicket.model.IModel;

/**
 * Toont een selectie van Vasco token statussen.
 */
public class TokenStatusCombobox extends EnumCombobox<TokenStatus>
{
	private static final long serialVersionUID = 1L;

	public TokenStatusCombobox(String id)
	{
		super(id, TokenStatus.values());
	}

	public TokenStatusCombobox(String id, IModel<TokenStatus> model)
	{
		super(id, model, TokenStatus.values());
	}
}
