/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.personen.PersoonType;

/**
 * Basis zoekfilter voor alle personen.
 * 
 * @author marrink
 * @param <T>
 */
public class PersoonZoekFilter<T extends IdObject> extends
		AbstractOrganisatieEenheidLocatieZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Veld voor het zoeken op combinaties van gegevens, zoals roepnaam + achternaam.
	 */
	private String multizoek;

	private String roepnaam;

	private String voornaam;

	private String voorvoegsel;

	private String achternaam;

	private Long bsn;

	private Geslacht geslacht;

	private PersoonType persoonType;

	private Boolean hasDebiteurennummer;

	private Date geboortedatum;

	public PersoonZoekFilter()
	{
	}

	public String getRoepnaam()
	{
		return roepnaam;
	}

	public void setRoepnaam(String roepnaam)
	{
		this.roepnaam = roepnaam;
	}

	public String getVoornaam()
	{
		return voornaam;
	}

	public void setVoornaam(String voornaam)
	{
		this.voornaam = voornaam;
	}

	public String getVoorvoegsel()
	{
		return voorvoegsel;
	}

	public void setVoorvoegsel(String voorvoegsel)
	{
		this.voorvoegsel = voorvoegsel;
	}

	public String getAchternaam()
	{
		return achternaam;
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = achternaam;
	}

	public Long getBsn()
	{
		return bsn;
	}

	public void setBsn(Long bsn)
	{
		this.bsn = bsn;
	}

	public Geslacht getGeslacht()
	{
		return geslacht;
	}

	public void setGeslacht(Geslacht geslacht)
	{
		this.geslacht = geslacht;
	}

	public PersoonType getPersoonType()
	{
		return persoonType;
	}

	public void setPersoonType(PersoonType persoonType)
	{
		this.persoonType = persoonType;
	}

	public String getMultizoek()
	{
		return multizoek;
	}

	public void setMultizoek(String multizoek)
	{
		this.multizoek = multizoek;
	}

	public void setHasDebiteurennummer(Boolean hasDebiteurennummer)
	{
		this.hasDebiteurennummer = hasDebiteurennummer;
	}

	public Boolean getHasDebiteurennummer()
	{
		return hasDebiteurennummer;
	}

	public void setGeboortedatum(Date geboortedatum)
	{
		this.geboortedatum = geboortedatum;
	}

	public Date getGeboortedatum()
	{
		return geboortedatum;
	}
}
