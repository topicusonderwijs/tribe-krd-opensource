package nl.topicus.eduarte.zoekfilters.dbs;

import java.util.Date;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.dbs.trajecten.templates.TrajectTemplate;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.INaamActiefZoekFilter;

public class TrajectTemplateZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<TrajectTemplate> implements
		INaamActiefZoekFilter<TrajectTemplate>
{
	private static final long serialVersionUID = 1L;

	private Boolean actief;

	@AutoForm(htmlClasses = "unit_160")
	private String naam;

	private Date beginDatumAutomatischeKoppeling;

	private Date eindDatumAutomatischeKoppeling;

	private Boolean metAutomatischeKoppeling;

	public TrajectTemplateZoekFilter()
	{
		addOrderByProperty("naam");
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	@Override
	public Boolean getActief()
	{
		return actief;
	}

	@Override
	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public Date getBeginDatumAutomatischeKoppeling()
	{
		return beginDatumAutomatischeKoppeling;
	}

	public void setBeginDatumAutomatischeKoppeling(Date beginDatumAutomatischeKoppeling)
	{
		this.beginDatumAutomatischeKoppeling = beginDatumAutomatischeKoppeling;
	}

	public Date getEindDatumAutomatischeKoppeling()
	{
		return eindDatumAutomatischeKoppeling;
	}

	public void setEindDatumAutomatischeKoppeling(Date eindDatumAutomatischeKoppeling)
	{
		this.eindDatumAutomatischeKoppeling = eindDatumAutomatischeKoppeling;
	}

	public Boolean getMetAutomatischeKoppeling()
	{
		return metAutomatischeKoppeling;
	}

	public void setMetAutomatischeKoppeling(Boolean metAutomatischeKoppeling)
	{
		this.metAutomatischeKoppeling = metAutomatischeKoppeling;
	}
}
