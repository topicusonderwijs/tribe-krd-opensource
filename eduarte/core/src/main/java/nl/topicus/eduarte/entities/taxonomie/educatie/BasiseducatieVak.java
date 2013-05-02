package nl.topicus.eduarte.entities.taxonomie.educatie;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Vak voor Basiseducatie.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class BasiseducatieVak extends Deelgebied
{
	private static final long serialVersionUID = 1L;

	public BasiseducatieVak()
	{
		super();
	}

	public BasiseducatieVak(EntiteitContext context)
	{
		super(context);
	}
}
