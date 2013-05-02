package nl.topicus.eduarte.web.pages.onderwijs.curriculum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoFieldSet;

import org.apache.wicket.markup.html.panel.Panel;

public class CurriculumWizardInfoPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	public CurriculumWizardInfoPanel(String id, CurriculumWizardModel model,
			CurriculumWizardVoortgang voortgang)
	{
		super(id);

		AutoFieldSet<CurriculumWizardModel> autoform =
			new AutoFieldSet<CurriculumWizardModel>("autoform", model);

		List<String> velden = new ArrayList<String>();

		switch (voortgang)
		{
			case Stap2:
				velden
					.addAll(Arrays.asList("organisatieEenheid", "locatie", "opleiding", "cohort"));
				break;
			case Stap3:
				velden.addAll(Arrays.asList("organisatieEenheid", "locatie", "opleiding", "cohort",
					"onderwijsproduct"));
				break;
			case Stap4:
				velden.addAll(Arrays.asList("organisatieEenheid", "locatie", "opleiding", "cohort",
					"onderwijsproduct", "onderwijsproductOnderwijstijd"));
				break;
			default:
				break;
		}

		autoform.setPropertyNames(velden);
		autoform.setSortAccordingToPropertyNames(true);
		autoform.setHtmlClasses("tblDatagrid");

		add(autoform);
	}
}
