/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.security.authentication;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Account specifiek voor {@link Medewerker}s.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class MedewerkerAccount extends Account
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = true)
	@Basic(optional = false)
	@Index(name = "idx_MwAccount_medewerker")
	@AutoForm(htmlClasses = "unit_max")
	private Medewerker medewerker;

	public MedewerkerAccount()
	{
	}

	public MedewerkerAccount(Medewerker medewerker)
	{
		setMedewerker(medewerker);
		setGebruikersnaam(medewerker.getAfkorting());
	}

	@Override
	public Persoon getEigenaar()
	{
		if (getMedewerker() == null)
			return new Persoon();
		return getMedewerker().getPersoon();
	}

	@Override
	public Deelnemer getDeelnemer()
	{
		return null;
	}

	@Override
	public IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > getOrganisatieEenheidLocatie()
	{
		return getMedewerker();
	}

	@Override
	public boolean isValid()
	{
		return isActief() && getMedewerker() != null && getMedewerker().isActief();
	}

	@Override
	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	@AutoForm(label = "Actief")
	@Override
	public String getActiefOmschrijving()
	{
		if (getMedewerker() != null && getMedewerker().isActief())
			return super.getActiefOmschrijving();
		return "Nee (Medewerker is niet actief)";
	}

}
