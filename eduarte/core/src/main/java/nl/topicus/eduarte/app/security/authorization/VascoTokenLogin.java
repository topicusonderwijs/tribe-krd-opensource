package nl.topicus.eduarte.app.security.authorization;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.helpers.vasco.TokenDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.vasco.Token;
import nl.topicus.eduarte.entities.security.authentication.vasco.TokenStatus;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.zoekfilters.vasco.TokenZoekFilter;

import org.apache.wicket.security.authentication.LoginException;

/**
 * LoginStrategy dat de gebruiker aanmeldt via een Vasco server. Het gegenereerde token
 * wordt geverifieerd bij de vasco server. Deze methode wordt alleen gebruikt wanneer een
 * instelling deze module heeft afgenomen en er tokens ingelezen zijn.
 */
public class VascoTokenLogin implements LoginStrategy
{
	private final String token;

	private final LoginStrategy chain;

	private Account account;

	public VascoTokenLogin(String token, LoginStrategy chain)
	{
		this.token = token;
		this.chain = chain;
	}

	@Override
	public Account login() throws LoginException
	{
		account = chain.login();

		if (account != null && isVascoModuleActief() && zijnTokensUitgegeven()
			&& account.getAuthorisatieNiveau() != AuthorisatieNiveau.SUPER)
		{
			Token tokenAccount = getToken();
			if (tokenAccount.getStatus() == TokenStatus.Geblokkeerd)
			{
				throw new LoginException("Het gekoppelde token is geblokkeerd");
			}
			if (tokenAccount.getStatus() == TokenStatus.Defect)
			{
				throw new LoginException("Het gekoppelde token staat als defect geregistreerd");
			}

			if (!tokenAccount.verifieerPassword(this.token))
			{
				return null;
			}
		}
		return account;
	}

	private boolean isVascoModuleActief()
	{
		return EduArteApp.get().isModuleActive(EduArteModuleKey.VASCO_TOKENS);
	}

	private boolean zijnTokensUitgegeven()
	{
		TokenDataAccessHelper helper = DataAccessRegistry.getHelper(TokenDataAccessHelper.class);

		return helper.zijnTokensUitgegeven();
	}

	private Token getToken() throws LoginException
	{
		TokenDataAccessHelper helper = DataAccessRegistry.getHelper(TokenDataAccessHelper.class);

		TokenZoekFilter filter = new TokenZoekFilter();
		filter.setAccount(account);
		List<Token> tokens = helper.list(filter);

		if (tokens.isEmpty())
		{
			throw new LoginException("Er is geen token toegekend aan dit account");
		}

		if (tokens.size() > 1)
		{
			throw new LoginException("Meer dan één token gevonden bij dit account");
		}

		Token tokenAccount = tokens.get(0);
		return tokenAccount;
	}
}
