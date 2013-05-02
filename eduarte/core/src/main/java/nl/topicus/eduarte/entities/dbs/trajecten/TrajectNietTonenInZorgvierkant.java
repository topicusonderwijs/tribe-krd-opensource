package nl.topicus.eduarte.entities.dbs.trajecten;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.dbs.NietTonenInZorgvierkant;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TrajectNietTonenInZorgvierkant extends NietTonenInZorgvierkant
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "traject", nullable = true)
	@Index(name = "idx_TonenInZorgv_traject")
	@IgnoreInGebruik
	private Traject traject;

	public TrajectNietTonenInZorgvierkant()
	{
	}

	public TrajectNietTonenInZorgvierkant(Traject traject, Medewerker medewerker)
	{
		super(medewerker);
		setTraject(traject);
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
