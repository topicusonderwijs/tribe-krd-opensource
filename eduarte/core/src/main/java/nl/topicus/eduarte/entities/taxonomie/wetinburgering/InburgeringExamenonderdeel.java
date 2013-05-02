package nl.topicus.eduarte.entities.taxonomie.wetinburgering;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Examenonderdeel voor wet inburgering.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class InburgeringExamenonderdeel extends Deelgebied
{
	private static final long serialVersionUID = 1L;

	public InburgeringExamenonderdeel()
	{
		super();
	}

	public InburgeringExamenonderdeel(EntiteitContext context)
	{
		super(context);
	}
}
