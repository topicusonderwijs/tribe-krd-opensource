package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;

import org.apache.wicket.model.IModel;

public class CurriculumZoekFilter extends AbstractOrganisatieEenheidLocatieZoekFilter<Curriculum>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_200")
	private IModel<Opleiding> opleiding;

	private IModel<Cohort> cohort;

	public CurriculumZoekFilter()
	{
	}

	public CurriculumZoekFilter(Opleiding opleiding)
	{
		setOpleiding(opleiding);
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Cohort getCohort()
	{
		return getModelObject(cohort);
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = makeModelFor(cohort);
	}

}
