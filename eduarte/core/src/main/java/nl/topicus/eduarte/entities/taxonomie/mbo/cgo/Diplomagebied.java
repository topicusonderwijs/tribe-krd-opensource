package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.mbo.AbstractMBOVerbintenisgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Diplomagebied extends AbstractMBOVerbintenisgebied
{
	private static final long serialVersionUID = 1L;

	public Diplomagebied()
	{
		super();
	}

	public Diplomagebied(EntiteitContext context)
	{
		super(context);
	}
}
