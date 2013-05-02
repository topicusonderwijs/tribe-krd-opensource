package nl.topicus.eduarte.entities.taxonomie.mbo;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Deelkwalificatie in de oude MBO-structuur
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Deelkwalificatie extends Deelgebied
{
	private static final long serialVersionUID = 1L;

	public Deelkwalificatie()
	{
		super();
	}

	public Deelkwalificatie(EntiteitContext context)
	{
		super(context);
	}
}
