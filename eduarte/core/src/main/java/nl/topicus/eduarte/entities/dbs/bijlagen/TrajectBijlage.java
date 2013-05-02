package nl.topicus.eduarte.entities.dbs.bijlagen;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TrajectBijlage extends DeelnemerBijlage
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@Basic(optional = false)
	@JoinColumn(nullable = true, name = "traject")
	@ForeignKey(name = "FK_TestBijlage_traject")
	@Index(name = "idx_Bijlage_traject")
	private Traject traject;

	public TrajectBijlage()
	{
	}

	public Traject getTraject()
	{
		return traject;
	}

	public void setTraject(Traject traject)
	{
		this.traject = traject;
	}

	@Override
	public IBijlageKoppelEntiteit<TrajectBijlage> getEntiteit()
	{
		return getTraject();
	}
}
