package nl.topicus.eduarte.entities.dbs.bijlagen;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.trajecten.BegeleidingsHandeling;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BegeleidingsBijlage extends DeelnemerBijlage
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@Basic(optional = false)
	@JoinColumn(nullable = true, name = "begeleidingsHandeling")
	@Index(name = "idx_Bijlage_BegHand")
	private BegeleidingsHandeling begeleidingsHandeling;

	public BegeleidingsBijlage()
	{
	}

	public BegeleidingsHandeling getBegeleidingsHandeling()
	{
		return begeleidingsHandeling;
	}

	public void setBegeleidingsHandeling(BegeleidingsHandeling begeleidingsHandeling)
	{
		this.begeleidingsHandeling = begeleidingsHandeling;
	}

	@Override
	public IBijlageKoppelEntiteit<BegeleidingsBijlage> getEntiteit()
	{
		return getBegeleidingsHandeling();
	}
}
