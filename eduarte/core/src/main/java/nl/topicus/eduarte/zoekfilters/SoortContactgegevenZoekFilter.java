/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.wiquery.DropDownCheckList;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.adres.StandaardContactgegeven;
import nl.topicus.eduarte.entities.adres.TypeContactgegeven;

/**
 * Filter voor {@link SoortContactgegeven}s.
 * 
 * @author hoeve
 */
public class SoortContactgegevenZoekFilter extends
		AbstractCodeNaamActiefZoekFilter<SoortContactgegeven> implements
		ICodeNaamActiefZoekFilter<SoortContactgegeven>
{
	private static final long serialVersionUID = 1L;

	private TypeContactgegeven typeContactgegeven;

	@AutoForm(editorClass = DropDownCheckList.class)
	private List<StandaardContactgegeven> standaardContactgegeven;

	private boolean geenStandaardContactgegevenIngevuldMeenemen = false;

	private List<Long> excludeIds;

	public SoortContactgegevenZoekFilter()
	{
		super(SoortContactgegeven.class);
	}

	public TypeContactgegeven getTypeContactgegeven()
	{
		return typeContactgegeven;
	}

	public void setTypeContactgegeven(TypeContactgegeven typeContactgegeven)
	{
		this.typeContactgegeven = typeContactgegeven;
	}

	public List<StandaardContactgegeven> getStandaardContactgegeven()
	{
		return standaardContactgegeven;
	}

	/**
	 * @param standaarden
	 *            De {@link StandaardContactgegeven} waarden (bv alleen persoon
	 *            contactgevens).
	 */
	public void setStandaardContactgegeven(StandaardContactgegeven... standaarden)
	{
		this.standaardContactgegeven = Arrays.asList(standaarden);
	}

	/**
	 * @param standaarden
	 *            De {@link StandaardContactgegeven} waarden (bv alleen persoon
	 *            contactgevens).
	 */
	public void setStandaardContactgegeven(List<StandaardContactgegeven> standaarden)
	{
		this.standaardContactgegeven = standaarden;
	}

	public void setExclude(List<SoortContactgegeven> contactgegevens)
	{
		excludeIds = new ArrayList<Long>();

		for (SoortContactgegeven ice : contactgegevens)
		{
			excludeIds.add(ice.getId());
		}
	}

	public List<Long> getExcludeIds()
	{
		return excludeIds;
	}

	public static final SoortContactgegevenZoekFilter getDefaultFilter()
	{
		SoortContactgegevenZoekFilter filter = new SoortContactgegevenZoekFilter();
		filter.addOrderByProperty("code");
		return filter;
	}

	public boolean isGeenStandaardContactgegevenIngevuldMeenemen()
	{
		return geenStandaardContactgegevenIngevuldMeenemen;
	}

	public void setGeenStandaardContactgegevenIngevuldMeenemen(
			boolean geenStandaardContactgegevenIngevuldMeenemen)
	{
		this.geenStandaardContactgegevenIngevuldMeenemen =
			geenStandaardContactgegevenIngevuldMeenemen;
	}
}
