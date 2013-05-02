package nl.topicus.cobra.security.openid.client;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OpenIdStartPage extends WebPage
{
	private static final Logger log = LoggerFactory.getLogger(OpenIdStartPage.class);

	public OpenIdStartPage()
	{
		super(new CompoundPropertyModel<OpenIdLoginModel>(new OpenIdLoginModel()));

		Form< ? > form = new Form<Void>("form");
		form.add(new AjaxLink<Void>("google")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				OpenIdStartPage.this.getModelObject().setOpenId(
					"https://www.google.com/accounts/o8/id");
				onSubmit();
			}
		});

		add(form);
	}

	// This is the "business end" of making the authentication request.
	// / The sequence of interaction with the OP is really hidden from us
	// / here by using RegistrationService.
	public void onSubmit()
	{
		// Delegate to Open ID code
		String userSuppliedIdentifier = OpenIdStartPage.this.getModel().getObject().getOpenId();
		log.info("OpenId User Supplied Identifier found: " + userSuppliedIdentifier);

		DiscoveryInformation discoveryInformation =
			getOpenIdService().performDiscoveryOnUserSuppliedIdentifier(userSuppliedIdentifier);

		// Store the disovery results in session.
		OpenIdLoginModel openidmodel = new OpenIdLoginModel();
		openidmodel.setDiscoveryInformation(discoveryInformation);
		setOpenIdLoginModel(openidmodel);

		// Create the AuthRequest
		AuthRequest authRequest =
			getOpenIdService().createOpenIdAuthRequest(discoveryInformation,
				getOpenIdService().getReturnToUrl());
		// Now take the AuthRequest and forward it on to the OP
		getRequestCycle().setRedirect(false);
		getResponse().redirect(authRequest.getDestinationUrl(true));
	}

	public abstract OpenIdService getOpenIdService();

	/**
	 * This is a model which contains all the required context information about the
	 * roundtrip.
	 */
	public abstract OpenIdLoginModel getOpenIdLoginModel();

	/**
	 * This is a model which contains all the required context information about the
	 * roundtrip. This needs to be set only once per roundtrip.
	 */
	public abstract void setOpenIdLoginModel(OpenIdLoginModel model);

	@SuppressWarnings("unchecked")
	public IModel<OpenIdLoginModel> getModel()
	{
		return (IModel<OpenIdLoginModel>) getDefaultModel();
	}

	public OpenIdLoginModel getModelObject()
	{
		return getModel().getObject();
	}
}
