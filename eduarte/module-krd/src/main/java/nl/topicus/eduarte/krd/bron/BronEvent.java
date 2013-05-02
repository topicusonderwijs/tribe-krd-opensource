package nl.topicus.eduarte.krd.bron;

import nl.topicus.onderwijs.duo.bron.BronException;

public interface BronEvent<T>
{
	/**
	 * @return <tt>true</tt> als dit event van toepassing is op de parameters waarmee het
	 *         event is geconstrueerd.
	 */
	public boolean isToepasselijk();

	public T createMelding() throws BronException;
}
