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
public class BPVCriteriaBPVDeelnemerProfiel extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvDeelnemerProfiel", nullable = false)
	@Index(name = "idx_bpvCrDln_dln")
	private BPVDeelnemerProfiel bpvDeelnemerProfiel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvCriteria", nullable = false)
	@Index(name = "idx_bpvCrDln_crit")
	private BPVCriteria criteria;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BPVCriteriaStatus status;

	public void setCriteria(BPVCriteria criteria)
	{
		this.criteria = criteria;
	}

	public BPVCriteria getCriteria()
	{
		return criteria;
	}

	public void setBpvDeelnemerProfiel(BPVDeelnemerProfiel bpvDeelnemerProfiel)
	{
		this.bpvDeelnemerProfiel = bpvDeelnemerProfiel;
	}

	public BPVDeelnemerProfiel getBpvDeelnemerProfiel()
	{
		return bpvDeelnemerProfiel;
	}

	public void setStatus(BPVCriteriaStatus status)
	{
		this.status = status;
	}

	public BPVCriteriaStatus getStatus()
	{
		return status;
	}
}