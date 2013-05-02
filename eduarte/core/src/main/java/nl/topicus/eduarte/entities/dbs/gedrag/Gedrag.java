package nl.topicus.eduarte.entities.dbs.gedrag;

import javax.persistence.Entity;

import nl.topicus.eduarte.entities.dbs.ZorgvierkantObject;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Abstract base class voor Incidenten en Notities.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public abstract class Gedrag extends InstellingEntiteit implements ZorgvierkantObject
{
	private static final long serialVersionUID = 1L;

	public Gedrag()
	{
	}
}
