package nl.topicus.eduarte.entities.resultaatstructuur;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppelt een deelnemer of groep aan een formatieve resultaatstructuur. Als een deelnemer
 * (al dan niet via een groep) gekoppeld is, kunnen op de structuur resultaten ingevoerd
 * worden voor de deelnemer.
 * 
 * @author papegaaij
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
public class ResultaatstructuurDeelnemer extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "deelnemer")
	@Index(name = "idx_resStrucDlnmr_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "groep")
	@Index(name = "idx_resStrucDlnmr_groep")
	private Groep groep;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "resultaatstructuur")
	@Index(name = "idx_resStrucDlnmr_structuur")
	private Resultaatstructuur resultaatstructuur;

	public ResultaatstructuurDeelnemer()
	{
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Groep getGroep()
	{
		return groep;
	}

	public void setGroep(Groep groep)
	{
		this.groep = groep;
	}

	public Resultaatstructuur getResultaatstructuur()
	{
		return resultaatstructuur;
	}

	public void setResultaatstructuur(Resultaatstructuur resultaatstructuur)
	{
		this.resultaatstructuur = resultaatstructuur;
	}

	public String getDeelnemerOfGroep()
	{
		return getDeelnemer() == null ? getGroep().toString() : getDeelnemer().toString();
	}

	public IdObject getEntity()
	{
		return getDeelnemer() == null ? getGroep() : getDeelnemer();
	}

	public void setEntity(IdObject entiteit)
	{
		if (entiteit instanceof Deelnemer)
			setDeelnemer((Deelnemer) entiteit);
		else
			setGroep((Groep) entiteit);
	}
}
