package ex1;

import java.io.Serializable;
import java.util.*;


/**
 * This class represents a data structure of an unweighted, undirected graph.
 * Nodes are added uniquely into the graph by key, and each node holds an information (String) field, and a tag (double) field.
 * Edges between nodes are represented as a simple class with 2 fields: node_info neighbor, and double weight.
 * Note: If a node A has a neighbor B on the graph, then A has an Edge object containing B, and B has an Edge to A.
 *
 * This class has some simple methods of get/set that acquire constant runtime O(1). And some other methods
 * like add and remove nodes and edges from the graph.
 *
 * Another outer class i've used is a node comparator class, which helps me decide which node is "smaller" by the 'tag' field.
 */

public class WGraph_DS implements weighted_graph, Serializable {

    /**
     * This class represents a node (vertex) on the graph.
     */
    private class Node implements node_info, Serializable {
        private int key;
        private String info;
        private double tag;


        public Node(int key) {
            this.key = key;
            this.info = "Node " + key;
            this.tag = 0;
        }


        @Override
        public String toString() {
            return "{" +
                    "key=" + key +
                    ", info='" + info + '\'' +
                    ", tag=" + tag +
                    '}';
        }

        @Override
        public int getKey() {
            return key;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            if (s != null) this.info = s;
        }

        @Override
        public double getTag() {
            return tag;
        }

        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return key == node.key &&
//                    Double.compare(node.tag, tag) == 0 &&
                    Objects.equals(info, node.info);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, info, tag);
        }
    }

    /**
     * This class represents an edge between whoever holds this object and the target node.
     * Extends Pair - An abstract class I've written, to represent a pair of objects, which is what I needed.
     *                For my implementation, the left object of the pair will be the neighbor, and the right object
     *                will be the weight of the edge between them.
     * @Fields: node_info neighbor - The target neighbor that is connected with the node who holds this object.
     *          double weight - The weight that the edge is set with.
     */
    private class Edge extends Pair implements Serializable {
        private node_info neighbor;
        private double weight;

        public Edge(Object leftObject, Object rightObject) {
            super(leftObject, rightObject);

            if (leftObject instanceof node_info && leftObject != null) this.neighbor = (node_info) leftObject;
            if (rightObject instanceof Double) this.weight = (double) rightObject;
        }

        public node_info getNeighbor() {
            return this.neighbor;
        }

        public double getWeight() {
            return this.weight;
        }
    }

    private HashMap<Integer, node_info> nodes; //A map of the nodes on this graph.
    private HashMap<Integer, HashMap<Integer, Edge>> edges; //A map of the edges on this graph. For each node(Integer key) store a
                                                            //map of Edges.
    private int nodeSize; //|V|
    private int edgeSize; //|E|
    private int countMC;  //Changes made on this graph. Adding/Removing nodes/edges from the graph increase this field by 1.

    //Default empty constructor, default initialization of the graph's fields.
    public WGraph_DS() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.nodeSize = 0;
        this.edgeSize = 0;
        this.countMC = 0;
    }

    /**
     * @Runtime: O(1).
     * @param key - the node_id to return
     * @returns the specified node from this graph.
     */
    @Override
    public node_info getNode(int key) {
        return nodes.get(key);
    }

    /**
     * Checks if node1 and node2 are connected on this graph.
     * @Runtime: O(1).
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (getNode(node1) != null && getNode(node2) != null) {
            return edges.get(node1).containsKey(node2);
        }
        return false;
    }

    /**
     * @Returns the weight of the edge between node1 and node2, if there is one, return -1 otherwise.
     * @param node1
     * @param node2
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2))
            return (edges.get(node1).get(node2).getWeight());

        return -1;
    }

    /**
     * Adds a node to this graph with the given key.
     * If a node with the same key already exists in this graph - no node is added.
     * @Runtime: O(1).
     * @param key
     */
    @Override
    public void addNode(int key) {
        if (!nodes.containsKey(key) && !edges.containsKey(key)) {
            this.nodes.put(key, new Node(key));
            this.edges.put(key, new HashMap<>());
            nodeSize++;
            countMC++;
            return;
        }
    }

    /**
     * Connect 2 nodes with an 'edge' between them.
     * The edge is between node1 and node2, and has a weight value.
     * Weight has to be natural (weight > 0).
     * If node1 == node2 then no connection is made.
     * If node1 and node2 are connected, no action is performed.
     * @Runtime: O(1), some constant time consuming operations.
     * @param node1
     * @param node2
     * @param w - weight
     */
    @Override
    public void connect(int node1, int node2, double w) {
        node_info n1 = getNode(node1);
        node_info n2 = getNode(node2);
        if (n1 != null && n2 != null && node1 != node2) {
            if (!hasEdge(node1, node2) && w > 0) {
                edges.get(node1).put(node2, new Edge(getNode(node2), w));
                edges.get(node2).put(node1, new Edge(getNode(node1), w));
                edgeSize++;
                countMC++;
            }
        }
    }

    /**
     * @returns Collection<node_info>. A shallow copy of this graph's node collection.
     */
    @Override
    public Collection<node_info> getV() {
        return this.nodes.values();
    }

    /**
     * @param node_id
     * @return Collection. null if node_id is null, empty collection if no neighbors exist.
     * @Runtime: O(k), where k is a amount of neighbors of node_id.
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if (getNode(node_id) != null) {
            Collection<node_info> neighbors = new ArrayList<>();
            Iterator<Edge> itr = edges.get(node_id).values().iterator();
            Edge e;

            while (itr.hasNext()) {
                e = itr.next();
                neighbors.add(e.getNeighbor());
            }
            return neighbors;
        }
        return null;
    }

    /**
     * Removes the node from this graph.
     * If a node is removed, so does all of the edges applied on this node.
     * @Runtime: O(|V|), removing a node and all of its neighbors.
     * @param key
     * @return the removed node.
     */
    @Override
    public node_info removeNode(int key) {

        if (nodes.containsKey(key)) {
            Iterator<node_info> itr = getV(key).iterator();

            while (itr.hasNext()) removeEdge(key, itr.next().getKey());

            node_info n = getNode(key);
            edges.remove(key); //Remove from edge list.
            nodes.remove(key); //Remove from nodes list.
            nodeSize--;
            countMC++;
            return n;
        }
        return null;
    }

    /**
     * Removes the edge between node1 and node2, and vice-versa.
     * @Runtime: O(1).
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {

            edges.get(node1).remove(node2);//Removing node1 -> node2
            edges.get(node2).remove(node1);//Removing node2 -> node1
            edgeSize--;
            countMC++;
        }
    }

    /**
     * @returns the current amount of nodes on this graph.
     */
    @Override
    public int nodeSize() {
        return this.nodeSize;
    }

    /**
     * @returns the amount of edges on this graph.
     */
    @Override
    public int edgeSize() {
        return this.edgeSize;
    }

    /**
     * @returns the amount of changes made to this graph since it was created.
     */
    @Override
    public int getMC() {
        return this.countMC;
    }

    @Override
    public String toString() {
        return "WGraph_DS{" +
                "graphNodes=" + nodes +
                ", \ngraphEdges=" + edges +
                ", \nnodeSize=" + nodeSize +
                ", \nedgeSize=" + edgeSize +
                ", \ncountMC=" + countMC +
                '}';
    }

    /**
     * Overriding equals(Object o) method in-order to compare to graphs.
     * For my implementation, 2 graphs are compared if they have the same
     * amount of nodes and edges.
     * For 2 graphs: G1 = (V1, E1), G2 = (V2, E2).
     *       G1 = G2 <==> |V1|+|E1| == |V2|+|E2|
     * @param o - the graph to compare with.
     * @return true iff this.graph equals (weighted_graph)o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return nodeSize == wGraph_ds.nodeSize &&
                edgeSize == wGraph_ds.edgeSize &&
//                countMC == wGraph_ds.countMC &&
                Objects.equals(nodes.size(), wGraph_ds.nodes.size()) &&
                Objects.equals(edges.size(), wGraph_ds.edges.size());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges, nodeSize, edgeSize, countMC);
    }
}

/**
 * An abstract generic class I've created to help visualize pairs of objects.
 * @param <L> - Any left object to initiate.
 * @param <R> - Any right object to initiate.
 */
abstract class Pair<L, R> implements Serializable {
    private L _leftObject;
    private R _rightObject;

    public Pair(L leftObject, R rightObject) {
        if (leftObject != null && rightObject != null) {
            _leftObject = leftObject;
            _rightObject = rightObject;
        }
    }

    public L getLeft() {
        return this._leftObject;
    }

    public R getRight() {
        return this._rightObject;
    }

}

/**
 * This simple class implements Comparator<node_info> in-order to compare
 * two given nodes from the graph.
 * Note: For my implementation, nodes are equal by tag.
 *
 * This class is instantiated to help the PriorityQueue<node_info> object decide
 * which node to bring up in the queue.
 */
class NodeComparator implements Comparator<node_info>, Serializable {
    @Override
    public int compare(node_info o1, node_info o2) {
        return (int) (o1.getTag() - o2.getTag());
    }
}



