package nl.topicus.eduarte.entities.dbs.bijlagen;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.Bijzonderheid;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BijzonderheidBijlage extends DeelnemerBijlage
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bijzonderheid", nullable = true)
	@Index(name = "idx_Bijlage_bijzonderheid")
	private Bijzonderheid bijzonderheid;

	public BijzonderheidBijlage()
	{
	}

	public Bijzonderheid getBijzonderheid()
	{
		return bijzonderheid;
	}

	public void setBijzonderheid(Bijzonderheid bijzonderheid)
	{
		this.bijzonderheid = bijzonderheid;
	}

	@Override
	public IBijlageKoppelEntiteit<BijzonderheidBijlage> getEntiteit()
	{
		return getBijzonderheid();
	}
}
