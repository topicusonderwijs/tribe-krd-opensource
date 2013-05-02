package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class InloopCollegeGroep extends InstellingEntiteit implements
		IInloopCollegeKoppeling<Groep>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "inloopCollege")
	@Index(name = "idx_InloopCollegeG_college")
	private InloopCollege inloopCollege;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groep", nullable = false)
	@Index(name = "idx_InloopCollegeG_groep")
	private Groep groep;

	public InloopCollegeGroep()
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

	public void setGroep(Groep groep)
	{
		this.groep = groep;
	}

	public Groep getGroep()
	{
		return groep;
	}

	@Override
	public void setKoppeling(Groep entiteit)
	{
		setGroep(entiteit);
	}

	@Override
	public Groep getKoppeling()
	{
		return getGroep();
	}
}
