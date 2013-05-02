/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.personen;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "ExtOrgContPersRol")
@IsViewWhenOnNoise
public class ExterneOrganisatieContactPersoonRol extends InstellingEntiteit implements
		IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 120, nullable = false)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column
	@AutoForm(label = "Praktijkopleider BPV")
	private Boolean praktijkopleiderBPV;

	@Column
	@AutoForm(label = "Contactpersoon BPV")
	private Boolean contactpersoonBPV;

	@Column(nullable = false)
	private boolean actief = true;

	public ExterneOrganisatieContactPersoonRol()
	{
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	public Boolean getPraktijkopleiderBPV()
	{
		return praktijkopleiderBPV;
	}

	public void setPraktijkopleiderBPV(Boolean praktijkopleiderBPV)
	{
		this.praktijkopleiderBPV = praktijkopleiderBPV;
	}

	public Boolean getContactpersoonBPV()
	{
		return contactpersoonBPV;
	}

	public void setContactpersoonBPV(Boolean contactpersoonBPV)
	{
		this.contactpersoonBPV = contactpersoonBPV;
	}

	@Override
	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}
}
