package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.apache.wicket.model.IModel;

public class CurriculumOnderwijsproductZoekFilter extends
		AbstractZoekFilter<CurriculumOnderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private IModel<Curriculum> curriculum;

	@AutoForm(htmlClasses = "unit_180")
	private IModel<Onderwijsproduct> onderwijsproduct;

	private CurriculumZoekFilter curriculumZoekFilter = new CurriculumZoekFilter();

	public CurriculumOnderwijsproductZoekFilter()
	{
	}

	public CurriculumOnderwijsproductZoekFilter(Curriculum curriculum)
	{
		setCurriculum(curriculum);
	}

	public Curriculum getCurriculum()
	{
		return getModelObject(curriculum);
	}

	public void setCurriculum(Curriculum curriculum)
	{
		this.curriculum = makeModelFor(curriculum);
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}

	public CurriculumZoekFilter getCurriculumZoekFilter()
	{
		return curriculumZoekFilter;
	}

	public void setCurriculumZoekFilter(CurriculumZoekFilter curriculumZoekFilter)
	{
		this.curriculumZoekFilter = curriculumZoekFilter;
	}

	@AutoForm(htmlClasses = "unit_180")
	public Opleiding getOpleiding()
	{
		return curriculumZoekFilter.getOpleiding();
	}

	public void setOpleiding(Opleiding opleiding)
	{
		curriculumZoekFilter.setOpleiding(opleiding);
	}

	public Cohort getCohort()
	{
		return curriculumZoekFilter.getCohort();
	}

	public void setCohort(Cohort cohort)
	{
		curriculumZoekFilter.setCohort(cohort);
	}

	@AutoForm(htmlClasses = "unit_150")
	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return curriculumZoekFilter.getOrganisatieEenheid();
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		curriculumZoekFilter.setOrganisatieEenheid(organisatieEenheid);
	}

	public Locatie getLocatie()
	{
		return curriculumZoekFilter.getLocatie();
	}

	public void setLocatie(Locatie locatie)
	{
		curriculumZoekFilter.setLocatie(locatie);
	}

	public void setAuthorizationContext(
			OrganisatieEenheidLocatieAuthorizationContext authorizationContext)
	{
		curriculumZoekFilter.setAuthorizationContext(authorizationContext);
	}

}