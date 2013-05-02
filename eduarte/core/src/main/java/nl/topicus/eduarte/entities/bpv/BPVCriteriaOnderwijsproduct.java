package nl.topicus.eduarte.entities.bpv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bpv.BPVCriteria.BPVCriteriaStatus;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Index;

@Entity
public class BPVCriteriaOnderwijsproduct extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "onderwijsproduct", nullable = false)
	@Index(name = "idx_bpvCrOp_op")
	private Onderwijsproduct onderwijsproduct;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvCriteria", nullable = false)
	@Index(name = "idx_bpvCrOp_crit")
	private BPVCriteria bpvCriteria;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BPVCriteriaStatus status;

	public BPVCriteriaOnderwijsproduct()
	{
	}

	public BPVCriteriaOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		setOnderwijsproduct(onderwijsproduct);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
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
}