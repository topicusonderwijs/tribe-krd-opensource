package nl.topicus.eduarte.entities.begineinddatum;

import java.util.Date;

import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.util.TimeUtil;

/**
 * Interface voor alle entiteiten die een begin- en einddatum hebben.
 * 
 * @author loite
 */
public interface IBeginEinddatumEntiteit extends IActiefEntiteit
{
	public static final Date MAX_DATE = TimeUtil.getInstance().isoStringAsDate("30000101");

	public static final Date MIN_DATE = TimeUtil.getInstance().isoStringAsDate("19900101");

	/**
	 * @return Begindatum
	 */
	public Date getBegindatum();

	/**
	 * @param begindatum
	 */
	public void setBegindatum(Date begindatum);

	/**
	 * @return Einddatum
	 */
	public Date getEinddatum();

	/**
	 * @param einddatum
	 */
	public void setEinddatum(Date einddatum);

	/**
	 * @return De einddatum of een datum ver in de toekomst indien de einddatum null is.
	 *         Deze datum kan het beste gebruikt worden bij het zoeken in de database
	 *         omdat het zoeken op een ingevulde waarde veel sneller is dan het zoeken op
	 *         een 'is null' waarde.
	 */
	public Date getEinddatumNotNull();

	/**
	 * @param peildatum
	 * @return true indien de entiteit actief is op de gegeven peildatum.
	 */
	public boolean isActief(Date peildatum);
}
