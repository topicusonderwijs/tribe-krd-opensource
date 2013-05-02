package nl.topicus.eduarte.entities.bpv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bpv.BPVCriteria.BPVCriteriaStatus;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Index;

@Entity
public class BPVCriteriaBPVPlaats extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvPlaats", nullable = false)
	@Index(name = "idx_bpvCrPl_plaats")
	private BPVPlaats bpvPlaats;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvCriteria", nullable = false)
	@Index(name = "idx_bpvCrPl_crit")
	private BPVCriteria bpvCriteria;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BPVCriteriaStatus status;

	public BPVCriteriaBPVPlaats()
	{
	}

	public BPVCriteriaBPVPlaats(BPVPlaats bpvPlaats)
	{
		this.bpvPlaats = bpvPlaats;
	}

	public void setBpvPlaats(BPVPlaats bpvPlaats)
	{
		this.bpvPlaats = bpvPlaats;
	}

	public BPVPlaats getBpvPlaats()
	{
		return bpvPlaats;
	}

	public void setStatus(BPVCriteriaStatus status)
	{
		this.status = status;
	}

	public BPVCriteriaStatus getStatus()
	{
		return status;
	}

	public void setBpvCriteria(BPVCriteria bpvCriteria)
	{
		this.bpvCriteria = bpvCriteria;
	}

	public BPVCriteria getBpvCriteria()
	{
		return bpvCriteria;
	}

	@Override
	public String toString()
	{
		return bpvCriteria.getNaam();
	}

}