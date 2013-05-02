package nl.topicus.eduarte.web.pages.onderwijs.curriculum;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VolgendeButton;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.core.principals.onderwijs.CurriculumWizard;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

@InPrincipal(CurriculumWizard.class)
@PageInfo(title = "Curriculum Wizard Stap 2", menu = {"Home > Amarantis > Curriculum Wizard > Volgende"})
public class CurriculumWizardStap2Page extends AbstractCurriculumWizardPage
{
	private Form<Void> form;

	public CurriculumWizardStap2Page(CurriculumWizardModel curriculumWizardModel)
	{
		super(HomeMenuItem.AmarantisCurriculumWizard, curriculumWizardModel,
			CurriculumWizardVoortgang.Stap2);

		curriculumWizardModel.createOnderwijsproduct();

		form = new Form<Void>("form");

		form.add(new CurriculumWizardInfoPanel("infopanel", curriculumWizardModel, getVoortgang()));

		AutoFieldSet<Onderwijsproduct> autoform =
			new AutoFieldSet<Onderwijsproduct>("autoform", new PropertyModel<Onderwijsproduct>(
				getCurriculumWizardModel(), "onderwijsproduct"));
		autoform.setRenderMode(RenderMode.EDIT);
		autoform.setSortAccordingToPropertyNames(true);
		autoform.setPropertyNames("code", "titel", "omschrijving", "soortProduct", "begindatum",
			"einddatum", "aggregatieniveau", "niveauaanduiding", "typeToets", "juridischEigenaar");

		autoform.addFieldModifier(new VisibilityModifier(getAggregatieNiveauZichtbaarModel(),
			"aggregatieniveau"));

		autoform.addModifier("code", new UniqueConstraintValidator<String>(autoform,
			"Onderwijsproduct", "code", "organisatie"));

		form.add(autoform);
		add(form);
		createComponents();
	}

	private IModel<Boolean> getAggregatieNiveauZichtbaarModel()
	{
		return new LoadableDetachableModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Boolean load()
			{
				return getCurriculumWizardModel().getOnderwijsproduct().getAggregatieniveau() == null;
			}
		};
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
				setResponsePage(new CurriculumWizardStap3Page(getCurriculumWizardModel()));
			}
		});
		voegAnnulerenKnopToe(panel);
	}
}
