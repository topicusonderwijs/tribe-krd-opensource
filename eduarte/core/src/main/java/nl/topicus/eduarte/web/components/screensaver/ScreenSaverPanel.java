package nl.topicus.eduarte.web.components.screensaver;

import java.io.Serializable;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.PostProcessModifier;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.settings.LoginConfiguration;
import nl.topicus.eduarte.entities.settings.LoginSetting;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;

public class ScreenSaverPanel extends ModalWindowBasePanel<Void>
{
	private static final long serialVersionUID = 1L;

	private static class Login implements Serializable
	{
		private static final long serialVersionUID = 1L;

		@AutoForm(readOnly = true)
		private String gebruikersnaam;

		@AutoForm(editorClass = PasswordTextField.class, required = true)
		private String wachtwoord;

		public Login(String gebruikersnaam)
		{
			this.gebruikersnaam = gebruikersnaam;
		}

		@SuppressWarnings("unused")
		public String getGebruikersnaam()
		{
			return gebruikersnaam;
		}

		public String getWachtwoord()
		{
			return wachtwoord;
		}

		@SuppressWarnings("unused")
		public void setWachtwoord(String wachtwoord)
		{
			this.wachtwoord = wachtwoord;
		}
	}

	private Form<Login> form;

	private Label logoutCounter;

	public ScreenSaverPanel(String id, final ScreenSaver modalWindow)
	{
		super(id, modalWindow);
		setOutputMarkupId(true);

		WebMarkupContainer logoutLabel = new WebMarkupContainer("logoutLabel")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && modalWindow.getSetting().getSessieTimeout() > 0;
			}
		};
		add(logoutLabel);

		logoutCounter = new Label("logoutOver", new AbstractReadOnlyModel<Long>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Long getObject()
			{
				return modalWindow.getMinutesRemaining();
			}
		});
		logoutCounter.setMarkupId("screenSaverLogoutCounter");
		logoutCounter.setOutputMarkupId(true);
		logoutLabel.add(logoutCounter);

		form =
			new Form<Login>("form", new Model<Login>(new Login(EduArteContext.get().getAccount()
				.getGebruikersnaam())));
		add(form);

		AutoFieldSet<Login> fields =
			new AutoFieldSet<Login>("login", form.getModel(), "U moet opnieuw aanmelden");
		fields.setRenderMode(RenderMode.EDIT);
		fields.addFieldModifier(new PostProcessModifier("wachtwoord")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
					FieldProperties<T, ? , ? > fieldProperties)
			{
				((PasswordTextField) field).setResetPassword(true);
			}
		});
		fields.addFieldModifier(new PostProcessModifier("wachtwoord")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
					FieldProperties<T, ? , ? > fieldProperties)
			{
				field.add(new AttributeModifier("onkeypress", true, new Model<String>(
					"capLock(event);")));
			}

		});
		form.add(fields);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxOpslaanButton(panel, form, "Aanmelden")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > pform)
			{
				refreshFeedback(target);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > pform)
			{
				formSubmit(target);
			}
		}.setMakeDefault(true));
	}

	private void formSubmit(AjaxRequestTarget target)
	{
		String password = (form.getModelObject()).getWachtwoord();
		Account account = EduArteContext.get().getAccount();
		int attempts =
			EduArteApp.get()
				.getLoginAttempts(account.getGebruikersnaam(), account.getOrganisatie());
		LoginConfiguration loginConf =
			DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
				LoginSetting.class).getValue();
		if (loginConf.isActief() && attempts > loginConf.getPogingen() - 1)
		{
			// als we hier een ander veld voor gebruiken kunnen we bij
			// latere pogingen vertellen dat account geblocked is.
			account.setActief(false);
			account.save();
			account.commit();
			// signaal sturen naar app?
			error(getLocalizedMessage("exception.login.tomany.attempts",
				"To many failed login attemps, account blocked"));
			EduArteApp.get().removeLoginAttempts(account.getGebruikersnaam(),
				account.getOrganisatie());

			refreshFeedback(target);
			return;
		}

		if (account.verifyPassword(password))
		{
			target.appendJavascript("changeMaskStyle('wicket-mask-dark');");
			// zou ook transparent kunnen wezen, maar boeie
			getModalWindow().close(target);
			EduArteSession.get().setScreensaverEnabled(false);
			EduArteApp.get().removeLoginAttempts(account.getGebruikersnaam(),
				account.getOrganisatie());
		}
		else
		{
			EduArteApp.get().incrementLoginAttempt(account.getGebruikersnaam(),
				account.getOrganisatie());
			if (loginConf.isActief())
			{
				Integer max = loginConf.getPogingen();
				error("Wachtwoord incorrect, nog " + (max - attempts - 1) + " poging(en) over.");
			}
			else
				error("Wachtwoord incorrect");
			refreshFeedback(target);
		}
	}

	private String getLocalizedMessage(String key, String defaultMsg)
	{
		return Application.get().getResourceSettings().getLocalizer().getString(key, null,
			defaultMsg);
	}
}
