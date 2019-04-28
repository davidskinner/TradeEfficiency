package com.company;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.String.format;

// Graph, Vertex, Edge were taken from an online source and then modified to work with this problem

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
		adjacencyMatrix[((int) vertex1.getId()) - 1][((int) vertex2.getId()) - 1] = edge.weight;
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

// Floyd-Warshall algorithm with negative edge tracking - ONLY Wikipedia pseudocode used to create it
class Floyd
{
	List<List<Vertex<Integer>>> FloydWarshallPositiveEdge(Graph<Integer> graph)
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
				if (graph.adjacencyMatrix[i][j] > 0)
				{
					dist[i][j] = graph.adjacencyMatrix[i][j];
					dist[i][j] = -1 * Math.log(dist[i][j]);
					next[i][j] = j;
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
					if (dist[i][j] > dist[i][k] + dist[k][j])
					{
						dist[i][j] = dist[i][k] + dist[k][j];
						next[i][j] = next[i][k];
					}
				}
			}
		}// end FW

		List<List<Vertex<Integer>>> output = new ArrayList<>();


		int start; // the first vertex that is negative on i,i
		for (int i = 0; i < size; i++)
		{
			//find starting point
			if (dist[i][i] < 0)
			{
				start = i;
				int current = next[i][i];

				List<Vertex<Integer>> path = new ArrayList<>();
				path.add(new Vertex<Integer>(start));


				while (current != start)
				{
					Vertex<Integer> temp = new Vertex<Integer>(current);
					if (path.contains(temp))
					{
						int tempnext = next[(int) temp.getId()][0];
						// trim beginning of path
						int j = 0;
						while (path.get(j).getId() != temp.getId())
						{
							path.remove(j);
						}

						break;
					} else
					{
						path.add(temp);
					}

					// go to the next one
					current = next[current][0];
				}
				output.add(path);

				return output;

			}
		}
		return output;

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
					graph = new Graph<Integer>(true, numberOfVertices);

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

		// for single source shortest path used in sparse graphs

		//		AllCyclesInDirectedGraphTarjan tarjan = new AllCyclesInDirectedGraphTarjan();
		//		List<List<Vertex<Integer>>> result = tarjan.findAllSimpleCycles(graph);
		//        AllCyclesInDirectedGraphJohnson johnson = new AllCyclesInDirectedGraphJohnson();
		//        List<List<Vertex<Integer>>> result = johnson.simpleCyles(graph);

		//        result.forEach(cycle -> {
		//            cycle.forEach(v -> System.out.print(v.getId() + " "));
		//            System.out.println();
		//        });


		Floyd floyd = new Floyd();
		List<List<Vertex<Integer>>> result = floyd.FloydWarshallPositiveEdge(graph);

		for (List<Vertex<Integer>> up : result)
		{
			for (Vertex<Integer> upup : up)
			{
				upup.id++;
			}
		}
		if (result.size() != 0)
			result.get(0).add(new Vertex<>(result.get(0).get(0).id));

		double temp;
		boolean efficient = true;
		String outputString = "no";
		StringBuilder builder = new StringBuilder();

		for (List<Vertex<Integer>> list : result)
		{
			temp = VertexListProduct(graph, list);
			if (temp > 1)
			{
				// set output string to yes
				outputString = "";
				builder.append("yes");
				builder.append("\n");


				DecimalFormat format = new DecimalFormat("0.#######");
				log("yes");
				//print edges
				for (int i = 0; i < list.size() - 1; i++)
				{
					builder.append(graph.map.get(VtoS(list.get(i), list.get(i + 1))).printEdgeForOutput());
					builder.append("\n");
				}
				builder.append("one kg of product " + String.valueOf(list.get(0).id) + " gets " + format.format(temp) + " kg of product " + String.valueOf(list.get(0).id) + " from the above sequence.");
				efficient = false;
				break; // break because we have determined it is inefficient
			}
		}

		if (efficient)
			builder.append("no");

		//output to file
		try (PrintStream out = new PrintStream(new FileOutputStream(outputFile))) {
			out.print(builder);

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}