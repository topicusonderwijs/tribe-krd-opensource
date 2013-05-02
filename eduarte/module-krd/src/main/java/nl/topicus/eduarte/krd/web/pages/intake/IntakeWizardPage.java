package nl.topicus.eduarte.krd.web.pages.intake;

import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.EmptyMenu;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractConfirmationLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.principals.deelnemer.DeelnemerIntakeWizard;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.DeelnemerZoekenPage;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;

@InPrincipal(DeelnemerIntakeWizard.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public abstract class IntakeWizardPage extends SecurePage implements IEditPage, VerbintenisProvider
{
	public IntakeWizardPage()
	{
		super(CoreMainMenuItem.Deelnemer);
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachFields(this);
		super.onDetach();
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new EmptyMenu(id);
	}

	public void setWizard(IntakeWizardModel model)
	{
		setDefaultModel(model);
	}

	public IntakeWizardModel getWizard()
	{
		return (IntakeWizardModel) getDefaultModel();
	}

	public ModelManager getManager()
	{
		return getWizard().getManager();
	}

	/**
	 * Slaat op en gaat naar de deelnemerkaart
	 */
	protected void voltooien()
	{
		getWizard().saveAll();
		// if (getWizard().getVerbintenisStap4() != null)
		// setResponsePage(new DeelnemerkaartPage(getWizard().getVerbintenisStap4()));
		// else
		setResponsePage(new DeelnemerkaartPage(getWizard().getDeelnemer()));
	}

	/**
	 * Voegt een voltooien-knop die opslaat en naar de opgeslagen deelnemer (verbintenis)
	 * gaat.
	 * 
	 * @param panel
	 * @param form
	 *            mag null zijn indien er geen submit gedaan hoeft te worden
	 */
	protected void voegVoltooienKnopToe(BottomRowPanel panel, final Form< ? > form)
	{
		// Geen shortcut key (alt-f10 is volgende)
		panel.addButton(new AbstractBottomRowButton(panel, "Voltooien", CobraKeyAction.GEEN,
			ButtonAlignment.RIGHT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected WebMarkupContainer getLink(String linkId)
			{
				if (form != null)
					return new SubmitLink(linkId, form)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onSubmit()
						{
							voltooien();
						}
					};
				return new Link<Void>(linkId)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick()
					{
						voltooien();
					}
				};
			}
		});
	}

	/**
	 * Voegt een annuleren-knop toe met waarschuwing
	 * 
	 * @param panel
	 */
	protected void voegAnnulerenKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new AbstractConfirmationLinkButton(panel, "Afbreken",
			CobraKeyAction.ANNULEREN, ButtonAlignment.RIGHT,
			"De gegevens zijn niet opgeslagen! Weet u zeker dat u wilt afbreken?")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				if (getWizard().getReturnPage() != null)
					setResponsePage(getWizard().getReturnPage());
				else
					setResponsePage(new DeelnemerZoekenPage());
			}
		});
	}

	protected abstract int getStapNummer();

	protected abstract String getStapTitel();

	protected int getAantalStappen()
	{
		return 4;
	}

	@Override
	public final Component createTitle(String id)
	{
		StringBuilder title = new StringBuilder();
		title.append("Intake " + EduArteApp.get().getDeelnemerTerm().toLowerCase() + " ");
		if (getWizard() != null && getWizard().getDeelnemer() != null)
		{
			Deelnemer deelnemer = getWizard().getDeelnemer();
			title.append(deelnemer.getDeelnemernummer()).append(' ');
			if (deelnemer.getPersoon().getAchternaam() != null)
				title.append(deelnemer.getPersoon().getVolledigeNaam()).append(' ');
		}
		title.append(" - Stap ").append(getStapNummer()).append(" van ").append(getAantalStappen())
			.append(" - ");
		title.append(getStapTitel());

		return new Label(id, title.toString());
	}

	@Override
	public Verbintenis getVerbintenis()
	{
		return getWizard().getVerbintenis();
	}
}
