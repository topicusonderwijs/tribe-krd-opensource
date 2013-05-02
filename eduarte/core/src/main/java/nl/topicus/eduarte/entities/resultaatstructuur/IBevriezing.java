package nl.topicus.eduarte.entities.resultaatstructuur;

import java.util.BitSet;

public interface IBevriezing
{
	public BitSet getBevorenPogingenAsSet();

	public void setBevorenPogingenAsSet(BitSet pogingen);
}
