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
 * @author idserda
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "ExtOrgContactgegeven")
public class ExterneOrganisatieContactgegeven extends LandelijkOfInstellingEntiteit implements
		IContactgegevenEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatie", nullable = false)
	@Index(name = "idx_ExtOrgContac_externeOrgan")
	private ExterneOrganisatie externeOrganisatie;

	@Column(length = 60, nullable = false)
	private String contactgegeven;

	@Column(nullable = false)
	private boolean geheim;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "soortContactgegeven")
	@Index(name = "idx_ExtOrgContact_soort")
	private SoortContactgegeven soortContactgegeven;

	@Column(nullable = false)
	private int volgorde;

	public ExterneOrganisatieContactgegeven()
	{
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatie;
	}

	public String getContactgegeven()
	{
		return contactgegeven;
	}

	public void setContactgegeven(String contactgegeven)
	{
		this.contactgegeven = contactgegeven;
	}

	public boolean isGeheim()
	{
		return geheim;
	}

	public void setGeheim(boolean geheim)
	{
		this.geheim = geheim;
	}

	public int getVolgorde()
	{
		return volgorde;
	}

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

	public SoortContactgegeven getSoortContactgegeven()
	{
		return soortContactgegeven;
	}

	public void setSoortContactgegeven(SoortContactgegeven soortContactgegeven)
	{
		this.soortContactgegeven = soortContactgegeven;
	}
}
