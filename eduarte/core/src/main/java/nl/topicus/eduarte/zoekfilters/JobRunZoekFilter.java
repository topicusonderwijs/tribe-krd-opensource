/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.jobs.logging.JobRun;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.security.authentication.Account;

import org.apache.wicket.model.IModel;

/**
 * @author maatman
 */
public class JobRunZoekFilter<T extends JobRun> extends AbstractZoekFilter<T>
{
	private static final long serialVersionUID = 1L;

	private Date begindatum;

	private Date einddatum;

	@AutoForm(htmlClasses = "unit_140")
	private String samenvatting;

	private IModel<Medewerker> medewerker;

	private IModel<Account> account;

	private Class< ? extends JobRun> subclass = JobRun.class;

	public JobRunZoekFilter(Class<T> subclass)
	{
		this.subclass = subclass;
		if (this.subclass == null)
			this.subclass = JobRun.class;
	}

	public Date getBeginDatum()
	{
		return begindatum;
	}

	public void setBeginDatum(Date begindatum)
	{
		this.begindatum = begindatum;
	}

	public Date getEindDatum()
	{
		return einddatum;
	}

	public void setEindDatum(Date einddatum)
	{
		this.einddatum = einddatum;
	}

	public final Class< ? extends JobRun> getSubclass()
	{
		return subclass;
	}

	public void setSamenvatting(String samenvatting)
	{
		this.samenvatting = samenvatting;
	}

	public String getSamenvatting()
	{
		return samenvatting;
	}

	public Medewerker getMedewerker()
	{
		return getModelObject(medewerker);
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = makeModelFor(medewerker);
	}

	public Account getAccount()
	{
		return getModelObject(account);
	}

	public void setAccount(Account account)
	{
		this.account = makeModelFor(account);
	}
}
