package nl.topicus.eduarte.entities.dbs.trajecten;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TrajectUitvoerder extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Medewerker", nullable = false)
	@ForeignKey(name = "FK_TrajUitv_Medew")
	@Index(name = "idx_TrajUitv_Medew")
	private Medewerker medewerker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Traject", nullable = false)
	@ForeignKey(name = "FK_TrajUitv_Traj")
	@Index(name = "idx_TrajUitv_Traj")
	@IgnoreInGebruik
	private Traject traject;

	public TrajectUitvoerder()
	{
	}

	public TrajectUitvoerder(Traject traject, Medewerker medewerker)
	{
		setMedewerker(medewerker);
		setTraject(traject);
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Traject getTraject()
	{
		return traject;
	}

	public void setTraject(Traject traject)
	{
		this.traject = traject;
	}
}
