package nl.topicus.eduarte.entities.taxonomie.educatie;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Elementcode voor Basiseducatie (dat is dus een andere taxonomie dan het VO!). De
 * VAVO-elementcodes vallen wel onder het VO, en komen dus bij deze entiteit niet voor.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class BasiseducatieElementcode extends Verbintenisgebied
{
	private static final long serialVersionUID = 1L;

	public BasiseducatieElementcode()
	{
		super();
	}

	public BasiseducatieElementcode(EntiteitContext context)
	{
		super(context);
	}
}
