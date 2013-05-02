package nl.topicus.eduarte.web.components.resultaat;

import org.jgrapht.graph.DefaultEdge;

public class ToetsEdge extends DefaultEdge
{
	private static final long serialVersionUID = 1L;

	private ToetsVertex source;

	private ToetsVertex target;

	public ToetsEdge(ToetsVertex van, ToetsVertex naar)
	{
		source = van;
		target = naar;
	}

	@Override
	public ToetsVertex getSource()
	{
		return source;
	}

	@Override
	public ToetsVertex getTarget()
	{
		return target;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ToetsEdge)
			return getSource().equals(((ToetsEdge) obj).getSource())
				&& getTarget().equals(((ToetsEdge) obj).getTarget());

		return false;
	}

	@Override
	public String toString()
	{
		return source + " -> " + target;
	}
}
