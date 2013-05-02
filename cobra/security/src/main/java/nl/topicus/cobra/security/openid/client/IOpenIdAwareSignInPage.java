package nl.topicus.cobra.security.openid.client;

/**
 * Interface om de functie doOpenIdLogin te exposen voor de {@link OpenIdEndPage} page.
 * 
 * @author hoeve
 */
public interface IOpenIdAwareSignInPage
{
	/**
	 * Deze functie moet @ClientSideCallable zijn zodat de OpenIdPopup het implementerende
	 * page kan informeren over de OpenId login attempt.
	 */
	public void doOpenIdLogin();
}
