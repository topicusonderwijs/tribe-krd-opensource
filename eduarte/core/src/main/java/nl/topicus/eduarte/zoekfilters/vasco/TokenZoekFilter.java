package nl.topicus.eduarte.zoekfilters.vasco;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.vasco.Token;
import nl.topicus.eduarte.entities.security.authentication.vasco.TokenStatus;
import nl.topicus.eduarte.web.components.choice.TokenStatusCombobox;
import nl.topicus.eduarte.web.components.quicksearch.account.AccountSearchEditor;
import nl.topicus.eduarte.zoekfilters.AbstractZoekFilter;

import org.apache.wicket.model.IModel;

public class TokenZoekFilter extends AbstractZoekFilter<Token>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_180")
	private String serienummer;

	@AutoForm(editorClass = AccountSearchEditor.class)
	private IModel<Account> account;

	@AutoForm(label = "Status", editorClass = TokenStatusCombobox.class, description = "Status van het token")
	private TokenStatus status;

	public String getSerienummer()
	{
		return serienummer;
	}

	public void setSerienummer(String serienummer)
	{
		this.serienummer = serienummer;
	}

	public Account getAccount()
	{
		return getModelObject(account);
	}

	public void setAccount(Account account)
	{
		this.account = makeModelFor(account);
	}

	public TokenStatus getStatus()
	{
		return status;
	}

	public void setStatus(TokenStatus status)
	{
		this.status = status;
	}
}
