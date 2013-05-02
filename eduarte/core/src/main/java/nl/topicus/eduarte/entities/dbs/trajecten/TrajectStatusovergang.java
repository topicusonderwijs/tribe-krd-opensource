package nl.topicus.eduarte.entities.dbs.trajecten;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TrajectStatusovergang extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(columnDefinition = "date", nullable = false)
	private Date datumTijd;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = false)
	@ForeignKey(name = "FK_TrajStato_medewerker")
	@Index(name = "idx_TrajStato_medewerker")
	private Medewerker medewerker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "traject", nullable = false)
	@ForeignKey(name = "FK_TrajStato_traject")
	@Index(name = "idx_TrajStato_traject")
	@IgnoreInGebruik
	private Traject traject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vanStatus", nullable = true)
	@ForeignKey(name = "FK_TrajStato_van")
	@Index(name = "idx_TrajStato_van")
	private TrajectStatusSoort vanStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "naarStatus", nullable = false)
	@ForeignKey(name = "FK_TrajStato_naar")
	@Index(name = "idx_TrajStato_naar")
	private TrajectStatusSoort naarStatus;

	public TrajectStatusovergang()
	{
	}

	public Date getDatumTijd()
	{
		return datumTijd;
	}

	public void setDatumTijd(Date datumTijd)
	{
		this.datumTijd = datumTijd;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Traject getTraject()
	{
		return traject;
	}

	public void setTraject(Traject traject)
	{
		this.traject = traject;
	}

	public TrajectStatusSoort getVanStatus()
	{
		return vanStatus;
	}

	public void setVanStatus(TrajectStatusSoort vanStatus)
	{
		this.vanStatus = vanStatus;
	}

	public TrajectStatusSoort getNaarStatus()
	{
		return naarStatus;
	}

	public void setNaarStatus(TrajectStatusSoort naarStatus)
	{
		this.naarStatus = naarStatus;
	}
}
