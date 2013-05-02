package nl.topicus.eduarte.participatie.zoekfilters;

import nl.topicus.eduarte.entities.participatie.Maatregel;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.IActiefZoekFilter;

/**
 * @author vandekamp
 */
public class MaatregelZoekFilter extends AbstractOrganisatieEenheidLocatieZoekFilter<Maatregel>
		implements IActiefZoekFilter<Maatregel>
{
	private static final long serialVersionUID = 1L;

	private Boolean actief = null;

	/**
	 * Constructor
	 */
	public MaatregelZoekFilter()
	{
	}

	/**
	 * @return Returns the actief.
	 */
	public Boolean getActief()
	{
		return actief;
	}

	/**
	 * @param actief
	 *            The actief to set.
	 */
	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}
}
