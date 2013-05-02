package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * De matrix voor Leren, Loopbaan en Burgerschap.
 * 
 * @author vandenbrink
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class LLBMatrix extends CompetentieMatrix
{
	private static final long serialVersionUID = 1L;

	public LLBMatrix()
	{
	}

	public LLBMatrix(EntiteitContext context)
	{
		super(context);
	}

	@Override
	public String getType()
	{
		return "LLB";
	}
}
