/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.IContactgegevenEntiteit;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "LocatieContactgegeven")
public class LocatieContactgegeven extends InstellingEntiteit implements IContactgegevenEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = false)
	@Index(name = "idx_LocatieContact_locatie")
	private Locatie locatie;

	@Column(length = 60, nullable = false)
	private String contactgegeven;

	@Column(nullable = false)
	private boolean geheim;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "soortContactgegeven")
	@Index(name = "idx_LocatieContact_soort")
	private SoortContactgegeven soortContactgegeven;

	@Column(nullable = false)
	private int volgorde;

	public LocatieContactgegeven()
	{
	}

	/**
	 * @return Returns the organisatieEenheid.
	 */
	public Locatie getLocatie()
	{
		return locatie;
	}

	/**
	 * @param locatie
	 *            The locatie to set.
	 */
	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	/**
	 * @return Returns the contactgegeven.
	 */
	public String getContactgegeven()
	{
		return contactgegeven;
	}

	/**
	 * @param contactgegeven
	 *            The contactgegeven to set.
	 */
	public void setContactgegeven(String contactgegeven)
	{
		this.contactgegeven = contactgegeven;
	}

	/**
	 * @return Returns the geheim.
	 */
	public boolean isGeheim()
	{
		return geheim;
	}

	/**
	 * @param geheim
	 *            The geheim to set.
	 */
	public void setGeheim(boolean geheim)
	{
		this.geheim = geheim;
	}

	/**
	 * @return Returns the volgorde.
	 */
	public int getVolgorde()
	{
		return volgorde;
	}

	/**
	 * @param volgorde
	 *            The volgorde to set.
	 */
	public void setVolgorde(int volgorde)
	{
		this.volgorde = volgorde;
	}

	/**
	 * @return Het contactgegeven of '**********' als het contactgegeven geheim is.
	 */
	public String getFormattedContactgegeven()
	{
		if (isGeheim())
		{
			return "**********";
		}
		return getContactgegeven();
	}

	/**
	 * @return Het contactgegeven gevolgd door ' (Geheim)' indien het contactgegeven
	 *         geheim is, en anders gewoon het contactgegeven.
	 */
	public String getTelefoonnummerInclGeheim()
	{
		if (isGeheim())
		{
			return getContactgegeven() + " (Geheim)";
		}
		return getContactgegeven();
	}

	/**
	 * @return Returns the soortContactgegeven.
	 */
	public SoortContactgegeven getSoortContactgegeven()
	{
		return soortContactgegeven;
	}

	/**
	 * @param soortContactgegeven
	 *            The soortContactgegeven to set.
	 */
	public void setSoortContactgegeven(SoortContactgegeven soortContactgegeven)
	{
		this.soortContactgegeven = soortContactgegeven;
	}

}
