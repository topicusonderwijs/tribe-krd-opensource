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
public class BPVCriteriaBPVKandidaat extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvKandidaat", nullable = false)
	@Index(name = "idx_bpvCrKa_kand")
	private BPVKandidaat bpvKandidaat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvCriteria", nullable = false)
	@Index(name = "idx_bpvCrKa_crit")
	private BPVCriteria bpvCriteria;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BPVCriteriaStatus status;

	public BPVCriteriaBPVKandidaat()
	{
	}

	public BPVCriteriaBPVKandidaat(BPVKandidaat bpvKandidaat)
	{
		setBpvKandidaat(bpvKandidaat);
	}

	public BPVCriteriaBPVKandidaat(BPVKandidaat bpvKandidaat, BPVCriteria bpvCriteria,
			BPVCriteriaStatus status)
	{
		setBpvKandidaat(bpvKandidaat);
		setBpvCriteria(bpvCriteria);
		setStatus(status);
	}

	public void setBpvKandidaat(BPVKandidaat bpvKandidaat)
	{
		this.bpvKandidaat = bpvKandidaat;
	}

	public BPVKandidaat getBpvKandidaat()
	{
		return bpvKandidaat;
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
	public BPVCriteriaBPVKandidaat clone()
	{
		return new BPVCriteriaBPVKandidaat(getBpvKandidaat(), getBpvCriteria(), getStatus());
	}
}