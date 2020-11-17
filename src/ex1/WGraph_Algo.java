package ex1;


//import java-jason.*;
import java.util.*;


public class WGraph_Algo implements weighted_graph_algorithms {

    weighted_graph graph;


    public WGraph_Algo() {
        this.graph = new WGraph_DS();
    }

    public WGraph_Algo(weighted_graph g) {
        init(g);
    }

    @Override
    public void init(weighted_graph g) {
        if (g != null) {
            weighted_graph newGraph = new WGraph_DS();
            this.graph = g;

            for (node_info node : g.getV()) //Iterate over each unvisited node and copy its connectivity component.
                if (node.getTag() == 0) BFSCopy(node, newGraph);

            this.graph = newGraph;
        } else
            this.graph = new WGraph_DS();


        resetTags(); //Using BFS so resetTags();
    }

    @Override
    public weighted_graph getGraph() {
        return copy();
    }

    @Override
    public weighted_graph copy() {

        weighted_graph g = new WGraph_DS();

        for (node_info node : graph.getV())
            if (node.getTag() == 0)
                BFSCopy(graph.getNode(node.getKey()), g);


        resetTags(); //Using BFS so resetTags();

        return g;
    }

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

                    System.out.println("Visiting node: " + curr.getKey());

                    if (!isVisited(neighbor, visited) && neighbor.getTag() == 0) {
                        neighbor.setTag(1);
                        System.out.println("\tVisiting neighbor: " + neighbor.getKey());
                        copyNodeToGraph(neighbor, copyTo);
                        q.add(neighbor);
                        copyTo.connect(neighbor.getKey(), curr.getKey(), this.graph.getEdge(neighbor.getKey(), curr.getKey()));
                    }
                }

                visited.add(start);
            }

            System.out.println("Finished Copying Connectivity component from node: " + start.getKey());

        }
    }

    @Override
    public boolean isConnected() {

        node_info n = getFirstNode(this.graph);

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
                resetTags();
                return false;
            }

        resetTags();
        return true;
    }

    /*
    NOT IMPLEMENTED YET!!!
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
            System.out.println(dist);
            return dist;
        }

        return -1;

    }

    /*
    NOT IMPLEMENTED YET!!!
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {

        if (graph.getNode(src) != null && graph.getNode(dest) != null) {

            List<node_info> visited = new LinkedList<>(); //A list of visited vertices.
            HashMap<Integer, node_info> prevNode = new HashMap<>(); //A map of parent nodes. for (Integer) key, map (node_info) parent.

            PriorityQueue<node_info> pq = new PriorityQueue<>(graph.nodeSize(), new NodeComparator()); //The BFS queue
            System.out.println(pq.comparator());

            boolean destinationFound = false;

            setTagsInfinity();
            graph.getNode(src).setTag(0);
            pq.add(graph.getNode(src));

            node_info curr;


            System.out.println("-------------Starting BFS...--------------");
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
                        } else {
                            prevNode.put(neighbor.getKey(), curr);
                        }

                        System.out.println("Node: " + neighbor.getKey() + ", Tag: " + neighbor.getTag());
                        if (!pq.contains((node_info) neighbor)) pq.add(neighbor);

                    }//Finished processing an unvisited neighbor
                } //Finished Iterating over neighbors.
                if (curr.getKey() == dest) destinationFound = true;
                visited.add(curr);

            }//Finished PriorityQueue operation

            System.out.println("-------------Finished BFS...--------------");

            for (node_info node : graph.getV()) {
                System.out.println(node.getKey() + " Tag: " + node.getTag());
            }

            //Rebuild path

            if (destinationFound) {
                resetTags();
                return rebuildPath(src, dest, prevNode);
            }
        }
        resetTags();
        return new LinkedList<>();
    }

    /*
    NOT IMPLEMENTED YET!!!
     */
    /**
     * Rebuild a given path (Assuming there is one).
     * Extract from each given node it's previous calling node and add's it to the list.
     *
     * @param from
     * @param to
     * @param prevNodes
     * @return
     */
    private List<node_info> rebuildPath(int from, int to, HashMap<Integer, node_info> prevNodes) {

        if (prevNodes.isEmpty() || (to == from)) return new LinkedList<>();

        List<node_info> path = new LinkedList<>();

        node_info current = graph.getNode(to);
        path.add(current);

        while (!prevNodes.isEmpty() && current.getKey() != from) {
            path.add(prevNodes.get(current.getKey()));
            current = prevNodes.get(current.getKey()); //current = current.next();
        }

        Collections.reverse(path);
        return path;
    }

    /*
    NOT IMPLEMENTED YET!!!
     */
    @Override
    public boolean save(String file) {


        String nodeString = graph.getNode(0) + ": {\n\t";

        String edge = "";
        int i = 0;

        for (node_info node : graph.getV()) {

            for (node_info neighbor : graph.getV(node.getKey())) {
                edge += ("(" + (neighbor.getKey() + ", " + graph.getEdge(neighbor.getKey(), node.getKey()))+ " ), ") ;
                if (i++ == graph.nodeSize() - 1) edge = edge.substring(0, edge.length()-2);
            }
            edge += "\n";
        }

        System.out.println(nodeString + edge);


        return false;
    }

    public static void writeJSONObject(String fileaName) {

//        JSONObject nodes = new JSObject();
    }

    /*
    NOT IMPLEMENTED YET!!!
     */
    @Override
    public boolean load(String file) {
        return false;
    }


    @Override
    public String toString() {
        return "WGraph_Algo{" +
                "graph=" + graph +
                '}';
    }

    private boolean isVisited(node_info node, List<node_info> visited) {
        return visited.contains((node_info) node);
    }

    /**
     * Runtime: O(1);
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
    }

    private void setTagsInfinity() {
        for (node_info node : graph.getV())
            node.setTag(Double.MAX_VALUE * 2);
    }

    private void copyNodeToGraph(node_info node, weighted_graph graph) {
        if (node != null && graph != null) {
            graph.addNode(node.getKey()); //Copy this new Node into the new graph.
            graph.getNode(node.getKey()).setInfo(node.getInfo());
        }
    }



}
