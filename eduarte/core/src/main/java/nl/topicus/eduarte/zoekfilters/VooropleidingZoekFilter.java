package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.vrijevelden.VooropleidingVrijVeld;

/**
 * @author loite
 */
public class VooropleidingZoekFilter extends
		AbstractVrijVeldableZoekFilter<VooropleidingVrijVeld, Vooropleiding>
{
	private static final long serialVersionUID = 1L;

	private Long id;

	public VooropleidingZoekFilter()
	{
		super(VooropleidingVrijVeld.class);
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return id;
	}
}
