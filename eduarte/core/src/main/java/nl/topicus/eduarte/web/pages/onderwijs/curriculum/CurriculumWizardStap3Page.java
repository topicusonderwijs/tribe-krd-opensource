package nl.topicus.eduarte.web.pages.onderwijs.curriculum;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VolgendeButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VorigeButton;
import nl.topicus.eduarte.core.principals.onderwijs.CurriculumWizard;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

@InPrincipal(CurriculumWizard.class)
@PageInfo(title = "Curriculum Wizard Stap 3", menu = {"Home > Amarantis > Curriculum Wizard > Volgende > Volgende"})
public class CurriculumWizardStap3Page extends AbstractCurriculumWizardPage
{
	private Form<Void> form;

	public CurriculumWizardStap3Page(CurriculumWizardModel curriculumWizardModel)
	{
		super(HomeMenuItem.AmarantisCurriculumWizard, curriculumWizardModel,
			CurriculumWizardVoortgang.Stap3);

		form = new Form<Void>("form");

		form.add(new CurriculumWizardInfoPanel("infopanel", curriculumWizardModel, getVoortgang()));

		AutoFieldSet<Onderwijsproduct> autoFieldSetLeft =
			new AutoFieldSet<Onderwijsproduct>("autoform", new PropertyModel<Onderwijsproduct>(
				getCurriculumWizardModel(), "onderwijsproduct"));

		autoFieldSetLeft.setPropertyNames("belasting", "belastingEC", "aantalWeken",
			"frequentiePerWeek", "tijdPerEenheid", "onderwijstijd", "individueel", "onafhankelijk",
			"begeleid");

		autoFieldSetLeft.setSortAccordingToPropertyNames(true);
		autoFieldSetLeft.setRenderMode(RenderMode.EDIT);

		autoFieldSetLeft.addFieldModifier(new RequiredModifier(true, "belasting", "aantalWeken",
			"frequentiePerWeek", "tijdPerEenheid"));

		autoFieldSetLeft.addFieldModifier(new EduArteAjaxRefreshModifier("tijdPerEenheid",
			"onderwijstijd"));

		/* Forceren dat model geupdate wordt */
		autoFieldSetLeft.addFieldModifier(new EduArteAjaxRefreshModifier("belastingEC"));
		autoFieldSetLeft.addFieldModifier(new EduArteAjaxRefreshModifier("belasting"));
		autoFieldSetLeft.addFieldModifier(new EduArteAjaxRefreshModifier("aantalWeken",
			"onderwijstijd"));
		autoFieldSetLeft.addFieldModifier(new EduArteAjaxRefreshModifier("frequentiePerWeek",
			"onderwijstijd"));

		form.add(autoFieldSetLeft);

		add(form);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new VorigeButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVorige()
			{
				setResponsePage(new CurriculumWizardStap2Page(getCurriculumWizardModel()));
			}

		});
		panel.addButton(new VolgendeButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVolgende()
			{
				setResponsePage(new CurriculumWizardStap4Page(getCurriculumWizardModel()));
			}
		});
		voegAnnulerenKnopToe(panel);
	}
}
