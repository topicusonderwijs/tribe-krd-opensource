package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.AbsentieRedenComboBox;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class AbsentieMeldingZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<AbsentieMelding>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_40")
	private Date beginDatumTijd;

	@AutoForm(htmlClasses = "unit_40")
	private Date eindDatumTijd;

	private IModel<Deelnemer> deelnemer;

	@AutoForm(editorClass = AbsentieRedenComboBox.class, htmlClasses = "unit_90")
	private IModel<AbsentieReden> absentieReden;

	private Boolean afgehandeld = null;

	private IModel<List<Deelnemer>> deelnemersList;

	private IModel<Opleiding> opleiding;

	private String achternaam;

	private String absr;

	private IModel<Groep> groep;

	/**
	 * Geeft aan of gezocht moet worden naar absentiemeldingen door een
	 * addLessOrEquals(begintijd/eindtijd) toe te voegen, of door een
	 * addLess(begintijd/eindtijd) toe te voegen
	 */
	private boolean beginEindtijdInclusief = true;

	public AbsentieMeldingZoekFilter()
	{
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Boolean isAfgehandeld()
	{
		return afgehandeld;
	}

	public void setAfgehandeld(Boolean afgehandeld)
	{
		this.afgehandeld = afgehandeld;
	}

	public AbsentieReden getAbsentieReden()
	{
		return getModelObject(absentieReden);
	}

	public void setAbsentieReden(AbsentieReden absentieReden)
	{
		this.absentieReden = makeModelFor(absentieReden);
	}

	public Date getBeginDatumTijd()
	{
		return beginDatumTijd;
	}

	public void setBeginDatumTijd(Date beginDatumTijd)
	{
		this.beginDatumTijd = beginDatumTijd;
	}

	public Date getEindDatumTijd()
	{
		return eindDatumTijd;
	}

	public void setEindDatumTijd(Date eindDatumTijd)
	{
		this.eindDatumTijd = eindDatumTijd;
	}

	public List<Deelnemer> getDeelnemersList()
	{
		return getModelObject(deelnemersList);
	}

	public void setDeelnemersList(List<Deelnemer> deelnemersList)
	{
		this.deelnemersList = makeModelFor(deelnemersList);
	}

	public boolean isBeginEindtijdInclusief()
	{
		return beginEindtijdInclusief;
	}

	public void setBeginEindtijdInclusief(boolean beginEindtijdInclusief)
	{
		this.beginEindtijdInclusief = beginEindtijdInclusief;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = makeModelFor(opleiding);
	}

	public Opleiding getOpleiding()
	{
		return getModelObject(opleiding);
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = achternaam;
	}

	public String getAchternaam()
	{
		return achternaam;
	}

	public void setAbsr(String absr)
	{
		this.absr = absr;
	}

	public String getAbsr()
	{
		return absr;
	}

	public void setGroep(Groep groep)
	{
		this.groep = makeModelFor(groep);
	}

	public Groep getGroep()
	{
		return getModelObject(groep);
	}

}
