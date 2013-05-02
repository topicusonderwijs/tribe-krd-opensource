/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.participatie.enums.AgendaDelenType;
import nl.topicus.eduarte.entities.participatie.enums.AgendaPrintOpmaak;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author N Henzen
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "Instelling")
public class AgendaInstellingen extends InstellingEntiteit
{

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "persoon")
	@Index(name = "idx_AInstellingen_persoon")
	@AutoForm(include = false)
	private Persoon persoon;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(htmlClasses = "unit_max")
	private AgendaPrintOpmaak printOpmaak;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(htmlClasses = "unit_max")
	private AgendaDelenType delenMetWerknemer;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@AutoForm(htmlClasses = "unit_max")
	private AgendaDelenType delenMetDeelnemer;

	private static final long serialVersionUID = 1L;

	public AgendaInstellingen()
	{
	}

	public AgendaInstellingen(Persoon persoon)
	{
		setPersoon(persoon);
		setPrintOpmaak(AgendaPrintOpmaak.KLEUR);
		setDelenMetDeelnemer(AgendaDelenType.VOLLEDIG);
		setDelenMetWerknemer(AgendaDelenType.VOLLEDIG);
	}

	public void setPersoon(Persoon persoon)
	{
		this.persoon = persoon;
	}

	public Persoon getPersoon()
	{
		return persoon;
	}

	public void setPrintOpmaak(AgendaPrintOpmaak printOpmaak)
	{
		this.printOpmaak = printOpmaak;
	}

	public AgendaPrintOpmaak getPrintOpmaak()
	{
		return printOpmaak;
	}

	public void setDelenMetWerknemer(AgendaDelenType delenMetWerknemer)
	{
		this.delenMetWerknemer = delenMetWerknemer;
	}

	public AgendaDelenType getDelenMetWerknemer()
	{
		return delenMetWerknemer;
	}

	public void setDelenMetDeelnemer(AgendaDelenType delenMetDeelnemer)
	{
		this.delenMetDeelnemer = delenMetDeelnemer;
	}

	public AgendaDelenType getDelenMetDeelnemer()
	{
		return delenMetDeelnemer;
	}
}
