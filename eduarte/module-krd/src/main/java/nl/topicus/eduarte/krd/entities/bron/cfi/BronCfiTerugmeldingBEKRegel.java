package nl.topicus.eduarte.krd.entities.bron.cfi;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.entities.RestrictedAccess;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * BEK Regel van een CFI-terugmelding.
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronCfiTerugmeldingBEKRegel extends BronCfiTerugmeldingRegel
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Integer aantalInschrVtBolOp0110;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Integer aantalInschrDtBblOp0110;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Integer aantalInschrDtDtbolOp0110;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Integer aantalDiplomasOp0110;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Integer aantalInschrVtBolOp0102;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Integer aantalInschrDtBblOp0102;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Integer aantalInschrDtDtbolOp0102;

	public BronCfiTerugmeldingBEKRegel()
	{
	}

	public BronCfiTerugmeldingBEKRegel(String[] velden)
	{
		super(BronCfiRegelType.BEK, velden);
		setOpleiding(velden[3]);
		aantalInschrVtBolOp0110 = Integer.valueOf(velden[4]);
		aantalInschrDtBblOp0110 = Integer.valueOf(velden[5]);
		aantalInschrDtDtbolOp0110 = Integer.valueOf(velden[6]);
		aantalDiplomasOp0110 = Integer.valueOf(velden[7]);
		aantalInschrVtBolOp0102 = Integer.valueOf(velden[8]);
		aantalInschrDtBblOp0102 = Integer.valueOf(velden[9]);
		aantalInschrDtDtbolOp0102 = Integer.valueOf(velden[10]);
		setOpmerking(velden[11]);
	}

	public Integer getAantalInschrVtBolOp0110()
	{
		return aantalInschrVtBolOp0110;
	}

	public Integer getAantalInschrDtBblOp0110()
	{
		return aantalInschrDtBblOp0110;
	}

	public Integer getAantalInschrDtDtbolOp0110()
	{
		return aantalInschrDtDtbolOp0110;
	}

	public Integer getAantalDiplomasOp0110()
	{
		return aantalDiplomasOp0110;
	}

	public Integer getAantalInschrVtBolOp0102()
	{
		return aantalInschrVtBolOp0102;
	}

	public Integer getAantalInschrDtBblOp0102()
	{
		return aantalInschrDtBblOp0102;
	}

	public Integer getAantalInschrDtDtbolOp0102()
	{
		return aantalInschrDtDtbolOp0102;
	}

}
