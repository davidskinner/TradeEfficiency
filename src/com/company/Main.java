package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

//Graph Class
class Graph<T>
{

    private List<Edge<T>> allEdges;
    private Map<Long,Vertex<T>> allVertex;
    boolean isDirected = false;

    public Graph(boolean isDirected){
        allEdges = new ArrayList<Edge<T>>();
        allVertex = new HashMap<Long,Vertex<T>>();
        this.isDirected = isDirected;
    }

    public void addEdge(long id1, long id2){
        addEdge(id1,id2,0);
    }

    //This works only for directed graph because for undirected graph we can end up
    //adding edges two times to allEdges
    public void addVertex(Vertex<T> vertex){
        if(allVertex.containsKey(vertex.getId())){
            return;
        }
        allVertex.put(vertex.getId(), vertex);
        for(Edge<T> edge : vertex.getEdges()){
            allEdges.add(edge);
        }
    }

    public Vertex<T> addSingleVertex(long id){
        if(allVertex.containsKey(id)){
            return allVertex.get(id);
        }
        Vertex<T> v = new Vertex<T>(id);
        allVertex.put(id, v);
        return v;
    }

    public Vertex<T> getVertex(long id){
        return allVertex.get(id);
    }

    public void addEdge(long id1,long id2, int weight){
        Vertex<T> vertex1 = null;
        if(allVertex.containsKey(id1)){
            vertex1 = allVertex.get(id1);
        }else{
            vertex1 = new Vertex<T>(id1);
            allVertex.put(id1, vertex1);
        }

        Vertex<T> vertex2 = null;
        if(allVertex.containsKey(id2)){
            vertex2 = allVertex.get(id2);
        }else{
            vertex2 = new Vertex<T>(id2);
            allVertex.put(id2, vertex2);
        }

        Edge<T> edge = new Edge<T>(vertex1,vertex2,isDirected,weight);
        allEdges.add(edge);
        vertex1.addAdjacentVertex(edge, vertex2);
        if(!isDirected){
            vertex2.addAdjacentVertex(edge, vertex1);
        }

    }

    public List<Edge<T>> getAllEdges(){
        return allEdges;
    }

    public Collection<Vertex<T>> getAllVertex(){
        return allVertex.values();
    }
    public void setDataForVertex(long id, T data){
        if(allVertex.containsKey(id)){
            Vertex<T> vertex = allVertex.get(id);
            vertex.setData(data);
        }
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        for(Edge<T> edge : getAllEdges()){
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

    Vertex(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }

    public void setData(T data){
        this.data = data;
    }

    public T getData(){
        return data;
    }

    public void addAdjacentVertex(Edge<T> e, Vertex<T> v){
        edges.add(e);
        adjacentVertex.add(v);
    }

    public String toString(){
        return String.valueOf(id);
    }

    public List<Vertex<T>> getAdjacentVertexes(){
        return adjacentVertex;
    }

    public List<Edge<T>> getEdges(){
        return edges;
    }

    public int getDegree(){
        return edges.size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
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
    private int weight;
    public float coefficent;

    Edge(Vertex<T> vertex1, Vertex<T> vertex2)
    {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
    }
    
    Edge(Vertex<T> vertex1, Vertex<T> vertex2, float coefficient)
    {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.coefficent = coefficient;
    }
    

    Edge(Vertex<T> vertex1, Vertex<T> vertex2, boolean isDirected, int weight)
    {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.weight = weight;
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

    int getWeight()
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

    @Override
    public String toString()
    {
        return "Edge [isDirected=" + isDirected + ", vertex1=" + vertex1 + ", vertex2=" + vertex2 + ", weight=" + weight + "]";
    }
}

//Tarjan's Algorithm for SCC
class TarjanStronglyConnectedComponent {

    private Map<Vertex<Integer>, Integer> visitedTime;
    private Map<Vertex<Integer>, Integer> lowTime;
    private Set<Vertex<Integer>> onStack;
    private Deque<Vertex<Integer>> stack;
    private Set<Vertex<Integer>> visited;
    private List<Set<Vertex<Integer>>> result;
    private int time;

    public List<Set<Vertex<Integer>>> scc(Graph<Integer> graph) {

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
        for (Vertex<Integer> vertex : graph.getAllVertex()) {
            if(visited.contains(vertex)) {
                continue;
            }
            sccUtil(vertex);
        }

        return result;
    }

    private void sccUtil(Vertex<Integer> vertex) {

        visited.add(vertex);
        visitedTime.put(vertex, time);
        lowTime.put(vertex, time);
        time++;
        stack.addFirst(vertex);
        onStack.add(vertex);

        for (Vertex child : vertex.getAdjacentVertexes()) {
            //if child is not visited then visit it and see if it has link back to vertex's ancestor. In that case update
            //low time to ancestor's visit time
            if (!visited.contains(child)) {
                sccUtil(child);
                //sets lowTime[vertex] = min(lowTime[vertex], lowTime[child]);
                lowTime.compute(vertex, (v, low) ->
                        Math.min(low, lowTime.get(child))
                );
            } //if child is on stack then see if it was visited before vertex's low time. If yes then update vertex's low time to that.
            else if (onStack.contains(child)) {
                //sets lowTime[vertex] = min(lowTime[vertex], visitedTime[child]);
                lowTime.compute(vertex, (v, low) -> Math.min(low, visitedTime.get(child))
                );
            }
        }

        //if vertex low time is same as visited time then this is start vertex for strongly connected component.
        //keep popping vertices out of stack still you find current vertex. They are all part of one strongly
        //connected component.
        if (visitedTime.get(vertex) == lowTime.get(vertex)) {
            Set<Vertex<Integer>> stronglyConnectedComponenet = new HashSet<>();
            Vertex v;
            do {
                v = stack.pollFirst();
                onStack.remove(v);
                stronglyConnectedComponenet.add(v);
            } while (!vertex.equals(v));
            result.add(stronglyConnectedComponenet);
        }
    }

//    public static void main(String args[]) {


//    }
}

class AllCyclesInDirectedGraphTarjan
{

    private Set<Vertex<Integer>> visited;
    private Deque<Vertex<Integer>> pointStack;
    private Deque<Vertex<Integer>> markedStack;
    private Set<Vertex<Integer>> markedSet;

    public AllCyclesInDirectedGraphTarjan() {
        reset();
    }

    private void reset() {
        visited = new HashSet<>();
        pointStack = new LinkedList<>();
        markedStack = new LinkedList<>();
        markedSet = new HashSet<>();
    }

    public List<List<Vertex<Integer>>> findAllSimpleCycles(Graph<Integer> graph) {
        reset();
        List<List<Vertex<Integer>>> result = new ArrayList<>();
        for(Vertex<Integer> vertex : graph.getAllVertex()) {
            findAllSimpleCycles(vertex, vertex, result);
            visited.add(vertex);
            while(!markedStack.isEmpty()) {
                markedSet.remove(markedStack.pollFirst());
            }
        }
        return result;
    }

    private boolean findAllSimpleCycles(Vertex start, Vertex<Integer> current,List<List<Vertex<Integer>>> result) {
        boolean hasCycle = false;
        pointStack.offerFirst(current);
        markedSet.add(current);
        markedStack.offerFirst(current);

        for (Vertex<Integer> w : current.getAdjacentVertexes()) {
            if (visited.contains(w)) {
                continue;
            } else if (w.equals(start)) {
                hasCycle = true;
                pointStack.offerFirst(w);
                List<Vertex<Integer>> cycle = new ArrayList<>();
                Iterator<Vertex<Integer>> itr = pointStack.descendingIterator();
                while(itr.hasNext()) {
                    cycle.add(itr.next());
                }
                pointStack.pollFirst();
                result.add(cycle);
            } else if (!markedSet.contains(w)) {
                hasCycle = findAllSimpleCycles(start, w, result) || hasCycle;
            }
        }

        if (hasCycle) {
            while(!markedStack.peekFirst().equals(current)) {
                markedSet.remove(markedStack.pollFirst());
            }
            markedSet.remove(markedStack.pollFirst());
        }

        pointStack.pollFirst();
        return hasCycle;
    }
}

public class Main
{
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

        String str= inputFile;
        String number = str.replaceAll("[^0-9]", "");

        outputFile = "output" + number + ".txt";

        File file = new File("/Users/davidskinner/Documents/Repositories/LocalTradeEfficiency/" + inputFile);

        int numberOfVertices = 0;
        Graph<Integer> graph = new Graph<>(true);

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            int counter = 0;
            while ((st = br.readLine()) != null)
            {
                String[] splitter = st.split(" ");

                if (counter == 0)
                {
                    // read in first line
                    numberOfVertices = Integer.valueOf(splitter[0]);
                    log("The number of vertices is: " + String.valueOf(numberOfVertices));

                } else
                {
                    //import the Edges
//                    activities.add(new Activity(splitter[0], splitter[1], splitter[2], splitter[3]));
                }
                System.out.println(st);

                counter++;
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        log("");

        graph.addEdge(1,2);
        graph.addEdge(2,3);
        graph.addEdge(3,4);
        graph.addEdge(4,1);
        graph.addEdge(2,1);
        graph.addEdge(5,1);
        graph.addEdge(1,5);

        AllCyclesInDirectedGraphTarjan tarjan = new AllCyclesInDirectedGraphTarjan();

        List<List<Vertex<Integer>>> result = tarjan.findAllSimpleCycles(graph);

        result.forEach(cycle -> {
            cycle.forEach(v -> System.out.print(v.getId() + " "));
            System.out.println();
        });
    }
}