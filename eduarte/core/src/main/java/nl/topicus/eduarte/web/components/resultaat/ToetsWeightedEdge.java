package nl.topicus.eduarte.web.components.resultaat;

import org.jgrapht.graph.DefaultWeightedEdge;

public class ToetsWeightedEdge extends DefaultWeightedEdge
{
	private static final long serialVersionUID = 1L;

	private ToetsCodepathVertex source;

	private ToetsCodepathVertex target;

	public ToetsWeightedEdge(ToetsCodepathVertex van, ToetsCodepathVertex naar)
	{
		source = van;
		target = naar;
	}

	@Override
	public ToetsCodepathVertex getSource()
	{
		return source;
	}

	public void setSource(ToetsCodepathVertex source)
	{
		this.source = source;
	}

	@Override
	public ToetsCodepathVertex getTarget()
	{
		return target;
	}

	public void setTarget(ToetsCodepathVertex target)
	{
		this.target = target;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof ToetsWeightedEdge)
			return getSource().equals(((ToetsWeightedEdge) obj).getSource())
				&& getTarget().equals(((ToetsWeightedEdge) obj).getTarget());

		return false;
	}

	@Override
	public double getWeight()
	{
		return super.getWeight();
	}

	@Override
	public String toString()
	{
		return source + " -> " + target;
	}
}
