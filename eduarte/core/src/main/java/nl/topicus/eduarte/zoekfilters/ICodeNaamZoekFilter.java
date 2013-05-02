package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;

public interface ICodeNaamZoekFilter<T> extends DetachableZoekFilter<T>
{
	public String getCode();

	public void setCode(String code);

	public String getNaam();

	public void setNaam(String naam);

	public Class<T> getEntityClass();
}
