package nl.topicus.eduarte.zoekfilters.dbs;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSoort;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.INaamActiefZoekFilter;

public class GesprekSoortZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<GesprekSoort> implements
		INaamActiefZoekFilter<GesprekSoort>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(htmlClasses = "unit_200")
	private String naam;

	@AutoForm(label = "status", editorClass = ActiefCombobox.class)
	private Boolean actief;

	public GesprekSoortZoekFilter()
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
}
