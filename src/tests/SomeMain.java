package tests;

import ex1.WGraph_Algo;
import ex1.WGraph_DS;
import ex1.weighted_graph;
import ex1.weighted_graph_algorithms;

public class SomeMain {

    static weighted_graph g;
    static weighted_graph_algorithms ga;

    public static void main(String[] args) {


        g = generateGraph(5, 4);
        ga = new WGraph_Algo(g);


        System.out.println(ga.shortestPathDist(0, 1));
        System.out.println(ga.shortestPath(0, 1));

//        weighted_graph g = ga.copy();
        g.removeNode(1);
        g.addNode(1);
        g.connect(1, 0, 9);

        ga.init(g);

        System.out.println(ga.isConnected());

        weighted_graph g1 = ga.copy();

        System.out.println(g1);

//        System.out.println(ga.shortestPathDist(0, 1));
//        System.out.println(ga.shortestPath(0, 1));
    }

    public static weighted_graph generateGraph(int nodeSize, int edgeSize) {

        weighted_graph g = new WGraph_DS();

        //Add vertices to the graph:
        for (int i = 0; i < nodeSize; i++) g.addNode(i);

        //Connect vertices:
        int j = 1;
        int currentEdgeSize;

        while (j <= edgeSize && edgeSize < (nodeSize * (nodeSize - 1))) {
            currentEdgeSize = g.edgeSize();
            int a = (int) (Math.random() * nodeSize); //Random (int) 0 <= a < nodeSize
            int b = (int) (Math.random() * nodeSize); //Random (int) 0 <= b < nodeSize
            int cd = (int) (Math.random() * 5) + 1;
            double weight = ((double) (int) (Math.random() * 10) + 3) / cd;
//            double weight = 3;
            g.connect(a, b, weight);
            if (currentEdgeSize < g.edgeSize())
                j++;
        }

        return g;
    }

    public static weighted_graph boardGraph1() {
        weighted_graph g = new WGraph_DS();

        g.addNode(0);
        g.addNode(1);
        g.addNode(0);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);

        g.connect(0, 1, 8);
        g.connect(0, 2, 4);
        g.connect(0, 3, 0.5);
        g.connect(1, 2, 1);
        g.connect(2, 3, 1);
        g.connect(2, 3, 0.5);
        g.connect(3, 4, 0.5);

        return g;
    }
}
