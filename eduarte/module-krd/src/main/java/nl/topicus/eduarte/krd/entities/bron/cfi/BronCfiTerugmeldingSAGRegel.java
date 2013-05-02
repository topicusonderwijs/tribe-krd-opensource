package nl.topicus.eduarte.krd.entities.bron.cfi;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.entities.RestrictedAccess;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * SAG Regel van een CFI-terugmelding.
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronCfiTerugmeldingSAGRegel extends BronCfiTerugmeldingRegel
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private String sagSignaal;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Integer aantalInschrSignalenOp0110;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Integer aantalInschrSignalenOp0102;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Integer aantalDiplomaSignalen;

	public BronCfiTerugmeldingSAGRegel()
	{
	}

	public BronCfiTerugmeldingSAGRegel(String[] velden)
	{
		super(BronCfiRegelType.SAG, velden);
		sagSignaal = velden[4];
		aantalInschrSignalenOp0110 = Integer.valueOf(velden[5]);
		aantalInschrSignalenOp0102 = Integer.valueOf(velden[6]);
		aantalDiplomaSignalen = Integer.valueOf(velden[7]);
		setOpmerking(velden[8]);
	}

	public String getSagSignaal()
	{
		return sagSignaal;
	}

	public Integer getAantalDiplomaSignalen()
	{
		return aantalDiplomaSignalen;
	}

	public Integer getAantalInschrSignalenOp0110()
	{
		return aantalInschrSignalenOp0110;
	}

	public Integer getAantalInschrSignalenOp0102()
	{
		return aantalInschrSignalenOp0102;
	}

}
