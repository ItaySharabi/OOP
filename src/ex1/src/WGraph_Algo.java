package ex1.src;

import java.io.*;
import java.util.*;

/**
 * This class represents a set of some graph theory algorithms that apply to any
 * weighted, undirected graph data structure, that implements the weighted_graph interface.
 *
 * The algorithms I've implemented are Breadth first - based traversal algorithms,
 * which are implemented using some Queue structure.
 * To find the shortest path, I've used a PriorityQueue to help speed things up.
 *
 * Note: resetTags() method must be called any time a BFS algorithm is executed
 * because it uses the node field: (double) tag, and changes it.
 *
 * Some resources that helped me implement some methods:
 *
 * @Dijkstra's shortest path: https://www.youtube.com/watch?v=pVfj6mxhdMw&t=561s
 * @Filewrite/read: https://github.com/simon-pikalov/Ariel_OOP_2020/blob/master/Classes/week_03/class3/ex1.src/class3/Points3D.java
 */
public class WGraph_Algo implements weighted_graph_algorithms {

    weighted_graph graph; //The graph to operate on


    public WGraph_Algo() {
        this.graph = new WGraph_DS();
    }

    /**
     * Initiate this graph with 'g'.
     * Calling init(weighted_graph g).
     *
     * @param g - The new graph to work on.
     */
    public WGraph_Algo(weighted_graph g) {
        init(g);
    }

    /**
     * Initiate this.graph with a shallow copy from a given weighted_graph object.
     *
     * @param g - given graph
     * @Runtime: O(1), passing a pointer.
     */
    @Override
    public void init(weighted_graph g) {
        if (g != null) this.graph = g;
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
     * and the main method copy() executes this BFS algorithm on every connectivity component on this.graph.
     *
     * @Runtime: O(| V | + | E |)
     * @returns a deep copy of this.graph.
     */
    @Override
    public weighted_graph copy() {

        weighted_graph g = new WGraph_DS();

        for (node_info node : graph.getV())
            if (node.getTag() == 0)
                BFSCopy(node, g);

//        System.out.println("Finished copying");
        resetTags(); //Using BFS so resetting tags.

        return g;
    }

    /**
     * As mentioned in copy(), this method traverses the graph breadth first,
     * inorder to copy it onto a new weighted_graph.
     * If the graph contains k > 1 connectivity components than this methods needs to
     * be executed k times.
     * <p>
     * Logic: 1. Copy first node into the new graph, enqueue it, and mark it as visited.
     * <p>
     * Repeat: 2. Dequeue the next node from the queue
     * 3. Iterate over the node's neighbor collection.
     * 4.
     *
     * @param start
     * @param copyTo
     * @Runtime: O(| V | + | E |).
     */
    public void BFSCopy(node_info start, weighted_graph copyTo) {

        if (start != null) {

            /*
            My Data Structures are: List<node_info>, keeps track of visited nodes. If now finished
            iterating over 'node' 's neighbors, add 'node' to the the visited nodes list. Regards the original graph nodes.
                                    Queue<node_info>, traversing the graph using a queue (Breadth First).
             */
            List<node_info> visited = new LinkedList<>();
            Queue<node_info> q = new LinkedList<>();


            q.add(start); //Add the first node to start traversing.
            copyNodeToGraph(start, copyTo); //Copy the start node into the graph to begin traversing.
            start.setTag(1); // node is marked as copied into the new graph if node.getTag == 1.

            node_info curr = null;

            while (!q.isEmpty()) {

                curr = q.poll();

                for (node_info neighbor : this.graph.getV(curr.getKey())) {

                    if (!visited.contains(neighbor) && neighbor.getTag() == 0) {
                        copyNodeToGraph(neighbor, copyTo);// neighbor's tag == 0 so copy it into the new graph.
                        neighbor.setTag(1); //neighbor was not copied yet so copy it.
                        q.add(neighbor); // neighbor was not visited so enqueue it.
                        copyTo.connect(neighbor.getKey(), curr.getKey(), this.graph.getEdge(neighbor.getKey(), curr.getKey()));
                        //Connect both neighbor and curr on the new graph with their original edge.

                    } else if (!(copyTo.hasEdge(curr.getKey(), neighbor.getKey()))) {
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
     *
     * @returns True iff there is a path from some node to any other node in the graph.
     * Note: A graph with 0 or 1 vertex is connected, a null graph is not connected.
     * @Runtime: O(| V | + | E |).
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
     * Calculates the distance (by weight) from node ex1.src to node dest.
     * This method relies on shortestPath(int ex1.src, int dest) by calling it and getting the specific path
     * and then returning the destination node's tag with getTag(). That represents the distance between ex1.src and dest.
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return the distance (double) by weight.
     * @Runtime: O(( | V | + | E |) + k), where k is the number of nodes on the path.
     */
    @Override
    public double shortestPathDist(int src, int dest) {

        if (src == dest) return 0;

        List<node_info> path = shortestPath(src, dest); //Execute Dijkstra's from ex1.src to dest

        if (path.size() > 0) //If path exists, it's size is greater than 0. return the tag of dest node.
            return path.get(path.indexOf(graph.getNode(dest))).getTag();

        return -1;

    }

    /**
     * This method is my favorite!
     * Traversing the graph Breadth first using a PriorityQueue to search for the shortest path
     * from node ex1.src to node dest.
     * Data structures I've used to implement:
     * 1. A List<node_info> (visited) to keep track of visited nodes,
     * 2. A HashMap<Integer, node_info> (prevNode) to keep track of which vertex called which neighbor with a shorter path.
     * 3. A List<node_info> (path) to save the shortest path from ex1.src to dest. (rebuildPath() method does that).
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @Runtime: O(| V | + | E |)
     * @returns a List<node_info> that contains all nodes on the shortest path from ex1.src to dest.
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
                    if (!visited.contains(neighbor)) {

                        double totalDist = graph.getEdge(neighbor.getKey(), curr.getKey());
                        totalDist += curr.getTag();
                        if (totalDist < neighbor.getTag()) {//if the current total distance is less then the known distance:
                            neighbor.setTag(totalDist);
                            updateCallingNode(prevNode, neighbor, curr);
                        }
                        if (!pq.contains(neighbor)) pq.add(neighbor);

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

        resetTags();
        return new LinkedList<>();
    }

    /**
     * Rebuild a given path (Assuming there is one).
     * Extract from each given node it's previous calling node and add it to the list.
     *
     * @param from
     * @param to
     * @param prevNodes
     * @Runtime: O(k), where k is the number of nodes on the path.
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

        copyList(path, newPath);

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
            init(g);

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

    /**
     * @Runtime: O(1);
     * @returns the first node to appear in graph.getV() Collection of nodes.
     */
    private node_info getFirstNode(weighted_graph graph) {
        if (graph != null)
            for (node_info n : graph.getV())
                return n;
        return null;
    }

    /**
     * @Runtime: O(| V |). Reset each node's tag field to be 0.
     */
    private void resetTags() {
        for (node_info n : graph.getV()) n.setTag(0);
    }

    /**
     * @Runtime: O(| V |)
     * This methods sets all of the node's tags to INFINITY.
     */
    private void setTagsInfinity() {
        for (node_info node : graph.getV())
            node.setTag(Double.MAX_VALUE * 2);
    }

    /**
     * @param node
     * @param graph
     * @Runtime: O(1)
     * This method copies a given node to a given graph.
     */
    private void copyNodeToGraph(node_info node, weighted_graph graph) {
        if (node != null && graph != null) {
            graph.addNode(node.getKey()); //Copy this new Node into the new graph.
            graph.getNode(node.getKey()).setInfo(node.getInfo());
            graph.getNode(node.getKey()).setTag(node.getTag());
        }
    }

    /**
     * @param from
     * @param to
     * @Runtime: O(k + k) = O(k), where k is the number of nodes in the original list.
     * This method helped me retrieve the node's tags after using the CopyBFS() method.
     * Generates a new empty graph and copies all given nodes from the original list to the graph,
     * then extract all of the graph nodes into the new list.
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

    /**
     * @param map    - The data table to update.
     * @param caller - the node who called its neighbor
     * @param callee - the neighbor of the calling node
     * @Runtime: O(1), some constant finite operations.
     */
    private void updateCallingNode(HashMap<Integer, node_info> map, node_info caller, node_info callee) {

        if (map.containsKey(caller.getKey())) map.remove(caller.getKey());
        map.put(caller.getKey(), callee);

    }


}
