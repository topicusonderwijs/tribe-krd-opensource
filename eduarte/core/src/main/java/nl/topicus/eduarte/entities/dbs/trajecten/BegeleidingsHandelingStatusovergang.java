package nl.topicus.eduarte.entities.dbs.trajecten;

import java.util.Date;

import javax.persistence.*;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(name = "BegHandStatOvrgang")
public class BegeleidingsHandelingStatusovergang extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(columnDefinition = "date", nullable = false)
	private Date datumTijd;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private BegeleidingsHandelingsStatussoort vanStatus;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BegeleidingsHandelingsStatussoort naarStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "begeleidingsHandeling", nullable = true)
	@ForeignKey(name = "FK_BHStatus_begHand")
	@Index(name = "idx_BHStatus_BegHand")
	private BegeleidingsHandeling begeleidingsHandeling;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = false)
	@ForeignKey(name = "FK_BHStatus_medewerker")
	@Index(name = "idx_BHStatus_medewerker")
	private Medewerker medewerker;

	public BegeleidingsHandelingStatusovergang()
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

	public BegeleidingsHandelingsStatussoort getVanStatus()
	{
		return vanStatus;
	}

	public void setVanStatus(BegeleidingsHandelingsStatussoort vanStatus)
	{
		this.vanStatus = vanStatus;
	}

	public BegeleidingsHandelingsStatussoort getNaarStatus()
	{
		return naarStatus;
	}

	public void setNaarStatus(BegeleidingsHandelingsStatussoort naarStatus)
	{
		this.naarStatus = naarStatus;
	}

	public BegeleidingsHandeling getBegeleidingsHandeling()
	{
		return begeleidingsHandeling;
	}

	public void setBegeleidingsHandeling(BegeleidingsHandeling begeleidingsHandeling)
	{
		this.begeleidingsHandeling = begeleidingsHandeling;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}
}
