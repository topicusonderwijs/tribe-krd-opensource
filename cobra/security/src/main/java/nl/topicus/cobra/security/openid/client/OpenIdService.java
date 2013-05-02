package nl.topicus.cobra.security.openid.client;

import java.util.List;

import nl.topicus.cobra.util.StringUtil;

import org.apache.wicket.PageParameters;
import org.apache.wicket.protocol.http.WebApplication;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.ParameterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OpenIdService
{
	private static final Logger log = LoggerFactory.getLogger(OpenIdService.class);

	/**
	 * Perform discovery on the User-Supplied identifier and return the
	 * DiscoveryInformation object that results from Association with the OP. This will
	 * probably be needed by the caller (stored in Session perhaps?).
	 * 
	 * I'm not thrilled about ConsumerManager being static, but it is very important to
	 * openid4java that the ConsumerManager object be the same instance all through a
	 * conversation (discovery, auth request, auth response) with the OP. I didn't dig
	 * terribly deeply, but suspect that part of the key exchange or the nonce uses the
	 * ConsumerManager's hash, or some other instance-specific construct to do its thing.
	 * 
	 * @param userSuppliedIdentifier
	 *            The User-Supplied identifier. It may already be normalized.
	 * 
	 * @return DiscoveryInformation - The resulting DisoveryInformation object returned by
	 *         openid4java following successful association with the OP.
	 */
	@SuppressWarnings("unchecked")
	public DiscoveryInformation performDiscoveryOnUserSuppliedIdentifier(
			String userSuppliedIdentifier)
	{
		DiscoveryInformation ret = null;

		try
		{
			// Perform discover on the User-Supplied Identifier
			List<DiscoveryInformation> discoveries =
				getConsumerManager().discover(userSuppliedIdentifier);
			// Pass the discoveries to the associate() method...
			ret = getConsumerManager().associate(discoveries);

		}
		catch (DiscoveryException e)
		{
			String message = "Error occurred during discovery!";
			log.error(message, e);
			throw new RuntimeException(message, e);
		}
		return ret;
	}

	/**
	 * Create an OpenID Auth Request, using the DiscoveryInformation object return by the
	 * openid4java library.
	 * 
	 * This method also uses the Simple Registration Extension to grant the Relying Party
	 * (RP).
	 * 
	 * @param discoveryInformation
	 *            The DiscoveryInformation that should have been previously obtained from
	 *            a call to performDiscoveryOnUserSuppliedIdentifier().
	 * 
	 * @param returnToUrl
	 *            The URL to which the OP will redirect once the authentication call is
	 *            complete.
	 * 
	 * @return AuthRequest - A "good-to-go" AuthRequest object packed with all kinds of
	 *         great OpenID goodies for the OpenID Provider (OP). The caller must take
	 *         this object and forward it on to the OP. Or call processAuthRequest() -
	 *         part of this Service Class.
	 */
	public AuthRequest createOpenIdAuthRequest(DiscoveryInformation discoveryInformation,
			String returnToUrl)
	{
		AuthRequest ret = null;
		//
		try
		{
			// Create the AuthRequest object
			ret = getConsumerManager().authenticate(discoveryInformation, returnToUrl);
		}
		catch (Exception e)
		{
			String message = "Exception occurred while building AuthRequest object!";
			log.error(message, e);
			throw new RuntimeException(message, e);
		}

		return ret;
	}

	/**
	 * Processes the returned information from an authentication request from the OP.
	 * 
	 * @param discoveryInformation
	 *            DiscoveryInformation that was created earlier in the conversation (by
	 *            openid4java). This will need to be verified with openid4java to make
	 *            sure everything went smoothly and there are no possible problems. This
	 *            object was probably stored in session and retrieved for use in calling
	 *            this method.
	 * 
	 * @param pageParameters
	 *            PageParameters passed to the page handling the return verificaion.
	 * 
	 * @param returnToUrl
	 *            The "return to" URL that was passed to the OP. It must match exactly, or
	 *            openid4java will issue a verification failed message in the logs.
	 */
	public void processReturn(DiscoveryInformation discoveryInformation,
			PageParameters pageParameters, String returnToUrl)
	{
		// Verify the Information returned from the OP
		// / This is required according to the spec
		ParameterList response = new ParameterList(pageParameters);
		try
		{
			VerificationResult verificationResult =
				getConsumerManager().verify(returnToUrl, response, discoveryInformation);
			Identifier verifiedIdentifier = verificationResult.getVerifiedId();
			String username =
				getOpenIdConverter().getUsernameByOpenId(verifiedIdentifier.getIdentifier());

			if (!StringUtil.isEmpty(username) && !StringUtil.isEmpty(verifiedIdentifier)
				&& verificationResult.getAuthResponse() instanceof AuthSuccess)
			{
				getOpenIdLoginModel().setOpenIdIdentifier(verifiedIdentifier.getIdentifier());
				getOpenIdLoginModel().setUsername(username);
			}
		}
		catch (Exception e)
		{
			String message = "Exception occurred while verifying response!";
			log.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	/**
	 * This is the manager which does a lot of work for us. The best would be to just
	 * initialize this once in your {@link WebApplication}.
	 */
	public abstract ConsumerManager getConsumerManager();

	/**
	 * This is a model which contains all the required context information about the
	 * roundtrip.
	 */
	public abstract OpenIdLoginModel getOpenIdLoginModel();

	/**
	 * This could be your local account DAO or DAH. This could be useful for when your
	 * local login procedure requires you to enter an username no matter which login
	 * option you chose (username/password, user spoofing, OpenId, tokens, etc ). Since
	 * the OpenId identifier is not the users username we need some form of converter.
	 */
	public abstract IOpenIdToUsernameConverter getOpenIdConverter();

	/**
	 * Generates the returnToUrl parameter that is passed to the OP. The User Agent (i.e.,
	 * the browser) will be directed to this page following authentication.
	 * 
	 * This is an absolute URL to the {@link OpenIdEndPage}.
	 * 
	 * @return String - the returnToUrl to be used for the authentication request.
	 */
	public abstract String getReturnToUrl();
}
