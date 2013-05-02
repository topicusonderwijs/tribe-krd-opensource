/*
 * Copyright (c) 2007, Topicus B.V. All rights
 * reserved.
 */
package nl.topicus.eduarte.web.pages.login;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.text.StatelessPasswordTextField;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.authorization.EduArteLogin;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.vasco.TokenDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.settings.RadiusServerSetting;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.parse.metapattern.MetaPattern;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Panel dat een inlog formulier toont voor organisaties die geen tokens afgenomen hebben
 * om toegang te verkrijgen tot EduArte. Dit formulier bevat enkel een username/password
 * combinatie.
 */
public class UsernamePasswordSignInPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private final IModel<Organisatie> organisatie;

	private final LoginEventProcessor authenticator;

	private boolean rememberMe = true;

	private String username = "";

	private String password = "";

	private String token = "";

	private boolean loginZonderToken = false;

	public UsernamePasswordSignInPanel(String id, Organisatie organisatie,
			LoginEventProcessor authenticator)
	{
		super(id);
		this.authenticator = authenticator;
		this.organisatie = ModelFactory.getModel(organisatie);

		add(new SignInForm("signInForm"));
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(organisatie);
		super.onDetach();
	}

	public Organisatie getDomain()
	{
		return organisatie.getObject();
	}

	public boolean getRememberMe()
	{
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe)
	{
		this.rememberMe = rememberMe;
	}

	public final class SignInForm extends Form<UsernamePasswordSignInPanel>
	{
		private static final long serialVersionUID = 1L;

		public SignInForm(final String id)
		{
			super(id);

			setMarkupId("loginForm");
			setOutputMarkupId(true);
			setModel(new CompoundPropertyModel<UsernamePasswordSignInPanel>(
				UsernamePasswordSignInPanel.this));

			add(new Label("organisatie", new PropertyModel<Organisatie>(
				UsernamePasswordSignInPanel.this, "domain")));
			add(new BookmarkablePageLink<Void>("wissel", EduArteApp.get().getLoginPage()));
			final WebMarkupContainer vascoTokenRefresh =
				new WebMarkupContainer("vascoTokenRefresh");
			vascoTokenRefresh.setOutputMarkupId(true);
			add(vascoTokenRefresh);
			TextField<String> usernameField = new TextField<String>("username");
			usernameField.setMarkupId("username");
			add(usernameField.setRequired(true).setPersistent(rememberMe).add(
				new AjaxFormComponentUpdatingBehavior("onchange")
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target)
					{
						target.addComponent(vascoTokenRefresh);
					}
				}));
			add(new StatelessPasswordTextField("password"));
			vascoTokenRefresh.add(new VascoTokenContainer("vascoTokenContainer"));
			vascoTokenRefresh.add(new CheckBox("rememberMe"));
			add(new OmzeilRadiusServerContainer("loginZonderTokenContainer"));
			add(new SubmitLink("aanmelden"));
		}

		@Override
		public final void onSubmit()
		{
			if (!rememberMe)
			{
				getPage().removePersistedFormData(SignInForm.class, true);
			}

			EduArteLogin login = new EduArteLogin();
			login.setUsername(username);
			login.setPassword(password);
			login.setToken(token);
			login.setOmzeilRadiusServer(loginZonderToken);

			authenticator.onLogin(login);
		}

	}

	/**
	 * Markup container voor het omzeilen van de Radius token login (indien de
	 * geselecteerde organisatie gekozen heeft voor het aanmelden via een Radius server)
	 */
	private final class OmzeilRadiusServerContainer extends WebMarkupContainer
	{
		private static final long serialVersionUID = 1L;

		@SpringBean
		private SettingsDataAccessHelper settings;

		private OmzeilRadiusServerContainer(String id)
		{
			super(id);
			add(new CheckBox("loginZonderToken").setOutputMarkupId(true));
		}

		@Override
		public boolean isVisible()
		{
			class RadiusVisible implements Runnable
			{
				@Override
				public void run()
				{
					RadiusServerSetting setting = settings.getSetting(RadiusServerSetting.class);
					setVisible(setting.getValue().isActief());
				}
			}
			EduArteContext.get().runInContext(new RadiusVisible(), getDomain());
			return super.isVisible();
		}
	}

	/**
	 * Markup container voor het kunnen invullen van de gegenereerde cijfers van een Vasco
	 * token. Enkel zichtbaar indien de geselecteerde organisatie gekozen heeft voor het
	 * authenticeren via Vasco tokens.
	 */
	private final class VascoTokenContainer extends WebMarkupContainer
	{
		private static final long serialVersionUID = 1L;

		private VascoTokenContainer(String id)
		{
			super(id);
			// setOutputMarkupPlaceholderTag(true);
			StatelessPasswordTextField tokenField = new StatelessPasswordTextField("token");
			tokenField.setType(String.class);
			tokenField.setRequired(false);
			tokenField.add(new PatternValidator(MetaPattern.DIGITS));
			tokenField.add(StringValidator.exactLength(6));
			tokenField.setLabel(new Model<String>("Gegenereerd token"));
			add(tokenField);
		}

		@Override
		public boolean isVisible()
		{
			class VascoVisible implements Runnable
			{
				@Override
				public void run()
				{
					setVisible(isModuleActief() && zijnTokensUitgegeven()
						&& !"root".equals(username));
				}

				private boolean isModuleActief()
				{
					EduArteApp app = EduArteApp.get();
					return app.isModuleActive(EduArteModuleKey.VASCO_TOKENS);
				}

				private boolean zijnTokensUitgegeven()
				{
					TokenDataAccessHelper helper =
						DataAccessRegistry.getHelper(TokenDataAccessHelper.class);
					return helper.zijnTokensUitgegeven();
				}
			}
			EduArteContext.get().runInContext(new VascoVisible(), getDomain());
			return super.isVisible();
		}
	}
}
