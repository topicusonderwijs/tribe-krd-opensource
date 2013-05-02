package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.participatie.InloopCollege;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class InloopcollegeZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<InloopCollege>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(label = "Datum")
	private Date inschrijfBeginDatum;

	@AutoForm(label = "t/m")
	private Date inschrijfEindDatum;

	private boolean toonVolleColleges = true;

	private Boolean toonIngeschrevenColleges;

	private IModel<Deelnemer> deelnemerModel;

	private IModel<Opleiding> opleidingModel;

	private String omschrijving;

	private IModel<List<Groep>> groepen;

	public InloopcollegeZoekFilter()
	{
	}

	public Date getInschrijfBeginDatum()
	{
		return inschrijfBeginDatum;
	}

	public void setInschrijfBeginDatum(Date inschrijfBeginDatum)
	{
		this.inschrijfBeginDatum = inschrijfBeginDatum;
	}

	public Date getInschrijfEindDatum()
	{
		return inschrijfEindDatum;
	}

	public void setInschrijfEindDatum(Date inschrijfEindDatum)
	{
		this.inschrijfEindDatum = inschrijfEindDatum;
	}

	/**
	 * !oude OnderwijsAanbod <-verwijderen na overzetting
	 * 
	 * @return Returns the Opleiding.
	 */
	public Opleiding getOpleiding()
	{
		if (opleidingModel == null)
			return null;
		return opleidingModel.getObject();
	}

	/**
	 * 
	 * !oude OnderwijsAanbod <-verwijderen na overzetting
	 */
	public void setOpleiding(Opleiding opleiding)
	{
		opleidingModel = makeModelFor(opleiding);
	}

	public List<Groep> getGroepen()
	{
		return getModelObject(groepen);
	}

	public void setGroepen(List<Groep> groepen)
	{
		this.groepen = makeModelFor(groepen);
	}

	public boolean isToonVolleColleges()
	{
		return toonVolleColleges;
	}

	public void setToonVolleColleges(boolean toonVolleColleges)
	{
		this.toonVolleColleges = toonVolleColleges;
	}

	public Deelnemer getDeelnemer()
	{
		if (deelnemerModel == null)
			return null;
		return deelnemerModel.getObject();
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		deelnemerModel = makeModelFor(deelnemer);
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setToonIngeschrevenColleges(boolean toonIngeschrevenColleges)
	{
		this.toonIngeschrevenColleges = toonIngeschrevenColleges;
	}

	public Boolean isToonIngeschrevenColleges()
	{
		return toonIngeschrevenColleges;
	}
}
