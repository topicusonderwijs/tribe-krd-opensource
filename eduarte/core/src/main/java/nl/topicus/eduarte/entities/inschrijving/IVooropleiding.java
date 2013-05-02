package nl.topicus.eduarte.entities.inschrijving;

import java.util.Date;

import nl.topicus.cobra.entities.TransientIdObject;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;

/**
 * Een vooropleiding of een eerdere verbintenis.
 * 
 * @author hop
 */
public interface IVooropleiding extends TransientIdObject
{
	/**
	 * @return Het {@link SoortOnderwijs} dat hoort bij deze vooropleiding of verbintenis.
	 *         Hangt af van de opleiding en of een diploma is behaald.
	 */
	public SoortOnderwijs getSoortOnderwijs();

	public String getOmschrijving();

	public String getBrincode();

	public String getOrganisatieOmschrijving();

	public String getNaamVooropleiding();

	public boolean isDiplomaBehaald();

	public Date getEinddatum();

	public Schooladvies getSchooladvies();

	public Integer getCitoscore();
}
