/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.Date;

import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek.IntakegesprekStatus;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.vrijevelden.IntakegesprekVrijVeld;

import org.apache.wicket.model.IModel;

/**
 * @author hoeve
 */
public class IntakegesprekZoekFilter extends
		AbstractVrijVeldableZoekFilter<IntakegesprekVrijVeld, Intakegesprek>
{
	private static final long serialVersionUID = 1L;

	private String achternaam;

	private String afkorting;

	private String soort;

	private String plaats;

	private String postcode;

	private Long persoonIdNot;

	private Long deelnemerId;

	private VerbintenisStatus verbintenisStatus;

	private IntakegesprekStatus status;

	private IModel<Medewerker> intaker;

	private Date datumTijd;

	public IntakegesprekZoekFilter()
	{
		super(IntakegesprekVrijVeld.class);
	}

	public String getAchternaam()
	{
		return achternaam;
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = achternaam;
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

	public void setVerbintenisStatus(VerbintenisStatus verbintenisStatus)
	{
		this.verbintenisStatus = verbintenisStatus;
	}

	public VerbintenisStatus getVerbintenisStatus()
	{
		return verbintenisStatus;
	}

	public Medewerker getIntaker()
	{
		return getModelObject(intaker);
	}

	public void setIntaker(Medewerker intaker)
	{
		this.intaker = makeModelFor(intaker);
	}

	public Date getDatumTijd()
	{
		return datumTijd;
	}

	public void setDatumTijd(Date datumTijd)
	{
		this.datumTijd = datumTijd;
	}

	public IntakegesprekStatus getStatus()
	{
		return status;
	}

	public void setStatus(IntakegesprekStatus status)
	{
		this.status = status;
	}
}
