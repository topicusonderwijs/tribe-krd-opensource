package nl.topicus.eduarte.web.pages.login;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.text.StatelessPasswordTextField;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.authorization.EduArteLogin;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.web.components.choice.OrganisatieCombobox;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class LandelijkBeheerSignInPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private final IModel<Organisatie> organisatie;

	private final LoginEventProcessor authenticator;

	public LandelijkBeheerSignInPanel(String id, Organisatie organisatie,
			LoginEventProcessor authenticator)
	{
		super(id);

		this.organisatie = ModelFactory.getModel(organisatie);
		this.authenticator = authenticator;

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

	public final class SignInForm extends Form<SignInForm>
	{
		private static final long serialVersionUID = 1L;

		private boolean rememberMe = true;

		private String username = "";

		private String password = "";

		private String aanmeldenAls = "";

		private IModel<Organisatie> aanmeldenOp;

		public SignInForm(final String id)
		{
			super(id);

			setModel(new CompoundPropertyModel<SignInForm>(this));
			setMarkupId("loginForm");
			setOutputMarkupId(true);
			aanmeldenOp = ModelFactory.getModel(null);

			add(new Label("organisatie", new PropertyModel<Organisatie>(
				LandelijkBeheerSignInPanel.this, "domain")));
			add(new BookmarkablePageLink<Void>("wissel", EduArteApp.get().getLoginPage()));
			add(new TextField<String>("username").setRequired(true).setPersistent(rememberMe));
			add(new StatelessPasswordTextField("password"));
			add(new TextField<String>("aanmeldenAls"));
			add(new OrganisatieCombobox("aanmeldenOp", new PropertyModel<Organisatie>(this,
				"aanmeldenOp"), false));
			add(new CheckBox("rememberMe", new PropertyModel<Boolean>(this, "rememberMe")));
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
			login.setAanmeldenAls(aanmeldenAls);
			login.setAanmeldenOp(getAanmeldenOp());

			authenticator.onLogin(login);
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(aanmeldenOp);
			super.onDetach();
		}

		public boolean getRememberMe()
		{
			return rememberMe;
		}

		public void setRememberMe(boolean rememberMe)
		{
			this.rememberMe = rememberMe;
		}

		public Organisatie getAanmeldenOp()
		{
			return aanmeldenOp.getObject();
		}

		public void setAanmeldenOp(Organisatie aanmeldenOp)
		{
			this.aanmeldenOp = ModelFactory.getModel(aanmeldenOp);
		}
	}
}
