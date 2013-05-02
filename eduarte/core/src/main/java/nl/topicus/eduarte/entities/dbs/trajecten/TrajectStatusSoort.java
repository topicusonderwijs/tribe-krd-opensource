/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.dbs.trajecten;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TrajectStatusSoort extends InstellingEntiteit implements IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 255)
	@AutoForm(htmlClasses = "unit_max")
	private String omschrijving;

	@Column(nullable = false)
	private boolean actief = true;

	@Column(nullable = false)
	private boolean trajectAfgesloten;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trajectStatusSoort")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@AutoForm(include = false)
	private List<ToegestaneStatusSoort> toegestaneStatusSoorten =
		new ArrayList<ToegestaneStatusSoort>();

	public TrajectStatusSoort()
	{
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public boolean isTrajectAfgesloten()
	{
		return trajectAfgesloten;
	}

	public void setTrajectAfgesloten(boolean trajectAfgesloten)
	{
		this.trajectAfgesloten = trajectAfgesloten;
	}

	public List<ToegestaneStatusSoort> getToegestaneStatusSoorten()
	{
		return toegestaneStatusSoorten;
	}

	public void setToegestaneStatusSoorten(List<ToegestaneStatusSoort> toegestaneStatusSoorten)
	{
		this.toegestaneStatusSoorten = toegestaneStatusSoorten;
	}

	@Override
	public String toString()
	{
		return getOmschrijving();
	}
}
