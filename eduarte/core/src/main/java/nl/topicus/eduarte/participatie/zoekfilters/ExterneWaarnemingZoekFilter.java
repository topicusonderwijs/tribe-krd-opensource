package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;

import nl.topicus.eduarte.entities.participatie.ExterneWaarneming;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

/**
 * @author vanderkamp
 */
public class ExterneWaarnemingZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<ExterneWaarneming>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date beginDatum;

	private Date eindDatum;

	private boolean verwerkt;

	public ExterneWaarnemingZoekFilter()
	{
	}

	/**
	 * @return Returns the beginDatum.
	 */
	public Date getBeginDatum()
	{
		return beginDatum;
	}

	/**
	 * @param beginDatum
	 *            The beginDatum to set.
	 */
	public void setBeginDatum(Date beginDatum)
	{
		this.beginDatum = beginDatum;
	}

	/**
	 * @return Returns the eindDatum.
	 */
	public Date getEindDatum()
	{
		return eindDatum;
	}

	/**
	 * @param eindDatum
	 *            The eindDatum to set.
	 */
	public void setEindDatum(Date eindDatum)
	{
		this.eindDatum = eindDatum;
	}

	/**
	 * @return Returns the verwerkt.
	 */
	public boolean isVerwerkt()
	{
		return verwerkt;
	}

	/**
	 * @param verwerkt
	 *            The verwerkt to set.
	 */
	public void setVerwerkt(boolean verwerkt)
	{
		this.verwerkt = verwerkt;
	}
}
