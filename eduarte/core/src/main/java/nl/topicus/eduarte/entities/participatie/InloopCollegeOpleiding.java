package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class InloopCollegeOpleiding extends InstellingEntiteit implements
		IInloopCollegeKoppeling<Opleiding>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "inloopCollege")
	@Index(name = "idx_InloopCollegeO_college")
	private InloopCollege inloopCollege;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "opleiding")
	@Index(name = "idx_InlpCollOp_opleiding")
	private Opleiding opleiding;

	public InloopCollegeOpleiding()
	{
	}

	@Override
	public InloopCollege getInloopCollege()
	{
		return inloopCollege;
	}

	@Override
	public void setInloopCollege(InloopCollege inloopCollege)
	{
		this.inloopCollege = inloopCollege;
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	@Override
	public void setKoppeling(Opleiding entiteit)
	{
		setOpleiding(entiteit);
	}

	@Override
	public Opleiding getKoppeling()
	{
		return getOpleiding();
	}
}
