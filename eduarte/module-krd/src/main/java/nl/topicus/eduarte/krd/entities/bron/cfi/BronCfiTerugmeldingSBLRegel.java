package nl.topicus.eduarte.krd.entities.bron.cfi;

import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * SBL Regel van een CFI-terugmelding.
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronCfiTerugmeldingSBLRegel extends BronCfiTerugmeldingRegel
{
	private static final long serialVersionUID = 1L;

	public BronCfiTerugmeldingSBLRegel()
	{
	}

	public BronCfiTerugmeldingSBLRegel(String[] velden)
	{
		super(BronCfiRegelType.SBL, velden);
		setOpmerking(velden[6]);
	}
}
