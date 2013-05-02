package nl.topicus.eduarte.entities.bpv;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Index;

@Entity
public class BPVPlaatsOpleiding extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opleiding", nullable = false)
	@Index(name = "idx_bpvPlOpl_opl")
	private Opleiding opleiding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvPlaats", nullable = false)
	@Index(name = "idx_bpvPlOpl_pl")
	private BPVPlaats bpvPlaats;

	public BPVPlaatsOpleiding()
	{

	}

	public BPVPlaatsOpleiding(BPVPlaats bpvPlaats)
	{
		setBpvPlaats(bpvPlaats);
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setBpvPlaats(BPVPlaats bpvPlaats)
	{
		this.bpvPlaats = bpvPlaats;
	}

	public BPVPlaats getBpvPlaats()
	{
		return bpvPlaats;
	}

	@Override
	public String toString()
	{
		return getOpleiding().toString();
	}

}