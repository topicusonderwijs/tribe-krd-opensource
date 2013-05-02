/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.inschrijving;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.RedenUitschrijvingDataAccessHelper;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.RedenUitval;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"code",
	"organisatie"})})
@Exportable
@IsViewWhenOnNoise
public class RedenUitschrijving extends CodeNaamActiefInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@AutoForm(description = "Selecteer dit wanneer het om de reden van uitschrijven mbt Verbintenis gaat en het de reden van overlijden is. Niet te wijzigingen als deze reden al aan een verbintenis of BPV inschrijving gekoppeld is.")
	private boolean overlijden;

	@Column(nullable = false)
	@AutoForm(description = "Selecteer dit wanneer het om de reden van uitschrijven mbt een verbintenis gaat")
	private boolean tonenBijVerbintenis;

	@Column(nullable = false)
	@AutoForm(description = "Selecteer dit wanneer het om de reden van uitschrijven mbt BPV gaat", label = "Tonen bij BPV")
	private boolean tonenBijBPV;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@AutoForm(description = "Bij uitval: de hoofdcategorie van de uitvalsredenen zoals gespecificeerd door de MBO-raad. Dit wordt bij BVE-deelnemers aangeleverd aan BRON", htmlClasses = "unit_max")
	private RedenUitval redenUitval;

	@Column(nullable = false)
	@AutoForm(description = "Selecteer dit wanneer deze reden betekent dat het diploma is behaald.")
	private boolean geslaagd;

	public static enum UitstroomredenWI
	{
		Afgerond("a", "Volledig afgeronde cursus"),
		Kinderopvang("c", "Geen passende kinderopvang"),
		Aanbod("d", "Geen passend aanbod"),
		Werk("e", "Werk (gestart tijdens cursus of uitgebreid tijdens cursus"),
		MeerStudiebelasting("f", "Meer studiebelasting is te zwaar gebleken"),
		VerwachteStudiebelasting("g", "Verwachte studiebelasting is te zwaar gebleken"),
		LangdurigZiek("h", "Langdurig ziek"),
		AndereRoute("i", "Andere route, met goedkeuring van de gemeente"),
		Vrijgesteld("j", "Vrijgesteld door gemeente"),
		Verhuizing("k", "Verhuizing buiten gebied gemeente"),
		NietVerschenen("l", "Zonder opgaaf van reden niet verschenen"),
		Zwangerschap("m", "Zwangerschap"),
		Overlijden("n", "Overlijden"),
		Overig("o", "Overig");

		private String code;

		private String omschrijving;

		private UitstroomredenWI(String code, String omschrijving)
		{
			this.code = code;
			this.omschrijving = omschrijving;
		}

		@Override
		public String toString()
		{
			return code + ". " + omschrijving;
		}
	}

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	@AutoForm(label = "Uitstroomreden WI", htmlClasses = "unit_max")
	private UitstroomredenWI uitstroomredenWI;

	public RedenUitschrijving()
	{
	}

	public boolean isOverlijden()
	{
		return overlijden;
	}

	public void setOverlijden(boolean overlijden)
	{
		this.overlijden = overlijden;
	}

	public static RedenUitschrijving getOverledenReden()
	{
		return DataAccessRegistry.getHelper(RedenUitschrijvingDataAccessHelper.class)
			.getOverlijden();
	}

	public void setTonenBijVerbintenis(boolean tonenBijVerbintenis)
	{
		this.tonenBijVerbintenis = tonenBijVerbintenis;
	}

	public boolean isTonenBijVerbintenis()
	{
		return tonenBijVerbintenis;
	}

	public void setTonenBijBPV(boolean tonenBijBPV)
	{
		this.tonenBijBPV = tonenBijBPV;
	}

	public boolean isTonenBijBPV()
	{
		return tonenBijBPV;
	}

	public void setRedenUitval(RedenUitval redenUitval)
	{
		this.redenUitval = redenUitval;
	}

	public RedenUitval getRedenUitval()
	{
		return redenUitval;
	}

	public void setUitstroomredenWI(UitstroomredenWI uitstroomredenWI)
	{
		this.uitstroomredenWI = uitstroomredenWI;
	}

	public UitstroomredenWI getUitstroomredenWI()
	{
		return uitstroomredenWI;
	}

	@Override
	public String toString()
	{
		return getCode() + " - " + getNaam();
	}

	public void setGeslaagd(boolean geslaagd)
	{
		this.geslaagd = geslaagd;
	}

	public boolean isGeslaagd()
	{
		return geslaagd;
	}
}
