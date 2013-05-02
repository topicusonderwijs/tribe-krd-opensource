/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.List;

import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.vrijevelden.PlaatsingVrijVeld;

/**
 * @author idserda
 */
public class PlaatsingZoekFilter extends
		AbstractVrijVeldableZoekFilter<PlaatsingVrijVeld, Plaatsing>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private String afkorting;

	private String soort;

	private String plaats;

	private String postcode;

	private Long persoonIdNot;

	private Long deelnemerId;

	private boolean lwoo;

	private List<VerbintenisStatus> verbintenisStatusNot;

	public PlaatsingZoekFilter()
	{
		super(PlaatsingVrijVeld.class);
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	public void setSoort(String soort)
	{
		this.soort = soort;
	}

	public String getSoort()
	{
		return soort;
	}

	public void setPlaats(String plaats)
	{
		this.plaats = plaats;
	}

	public String getPlaats()
	{
		return plaats;
	}

	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	public String getPostcode()
	{
		return postcode;
	}

	public boolean heeftAdresCriteria()
	{
		return (postcode != null || plaats != null);
	}

	public void setPersoonIdNot(Long persoonIdNot)
	{
		this.persoonIdNot = persoonIdNot;
	}

	public Long getPersoonIdNot()
	{
		return persoonIdNot;
	}

	public void setDeelnemerId(Long deelnemerId)
	{
		this.deelnemerId = deelnemerId;
	}

	public Long getDeelnemerId()
	{
		return deelnemerId;
	}

	public void setVerbintenisStatusNot(List<VerbintenisStatus> verbintenisStatusNot)
	{
		this.verbintenisStatusNot = verbintenisStatusNot;
	}

	public List<VerbintenisStatus> getVerbintenisStatusNot()
	{
		return verbintenisStatusNot;
	}

	public void setLwoo(boolean lwoo)
	{
		this.lwoo = lwoo;
	}

	public boolean isLwoo()
	{
		return lwoo;
	}
}
