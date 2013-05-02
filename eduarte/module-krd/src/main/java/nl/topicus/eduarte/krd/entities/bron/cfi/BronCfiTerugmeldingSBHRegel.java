package nl.topicus.eduarte.krd.entities.bron.cfi;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * SBH Regel van een CFI-terugmelding.
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronCfiTerugmeldingSBHRegel extends BronCfiTerugmeldingRegel
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Integer aantal;

	public BronCfiTerugmeldingSBHRegel()
	{
	}

	public BronCfiTerugmeldingSBHRegel(String[] velden)
	{
		super(BronCfiRegelType.SBH, velden);
		setAantal(Integer.valueOf(velden[4]));
		setOpmerking(velden[5]);
	}

	public void setAantal(Integer aantal)
	{
		this.aantal = aantal;
	}

	public Integer getAantal()
	{
		return aantal;
	}
}
