package nl.topicus.eduarte.web.pages.beheer.vasco;

import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractAjaxLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxVerwijderButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.vasco.Token;
import nl.topicus.eduarte.entities.security.authentication.vasco.TokenStatus;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

public class TokenEditModalWindowPanel extends ModalWindowBasePanel<Token>
{
	private static final long serialVersionUID = 1L;

	private Form<Token> form;

	public TokenEditModalWindowPanel(String id, CobraModalWindow<Token> modalWindow)
	{
		super(id, modalWindow);

		form = new Form<Token>("form", modalWindow.getModel());
		add(form);

		AutoFieldSet<Token> fieldset = new AutoFieldSet<Token>("fieldset", form.getModel());
		fieldset.setPropertyNames("serienummer", "status", "gebruiker");
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);
		fieldset.setOutputMarkupId(true);

		Token token = form.getModelObject();
		boolean toekenbaar =
			token.getStatus() == TokenStatus.Uitgegeven
				|| token.getStatus() == TokenStatus.Beschikbaar;
		fieldset.addFieldModifier(new EnableModifier(toekenbaar, "gebruiker"));
		form.add(fieldset);

		createComponents();
	}

	private void blokkeerToken(AjaxRequestTarget target, Token token)
	{
		token.blokkeer();
		token.commit();
		form.setModelObject(token);
		target.addComponent(TokenEditModalWindowPanel.this);
		refreshFeedback(target);
	}

	private void deblokkeerToken(AjaxRequestTarget target, Token token)
	{
		token.deblokkeer();
		token.commit();
		target.addComponent(TokenEditModalWindowPanel.this);
	}

	private void meldTokenDefect(AjaxRequestTarget target, Token token)
	{
		token.meldDefect();
		token.commit();
		target.addComponent(TokenEditModalWindowPanel.this);
	}

	private void repareerToken(AjaxRequestTarget target, Token token)
	{
		token.repareer();
		token.commit();
		target.addComponent(TokenEditModalWindowPanel.this);
	}

	private void opslaanToken(AjaxRequestTarget target, Token token)
	{
		Account gebruiker = token.getGebruiker();
		if (gebruiker != null
			&& (token.getStatus() == TokenStatus.Beschikbaar || token.getStatus() == TokenStatus.Uitgegeven))
		{
			token.neemIn();
			token.geefUit(gebruiker);
		}
		else if (token.getStatus() == TokenStatus.Uitgegeven && gebruiker == null)
		{
			token.neemIn();
		}
		token.commit();
		getModalWindow().close(target);
	}

	private void verwijderToken(AjaxRequestTarget target, Token token)
	{
		token.neemIn();
		token.setGearchiveerd(true);
		token.commit();

		getSession().info("Token " + token.getSerienummer() + " is verwijderd");
		getModalWindow().close(target);
	}

	private Token getToken()
	{
		return form.getModelObject();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new AjaxVerwijderButton(panel, "Verwijder",
			"Weet u zeker dat u dit token wilt verwijderen?")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				verwijderToken(target, getToken());
			}

			@Override
			public boolean isVisible()
			{
				return true;
			}
		});
		panel.addButton(new AbstractAjaxLinkButton(panel, "Blokkeer", CobraKeyAction.GEEN)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				blokkeerToken(target, getToken());
			}

			@Override
			public boolean isVisible()
			{
				Token token = getToken();
				return token.getStatus() != TokenStatus.Geblokkeerd
					&& token.getStatus() != TokenStatus.Defect;
			}
		});
		panel.addButton(new AbstractAjaxLinkButton(panel, "Deblokkeer", CobraKeyAction.GEEN)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				deblokkeerToken(target, getToken());
			}

			@Override
			public boolean isVisible()
			{
				return getToken().getStatus() == TokenStatus.Geblokkeerd;
			}
		});
		panel.addButton(new AbstractAjaxLinkButton(panel, "Meld defect", CobraKeyAction.GEEN)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				meldTokenDefect(target, getToken());
			}

			@Override
			public boolean isVisible()
			{
				return getToken().getStatus() != TokenStatus.Defect;
			}
		});
		panel.addButton(new AbstractAjaxLinkButton(panel, "Repareer", CobraKeyAction.GEEN)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				repareerToken(target, getToken());
			}

			@Override
			public boolean isVisible()
			{
				return getToken().getStatus() == TokenStatus.Defect;
			}
		});
		panel.addButton(new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				getModalWindow().close(target);
			}
		});
		panel.addButton(new AjaxOpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > _form)
			{
				Token token = form.getModelObject();
				opslaanToken(target, token);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > _form)
			{
				refreshFeedback(target);
			}
		}.setMakeDefault(true));
	}
}
