package nl.topicus.eduarte.web.pages.onderwijs.curriculum;

import java.util.Arrays;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VolgendeButton;
import nl.topicus.eduarte.core.principals.onderwijs.CurriculumWizard;
import nl.topicus.eduarte.web.components.autoform.OpleidingOrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;

import org.apache.wicket.markup.html.form.Form;

@InPrincipal(CurriculumWizard.class)
@PageInfo(title = "Curriculum Wizard Stap 1", menu = {"Home > Amarantis > Curriculum Wizard"})
public class CurriculumWizardStap1Page extends AbstractCurriculumWizardPage
{
	private Form<Void> form;

	public CurriculumWizardStap1Page()
	{
		this(new CurriculumWizardModel());
	}

	public CurriculumWizardStap1Page(CurriculumWizardModel curriculumWizardModel)
	{
		super(HomeMenuItem.AmarantisCurriculumWizard, curriculumWizardModel,
			CurriculumWizardVoortgang.Stap1);

		form = new Form<Void>("form");

		final AutoFieldSet<CurriculumWizardModel> autoform =
			new AutoFieldSet<CurriculumWizardModel>("autoform", getCurriculumWizardModel());

		autoform.setPropertyNames(Arrays.asList("organisatieEenheid", "locatie", "opleiding",
			"cohort"));
		autoform.setSortAccordingToPropertyNames(true);
		autoform.setRenderMode(RenderMode.EDIT);

		autoform.addFieldModifier(new OpleidingOrganisatieEenheidLocatieFieldModifier());

		form.add(autoform);

		add(form);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new VolgendeButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVolgende()
			{
				setResponsePage(new CurriculumWizardStap2Page(getCurriculumWizardModel()));
			}
		});
		voegAnnulerenKnopToe(panel);
	}

}
