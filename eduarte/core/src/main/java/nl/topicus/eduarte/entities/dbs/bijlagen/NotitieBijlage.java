package nl.topicus.eduarte.entities.dbs.bijlagen;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.gedrag.Notitie;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Bijlage die gekoppeld is aan een notitie en een deelnemer.
 * 
 * @author harmsen
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class NotitieBijlage extends DeelnemerBijlage
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@Basic(optional = false)
	@JoinColumn(name = "notitie", nullable = true)
	@Index(name = "idx_Bijlage_notitie")
	private Notitie notitie;

	public Notitie getNotitie()
	{
		return notitie;
	}

	public void setNotitie(Notitie notitie)
	{
		this.notitie = notitie;
	}

	@Override
	public IBijlageKoppelEntiteit<NotitieBijlage> getEntiteit()
	{
		return getNotitie();
	}
}
