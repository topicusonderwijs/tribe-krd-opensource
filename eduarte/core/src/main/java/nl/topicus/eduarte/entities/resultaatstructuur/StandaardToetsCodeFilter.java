/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.resultaatstructuur;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen opleiding, cohort en ToetsCodeFilter. Wordt gebruikt om te bepalen
 * bij welke ToetsCodeFilter als standaard gebruikt moet worden.
 * 
 * @author papegaaij
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(name = "StandaardToetsCodeFilter", uniqueConstraints = {@UniqueConstraint(columnNames = {
	"opleiding", "cohort"})})
public class StandaardToetsCodeFilter extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opleiding", nullable = false)
	@Index(name = "idx_StdToetsCFilter_opleiding")
	@AutoForm(include = false)
	private Opleiding opleiding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cohort", nullable = false)
	@Index(name = "idx_StdToetsCFilter_cohort")
	private Cohort cohort;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "toetsCodeFilter", nullable = false)
	@Index(name = "idx_StdToetsCFilter_filter")
	private ToetsCodeFilter toetsCodeFilter;

	public StandaardToetsCodeFilter()
	{
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort;
	}

	public Cohort getCohort()
	{
		return cohort;
	}

	public void setToetsCodeFilter(ToetsCodeFilter toetsCodeFilter)
	{
		this.toetsCodeFilter = toetsCodeFilter;
	}

	public ToetsCodeFilter getToetsCodeFilter()
	{
		return toetsCodeFilter;
	}
}
