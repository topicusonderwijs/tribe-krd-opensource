package nl.topicus.eduarte.entities.bpv;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Index;

@Entity
public class BPVKandidaatOnderwijsproduct extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "onderwijsproduct", nullable = false)
	@Index(name = "idx_bpvKandOp_op")
	private Onderwijsproduct onderwijsproduct;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvKandidaat", nullable = false)
	@Index(name = "idx_bbpKandOp_kand")
	private BPVKandidaat bpvKandidaat;

	public BPVKandidaatOnderwijsproduct()
	{
	}

	public BPVKandidaatOnderwijsproduct(BPVKandidaat bpvKandidaat)
	{
		setBpvKandidaat(bpvKandidaat);
	}

	public BPVKandidaatOnderwijsproduct(BPVKandidaat bpvKandidaat, Onderwijsproduct onderwijsproduct)
	{
		setOnderwijsproduct(onderwijsproduct);
		setBpvKandidaat(bpvKandidaat);
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public void setBpvKandidaat(BPVKandidaat bpvKandidaat)
	{
		this.bpvKandidaat = bpvKandidaat;
	}

	public BPVKandidaat getBpvKandidaat()
	{
		return bpvKandidaat;
	}

	@Override
	public BPVKandidaatOnderwijsproduct clone()
	{
		return new BPVKandidaatOnderwijsproduct(getBpvKandidaat(), getOnderwijsproduct());
	}
}