/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.security.authentication;

import javax.persistence.Entity;

import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Account specifiek voor digitaal aanmeld(st)er
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DigitaalAanmelderAccount extends Account
{
	private static final long serialVersionUID = 1L;

	public DigitaalAanmelderAccount()
	{
	}

	@Override
	public Persoon getEigenaar()
	{
		return null;
	}

	@Override
	public Medewerker getMedewerker()
	{
		return null;
	}

	@Override
	public Deelnemer getDeelnemer()
	{
		return null;
	}

	@Override
	public IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > getOrganisatieEenheidLocatie()
	{
		return null;
	}

	@Override
	public boolean isValid()
	{
		return isActief() && getOrganisatie().getRechtenSoort() == RechtenSoort.INSTELLING;
	}

	@Override
	public RechtenSoort getRechtenSoort()
	{
		return RechtenSoort.DIGITAALAANMELDER;
	}
}
