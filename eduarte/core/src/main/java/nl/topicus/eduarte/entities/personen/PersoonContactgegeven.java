/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.personen;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.IContactgegevenEntiteit;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.adres.SoortContactgegeven;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Exportable
@IsViewWhenOnNoise
public class PersoonContactgegeven extends InstellingEntiteit implements IContactgegevenEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * Lazy omdat lijst met telefoonnummers vanuit de persoon wordt opgevraagd, oftewel de
	 * persoon zal in het algemeen al aanwezig zijn in de 1st of 2nd level cache.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persoon", nullable = false)
	@Index(name = "idx_PContact_persoon")
	private Persoon persoon;

	/**
	 * Email: 64 chars + 1 @ sign + 255 chars = 320 characters
	 */
	@Column(length = 320, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String contactgegeven;

	@Column(nullable = false)
	private boolean geheim;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "soortContactgegeven", nullable = false)
	@Index(name = "idx_PContact_soortContact")
	private SoortContactgegeven soortContactgegeven;

	@Column(nullable = false)
	@Index(name = "idx_PContact_volgorde")
	private int volgorde;

	public PersoonContactgegeven()
	{
	}

	public Persoon getPersoon()
	{
		return persoon;
	}

	public void setPersoon(Persoon persoon)
	{
		this.persoon = persoon;
	}

	@Exportable
	public String getContactgegeven()
	{
		return contactgegeven;
	}

	public void setContactgegeven(String contactgegeven)
	{
		this.contactgegeven = contactgegeven;
	}

	@Exportable
	public boolean isGeheim()
	{
		return geheim;
	}

	public void setGeheim(boolean geheim)
	{
		this.geheim = geheim;
	}

	@Exportable
	public SoortContactgegeven getSoortContactgegeven()
	{
		return soortContactgegeven;
	}

	public void setSoortContactgegeven(SoortContactgegeven soortContactgegeven)
	{
		this.soortContactgegeven = soortContactgegeven;
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
	 * @return Het contactgegeven of '**********' als het geheim is.
	 */
	public String getFormattedContactgegeven()
	{
		if (isGeheim())
		{
			return "***Geheim***";
		}
		return getContactgegeven();
	}

	/**
	 * @return Het gegeven gevolgd door ' (Geheim)' indien het gegeven geheim is, en
	 *         anders gewoon het gegeven.
	 */
	@Exportable
	public String getContactgegevenInclGeheim()
	{
		if (isGeheim())
		{
			return getContactgegeven() + " (Geheim)";
		}
		return getContactgegeven();
	}

	@Override
	public String toString()
	{
		return contactgegeven;
	}

}
