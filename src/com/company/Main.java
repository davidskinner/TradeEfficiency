package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import static java.lang.String.format;


//Graph Class
class Graph<T>
{
	//string is two vertices (K,V) -> ex. (12,EDGE)
	public HashMap<String, Edge<T>> map = new HashMap<>();

	public double[][] adjacencyMatrix;

	private List<Edge<T>> allEdges;
	private Map<Long, Vertex<T>> allVertex;
	boolean isDirected = false;

	public Graph()
	{

	}

	public Graph(boolean isDirected)
	{
		allEdges = new ArrayList<Edge<T>>();
		allVertex = new HashMap<Long, Vertex<T>>();
		this.isDirected = isDirected;
	}

	public Graph(boolean isDirected, int vertices)
	{
		adjacencyMatrix = new double[vertices][vertices];
		allEdges = new ArrayList<Edge<T>>();
		allVertex = new HashMap<Long, Vertex<T>>();
		this.isDirected = isDirected;
	}

	public void addEdge(long id1, long id2)
	{
		addEdge(id1, id2, 0);
	}

	//This works only for directed graph because for undirected graph we can end up
	//adding edges two times to allEdges

	public void addVertex(Vertex<T> vertex)
	{
		if (allVertex.containsKey(vertex.getId()))
		{
			return;
		}
		allVertex.put(vertex.getId(), vertex);
		for (Edge<T> edge : vertex.getEdges())
		{
			allEdges.add(edge);
		}
	}

	public Vertex<T> addSingleVertex(long id)
	{
		if (allVertex.containsKey(id))
		{
			return allVertex.get(id);
		}
		Vertex<T> v = new Vertex<T>(id);
		allVertex.put(id, v);
		return v;
	}

	public Vertex<T> getVertex(long id)
	{
		return allVertex.get(id);
	}

	public void addEdge(long id1, long id2, float weight)
	{
		Vertex<T> vertex1 = null;
		if (allVertex.containsKey(id1))
		{
			vertex1 = allVertex.get(id1);
		} else
		{
			vertex1 = new Vertex<T>(id1);
			allVertex.put(id1, vertex1);
		}

		Vertex<T> vertex2 = null;
		if (allVertex.containsKey(id2))
		{
			vertex2 = allVertex.get(id2);
		} else
		{
			vertex2 = new Vertex<T>(id2);
			allVertex.put(id2, vertex2);
		}

		Edge<T> edge = new Edge<T>(vertex1, vertex2, isDirected, weight);
		allEdges.add(edge);
		map.put(String.valueOf(vertex1) + String.valueOf(vertex2), edge);
		vertex1.addAdjacentVertex(edge, vertex2);
		if (!isDirected)
		{
			vertex2.addAdjacentVertex(edge, vertex1);
		}
	}

	public void addEdge(long id1, long id2, float from, float to)
	{
		Vertex<T> vertex1 = null;
		if (allVertex.containsKey(id1))
		{
			vertex1 = allVertex.get(id1);
		} else
		{
			vertex1 = new Vertex<T>(id1);
			allVertex.put(id1, vertex1);
		}

		Vertex<T> vertex2 = null;
		if (allVertex.containsKey(id2))
		{
			vertex2 = allVertex.get(id2);
		} else
		{
			vertex2 = new Vertex<T>(id2);
			allVertex.put(id2, vertex2);
		}

		Edge<T> edge = new Edge<T>(vertex1, vertex2, isDirected, from, to);
		allEdges.add(edge);
		adjacencyMatrix[((int) vertex1.getId())-1][((int) vertex2.getId())-1] = edge.weight;
		map.put(String.valueOf(vertex1) + String.valueOf(vertex2), edge);
		vertex1.addAdjacentVertex(edge, vertex2);
		if (!isDirected)
		{
			vertex2.addAdjacentVertex(edge, vertex1);
		}

	}

	public List<Edge<T>> getAllEdges()
	{
		return allEdges;
	}

	public Collection<Vertex<T>> getAllVertex()
	{
		return allVertex.values();
	}

	public void setDataForVertex(long id, T data)
	{
		if (allVertex.containsKey(id))
		{
			Vertex<T> vertex = allVertex.get(id);
			vertex.setData(data);
		}
	}

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		for (Edge<T> edge : getAllEdges())
		{
			buffer.append(edge.getVertex1() + " " + edge.getVertex2() + " " + edge.getWeight());
			buffer.append("\n");
		}
		return buffer.toString();
	}
}

// Vertex Class
class Vertex<T>
{
	long id;
	private T data;
	private List<Edge<T>> edges = new ArrayList<>();
	private List<Vertex<T>> adjacentVertex = new ArrayList<>();

	Vertex(long id)
	{
		this.id = id;
	}

	public long getId()
	{
		return id;
	}

	public void setData(T data)
	{
		this.data = data;
	}

	public T getData()
	{
		return data;
	}

	public void addAdjacentVertex(Edge<T> e, Vertex<T> v)
	{
		edges.add(e);
		adjacentVertex.add(v);
	}

	public String toString()
	{
		return String.valueOf(id);
	}

	public List<Vertex<T>> getAdjacentVertexes()
	{
		return adjacentVertex;
	}

	public List<Edge<T>> getEdges()
	{
		return edges;
	}

	public int getDegree()
	{
		return edges.size();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (id != other.id)
			return false;
		return true;
	}
}

// Edge Class
class Edge<T>
{
	private boolean isDirected = false;
	private Vertex<T> vertex1;
	private Vertex<T> vertex2;
	public double weight;
	public float from;
	public float to;

	Edge(Vertex<T> vertex1, Vertex<T> vertex2)
	{
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
	}

	Edge(Vertex<T> vertex1, Vertex<T> vertex2, boolean isDirected, float weight)
	{
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.weight = weight;
		this.isDirected = isDirected;
	}


	Edge(Vertex<T> vertex1, Vertex<T> vertex2, boolean isDirected, float fromWeight, float toWeight)
	{
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.from = fromWeight;
		this.to = toWeight;
		this.weight = toWeight / fromWeight;
		this.isDirected = isDirected;
	}

	Edge(Vertex<T> vertex1, Vertex<T> vertex2, boolean isDirected)
	{
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.isDirected = isDirected;
	}

	Vertex<T> getVertex1()
	{
		return vertex1;
	}

	Vertex<T> getVertex2()
	{
		return vertex2;
	}

	double getWeight()
	{
		return weight;
	}

	public boolean isDirected()
	{
		return isDirected;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vertex1 == null) ? 0 : vertex1.hashCode());
		result = prime * result + ((vertex2 == null) ? 0 : vertex2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (vertex1 == null)
		{
			if (other.vertex1 != null)
				return false;
		} else if (!vertex1.equals(other.vertex1))
			return false;
		if (vertex2 == null)
		{
			if (other.vertex2 != null)
				return false;
		} else if (!vertex2.equals(other.vertex2))
			return false;
		return true;
	}

	public String printEdgeForOutput()
	{
		DecimalFormat format = new DecimalFormat("0.##");
		StringBuilder builder = new StringBuilder();
		builder.append(this.vertex1);
		builder.append(" ");
		builder.append(this.vertex2);
		builder.append(" ");
		builder.append(format.format(this.from));
		builder.append(" ");
		builder.append(format.format(this.to));

		return builder.toString();
	}

	@Override
	public String toString()
	{
		return "Edge [isDirected=" + isDirected + ", vertex1=" + vertex1 + ", vertex2=" + vertex2 + ", weight=" + weight + "]";
	}
}

class TarjanStronglyConnectedComponent
{

	private Map<Vertex<Integer>, Integer> visitedTime;
	private Map<Vertex<Integer>, Integer> lowTime;
	private Set<Vertex<Integer>> onStack;
	private Deque<Vertex<Integer>> stack;
	private Set<Vertex<Integer>> visited;
	private List<Set<Vertex<Integer>>> result;
	private int time;

	public List<Set<Vertex<Integer>>> scc(Graph<Integer> graph)
	{

		//keeps the time when every vertex is visited
		time = 0;
		//keeps map of vertex to time it was visited
		visitedTime = new HashMap<>();

		//keeps map of vertex and time of first vertex visited in current DFS
		lowTime = new HashMap<>();

		//tells if a vertex is in stack or not
		onStack = new HashSet<>();

		//stack of visited vertices
		stack = new LinkedList<>();

		//tells if vertex has ever been visited or not. This is for DFS purpose.
		visited = new HashSet<>();

		//stores the strongly connected components result;
		result = new ArrayList<>();

		//start from any vertex in the graph.
		for (Vertex<Integer> vertex : graph.getAllVertex())
		{
			if (visited.contains(vertex))
			{
				continue;
			}
			sccUtil(vertex);
		}

		return result;
	}

	private void sccUtil(Vertex<Integer> vertex)
	{

		visited.add(vertex);
		visitedTime.put(vertex, time);
		lowTime.put(vertex, time);
		time++;
		stack.addFirst(vertex);
		onStack.add(vertex);

		for (Vertex child : vertex.getAdjacentVertexes())
		{
			//if child is not visited then visit it and see if it has link back to vertex's ancestor. In that case update
			//low time to ancestor's visit time
			if (!visited.contains(child))
			{
				sccUtil(child);
				//sets lowTime[vertex] = min(lowTime[vertex], lowTime[child]);
				lowTime.compute(vertex, (v, low) -> Math.min(low, lowTime.get(child)));
			} //if child is on stack then see if it was visited before vertex's low time. If yes then update vertex's low time to that.
			else if (onStack.contains(child))
			{
				//sets lowTime[vertex] = min(lowTime[vertex], visitedTime[child]);
				lowTime.compute(vertex, (v, low) -> Math.min(low, visitedTime.get(child)));
			}
		}

		//if vertex low time is same as visited time then this is start vertex for strongly connected component.
		//keep popping vertices out of stack still you find current vertex. They are all part of one strongly
		//connected component.
		if (visitedTime.get(vertex) == lowTime.get(vertex))
		{
			Set<Vertex<Integer>> stronglyConnectedComponenet = new HashSet<>();
			Vertex v;
			do
			{
				v = stack.pollFirst();
				onStack.remove(v);
				stronglyConnectedComponenet.add(v);
			} while (!vertex.equals(v));
			result.add(stronglyConnectedComponenet);
		}
	}
}

class AllCyclesInDirectedGraphTarjan
{

	public static String VtoS(Vertex<Integer> from, Vertex<Integer> to)
	{
		return String.valueOf(from.id) + String.valueOf(to.id);
	}

	public static Double GetWeight(Graph<Integer> graph, Vertex<Integer> from, Vertex<Integer> to)
	{
		return graph.map.get(VtoS(from, to)).weight;
	}

	public static Double VertexListProduct(Graph<Integer> graph, List<Vertex<Integer>> list)
	{
		double product = 1;
		for (int i = 0; i < list.size() - 1; i++)
		{
			product = product * GetWeight(graph, list.get(i), list.get(i + 1));
		}
		return product;
	}

	private Set<Vertex<Integer>> visited;
	private Deque<Vertex<Integer>> pointStack;
	private Deque<Vertex<Integer>> markedStack;
	private Set<Vertex<Integer>> markedSet;

	public AllCyclesInDirectedGraphTarjan()
	{
		reset();
	}

	private void reset()
	{
		visited = new HashSet<>();
		pointStack = new LinkedList<>();
		markedStack = new LinkedList<>();
		markedSet = new HashSet<>();
	}

	public List<List<Vertex<Integer>>> findAllSimpleCycles(Graph<Integer> graph)
	{
		reset();
		List<List<Vertex<Integer>>> result = new ArrayList<>();
		for (Vertex<Integer> vertex : graph.getAllVertex())
		{

			System.out.println(vertex.getId());
			findAllSimpleCycles(vertex, vertex, result, graph);
			visited.add(vertex);
			while (!markedStack.isEmpty())
			{
				markedSet.remove(markedStack.pollFirst());
			}
		}
		return result;
	}

	private boolean findAllSimpleCycles(Vertex start, Vertex<Integer> current, List<List<Vertex<Integer>>> result, Graph<Integer> graph)
	{
		boolean hasCycle = false;
		pointStack.offerFirst(current);
		markedSet.add(current);
		markedStack.offerFirst(current);

		for (Vertex<Integer> w : current.getAdjacentVertexes())
		{
			if (visited.contains(w))
			{
				continue;
			} else if (w.equals(start))
			{
				// we have found a cycle because we are back at start
				hasCycle = true;
				pointStack.offerFirst(w);
				List<Vertex<Integer>> cycle = new ArrayList<>();
				Iterator<Vertex<Integer>> itr = pointStack.descendingIterator();
				while (itr.hasNext())
				{
					cycle.add(itr.next());
				}
				pointStack.pollFirst();

				double temp = VertexListProduct(graph, cycle);

				if (temp > 1)
				{
					result.add(cycle);
					System.out.println("cycle added");
				}
			} else if (!markedSet.contains(w))
			{
				hasCycle = findAllSimpleCycles(start, w, result, graph) || hasCycle; // recurse
			}

			//            System.out.println(result.size());
			//            result.forEach(cycle -> {
			//                cycle.forEach(v -> System.out.print(v.getId() + " "));
			//                System.out.println();
			//            });
		}

		if (hasCycle)
		{
			while (!markedStack.peekFirst().equals(current))
			{
				markedSet.remove(markedStack.pollFirst());
			}
			markedSet.remove(markedStack.pollFirst());
		}
		pointStack.pollFirst();
		return hasCycle;
	}
}

class AllCyclesInDirectedGraphJohnson
{
	public static String VtoS(Vertex<Integer> from, Vertex<Integer> to)
	{
		return String.valueOf(from.id) + String.valueOf(to.id);
	}

	public static Double GetWeight(Graph<Integer> graph, Vertex<Integer> from, Vertex<Integer> to)
	{
		return graph.map.get(VtoS(from, to)).weight;
	}

	public static Double VertexListProduct(Graph<Integer> graph, List<Vertex<Integer>> list)
	{
		double product = 1;
		for (int i = 0; i < list.size() - 1; i++)
		{
			product = product * GetWeight(graph, list.get(i), list.get(i + 1));
		}
		return product;
	}

	Set<Vertex<Integer>> blockedSet;
	Map<Vertex<Integer>, Set<Vertex<Integer>>> blockedMap;
	Deque<Vertex<Integer>> stack;
	List<List<Vertex<Integer>>> allCycles;

	/**
	 * Main function to find all cycles
	 */
	public List<List<Vertex<Integer>>> simpleCyles(Graph<Integer> graph)
	{

		blockedSet = new HashSet<>();
		blockedMap = new HashMap<>();
		stack = new LinkedList<>();
		allCycles = new ArrayList<>();
		long startIndex = 1;
		TarjanStronglyConnectedComponent tarjan = new TarjanStronglyConnectedComponent();
		while (startIndex <= graph.getAllVertex().size())
		{
			System.out.println(startIndex);
			Graph<Integer> subGraph = createSubGraph(startIndex, graph);
			List<Set<Vertex<Integer>>> sccs = tarjan.scc(subGraph);
			//this creates graph consisting of strongly connected components only and then returns the
			//least indexed vertex among all the strongly connected component graph.
			//it also ignore one vertex graph since it wont have any cycle.
			Optional<Vertex<Integer>> maybeLeastVertex = leastIndexSCC(sccs, subGraph);
			if (maybeLeastVertex.isPresent())
			{
				Vertex<Integer> leastVertex = maybeLeastVertex.get();
				blockedSet.clear();
				blockedMap.clear();
				findCyclesInSCG(leastVertex, leastVertex);
				startIndex = leastVertex.getId() + 1;
			} else
			{
				break;
			}
		}
		return allCycles;
	}

	private Optional<Vertex<Integer>> leastIndexSCC(List<Set<Vertex<Integer>>> sccs, Graph<Integer> subGraph)
	{
		long min = Integer.MAX_VALUE;
		Vertex<Integer> minVertex = null;
		Set<Vertex<Integer>> minScc = null;
		for (Set<Vertex<Integer>> scc : sccs)
		{
			if (scc.size() == 1)
			{
				continue;
			}
			for (Vertex<Integer> vertex : scc)
			{
				if (vertex.getId() < min)
				{
					min = vertex.getId();
					minVertex = vertex;
					minScc = scc;
				}
			}
		}

		if (minVertex == null)
		{
			return Optional.empty();
		}
		Graph<Integer> graphScc = new Graph<>(true);
		for (Edge<Integer> edge : subGraph.getAllEdges())
		{
			if (minScc.contains(edge.getVertex1()) && minScc.contains(edge.getVertex2()))
			{
				graphScc.addEdge(edge.getVertex1().getId(), edge.getVertex2().getId());
			}
		}
		return Optional.of(graphScc.getVertex(minVertex.getId()));
	}

	private void unblock(Vertex<Integer> u)
	{
		blockedSet.remove(u);
		if (blockedMap.get(u) != null)
		{
			blockedMap.get(u).forEach(v ->
			{
				if (blockedSet.contains(v))
				{
					unblock(v);
				}
			});
			blockedMap.remove(u);
		}
	}

	private boolean findCyclesInSCG(Vertex<Integer> startVertex, Vertex<Integer> currentVertex)
	{
		boolean foundCycle = false;
		stack.push(currentVertex);
		blockedSet.add(currentVertex);

		for (Edge<Integer> e : currentVertex.getEdges())
		{
			Vertex<Integer> neighbor = e.getVertex2();
			//if neighbor is same as start vertex means cycle is found.
			//Store contents of stack in final result.
			if (neighbor == startVertex)
			{
				List<Vertex<Integer>> cycle = new ArrayList<>();
				stack.push(startVertex);
				cycle.addAll(stack);
				Collections.reverse(cycle);
				stack.pop();
				allCycles.add(cycle);
				foundCycle = true;
			} //explore this neighbor only if it is not in blockedSet.
			else if (!blockedSet.contains(neighbor))
			{
				boolean gotCycle = findCyclesInSCG(startVertex, neighbor);
				foundCycle = foundCycle || gotCycle;
			}
		}
		//if cycle is found with current vertex then recursively unblock vertex and all vertices which are dependent on this vertex.
		if (foundCycle)
		{
			//remove from blockedSet  and then remove all the other vertices dependent on this vertex from blockedSet
			unblock(currentVertex);
		} else
		{
			//if no cycle is found with current vertex then don't unblock it. But find all its neighbors and add this
			//vertex to their blockedMap. If any of those neighbors ever get unblocked then unblock current vertex as well.
			for (Edge<Integer> e : currentVertex.getEdges())
			{
				Vertex<Integer> w = e.getVertex2();
				Set<Vertex<Integer>> bSet = getBSet(w);
				bSet.add(currentVertex);
			}
		}
		//remove vertex from the stack.
		stack.pop();
		return foundCycle;
	}

	private Set<Vertex<Integer>> getBSet(Vertex<Integer> v)
	{
		return blockedMap.computeIfAbsent(v, (key) -> new HashSet<>());
	}

	private Graph createSubGraph(long startVertex, Graph<Integer> graph)
	{
		Graph<Integer> subGraph = new Graph<>(true);
		for (Edge<Integer> edge : graph.getAllEdges())
		{
			if (edge.getVertex1().getId() >= startVertex && edge.getVertex2().getId() >= startVertex)
			{
				subGraph.addEdge(edge.getVertex1().getId(), edge.getVertex2().getId());
			}
		}
		return subGraph;
	}
}

class Floyd
{
	Boolean FloydWarshallBool(Graph<Integer> graph)
	{

		int size = graph.adjacencyMatrix.length;
		double dist[][] = new double[size][size];

		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				dist[i][j] = Double.POSITIVE_INFINITY;
			}
		}

		int next[][] = new int[size][size];

//		for each edge (u,v)
//		dist[u][v] ← w(u,v)  // the weight of the edge (u,v)
//		next[u][v] ← v
//		for (Edge<Integer> edge : graph.getAllEdges())
//		{
//			edge.weight = -1*Math.log(edge.weight);
//		}
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if(graph.adjacencyMatrix[i][j] > 0)
				{
					dist[i][j] = graph.adjacencyMatrix[i][j];
					dist[i][j] = -1* Math.log(dist[i][j]);
					next[i][j] = j+1;
				}

			}
		}

//		for each vertex v
//		dist[v][v] ← 0
//		next[v][v] ← v
		for (int i = 0; i < size; i++)
		{
			// along diagonal
			dist[i][i] = 0;
			next[i][i] = i;
		}

//		for k from 1 to |V| // standard Floyd-Warshall implementation
//		for i from 1 to |V|
//		for j from 1 to |V|
//		if dist[i][j] > dist[i][k] + dist[k][j] then
//		dist[i][j] ← dist[i][k] + dist[k][j]
//		next[i][j] ← next[i][k]

		for (int k = 0; k < size; k++)
		{
			for (int i = 0; i < size; i++)
			{
				for (int j = 0; j < size; j++)
				{
					if(dist[i][j] > dist[i][k] + dist[k][j])
					{
						dist[i][j] = dist[i][k] + dist[k][j];
						next[i][j] = next[i][k];
					}
				}
			}
		}// end FW

		//print dist
		for (int i = 0; i < next.length; i++)
		{
			System.out.print(String.valueOf(i+1)+": ");
			for (int j = 0; j < next.length; j++)
			{
				System.out.printf("%.2f ", dist[i][j]);

			}
			System.out.println();
		}

		//print next
		System.out.println();
		for (int i = 0; i < next.length; i++)
		{
			System.out.print(String.valueOf(i+1)+": ");
			for (int j = 0; j < next.length; j++)
			{
				System.out.print( next[i][j]+ " ");
			}
			System.out.println();
		}

		int start; // the first vertex that is negative on i,i
		for (int i = 0; i < size; i++)
		{
			//find starting point
			if(dist[i][i] < 0)
			{
//				System.out.println("dist[i][i] is : "+ String.valueOf(dist[i][i]));
				System.out.println("i is: " + String.valueOf(i+1));
//				System.out.println("next[i]][i] is " + String.valueOf(next[i][i]));
				start = i;
				int current = next[i][i];

				while(current != start)
				{
					System.out.println(current);
					current = next[current][0];
				}

//				while(start != 0)
//				{
//					start = next[start][0];
//					System.out.println(start);
//
//				}

				return true;

			}
		}
		return false;

	}
}
class FloydWarshallAllPairShortestPath
{

	class NegativeWeightCycleException extends RuntimeException
	{

	}

	private static final int INF = 1000000;

	public int[][] allPairShortestPath(int[][] distanceMatrix)
	{

		int distance[][] = new int[distanceMatrix.length][distanceMatrix.length];
		int path[][] = new int[distanceMatrix.length][distanceMatrix.length];

		for (int i = 0; i < distanceMatrix.length; i++)
		{
			for (int j = 0; j < distanceMatrix[i].length; j++)
			{
				distance[i][j] = distanceMatrix[i][j];
				if (distanceMatrix[i][j] != INF && i != j)
				{
					path[i][j] = i;
				} else
				{
					path[i][j] = -1;
				}
			}
		}

		for (int k = 0; k < distanceMatrix.length; k++)
		{
			for (int i = 0; i < distanceMatrix.length; i++)
			{
				for (int j = 0; j < distanceMatrix.length; j++)
				{
					if (distance[i][k] == INF || distance[k][j] == INF)
					{
						continue;
					}
					if (distance[i][j] > distance[i][k] + distance[k][j])
					{
						distance[i][j] = distance[i][k] + distance[k][j];
						path[i][j] = path[k][j];
					}
				}
			}
		}

		//look for negative weight cycle in the graph
		//if values on diagonal of distance matrix is negative
		//then there is negative weight cycle in the graph.
		for (int i = 0; i < distance.length; i++)
		{
			if (distance[i][i] < 0)
			{
				throw new NegativeWeightCycleException();
			}
		}

		printPath(path, 3, 1);
		return distance;
	}

	public void printPath(int[][] path, int start, int end)
	{
		if (start < 0 || end < 0 || start >= path.length || end >= path.length)
		{
			throw new IllegalArgumentException();
		}

		System.out.println("Actual path - between " + start + " " + end);
		Deque<Integer> stack = new LinkedList<>();
		stack.addFirst(end);
		while (true)
		{
			end = path[start][end];
			if (end == -1)
			{
				return;
			}
			stack.addFirst(end);
			if (end == start)
			{
				break;
			}
		}

		while (!stack.isEmpty())
		{
			System.out.print(stack.pollFirst() + " ");
		}

		System.out.println();
	}
}

public class Main
{
	public static String VtoS(Vertex<Integer> from, Vertex<Integer> to)
	{
		return String.valueOf(from.id) + String.valueOf(to.id);
	}

	public static Double GetWeight(Graph<Integer> graph, Vertex<Integer> from, Vertex<Integer> to)
	{
		return graph.map.get(VtoS(from, to)).weight;
	}

	public static Double VertexListProduct(Graph<Integer> graph, List<Vertex<Integer>> list)
	{
		double product = 1;
		for (int i = 0; i < list.size() - 1; i++)
		{
			product = product * GetWeight(graph, list.get(i), list.get(i + 1));
		}
		return product;
	}

	public static Double CalculateWeight(String from, String to)
	{
		// to divided by from
		return Double.valueOf(to) / Double.valueOf(from);
	}

	public static void log(String str)
	{
		System.out.println(str);
	}

	public static void main(String[] args)
	{

		String inputFile = "";
		String outputFile = "";

		try
		{
			if (args[0] == "")
			{
				throw new RuntimeException("args[0] is the input file name");
			}
		} catch (Exception e)
		{
			throw new RuntimeException("Make sure to pass valid strings for file names as arguments");
		}

		inputFile = args[0];

		String str = inputFile;
		String number = str.replaceAll("[^0-9]", "");

		outputFile = "output" + number + ".txt";

		File file = new File("/Users/davidskinner/Documents/Repositories/LocalTradeEfficiency/" + inputFile);

		int numberOfVertices = 0;
		Graph<Integer> graph = new Graph<Integer>();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));

			String line;
			int counter = 0;
			while ((line = br.readLine()) != null)
			{
				String[] splitter = line.split(" ");

				if (counter == 0)
				{
					// read in first line
					numberOfVertices = Integer.valueOf(splitter[0]);
					//                    log("The number of vertices is: " + String.valueOf(numberOfVertices));
					graph = new Graph<Integer>(true,numberOfVertices);

				} else
				{
					//import the Edges
					graph.addEdge(Long.valueOf(splitter[0]), Long.valueOf(splitter[1]), Float.valueOf(splitter[2]), Float.valueOf(splitter[3]));
				}
				//                System.out.println(line);

				counter++;
			}

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		//        log("");

//		AllCyclesInDirectedGraphTarjan tarjan = new AllCyclesInDirectedGraphTarjan();
//		List<List<Vertex<Integer>>> result = tarjan.findAllSimpleCycles(graph);
		//        AllCyclesInDirectedGraphJohnson johnson = new AllCyclesInDirectedGraphJohnson();
		//        List<List<Vertex<Integer>>> result = johnson.simpleCyles(graph);

		//        result.forEach(cycle -> {
		//            cycle.forEach(v -> System.out.print(v.getId() + " "));
		//            System.out.println();
		//        });

		double temp;
		boolean efficient = true;
//		for (List<Vertex<Integer>> list : result)
//		{
//			temp = VertexListProduct(graph, list);
//			//            log( list.toString()+ " "+ String.valueOf(temp));
//			if (temp > 1)
//			{
//				DecimalFormat format = new DecimalFormat("0.#######");
//				log("yes");
//				//print edges
//				for (int i = 0; i < list.size() - 1; i++)
//				{
//					log(graph.map.get(VtoS(list.get(i), list.get(i + 1))).printEdgeForOutput());
//				}
//				log("one kg of product " + String.valueOf(list.get(0).id) + " gets " + format.format(temp) + " kg of product " + String.valueOf(list.get(0).id) + " from the above sequence.");
//				efficient = false;
//				break; // break because we have determined it is inefficient
//			}
//		}

//		if (efficient)
//			log("no");

		log("");

		Floyd floyd = new Floyd();
        boolean result = floyd.FloydWarshallBool(graph);
		System.out.println(result);

		//        result.forEach(cycle -> {
		//            cycle.forEach(v -> System.out.print(v.getId() + " "));
		//            System.out.println();
		//        });

//		int INF = 10000000;
//		int[][] d = {{0, 3, 6, 15}, {INF, 0, -2, INF}, {INF, INF, 0, 2}, {1, INF, INF, 0}};
//
//		FloydWarshallAllPairShortestPath shortestPath = new FloydWarshallAllPairShortestPath();
//		int[][] distance = shortestPath.allPairShortestPath(d);
//		System.out.println("Minimum Distance matrix");
//		for (int i = 0; i < distance.length; i++)
//		{
//			for (int j = 0; j < distance.length; j++)
//			{
//				System.out.print(distance[i][j] + " ");
//			}
//			System.out.println("");
//		}
	}
}