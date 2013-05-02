package nl.topicus.cobra.security.openid.client;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OpenIdEndPage extends WebPage
{
	private static final Logger log = LoggerFactory.getLogger(OpenIdEndPage.class);

	public OpenIdEndPage()
	{
		this(new PageParameters());
	}

	public OpenIdEndPage(PageParameters pageParameters)
	{
		if (!pageParameters.isEmpty())
		{
			log.info("OpenId roundtrip processing...");

			//
			// If this is a return trip (the OP will redirect here once authentication
			// / is complete), then verify the response. If it looks good, send the
			// / user to the RegistrationSuccessPage. Otherwise, display a message.
			//
			Boolean isReturn = pageParameters.getBoolean("is_return");
			if (isReturn)
			{
				log.info("OpenId roundtrip validating...");
				//
				// Delegate to the Service object to do verification. It will return
				// / the RegistrationModel to use to display the information that was
				// / retrieved from the OP about the User-Supplied identifier. The
				// / RegistrationModel reference will be null if there was a problem
				// / (check the logs for more information if this happens).
				//
				getOpenIdService().processReturn(getOpenIdLoginModel().getDiscoveryInformation(),
					pageParameters, getOpenIdService().getReturnToUrl());

				log.info("OpenId roundtrip validation complete.");
			}
		}
	}

	public abstract OpenIdService getOpenIdService();

	/**
	 * This is a model which contains all the required context information about the
	 * roundtrip.
	 */
	public abstract OpenIdLoginModel getOpenIdLoginModel();
}
