/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.jobs.logging;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.jobs.EduArteJob;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Entity om logging bij te houden van de verschillende jobruns. Aangeraden word om per
 * specifieke job een subclass van deze taak te maken zodat makkelijker de log uitgesplits
 * kan worden per type job
 * 
 * @author marrink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class JobRun extends OrganisatieEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@Temporal(value = TemporalType.TIMESTAMP)
	@AutoForm(label = "Starttijd")
	private Date runStart;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	@AutoForm(label = "Eindtijd")
	private Date runEinde;

	@Index(name = "idx_JobRun_gestartDoor")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gestartDoor", nullable = true)
	private Medewerker gestartDoor;

	@Index(name = "idx_JobRun_gestartDoorAccount")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gestartDoorAccount", nullable = true)
	private Account gestartDoorAccount;

	@Column(length = 256, nullable = false)
	private String samenvatting;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "jobRun", cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<JobRunDetail> details;

	public JobRun()
	{

	}

	public Date getRunStart()
	{
		return runStart;
	}

	public void setRunStart(Date runStart)
	{
		this.runStart = runStart;
	}

	public Date getRunEinde()
	{
		return runEinde;
	}

	public void setRunEinde(Date runEinde)
	{
		this.runEinde = runEinde;
	}

	public Medewerker getGestartDoor()
	{
		return gestartDoor;
	}

	public void setGestartDoor(Medewerker gestartDoor)
	{
		this.gestartDoor = gestartDoor;
	}

	/**
	 * @return de account welke de job heeft gestart, dit omdat accounts niet persee een
	 *         medewerker hoeven te hebben.
	 */
	public Account getGestartDoorAccount()
	{
		return gestartDoorAccount;
	}

	@AutoForm(label = "Gestart door")
	public String getGestartDoorOmschrijving()
	{
		if (getGestartDoor() != null)
			return getGestartDoor().getPersoon().getVolledigeNaam();
		return "<Automatisch>";
	}

	public void setGestartDoorAccount(Account gestartDoorAccount)
	{
		this.gestartDoorAccount = gestartDoorAccount;
	}

	public String getSamenvatting()
	{
		return samenvatting;
	}

	public void setSamenvatting(String samenvatting)
	{
		this.samenvatting = StringUtil.truncate(samenvatting, 256, "...");
	}

	public List<JobRunDetail> getDetails()
	{
		if (details == null)
			details = new ArrayList<JobRunDetail>();

		return details;
	}

	public void setDetails(List<JobRunDetail> details)
	{
		this.details = details;
	}

	/**
	 * @return Starttijd van de job in normale datum/tijdnotatie.
	 */
	public String getRunStartFormatted()
	{
		return TimeUtil.getInstance().formatDateTime(getRunStart());
	}

	/**
	 * @return Eindtijd van de job in normale datum/tijdnotatie.
	 */
	public String getRunEindeFormatted()
	{
		return TimeUtil.getInstance().formatDateTime(getRunEinde());
	}

	public String getNaam()
	{
		return Hibernate.getClass(this).getSimpleName();
	}

	/**
	 * @return class van de job die dit soort jobruns uitvoert.
	 */
	public abstract Class< ? extends EduArteJob> getJobClass();

	/**
	 * Voegt een {@link JobRunDetail} toe aan de lijst van details.
	 */
	public void info(String message)
	{
		JobRunDetail detail = new JobRunDetail(this);
		detail.setUitkomst(message.replaceAll("\n", "<br/>"));
		getDetails().add(detail);
	}

	/**
	 * Voegt een {@link JobRunDetail} toe aan de lijst van details.
	 */
	public void error(String message)
	{
		JobRunDetail detail = new JobRunDetail(this);
		detail.setUitkomst(message.replaceAll("\n", "<br/>"));
		getDetails().add(detail);
	}
}
