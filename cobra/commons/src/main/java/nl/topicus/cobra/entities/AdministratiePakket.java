package nl.topicus.cobra.entities;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * <tt>AdministratiePakket</tt> wordt gebruikt voor communicatie met externe instanties.
 * De waarden worden gezet in de initialisatie van de applicatie (bijvoorbeeld
 * <tt>EduArteApp</tt>. Een externe instantie die de naam en versie van ons pakket wil
 * hebben is BRON (zie bijvoorbeeld <tt>BronBatch</tt> uit module KRD).
 * 
 * De standaardwaarden zijn gegokt om <tt>NullPointerExceptions</tt> te voorkomen.
 * 
 * @author dashorst
 */
public final class AdministratiePakket implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static final AdministratiePakket pakket = new AdministratiePakket();

	/**
	 * De naam zoals deze naar externe instanties wordt gecommuniceerd.
	 */
	private String naam = "EduArte";

	/**
	 * De versie zoals deze naar externe instanties wordt gecommuniceerd.
	 */
	private String versie = "1.0";

	/**
	 * De naam zoals deze naar externe instanties wordt gecommuniceerd.
	 */
	public String getNaam()
	{
		return naam;
	}

	/**
	 * De versie zoals deze naar externe instanties wordt gecommuniceerd.
	 */
	public String getVersie()
	{
		return versie;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public void setVersie(String versie)
	{
		this.versie = versie;
	}

	public static AdministratiePakket getPakket()
	{
		return pakket;
	}

	@SuppressWarnings("unused")
	private Object readResolve() throws ObjectStreamException
	{
		return pakket;
	}
}
