package nl.topicus.eduarte.web.components.resultaat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.ToetsCodePathMode;

import org.apache.commons.lang.time.StopWatch;
import org.jgrapht.DirectedGraph;
import org.jgrapht.EdgeFactory;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToetsDirectedWeightedGraph extends
		AbstractBaseGraph<ToetsCodepathVertex, ToetsWeightedEdge> implements
		WeightedGraph<ToetsCodepathVertex, ToetsWeightedEdge>,
		DirectedGraph<ToetsCodepathVertex, ToetsWeightedEdge>
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(ToetsDirectedWeightedGraph.class);

	private Map<String, ToetsCodepathVertex> vertices = new HashMap<String, ToetsCodepathVertex>();

	private Map<ToetsCodepathVertex, List<ToetsWeightedEdge>> vanedges =
		new HashMap<ToetsCodepathVertex, List<ToetsWeightedEdge>>();

	private Map<ToetsCodepathVertex, List<ToetsWeightedEdge>> naaredges =
		new HashMap<ToetsCodepathVertex, List<ToetsWeightedEdge>>();

	public ToetsDirectedWeightedGraph()
	{
		super(new EdgeFactory<ToetsCodepathVertex, ToetsWeightedEdge>()
		{
			private static final long serialVersionUID = 1L;

			public ToetsWeightedEdge createEdge(ToetsCodepathVertex source,
					ToetsCodepathVertex target)
			{
				return ReflectionUtil.invokeConstructor(ToetsWeightedEdge.class, source, target);
			}
		}, false, false);
	}

	public boolean containsVertex(String code)
	{
		return vertices.containsKey(code);
	}

	public ToetsCodepathVertex getVertex(String code)
	{
		return vertices.get(code);
	}

	public boolean containsEdge(ToetsCodepathVertex vertex)
	{
		return vanedges.containsKey(vertex) || naaredges.containsKey(vertex);
	}

	public List<ToetsWeightedEdge> getEdges(ToetsCodepathVertex vertex)
	{
		if (vanedges.containsKey(vertex))
			return vanedges.get(vertex);
		else
			return naaredges.get(vertex);
	}

	@Override
	public boolean addEdge(ToetsCodepathVertex sourceVertex, ToetsCodepathVertex targetVertex,
			ToetsWeightedEdge e)
	{
		List<ToetsWeightedEdge> vanedge;
		List<ToetsWeightedEdge> naaredge;

		if (vanedges.containsKey(sourceVertex))
			vanedge = vanedges.get(sourceVertex);
		else
			vanedge = new ArrayList<ToetsWeightedEdge>();

		if (naaredges.containsKey(targetVertex))
			naaredge = naaredges.get(targetVertex);
		else
			naaredge = new ArrayList<ToetsWeightedEdge>();

		vanedge.add(e);
		naaredge.add(e);

		return super.addEdge(sourceVertex, targetVertex, e);
	}

	@Override
	public ToetsWeightedEdge addEdge(ToetsCodepathVertex sourceVertex,
			ToetsCodepathVertex targetVertex)
	{
		ToetsWeightedEdge e = super.addEdge(sourceVertex, targetVertex);

		List<ToetsWeightedEdge> vanedge;
		List<ToetsWeightedEdge> naaredge;

		if (vanedges.containsKey(sourceVertex))
			vanedge = vanedges.get(sourceVertex);
		else
			vanedge = new ArrayList<ToetsWeightedEdge>();

		if (naaredges.containsKey(targetVertex))
			naaredge = naaredges.get(targetVertex);
		else
			naaredge = new ArrayList<ToetsWeightedEdge>();

		vanedge.add(e);
		naaredge.add(e);

		return e;
	}

	@Override
	public boolean addVertex(ToetsCodepathVertex v)
	{
		vertices.put(v.getCodepath(), v);

		return super.addVertex(v);
	}

	@Override
	public boolean removeAllEdges(Collection< ? extends ToetsWeightedEdge> edges)
	{
		for (ToetsWeightedEdge e : edges)
		{
			for (List<ToetsWeightedEdge> vanedge : vanedges.values())
				vanedge.remove(e);

			for (List<ToetsWeightedEdge> naaredge : naaredges.values())
				naaredge.remove(e);
		}

		return super.removeAllEdges(edges);
	}

	@Override
	protected boolean removeAllEdges(ToetsWeightedEdge[] edges)
	{
		for (ToetsWeightedEdge e : edges)
		{
			for (List<ToetsWeightedEdge> vanedge : vanedges.values())
				vanedge.remove(e);

			for (List<ToetsWeightedEdge> naaredge : naaredges.values())
				naaredge.remove(e);
		}

		return super.removeAllEdges(edges);
	}

	@Override
	public Set<ToetsWeightedEdge> removeAllEdges(ToetsCodepathVertex sourceVertex,
			ToetsCodepathVertex targetVertex)
	{
		return super.removeAllEdges(sourceVertex, targetVertex);
	}

	@Override
	public boolean removeAllVertices(
			@SuppressWarnings("hiding") Collection< ? extends ToetsCodepathVertex> vertices)
	{
		for (ToetsCodepathVertex vanvertex : vertices)
		{
			this.vertices.remove(vanvertex);
			vanedges.remove(vanvertex);
			naaredges.remove(vanvertex);
		}

		return super.removeAllVertices(vertices);
	}

	@Override
	public boolean removeEdge(ToetsWeightedEdge e)
	{
		for (List<ToetsWeightedEdge> vanedge : vanedges.values())
			vanedge.remove(e);

		for (List<ToetsWeightedEdge> naaredge : naaredges.values())
			naaredge.remove(e);

		return super.removeEdge(e);
	}

	@Override
	public ToetsWeightedEdge removeEdge(ToetsCodepathVertex sourceVertex,
			ToetsCodepathVertex targetVertex)
	{
		Iterator<ToetsWeightedEdge> iter = vanedges.get(sourceVertex).iterator();

		while (iter.hasNext())
		{
			ToetsWeightedEdge e = iter.next();
			if (e.getTarget().equals(targetVertex))
				iter.remove();
		}

		iter = naaredges.get(sourceVertex).iterator();
		while (iter.hasNext())
		{
			ToetsWeightedEdge e = iter.next();
			if (e.getSource().equals(sourceVertex))
				iter.remove();
		}

		return super.removeEdge(sourceVertex, targetVertex);
	}

	@Override
	public boolean removeVertex(ToetsCodepathVertex v)
	{
		vanedges.remove(v);
		naaredges.remove(v);
		vertices.remove(v.getCodepath());

		return super.removeVertex(v);
	}

	public static List<String> sortToetsen(List<Resultaatstructuur> structuren,
			ToetsCodePathMode mode)
	{
		StopWatch stopwatch = new StopWatch();
		stopwatch.start();

		ArrayList<ToetsDirectedWeightedGraph> graphs = new ArrayList<ToetsDirectedWeightedGraph>();

		for (Resultaatstructuur struct : structuren)
			traverseToetsen(null, struct.getEindresultaat(), 0, mode, graphs);

		stopwatch.split();
		log.debug("graph generation took " + stopwatch.toSplitString() + ".");
		stopwatch.reset();
		stopwatch.start();

		for (int i = 0; i < graphs.size(); i++)
		{
			ToetsDirectedWeightedGraph graph = graphs.get(i);
			CycleDetector<ToetsCodepathVertex, ToetsWeightedEdge> detector =
				new CycleDetector<ToetsCodepathVertex, ToetsWeightedEdge>(graph);

			boolean hasCycles = detector.detectCycles();
			Set<ToetsCodepathVertex> vertices = detector.findCycles();

			if (hasCycles)
				log.debug("graph for level " + i + " has cycles, where " + vertices.size()
					+ " are involved.");
			else
				log.debug("graph for level " + i + " has no cycles.");

			while (hasCycles)
			{
				log.debug("removing 1 edge for graph at level " + i + ".");
				ToetsWeightedEdge weakestEdge = detectWeakestLink(graph, vertices);
				graph.removeEdge(weakestEdge);

				detector = new CycleDetector<ToetsCodepathVertex, ToetsWeightedEdge>(graph);
				hasCycles = detector.detectCycles();
				vertices = detector.findCycles();

				if (hasCycles)
					log.debug("graph for level " + i + " still has cycles, where "
						+ vertices.size() + " are involved.");
				else
					log.debug("graph for level " + i + " has no more cycles.");
			}
		}

		stopwatch.split();
		log.debug("graph cycle elimination took " + stopwatch.toSplitString() + ".");

		List<String> sortedToetsen = new ArrayList<String>();
		for (Resultaatstructuur curStructuur : structuren)
		{
			collectToetsen(sortedToetsen, graphs, curStructuur.getEindresultaat().getToetscodePath(
				mode), 1);
		}
		log.debug("order: " + sortedToetsen);

		return sortedToetsen;
	}

	private static void collectToetsen(List<String> ret, List<ToetsDirectedWeightedGraph> graphs,
			String codepath, int level)
	{
		if (ret.contains(codepath))
			return;
		if (level >= graphs.size())
		{
			ret.add(codepath);
			return;
		}

		ToetsDirectedWeightedGraph graph = graphs.get(level);
		TopologicalOrderIterator<ToetsCodepathVertex, ToetsWeightedEdge> iterator =
			new TopologicalOrderIterator<ToetsCodepathVertex, ToetsWeightedEdge>(graph);
		while (iterator.hasNext())
		{
			String nextCodePath = iterator.next().getCodepath();
			if (nextCodePath.startsWith(codepath))
				collectToetsen(ret, graphs, nextCodePath, level + 1);
		}
		ret.add(codepath);
	}

	private static ToetsCodepathVertex traverseToetsen(ToetsCodepathVertex prevVertex, Toets toets,
			int level, ToetsCodePathMode mode, ArrayList<ToetsDirectedWeightedGraph> graphs)
	{
		ToetsDirectedWeightedGraph graph = new ToetsDirectedWeightedGraph();

		// controleren of er al een graph is voor deze level.
		if (graphs.size() > level)
			graph = graphs.get(level);
		else
			graphs.add(level, graph);

		// controleren of de vertex er al is.
		ToetsCodepathVertex thisVertex = new ToetsCodepathVertex(toets.getToetscodePath(mode));
		if (graph.containsVertex(thisVertex.getCodepath()))
		{
			thisVertex = graph.getVertex(thisVertex.getCodepath());

			if (prevVertex != null)
			{
				ToetsWeightedEdge edge = graph.getEdge(prevVertex, thisVertex);
				if (edge == null)
				{
					edge = graph.addEdge(prevVertex, thisVertex);
					graph.setEdgeWeight(edge, 1);
				}
				else
				{
					graph.setEdgeWeight(edge, graph.getEdgeWeight(edge) + 1);
					// log
					// .debug("updating edge " + edge.getSource().getCodepath() + " -> "
					// + edge.getTarget().getCodepath() + " / weight "
					// + graph.getEdgeWeight(edge));
				}
			}
		}
		else
		{
			graph.addVertex(thisVertex);

			if (prevVertex != null)
			{
				ToetsWeightedEdge edge = graph.addEdge(prevVertex, thisVertex);
				graph.setEdgeWeight(edge, 1);
				// log.debug("adding edge " + edge.getSource().getCodepath() + " -> "
				// + edge.getTarget().getCodepath() + " / weight " +
				// graph.getEdgeWeight(edge));
			}
		}

		ToetsCodepathVertex prevChild = null;
		for (Toets childtoets : toets.getChildren())
			prevChild = traverseToetsen(prevChild, childtoets, (level + 1), mode, graphs);

		return thisVertex;
	}

	private static ToetsWeightedEdge detectWeakestLink(ToetsDirectedWeightedGraph graph,
			Set<ToetsCodepathVertex> vertices)
	{
		ToetsWeightedEdge weakestEdge = null;

		for (ToetsCodepathVertex curVertex1 : vertices)
		{
			for (ToetsCodepathVertex curVertex2 : vertices)
			{
				if (curVertex1 != curVertex2)
				{
					ToetsWeightedEdge curEdge = graph.getEdge(curVertex1, curVertex2);
					if (curEdge != null
						&& (weakestEdge == null || (curEdge.getWeight() < weakestEdge.getWeight())))
						weakestEdge = curEdge;
				}
			}
		}

		return weakestEdge;
	}
}
