package nl.topicus.eduarte.entities.taxonomie.wetinburgering;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Doel voor inburgering; Inburgering of Staatsexamen
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class DoelInburgering extends Verbintenisgebied
{
	private static final long serialVersionUID = 1L;

	public DoelInburgering()
	{
		super();
	}

	public DoelInburgering(EntiteitContext context)
	{
		super(context);
	}
}
