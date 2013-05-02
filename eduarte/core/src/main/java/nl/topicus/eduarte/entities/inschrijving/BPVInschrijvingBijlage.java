package nl.topicus.eduarte.entities.inschrijving;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen bpv-inschrijving en bijlage
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BPVInschrijvingBijlage extends DeelnemerBijlage
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "bpvInschrijving")
	@Index(name = "idx_VerbBijlage_bpvInschr")
	private BPVInschrijving bpvInschrijving;

	public BPVInschrijvingBijlage()
	{
	}

	public BPVInschrijving getBpvInschrijving()
	{
		return bpvInschrijving;
	}

	public void setBpvInschrijving(BPVInschrijving bpvInschrijving)
	{
		this.bpvInschrijving = bpvInschrijving;
	}

}
