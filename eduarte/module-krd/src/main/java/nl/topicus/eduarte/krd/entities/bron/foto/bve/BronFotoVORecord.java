package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Record in een VO fotobestand.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotoVORecord extends BronFotoRecord
{
	private static final long serialVersionUID = 1L;

	public BronFotoVORecord()
	{
	}

	public BronFotoVORecord(String[] velden)
	{
		super(velden);
	}

}
