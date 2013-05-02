package nl.topicus.eduarte.entities.resultaatstructuur;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppelt een medewerker aan een formatieve resultaatstructuur. Als een medewerker
 * gekoppeld is, kan deze resultaten invoeren voor de structuur. Alleen de auteur kan de
 * structuur bewerken.
 * 
 * @author papegaaij
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
public class ResultaatstructuurMedewerker extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "medewerker")
	@Index(name = "idx_resStrucMdwk_medewerker")
	private Medewerker medewerker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "resultaatstructuur")
	@Index(name = "idx_resStrucMdwk_structuur")
	private Resultaatstructuur resultaatstructuur;

	public ResultaatstructuurMedewerker()
	{
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Resultaatstructuur getResultaatstructuur()
	{
		return resultaatstructuur;
	}

	public void setResultaatstructuur(Resultaatstructuur resultaatstructuur)
	{
		this.resultaatstructuur = resultaatstructuur;
	}
}
