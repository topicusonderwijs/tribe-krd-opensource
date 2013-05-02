package nl.topicus.cobra.security.openid.client;

/**
 * Interface om de functie doOpenIdLogin te exposen voor de {@link OpenIdEndPage} page.
 * 
 * @author hoeve
 */
public interface IOpenIdAwareSignInPanel
{
	/**
	 * Roep deze functie aan vanaf de {@link IOpenIdAwareSignInPage} zodat de panel de
	 * submit kan doen van het form.
	 */
	public void doOpenIdLogin();
}
