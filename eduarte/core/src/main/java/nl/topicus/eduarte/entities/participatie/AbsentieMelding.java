/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.participatie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.Time;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieMeldingDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.participatie.enums.IParticipatieBlokObject;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieMeldingZoekFilter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een absentiemelding voor een deelnemer. Een absentiemelding geeft aan of een deelnemer
 * in een bepaalde periode afwezig is geweest of zal zijn. Een absentiemelding is niet
 * gekoppeld aan het rooster of aan een afspraak, maar is puur een indicatie waarom een
 * deelnemer in een bepaalde periode niet aanwezig is. Waarnemingen kunnen vervolgens
 * gekoppeld worden aan absentiemeldingen om op die manier afgehandeld te worden.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class AbsentieMelding extends InstellingEntiteit implements IParticipatieBlokObject
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_AbsentieMelding_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "absentieReden", nullable = false)
	@Index(name = "idx_absentMeld_absentReden")
	private AbsentieReden absentieReden;

	/**
	 * Als een absentieMelding niet eenmalig is, maar elke X weken terugkeert wordt een
	 * HerhalendeAbsentieMelding aangemaakt. Alle absentieMeldingen die het resultaat zijn
	 * van de herhalende absentieMelding verwijzen naar dezelfde herhalende
	 * absentieMelding. Op deze manier kan bij het wijzigen van een herhalende
	 * absentieMelding eventueel ook alle andere bijbehorende absentieMeldingen gewijzigd
	 * worden.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "herhalendeAbsentieMelding", nullable = true)
	@Index(name = "idx_Afspraak_herhalendeAbsen")
	private HerhalendeAbsentieMelding herhalendeAbsentieMelding;

	/**
	 * beginDatumTijd bevat zowel de begindatum als de begintijd van de melding. De
	 * begintijd moet in veel gevallen gehaald worden uit het beginlesuur dat door de
	 * gebruiker ingevuld wordt. Als de melding voor een gehele dag is, of voor meerdere
	 * dagen, is de begintijd gelijk aan 00:00:00.
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date beginDatumTijd;

	/**
	 * eindDatumTijd bevat zowel de einddatum als de eindtijd van de melding. De eindtijd
	 * moet in veel gevallen gehaald worden uit het eindlesuur dat door de gebruiker
	 * ingevuld wordt. Als de melding voor een gehele dag is, of voor meerdere dagen, is
	 * de eindtijd gelijk aan 23:59:59. De eindDatumTijd is dus inclusief.
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date eindDatumTijd;

	/**
	 * Het beginlesuur waarop de absentiemelding ingaat. Dit is tot op zekere hoogte
	 * redundant met de begintijd, maar is toch handige informatie; de gebruikers willen
	 * dit vaak kunnen zien en het terugrekenen is lastig. Als beginLesuur null is, is
	 * eindLesuur ook per definitie null, en is de absentiemelding voor een gehele dag of
	 * voor meerdere dagen. Als begin- en eindlesuur wel ingevuld zijn, moet de datum van
	 * de begin- en einddatumtijd gelijk zijn aan elkaar.
	 */
	@Column(nullable = true)
	private Integer beginLesuur;

	@Column(nullable = true)
	private Integer eindLesuur;

	/**
	 * Indicatie of de absentiemelding afgehandeld is. Als de melding nog niet afgehandeld
	 * is, is de melding nog 'open' en wordt deze standaard getoond op het invoerscherm
	 * voor deelnemers.
	 */
	@Column(nullable = false)
	private boolean afgehandeld;

	/**
	 * Eventuele opmerkingen die specifiek over deze absentiemelding gaan.
	 */
	@Column(length = 1024, nullable = true)
	private String opmerkingen;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "absentieMelding")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<Waarneming> waarnemingen = new ArrayList<Waarneming>();

	/**
	 * soort van copy contructor
	 * 
	 * @param absentieMelding
	 */
	public AbsentieMelding(AbsentieMelding absentieMelding)
	{
		this.deelnemer = absentieMelding.getDeelnemer();
		this.absentieReden = absentieMelding.getAbsentieReden();
		this.beginDatumTijd = absentieMelding.getBeginDatumTijd();
		this.eindDatumTijd = absentieMelding.getEindDatumTijd();
		this.beginLesuur = absentieMelding.getBeginLesuur();
		this.eindLesuur = absentieMelding.getEindLesuur();
		this.afgehandeld = absentieMelding.isAfgehandeld();
		this.opmerkingen = absentieMelding.getOpmerkingen();
	}

	public AbsentieMelding()
	{
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public AbsentieReden getAbsentieReden()
	{
		return absentieReden;
	}

	public void setAbsentieReden(AbsentieReden absentieReden)
	{
		this.absentieReden = absentieReden;
	}

	public Date getBeginDatumTijd()
	{
		return beginDatumTijd;
	}

	public void setBeginDatumTijd(Date beginDatumTijd)
	{
		this.beginDatumTijd = beginDatumTijd;
	}

	public Date getEindDatumTijd()
	{
		return eindDatumTijd;
	}

	public void setEindDatumTijd(Date eindDatumTijd)
	{
		this.eindDatumTijd = eindDatumTijd;
	}

	public Integer getBeginLesuur()
	{
		return beginLesuur;
	}

	public void setBeginLesuur(Integer beginLesuur)
	{
		this.beginLesuur = beginLesuur;
	}

	public Integer getEindLesuur()
	{
		return eindLesuur;
	}

	public void setEindLesuur(Integer eindLesuur)
	{
		this.eindLesuur = eindLesuur;
	}

	public boolean isAfgehandeld()
	{
		return afgehandeld;
	}

	public void setAfgehandeld(boolean afgehandeld)
	{
		this.afgehandeld = afgehandeld;
	}

	public String getOpmerkingen()
	{
		return opmerkingen;
	}

	public void setOpmerkingen(String opmerkingen)
	{
		this.opmerkingen = opmerkingen;
	}

	public HerhalendeAbsentieMelding getHerhalendeAbsentieMelding()
	{
		return herhalendeAbsentieMelding;
	}

	public void setHerhalendeAbsentieMelding(HerhalendeAbsentieMelding herhalendeAbsentieMelding)
	{
		this.herhalendeAbsentieMelding = herhalendeAbsentieMelding;
	}

	/**
	 * @return String met de begin/einddatum dan wel datum en begin/eindlesuur.
	 */
	public String getBeginEind()
	{
		return getBeginEind(true);
	}

	/**
	 * @return String met begin/eindtijd van deze melding (zonder datum).
	 */
	public String getBeginEindZonderDatum()
	{
		return getBeginEind(false);
	}

	private String getBeginEind(boolean metDatum)
	{
		StringBuilder res = new StringBuilder();
		if (getBeginLesuur() != null && getBeginLesuur() != 0)
		{
			if (metDatum)
			{
				res.append(TimeUtil.getInstance().formatDate(getBeginDatumTijd())).append(" ");
			}
			if (getBeginLesuur().intValue() == getEindLesuur().intValue())
			{
				res.append(getBeginLesuur()).append("e uur ");
			}
			else
			{
				res.append(getBeginLesuur()).append("e t/m ").append(getEindLesuur()).append(
					"e uur");
			}
		}
		else
		{
			Date begindatum = TimeUtil.getInstance().asDate(getBeginDatumTijd());
			Date einddatum = TimeUtil.getInstance().asDate(getEindDatumTijd());
			if (begindatum.equals(einddatum))
			{
				if (isVoorHeleDagen())
				{
					res.append(TimeUtil.getInstance().formatDate(getBeginDatumTijd()));
				}
				else
				{
					if (metDatum)
					{
						res.append(TimeUtil.getInstance().formatDate(getBeginDatumTijd()));
					}
					Time beginTijd = new Time(getBeginDatumTijd().getTime());
					Time eindTijd = new Time(getEindDatumTijd().getTime());
					if (!eindTijd.before(beginTijd))
					{
						res.append(" ").append(beginTijd).append(" - ").append(eindTijd);
					}
				}
			}
			else
			{
				res.append(TimeUtil.getInstance().formatDate(getBeginDatumTijd())).append(" t/m ");
				if (getEindDatumTijd() != null)
					res.append(TimeUtil.getInstance().formatDate(getEindDatumTijd()));
				else
					res.append("...");
			}
		}
		return res.toString();
	}

	/**
	 * @return Ja indien afgehandeld, en anders Nee.
	 */
	public String getAfgehandeldOmschrijving()
	{
		return isAfgehandeld() ? "Ja" : "Nee";
	}

	/**
	 * @return Toekomstig, Vandaag, Gisteren, Deze maand, Dit schooljaar, 2006/2007 etc.
	 */
	public String getGroepeerDatumOmschrijving()
	{
		return TimeUtil.getInstance().getDateGroup(getBeginDatumTijd());
	}

	/**
	 * @return geeft true terug als deze absentiemelding voor 1 hele dag is, anders false
	 */
	public boolean isVoorHeleDagen()
	{
		if (beginDatumTijd != null && eindDatumTijd != null)
		{
			Date beginDag = TimeUtil.getInstance().maakBeginVanDagVanDatum(beginDatumTijd);
			Date eindDag = TimeUtil.getInstance().maakEindeVanDagVanDatum(eindDatumTijd);
			if (beginDatumTijd.getTime() == (beginDag.getTime())
				|| eindDatumTijd.getTime() == eindDag.getTime())
				return true;
			if (TimeUtil.getInstance().getDifferenceInDays(beginDatumTijd, eindDatumTijd) > 0)
				return true;
		}
		return false;

	}

	/**
	 * @return true als er overlappende meldingen bestaan, anders false
	 */
	public boolean bestaanOverlappendeMeldingen()
	{
		AbsentieMeldingZoekFilter filter = new AbsentieMeldingZoekFilter();
		filter.setDeelnemer(getDeelnemer());
		filter.setBeginDatumTijd(getBeginDatumTijd());
		filter.setEindDatumTijd(getEindDatumTijd());
		List<AbsentieMelding> absentieMeldingenList = new ArrayList<AbsentieMelding>();
		absentieMeldingenList =
			DataAccessRegistry.getHelper(AbsentieMeldingDataAccessHelper.class)
				.getOverlappendeMeldingen(filter);
		if (absentieMeldingenList != null && !absentieMeldingenList.isEmpty())
		{
			if (absentieMeldingenList.contains(this))
			{
				if (absentieMeldingenList.size() > 1)
					return true;
			}
			else if (absentieMeldingenList.size() > 0)
				return true;
		}
		return false;
	}

	@Override
	public String getCssClass()
	{
		String cssClass = "red-cell";
		if (getAbsentieReden().isGeoorloofd())
			cssClass = "yellow-cell";
		return cssClass;
	}

	@Override
	public String getTitle()
	{
		String title = getAbsentieReden().getOmschrijving();
		title += " " + getBeginEind();
		if (getOpmerkingen() != null)
			title += " [" + getOpmerkingen() + "]";
		return title;
	}

	@Override
	public boolean isActiefOpDatum(Date datum)
	{
		return (!getBeginDatumTijd().after(datum)) && getEindDatumTijd().after(datum);
	}

	// Als deze gebruikt werd moet dit op de pagina opgelost worden
	// Henzen 17-06-2009 Overzetten Participatie
	// /**
	// * @return "Ja", als de ingelogde gebruiker eigenaar is van deze absentiemelding,
	// * anders "Nee"
	// */
	// public String isEigenaarOmschrijving()
	// {
	// return isEigenaar() ? "Ja" : "Nee";
	// }

	/**
	 * @return "Ja", als de ingelogde gebruiker eigenaar is van deze absentiemelding,
	 *         anders "Nee"
	 */
	public boolean isEigenaar(Account gebruiker)
	{
		return gebruiker.equals(getCreatedBy());
	}

	public List<Waarneming> getWaarnemingen()
	{
		return waarnemingen;
	}

	public void setWaarnemingen(List<Waarneming> waarnemingen)
	{
		this.waarnemingen = waarnemingen;
	}
}
