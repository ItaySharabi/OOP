package ex1;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WGraph_Algo implements weighted_graph_algorithms{

    weighted_graph graph;

    public WGraph_Algo () {
        this.graph = new WGraph_DS();
    }

    public WGraph_Algo(weighted_graph g) {
        init(g);
    }

    @Override
    public void init(weighted_graph g) {
        if (g != null) this.graph = g;
    }

    @Override
    public weighted_graph getGraph() {
        return this.graph;
    }

    @Override
    public weighted_graph copy() {
        return null;
    }

    @Override
    public boolean isConnected() {

        node_info n = getFirstNode();

        Queue<node_info> q = new LinkedList<>();
        q.add(n);

        while (!q.isEmpty()) {

            n = q.poll();

            for (node_info neighbor: graph.getV(n.getKey())) {
                if (neighbor.getTag() == 0) { // If neighbor was not visited.
                    neighbor.setTag(1);
                    q.add(neighbor);
                }
            }
        }

        for (node_info node : graph.getV())
            if (node.getTag() == 0) return false;

        return true;

    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

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

    /**
     * Runtime: O(1);
     * @returns the first node to appear in graph.getV() Collection of nodes.
     */
    private node_info getFirstNode() {
        for (node_info n : graph.getV())
            return n;
        return null;
    }


}
