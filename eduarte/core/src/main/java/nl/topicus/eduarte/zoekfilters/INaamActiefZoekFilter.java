package nl.topicus.eduarte.zoekfilters;

/**
 * 
 * 
 * @author vanharen
 * @param <T>
 */
public interface INaamActiefZoekFilter<T> extends IActiefZoekFilter<T>
{
	public String getNaam();

	public void setNaam(String naam);
}
