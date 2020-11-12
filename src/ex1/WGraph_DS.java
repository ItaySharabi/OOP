package ex1;

import java.awt.*;
import java.util.*;
import java.util.List;

public class WGraph_DS implements weighted_graph{


    private class Node implements node_info {

        public int key;
        private String info;
        private double tag;

        public Node() {
            this.key = nodeSize() + 1;
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
    }

    private class Edge {
        private node_info to, from;
        private double weight;


        public Edge (node_info to, double weight) {
            if (to != null) {
                this.to = to;
                this.weight = weight;
                this.from = null;
            }

        }
        public node_info getDest() {
            return this.to;
        }
        public double getWeight() {
            return this.weight;
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "to=" + to +
//                    ",\t from=" + from +
                    ",\t weight=" + weight +
                    '}';
        }
    }


    private HashMap<Integer, node_info> graphNodes;
    private HashMap<node_info, HashMap<Integer, Edge>> graphEdges; //node_info = source node, Integer = destination key, Edge = the object representing neighborhood.
    private int nodeSize;
    private int edgeSize;
    private int countMC;


    public WGraph_DS () {
        this.graphNodes = new HashMap<>();
        this.graphEdges = new HashMap<>();
        this.nodeSize = 0;
        this.edgeSize = 0;
        this.countMC = 0;

    }

    @Override
    public node_info getNode(int key) {
        return graphNodes.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (getNode(node1) != null && getNode(node2) != null) {
            return graphEdges.get(getNode(node1)).containsKey(node2);
        }
        return false;
    }

    @Override
    public double getEdge(int node1, int node2) {

//        if (getNode(node1) != null)
//            if (graphEdges.get(getNode(node1)).get(node2) != null)
        if (hasEdge(node1, node2))
            return graphEdges.get(getNode(node1)).get(node2).getWeight();

        return -1;
    }

    @Override
    public void addNode(int key) {
        if (!graphNodes.containsKey(key) && !graphEdges.containsKey(getNode(key))) {
            this.graphNodes.put(key, new Node());
            this.graphEdges.put(getNode(key), new HashMap<>());
            nodeSize++;
            countMC++;
            return;
        }
        System.out.println("A node with the same key is already in the graph!");
    }

    @Override
    public void connect(int node1, int node2, double w) {
        if (getNode(node1) != null && getNode(node2) != null && node1 != node2) {
            graphEdges.get(getNode(node1)).put(node2, new Edge(getNode(node2), w));
            graphEdges.get(getNode(node2)).put(node1, new Edge(getNode(node1), w));
            edgeSize++;
            countMC++;
        }
    }

    /**
     *
     * @returns Collection<node_info>. A shallow copy of the graph's node collection.
     */
    @Override
    public Collection<node_info> getV() {
        return this.graphNodes.values();
    }

    @Override
    public Collection<node_info> getV(int node_id) {
        if (getNode(node_id) != null) {
            Collection<node_info> neighbors = new ArrayList<>();
            Iterator<Edge> itr = graphEdges.get(getNode(node_id)).values().iterator();
            Edge e;

            while(itr.hasNext()) {
                e = itr.next();
                neighbors.add(e.getDest());
            }
            return neighbors;
        }
        return null;
    }

    @Override
    public node_info removeNode(int key) {
        return null;
    }

    @Override
    public void removeEdge(int node1, int node2) {

        if (hasEdge(node1, node2)) {

            //Some logic executed here...
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
                "graphNodes=" + graphNodes +
                ", \ngraphEdges=" + graphEdges +
                ", \nnodeSize=" + nodeSize +
                ", \nedgeSize=" + edgeSize +
                ", \ncountMC=" + countMC +
                '}';
    }
}
