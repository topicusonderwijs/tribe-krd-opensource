package nl.topicus.eduarte.dao.helpers.vasco;

import nl.topicus.cobra.dao.helpers.ZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.vasco.Token;
import nl.topicus.eduarte.zoekfilters.vasco.TokenZoekFilter;

public interface TokenDataAccessHelper extends ZoekFilterDataAccessHelper<Token, TokenZoekFilter>
{
	boolean zijnTokensUitgegeven();
}
