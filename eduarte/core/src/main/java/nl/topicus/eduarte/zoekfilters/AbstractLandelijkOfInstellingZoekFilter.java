package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.zoekfilters.NullFilter;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

public class AbstractLandelijkOfInstellingZoekFilter<T extends LandelijkOfInstellingEntiteit>
		extends AbstractZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	private Boolean landelijk;

	public AbstractLandelijkOfInstellingZoekFilter()
	{
	}

	public Boolean getLandelijk()
	{
		return landelijk;
	}

	public void setLandelijk(Boolean landelijk)
	{
		this.landelijk = landelijk;
	}

	public NullFilter getOrganisatieFilter()
	{
		if (landelijk == null)
			return NullFilter.DontCare;
		return landelijk ? NullFilter.IsNull : NullFilter.IsNotNull;
	}
}
