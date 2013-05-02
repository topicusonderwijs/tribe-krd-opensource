package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.cobra.web.components.choice.ActiefCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.entities.participatie.enums.AbsentieSoort;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.IActiefZoekFilter;

/**
 * @author loite
 */
public class AbsentieRedenZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<AbsentieReden> implements
		IActiefZoekFilter<AbsentieReden>
{
	private static final long serialVersionUID = 1L;

	private boolean tonenBijWaarnemingen;

	@AutoForm(editorClass = ActiefCombobox.class)
	private Boolean actief;

	private String omschrijving;

	private AbsentieSoort absentieSoort;

	private boolean alleenToegestaanVoorDeelnemers;

	public AbsentieRedenZoekFilter()
	{
	}

	public boolean isTonenBijWaarnemingen()
	{
		return tonenBijWaarnemingen;
	}

	public void setTonenBijWaarnemingen(boolean tonenBijWaarnemingen)
	{
		this.tonenBijWaarnemingen = tonenBijWaarnemingen;
	}

	public Boolean getActief()
	{
		return actief;
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public AbsentieSoort getAbsentieSoort()
	{
		return absentieSoort;
	}

	public void setAbsentieSoort(AbsentieSoort absentieSoort)
	{
		this.absentieSoort = absentieSoort;
	}

	public void setAlleenToegestaanVoorDeelnemers(boolean alleenToegestaanVoorDeelnemers)
	{
		this.alleenToegestaanVoorDeelnemers = alleenToegestaanVoorDeelnemers;
	}

	public boolean isAlleenToegestaanVoorDeelnemers()
	{
		return alleenToegestaanVoorDeelnemers;
	}
}
