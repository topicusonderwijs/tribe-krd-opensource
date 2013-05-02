package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Set;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.providers.OrganisatieEenheidProvider;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

/**
 * @author vandekamp
 */
public class AfspraakTypeZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<AfspraakType> implements
		OrganisatieEenheidProvider
{
	private static final long serialVersionUID = 1L;

	@AutoForm(editorClass = ActiefCombobox.class)
	private Boolean actief;

	private Set<AfspraakTypeCategory> categories;

	private String naam;

	private String omschrijving;

	private boolean includeMedewerkerOnly = true;

	public AfspraakTypeZoekFilter()
	{
	}

	public Boolean isActief()
	{
		return actief;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public Set<AfspraakTypeCategory> getCategories()
	{
		return categories;
	}

	public void setCategories(Set<AfspraakTypeCategory> categories)
	{
		this.categories = categories;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public boolean getIncludeMedewerkerOnly()
	{
		return includeMedewerkerOnly;
	}

	public void setIncludeMedewerkerOnly(boolean includeMedewerkerOnly)
	{
		this.includeMedewerkerOnly = includeMedewerkerOnly;
	}
}
