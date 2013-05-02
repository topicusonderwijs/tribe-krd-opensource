package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

public class ToetsVertex
{
	private Toets toets;

	public ToetsVertex(Toets toets)
	{
		this.toets = toets;
	}

	public Toets getToets()
	{
		return toets;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ToetsVertex)
			return getToets().equals(((ToetsVertex) obj).getToets());

		return false;
	}

	@Override
	public String toString()
	{
		return toets.toString();
	}
}
