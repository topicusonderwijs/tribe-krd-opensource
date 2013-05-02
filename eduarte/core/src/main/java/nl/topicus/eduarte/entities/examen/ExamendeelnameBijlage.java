package nl.topicus.eduarte.entities.examen;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen examendeelname en bijlage
 * 
 * @author hoeve
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ExamendeelnameBijlage extends DeelnemerBijlage
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "examendeelname")
	@Index(name = "idx_EDBijlage_examendeelname")
	private Examendeelname examendeelname;

	public ExamendeelnameBijlage()
	{
	}

	public Examendeelname getExamendeelname()
	{
		return examendeelname;
	}

	public void setExamendeelname(Examendeelname examendeelname)
	{
		this.examendeelname = examendeelname;
	}

}
