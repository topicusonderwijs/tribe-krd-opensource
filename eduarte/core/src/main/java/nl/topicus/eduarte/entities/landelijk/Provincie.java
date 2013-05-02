package nl.topicus.eduarte.entities.landelijk;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.ProvincieDataAccessHelper;
import nl.topicus.eduarte.entities.ICodeNaamEntiteit;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * De Nederlandse provincien
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Provincie extends BeginEinddatumLandelijkEntiteit implements ICodeNaamEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 10, nullable = false)
	@Index(name = "idx_Provincie_code")
	private String code;

	@Column(length = 100, nullable = false)
	@Index(name = "idx_Provincie_naam")
	private String naam;

	@Column(length = 10, nullable = false)
	private String afkorting;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Provincie()
	{
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *            The code to set.
	 */
	public void setCode(String code)
	{
		this.code = code;
	}

	/**
	 * @return Returns the naam.
	 */
	public String getNaam()
	{
		return naam;
	}

	/**
	 * @param naam
	 *            The naam to set.
	 */
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	private static ProvincieDataAccessHelper getHelper()
	{
		return DataAccessRegistry.getHelper(ProvincieDataAccessHelper.class);
	}

	/**
	 * @param code
	 * @return De provincie met de gegeven code
	 */
	public static Provincie getProvincie(String code)
	{
		return getHelper().get(code);
	}

	public static Provincie getByNaam(String naam)
	{
		return getHelper().getByNaam(naam);
	}

	/**
	 * @see nl.topicus.eduarte.entities.Entiteit#toString()
	 */
	@Override
	public String toString()
	{
		return getNaam();
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	public String getAfkorting()
	{
		return afkorting;
	}

}
