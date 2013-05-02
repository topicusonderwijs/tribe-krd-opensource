package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.mbo.AbstractMBOVerbintenisgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Opleidingsdomein is het hoogste element in de cgo-taxonomie.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Opleidingsdomein extends AbstractMBOVerbintenisgebied
{
	private static final long serialVersionUID = 1L;

	public Opleidingsdomein()
	{
		super();
	}

	public Opleidingsdomein(EntiteitContext context)
	{
		super(context);
	}
}
