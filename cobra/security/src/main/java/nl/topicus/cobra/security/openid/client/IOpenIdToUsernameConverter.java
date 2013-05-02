package nl.topicus.cobra.security.openid.client;

/**
 * This could be useful for when your local login procedure requires you to enter an
 * username no matter which login option you chose (username/password, user spoofing,
 * OpenId, tokens, etc ). Since the OpenId identifier is not the users username we need
 * some form of converter.
 */
public interface IOpenIdToUsernameConverter
{
	public String getUsernameByOpenId(String openIdIdentifier);
}
