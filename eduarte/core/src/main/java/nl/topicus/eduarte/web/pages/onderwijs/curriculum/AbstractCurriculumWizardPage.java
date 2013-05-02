package nl.topicus.eduarte.web.pages.onderwijs.curriculum;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.MenuItemKey;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractConfirmationLinkButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.web.components.menu.HomeMenu;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.CurriculumOnderwijsproductOpleidingOverzichtPage;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;

/* FIXME @RequiredSecurityCheck(OrganisatieEenheidLocatieKoppelbaarSecurityCheck.class) */
public abstract class AbstractCurriculumWizardPage extends SecurePage
{
	private CurriculumWizardModel curriculumWizardModel;

	private MenuItemKey selectedMenuItem;

	private CurriculumWizardVoortgang voortgang;

	public AbstractCurriculumWizardPage(MenuItemKey selectedMenuItem,
			CurriculumWizardModel curriculumWizardModel, CurriculumWizardVoortgang voortgang)
	{
		super(CoreMainMenuItem.Home);
		this.curriculumWizardModel = curriculumWizardModel;
		this.selectedMenuItem = selectedMenuItem;
		this.voortgang = voortgang;
		setDefaultModel(curriculumWizardModel);
	}

	public CurriculumWizardModel getCurriculumWizardModel()
	{
		return curriculumWizardModel;
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new HomeMenu(id, selectedMenuItem);
	}

	@Override
	public final Component createTitle(String id)
	{
		return new Label(id, "Curriculum Wizard Amarantis");
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachQuietly(curriculumWizardModel);
	}

	public MenuItemKey getSelectedMenuItem()
	{
		return selectedMenuItem;
	}

	public CurriculumWizardVoortgang getVoortgang()
	{
		return voortgang;
	}

	protected void voegVoltooienKnopToe(BottomRowPanel panel, final Form< ? > form)
	{
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

	private void voltooien()
	{
		getCurriculumWizardModel().save();
		setResponsePage(new CurriculumOnderwijsproductOpleidingOverzichtPage(
			getCurriculumWizardModel().getCurriculum(), new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new CurriculumWizardStap1Page();
				}

				@Override
				public Class< ? extends Page> getPageIdentity()
				{
					return CurriculumWizardStap1Page.class;
				}
			}));
	}

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
				setResponsePage(new CurriculumWizardStap1Page());
			}
		});
	}
}