package nl.topicus.eduarte.krd.entities.bron.cfi;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.entities.RestrictedAccess;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * SIN Regel van een CFI-terugmelding.
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronCfiTerugmeldingSINRegel extends BronCfiTerugmeldingRegel
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private String sinSignaal;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Boolean inschrijving0110Signaal;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Boolean inschrijving0102Signaal;

	@Column(nullable = true)
	@RestrictedAccess(hasSetter = false)
	private Boolean diplomaSignaal;

	public BronCfiTerugmeldingSINRegel()
	{
	}

	public BronCfiTerugmeldingSINRegel(String[] velden)
	{
		super(BronCfiRegelType.SIN, velden);
		sinSignaal = velden[6];
		inschrijving0110Signaal = parseBoolean(velden[7]);
		inschrijving0102Signaal = parseBoolean(velden[8]);
		diplomaSignaal = parseBoolean(velden[9]);
		setOpmerking(velden[10]);
	}

	private Boolean parseBoolean(String string)
	{
		if ("J".equalsIgnoreCase(string))
			return true;
		if ("N".equalsIgnoreCase(string))
			return false;
		return null;
	}

	public String getSinSignaal()
	{
		return sinSignaal;
	}

	public Boolean isInschrijving0110Signaal()
	{
		return inschrijving0110Signaal;
	}

	public Boolean isInschrijving0102Signaal()
	{
		return inschrijving0102Signaal;
	}

	public Boolean isDiplomaSignaal()
	{
		return diplomaSignaal;
	}

}
