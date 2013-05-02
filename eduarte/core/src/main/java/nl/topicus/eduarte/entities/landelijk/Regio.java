package nl.topicus.eduarte.entities.landelijk;

import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.RegioDataAccessHelper;
import nl.topicus.eduarte.entities.ICodeNaamEntiteit;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumLandelijkEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * De regio's van Nederland
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Regio extends BeginEinddatumLandelijkEntiteit implements ICodeNaamEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 10, nullable = false)
	@Index(name = "idx_Regio_code")
	private String code;

	@Column(length = 100, nullable = false)
	@Index(name = "idx_Regio_naam")
	private String naam;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Regio()
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

	private static RegioDataAccessHelper getHelper()
	{
		return DataAccessRegistry.getHelper(RegioDataAccessHelper.class);
	}

	/**
	 * @param code
	 * @return De regio met de gegeven code
	 */
	public static Regio getRegio(String code)
	{
		return getHelper().get(code);
	}

}
