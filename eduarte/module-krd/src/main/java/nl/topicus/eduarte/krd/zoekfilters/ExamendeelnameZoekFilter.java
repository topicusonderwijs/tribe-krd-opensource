package nl.topicus.eduarte.krd.zoekfilters;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.examen.ExamenWorkflow;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.opleiding.SoortOnderwijsTax;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.web.components.choice.CohortCombobox;
import nl.topicus.eduarte.web.components.choice.ExamenstatusCombobox;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class ExamendeelnameZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<Examendeelname>
{
	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemer;

	private IModel<Verbintenis> verbintenis;

	@AutoForm(editorClass = ExamenstatusCombobox.class)
	private IModel<Examenstatus> examenstatus;

	private IModel<List<Examenstatus>> examenstatusList;

	@AutoForm(editorClass = ExamenstatusCombobox.class)
	private IModel<Examenstatus> examenstatusOngelijkAan;

	private IModel<ExamenWorkflow> examenworkflow;

	private IModel<Taxonomie> taxonomie;

	private String officieelofaanspreek;

	private IModel<Schooljaar> schooljaar;

	private BronOnderwijssoort bronOnderwijssoort;

	@AutoForm(htmlClasses = "unit_150")
	private IModel<Opleiding> opleiding;

	private IModel<Groep> groep;

	@AutoForm(editorClass = CohortCombobox.class)
	private IModel<Cohort> cohort;

	private boolean alleenGewijzigde;

	private List<SoortOnderwijsTax> soortOnderwijsTax = new ArrayList<SoortOnderwijsTax>();

	public ExamendeelnameZoekFilter()
	{
	}

	public ExamendeelnameZoekFilter(Deelnemer deelnemer)
	{
		setDeelnemer(deelnemer);
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject(verbintenis);
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = makeModelFor(verbintenis);
	}

	public void setVerbintenisModel(IModel<Verbintenis> verbintenisModel)
	{
		this.verbintenis = verbintenisModel;
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Examenstatus getExamenstatus()
	{
		return getModelObject(examenstatus);
	}

	public void setExamenstatus(Examenstatus examenstatus)
	{
		this.examenstatus = makeModelFor(examenstatus);
	}

	public List<Examenstatus> getExamenstatusList()
	{
		return getModelObject(examenstatusList);
	}

	public void setExamenstatusList(List<Examenstatus> examenstatusList)
	{
		this.examenstatusList = makeModelFor(examenstatusList);
	}

	public Examenstatus getExamenstatusOngelijkAan()
	{
		return getModelObject(examenstatusOngelijkAan);
	}

	public void setExamenstatusOngelijkAan(Examenstatus examenstatusOngelijkAan)
	{
		this.examenstatusOngelijkAan = makeModelFor(examenstatusOngelijkAan);
	}

	public ExamenWorkflow getExamenworkflow()
	{
		return getModelObject(examenworkflow);
	}

	public void setExamenworkflow(ExamenWorkflow examenworkflow)
	{
		this.examenworkflow = makeModelFor(examenworkflow);
	}

	public Taxonomie getTaxonomie()
	{
		return getModelObject(taxonomie);
	}

	public void setTaxonomie(Taxonomie taxonomie)
	{
		this.taxonomie = makeModelFor(taxonomie);
	}

	public String getOfficieelofaanspreek()
	{
		return officieelofaanspreek;
	}

	public void setOfficieelofaanspreek(String officieelofaanspreek)
	{
		this.officieelofaanspreek = officieelofaanspreek;
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Groep getGroep()
	{
		return getModelObject(groep);
	}

	public void setGroep(Groep groep)
	{
		this.groep = makeModelFor(groep);
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = makeModelFor(cohort);
	}

	public Cohort getCohort()
	{
		return getModelObject(cohort);
	}

	public void setSchooljaar(Schooljaar schooljaar)
	{
		this.schooljaar = makeModelFor(schooljaar);
	}

	public Schooljaar getSchooljaar()
	{
		return getModelObject(schooljaar);
	}

	public void setBronOnderwijssoort(BronOnderwijssoort bronOnderwijssoort)
	{
		this.bronOnderwijssoort = bronOnderwijssoort;
	}

	public BronOnderwijssoort getBronOnderwijssoort()
	{
		return bronOnderwijssoort;
	}

	public void setAlleenGewijzigde(boolean alleenGewijzigde)
	{
		this.alleenGewijzigde = alleenGewijzigde;
	}

	public boolean isAlleenGewijzigde()
	{
		return alleenGewijzigde;
	}

	public void setSoortOnderwijsTax(List<SoortOnderwijsTax> soortOnderwijsTax)
	{
		this.soortOnderwijsTax = soortOnderwijsTax;
	}

	public List<SoortOnderwijsTax> getSoortOnderwijsTax()
	{
		return soortOnderwijsTax;
	}
}
