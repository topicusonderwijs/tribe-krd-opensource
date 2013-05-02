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
public class BronCfiTerugmeldingEXPRegel extends BronCfiTerugmeldingRegel
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Integer aantalDeelnemers;

	public BronCfiTerugmeldingEXPRegel()
	{
	}

	public BronCfiTerugmeldingEXPRegel(String[] velden)
	{
		super(BronCfiRegelType.EXP, velden);
		setOpleiding(velden[3]);
		setAantalDeelnemers(Integer.valueOf(velden[4]));
		setOpmerking(velden[5]);
	}

	public void setAantalDeelnemers(Integer aantalDeelnemers)
	{
		this.aantalDeelnemers = aantalDeelnemers;
	}

	public Integer getAantalDeelnemers()
	{
		return aantalDeelnemers;
	}
}
