package nl.topicus.eduarte.web.components.resultaat;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.AbstractBaseGraph;

public class ToetsDirectedGraph extends AbstractBaseGraph<ToetsVertex, ToetsEdge> implements
		DirectedGraph<ToetsVertex, ToetsEdge>
{
	private static final long serialVersionUID = 1L;

	public ToetsDirectedGraph()
	{
		super(new EdgeFactory<ToetsVertex, ToetsEdge>()
		{
			private static final long serialVersionUID = 1L;

			public ToetsEdge createEdge(ToetsVertex source, ToetsVertex target)
			{
				return new ToetsEdge(source, target);
			}
		}, true, true);
	}

	public static ToetsDirectedGraph createRecalcGraph(Collection<Toets> toetsen)
	{
		ToetsDirectedGraph ret = new ToetsDirectedGraph();
		Set<Toets> allRecalcToetsen = new HashSet<Toets>(toetsen);
		for (Toets curToets : toetsen)
		{
			allRecalcToetsen.addAll(curToets.getRecalcRequired());
		}
		Map<Toets, ToetsVertex> toetsVertices = new HashMap<Toets, ToetsVertex>();
		for (Toets curRecalcToets : allRecalcToetsen)
		{
			ToetsVertex vertex = new ToetsVertex(curRecalcToets);
			ret.addVertex(vertex);
			toetsVertices.put(curRecalcToets, vertex);
		}
		for (Toets curRecalcToets : allRecalcToetsen)
		{
			for (Toets curRecalcDepToets : curRecalcToets.getRecalcRequired())
			{
				ret
					.addEdge(toetsVertices.get(curRecalcToets), toetsVertices
						.get(curRecalcDepToets));
			}
		}
		return ret;
	}
}
