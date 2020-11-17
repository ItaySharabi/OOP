package ex1;

import java.util.*;


public class WGraph_DS implements weighted_graph {


    private class Node implements node_info {

        private int key;
        private String info;
        private double tag;
        private HashMap<Integer, node_info> neighbors;


        public Node(int key) {
            this.key = key;
            this.info = "Node " + key;
            this.tag = 0;
            neighbors = new HashMap<>();
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

        public void printNode() {
            System.out.println("Node: " + this.key);
        }

//        /**
//         * Compares 2 given nodes by tag value;
//         * @param o1 first node.
//         * @param o2 second node.
//         * @returns a value greater than 0 if o1 > o2.
//         *          a value lower than 0 if 01 < o2.
//         *          0 if o1 == o2.
//         *
//         *          IF ONE OF THE NODES IS NULL RETURNS -INFINITY.
//         */
//        @Override
//        public int compare(node_info o1, node_info o2) {
//            if (o1 != null && o2 != null)
//                return (int) (o1.getTag() - o2.getTag());
//            return Integer.MIN_VALUE;
//        }
    }

    private HashMap<Integer, node_info> nodes;
    private HashMap<Integer, HashMap<Integer, Edge>> edges; //node_info = source node, Integer = destination key, Edge = the object representing neighborhood.
    private int nodeSize;
    private int edgeSize;
    private int countMC;
//    public static Graph_Comp _comp = new Graph_Comp();


    public WGraph_DS() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.nodeSize = 0;
        this.edgeSize = 0;
        this.countMC = 0;
    }

    @Override
    public node_info getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (getNode(node1) != null && getNode(node2) != null) {
            return edges.get(node1).containsKey(node2);
        }
        return false;
    }

    @Override
    public double getEdge(int node1, int node2) {
        if (hasEdge(node1, node2))
            return (edges.get(node1).get(node2).getWeight());

        return -1;
    }

    @Override
    public void addNode(int key) {
        if (!nodes.containsKey(key) && !edges.containsKey(key)) {
            this.nodes.put(key, new Node(key));
            this.edges.put(key, new HashMap<>());
            nodeSize++;
            countMC++;
            return;
        }
        System.out.println("A node with the same key is already in the graph!");
    }

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
                System.out.println("Connected: " + node1 + " -> " + node2 + ", weight = " + w);
            }
        }
    }

    /**
     * @returns Collection<node_info>. A shallow copy of the graph's node collection.
     */
    @Override
    public Collection<node_info> getV() {
        return this.nodes.values();
    }

    /**
     * @param node_id
     * @return Collection. null if node_id is null, empty collection of no neighbors exist.
     * @Returns 'node_id''s neighbors as a Collection of node_info
     * Runtime: O(k), where k is a amount of neighbors of node_id.
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

    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1, node2)) {

            edges.get(node1).remove(node2);//Removing node1 -> node2
            edges.get(node2).remove(node1);//Removing node2 -> node1
            edgeSize--;
            countMC++;
        }
    }

    @Override
    public int nodeSize() {
        return this.nodeSize;
    }

    @Override
    public int edgeSize() {
        return this.edgeSize;
    }

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
}

//class Pair {
//    private node_info neighbor;
//    private double weight;
//
//    public Pair(node_info neighbor, double weight) {
//        if (neighbor != null && weight > 0) {
//            this.neighbor = neighbor;
//            this.weight = weight;
//        }
//    }
//
//    public node_info getNeighbor() {
//        return this.neighbor;
//    }
//
//    public double getWeight() {
//        return this.weight;
//    }
//}

abstract class Pair<L, R> {
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

class Edge extends Pair {

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

class NodeComparator implements Comparator<node_info> {
    @Override
    public int compare(node_info o1, node_info o2) {
        return (int) (o1.getTag() - o2.getTag());
    }
}

class Graph_Comp implements Comparator<weighted_graph> {

    /**
     * Comparing graphs if the have the same set of nodes (by key) and the same set of edges.
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(weighted_graph o1, weighted_graph o2) {


        if (o1.edgeSize() < o2.edgeSize() || o1.edgeSize() > o2.edgeSize()) return -1;
        if (o2.nodeSize() <= o1.nodeSize()) {
            for (node_info nodeFromO1 : o1.getV()) {
                node_info nodeFromo2;
                if (o2.getNode(nodeFromO1.getKey()) != null) {
                    nodeFromo2 = o2.getNode(nodeFromO1.getKey());

                    if (o2.getV(nodeFromo2.getKey()).size() == o1.getV(nodeFromO1.getKey()).size()) {
                        for (node_info neighborO1 : o1.getV(nodeFromO1.getKey())) {
                            if (!o2.hasEdge(nodeFromo2.getKey(), neighborO1.getKey())) return -1;
                        }
                    } else return -1;
                } else return -1;

            }
        }
        return 0;
    }
}

