package ex1;

import java.io.*;
import java.util.*;

/**
 * This class represents a set of some graph theory algorithms that apply to any
 * weighted, undirected graph data structure, that implements the weighted_graph interface.
 *
 * The algorithms I've implemented are Breadth first - based traversal algorithms,
 * which are implemented using a Queue structure.
 * To find the shortest path, I've used a PriorityQueue to help speed things up.
 *
 * The algorithms in this class were all written by me exclusively, with some resources I've used such as:
 * @Dijkstra's shortest path idea: https://www.youtube.com/watch?v=pVfj6mxhdMw&t=561s
 * @Filewrite/read code: https://github.com/simon-pikalov/Ariel_OOP_2020/blob/master/Classes/week_03/class3/src/class3/Points3D.java
 */
public class WGraph_Algo implements weighted_graph_algorithms {

    weighted_graph graph; //The graph to operate on


    public WGraph_Algo() {
        this.graph = new WGraph_DS();
    }

    /**
     * Initiate this graph with 'g'.
     * Calling init(weighted_graph g).
     * @param g - The new graph to work on.
     */
    public WGraph_Algo(weighted_graph g) {
        init(g);
    }

    /**
     * Instantiate this.graph with a shallow copy from a given weighted_graph object.
     * @Runtime: O(1), passing a pointer.
     * I've implemented this method before by deep copying the given graph g, and
     * then instantiate this.graph with the made copy.
     *
     * Pros: 1. Encapsulation: The given graph g will not be corrupted from within this class.
     *
     * Cons: 1. That was not asked.
     *       2. I can potentially corrupt the given graph g.
     *       3. Runtime: O(|V|+|E|)
     * @param g - given graph
     */
    @Override
    public void init(weighted_graph g) {
        if (g != null) this.graph = g;
//        if (g != null) {
//            weighted_graph newGraph = new WGraph_DS();
//            this.graph = g;
//
//            for (node_info node : g.getV()) //Iterate over each unvisited node and copy its connectivity component.
//                if (node.getTag() == 0) BFSCopy(node, newGraph);
//
//            System.out.println("init with graph: " + g);
//            resetTags(); //Using BFS on @Param g, reseting its tags.
//            this.graph = newGraph;
//        } else {
//            this.graph = new WGraph_DS();
//            System.out.println("init new graph");
//        }
//
//
//        resetTags(); //Using BFS so resetting tags.
    }

    /**
     * @returns a pointer to this.graph
     */
    @Override
    public weighted_graph getGraph() {
        return this.graph;
    }

    /**
     * Creates a deep copy of this.graph into a new weighted_graph.
     * I've implemented a BFSCopy(node_info start, weighted_graph copyTo) method,
     * which traverses a given connectivity component starting from node 'start',
     * and the main method copy() executes this BFS algo on every connectivity component on this.graph.
     *
     * @Runtime: O(|V|+|E|)
     * @returns a deep copy of this.graph.
     */
    @Override
    public weighted_graph copy() {

        weighted_graph g = new WGraph_DS();

        for (node_info node : graph.getV())
            if (node.getTag() == 0)
                BFSCopy(node, g);

        System.out.println("Finished copying");
        resetTags(); //Using BFS so resetting tags.

        return g;
    }

    /**
     * As mentioned in copy(), this method traverses the graph breadth first,
     * inorder to copy it onto a new weighted_graph.
     * If the graph contains more than k > 1 connectivity components than this methods needs to
     * be executed k times.
     *
     * @Runtime: O(|V|+|E|).
     * @param start
     * @param copyTo
     */
    public void BFSCopy(node_info start, weighted_graph copyTo) {

        if (start != null) {

            /*
            My Data Structures are: List<node_info>, keeps track of visited nodes. If a nodes was now popped out
            of the queue, add it the the visited nodes list.
                                    Queue<node_info>, traversing the graph using a queue (Breadth First).
             */
            List<node_info> visited = new LinkedList<>();
            Queue<node_info> q = new LinkedList<>();


            q.add(start); //Add the first node to start traversing.
            copyNodeToGraph(start, copyTo); //Copy the start node into the graph to begin traversing.


            node_info curr = null;

            while (!q.isEmpty()) {

                curr = q.poll();

                for (node_info neighbor : this.graph.getV(curr.getKey())) {


                    if (!visited.contains(neighbor) && neighbor.getTag() == 0) {
                        neighbor.setTag(1);
                        copyNodeToGraph(neighbor, copyTo);
                        q.add(neighbor);
                        copyTo.connect(neighbor.getKey(), curr.getKey(), this.graph.getEdge(neighbor.getKey(), curr.getKey()));

                    } else if ((graph.hasEdge(curr.getKey(), neighbor.getKey())) && !(copyTo.hasEdge(curr.getKey(), neighbor.getKey()))) {
                        copyTo.connect(neighbor.getKey(), curr.getKey(), this.graph.getEdge(neighbor.getKey(), curr.getKey()));
                    }
                }

                visited.add(start);
            }
        }
    }

    /**
     * Traversing the graph Breath first, from the first node returned from the graph,
     * to check if the graph is connected.
     * @returns True iff there is a path from some node to any other node in the graph.
     *          Note: A graph with 0 or 1 vertex is connected, a null graph is not connected.
     * @Runtime: O(|V|+|E|).
     */
    @Override
    public boolean isConnected() {

        node_info n = getFirstNode(this.graph);
        if (graph.nodeSize() <= 1) return true;
        if (n == null) return false;

        Queue<node_info> q = new LinkedList<>();
        q.add(n);


        while (!q.isEmpty()) {

            n = q.poll();

            for (node_info neighbor : graph.getV(n.getKey())) {
                if (neighbor.getTag() == 0) { // If neighbor was not visited.
                    neighbor.setTag(1);
                    q.add(neighbor);
                }
            }
        }

        for (node_info node : graph.getV())
            if (node.getTag() == 0) {
                System.out.println("The graph is not connected");
                resetTags();
                return false;
            }

        System.out.println("The graph is connected");
        resetTags();
        return true;
    }

    /**
     * Calculates the distance (by weight) from node src to node dest.
     * This method relies on shortestPath(int src, int dest) by calling it and getting the specific path
     * and then adding together the total distance.
     *
     * @Runtime: O((|V|+|E|) + k), where k is the number of nodes on the path.
     * @param src - start node
     * @param dest - end (target) node
     * @return the distance (double) by weight.
     */
    @Override
    public double shortestPathDist(int src, int dest) {

        if (src == dest) return 0;

        List<node_info> path = shortestPath(src, dest);

        if (path.size() > 0) {
            double dist = 0;
            Iterator<node_info> itr = path.iterator();


            node_info node = itr.next(); //Start from the first node in the list.

            while (itr.hasNext()) {
                node_info next = itr.next(); //Mark the second node
                dist += graph.getEdge(node.getKey(), next.getKey());
                System.out.println("Edge from " + node.getKey() + " To " + next.getKey() + " With weight: " + graph.getEdge(node.getKey(), next.getKey()));
                node = next;
            }
            System.out.println("The shortest path from " + src + " to " + dest + " is " + dist + " long");

            return dist;
        }
        System.out.println("The shortest path from " + src + " to " + dest + " does not exist!");
        return -1;

    }

    /**
     * This method is my favorite!
     * Traversing the graph Breadth first using a PriorityQueue to search for the shortest path
     * from node src to node dest.
     * Data structures I've used to implement:
     * 1. A List<node_info> (visited) to keep track of visited nodes,
     * 2. A HashMap<Integer, node_info> (prevNode) to keep track of which vertex called which neighbor with a shorter path.
     * 3. A List<node_info> (path) to save the shortest path from src to dest. (rebuildPath() method does that).
     *
     * @Runtime: O(|V|+|E|)
     * @param src - start node
     * @param dest - end (target) node
     * @returns a List<node_info> that contains all nodes on the shortest path from src to dest.
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {

        if (graph.getNode(src) != null && graph.getNode(dest) != null) {

            List<node_info> visited = new LinkedList<>(); //A list of visited vertices.
            HashMap<Integer, node_info> prevNode = new HashMap<>(); //A map of parent nodes. for (Integer) key, map (node_info) parent.

            PriorityQueue<node_info> pq = new PriorityQueue<>(graph.nodeSize(), new NodeComparator()); //The BFS queue

            boolean destinationFound = false;

            setTagsInfinity();
            graph.getNode(src).setTag(0);
            pq.add(graph.getNode(src));

            node_info curr;

            while (!pq.isEmpty()) {

                curr = pq.poll();

                for (node_info neighbor : graph.getV(curr.getKey())) {
                    if (!isVisited(neighbor, visited)) {
                        double totalDist = graph.getEdge(neighbor.getKey(), curr.getKey());
                        totalDist += curr.getTag();
                        if (totalDist < neighbor.getTag()) //if the current total distance is less then the known distance:
                            neighbor.setTag(totalDist);
                        if (prevNode.containsKey(neighbor.getKey())) { // if this neighbor had a prev node, remove and put back.
                            prevNode.remove(neighbor.getKey());
                            prevNode.put(neighbor.getKey(), curr);

                        } else { // If node doesn't have a prev node already
                            prevNode.put(neighbor.getKey(), curr);
                        }

                        if (!pq.contains((node_info) neighbor)) pq.add(neighbor);

                    }//Finished processing an unvisited neighbor
                } //Finished Iterating over neighbors.
                if (curr.getKey() == dest) destinationFound = true;
                visited.add(curr);

            }//Finished PriorityQueue operation

            //Rebuild path

            if (destinationFound) {
                List<node_info> path = rebuildPath(src, dest, prevNode);
                resetTags();
                return path;
            }
        }

        System.out.println("One of the nodes is null, path does not exist");
        resetTags();
        return new LinkedList<>();
    }

    /**
     * Rebuild a given path (Assuming there is one).
     * Extract from each given node it's previous calling node and add it to the list.
     *
     * @Runtime: O(k), where k is the number of nodes on the path.
     * @param from
     * @param to
     * @param prevNodes
     * @returns a list with all rellevant nodes.
     */
    private List<node_info> rebuildPath(int from, int to, HashMap<Integer, node_info> prevNodes) {

        if (prevNodes.isEmpty() || (to == from)) return new LinkedList<>();

        List<node_info> path = new LinkedList<>();
        List<node_info> newPath = new LinkedList<>();

        node_info current = graph.getNode(to);
        path.add(current);

        while (!prevNodes.isEmpty() && current.getKey() != from) {

            path.add(prevNodes.get(current.getKey()));
            current = prevNodes.get(current.getKey()); //current = current.next();
        }

        Collections.reverse(path);
        copyList(path, newPath);

        System.out.println("Finished rebuilding path from " + from + " to " + to);
        return new LinkedList<>(newPath);
    }

    /**
     * Saves this.graph on a text file.
     * To implement I've used our course's github from class3.
     * https://github.com/simon-pikalov/Ariel_OOP_2020/blob/master/Classes/week_03/class3/src/class3/Points3D.java
     */
    @Override
    public boolean save(String file) {

        String path = "D:\\Users\\User\\Desktop\\";

        try {

            FileOutputStream output = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeObject(this.graph);
            oos.flush();
            oos.close();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Loads a graph from given path 'file'.
     * To implement I've used our course's github from class3.
     * https://github.com/simon-pikalov/Ariel_OOP_2020/blob/master/Classes/week_03/class3/src/class3/Points3D.java
     */
    @Override
    public boolean load(String file) {

        String mainPath = "D:\\Users\\User\\Desktop\\";

        try {
            File f = new File(file);
            FileInputStream input = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(input);

            weighted_graph g = (weighted_graph) ois.readObject();

            ois.close();
            input.close();

            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String toString() {
        return "WGraph_Algo{" +
                "graph=" + graph +
                '}';
    }


    /**
     * My private methods to help me organize the code.
     * Runtimes and explanations.
     */
    private boolean isVisited(node_info node, List<node_info> visited) {
        return visited.contains((node_info) node);
    }

    /**
     * @Runtime: O(1);
     *
     * @returns the first node to appear in graph.getV() Collection of nodes.
     */
    private node_info getFirstNode(weighted_graph graph) {
        if (graph != null)
            for (node_info n : graph.getV())
                return n;
        return null;
    }

    private void resetTags() {
        for (node_info n : graph.getV()) n.setTag(0);
        System.out.println("Tags are reset to 0!");
    }

    /**
     * @Runtime: O(|V|)
     * This methods sets all of the node's tags to INFINITY.
     */
    private void setTagsInfinity() {
        for (node_info node : graph.getV())
            node.setTag(Double.MAX_VALUE * 2);
    }

    /**
     * @Runtime: O(1)
     * This method copies a given node to a given graph.
     * @param node
     * @param graph
     */
    private void copyNodeToGraph(node_info node, weighted_graph graph) {
        if (node != null && graph != null) {
            graph.addNode(node.getKey()); //Copy this new Node into the new graph.
            graph.getNode(node.getKey()).setInfo(node.getInfo());
            graph.getNode(node.getKey()).setTag(node.getTag());
        }
    }

    /**
     * @Runtime: O(k + k) = O(k), where k is the number of nodes in the original list.
     * This method helped me retrieve the node's tags after using the CopyBFS() method.
     * Generates a new empty graph and copies all given nodes from the original list to the graph,
     * then extract all of the graph nodes into the new list.
     * @param from
     * @param to
     */
    private void copyList(List<node_info> from, List<node_info> to) {

        weighted_graph g = new WGraph_DS();


        for (node_info node : from) {
            copyNodeToGraph(node, g);
        }


        for (node_info node : g.getV()) {
            to.add(node);
        }
    }


}
