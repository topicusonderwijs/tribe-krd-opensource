package nl.topicus.eduarte.entities.taxonomie.vo;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * De landelijke vakken voor het VO.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class LandelijkVak extends Deelgebied
{
	private static final long serialVersionUID = 1L;

	public LandelijkVak()
	{
		super();
	}

	public LandelijkVak(EntiteitContext context)
	{
		super(context);
	}
}
