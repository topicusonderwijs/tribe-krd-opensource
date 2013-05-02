package nl.topicus.cobra.util;

import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator die alle data geeft tussen een begindatum en een einddatum. De eerste datum
 * die teruggegeven wordt is de begindatum. De laatste datum die teruggegeven wordt is de
 * einddatum. (Begin- en einddatum zijn dus inclusief.)
 * 
 * @author loite
 */
public class DateIterator implements Iterator<Date>
{

	private final Date eind;

	private Date current;

	/**
	 * Constructor
	 * 
	 * @param begin
	 * @param eind
	 */
	public DateIterator(Date begin, Date eind)
	{
		this.eind = eind;
		current = begin;
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		return !current.equals(eind);
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Date next()
	{
		if (!hasNext())
		{
			throw new NoSuchElementException("Laatste datum reeds bereikt");
		}
		current = TimeUtil.getInstance().addDays(current, 1);
		return current;
	}

	/**
	 * @throws UnsupportedOperationException
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException("remove wordt niet ondersteund");
	}

}
