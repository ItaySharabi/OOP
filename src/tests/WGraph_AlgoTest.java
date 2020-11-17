package tests;

import ex1.WGraph_Algo;
import ex1.WGraph_DS;
import ex1.weighted_graph;
import ex1.weighted_graph_algorithms;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

    @Test
    void init() {
        weighted_graph g = createLinearGraph(10, 10);

        weighted_graph_algorithms ga = new WGraph_Algo(g);

        weighted_graph g1 = ga.copy(); //Deep copy g1

        g1.removeNode(6); //Remove node 6 from g1

        ga.init(g1); //Init ga with g1 (sending shallow pointer so far).

        g1 = ga.copy(); //Deep copy g1 again

        assertNotNull(6); //Make sure node 6 is not null

        g1.addNode(12);

        ga.init(g1);

        g1 = ga.copy();

        assertNotNull(g1.getNode(12));
        assertNull(g1.getNode(6));
    }

    @Test
    void copy() {
        weighted_graph g = createLinearGraph(10, 10);

        weighted_graph_algorithms ga = new WGraph_Algo(g); //Creates a graph_algo initiated with g. Uses copy() once.

        weighted_graph g1 = ga.copy(); //Using copy second time and save it in g1.


        assertEquals(g1.nodeSize() + g1.edgeSize(), ga.copy().nodeSize() + ga.copy().edgeSize()); //Using copy() 3rd + 4th time and checking if nodesize() + edgeSize() equals
    }

    @Test
    void shortestPath() {
    }

    @Test
    void getGraph() {
        weighted_graph g = createLinearGraph(10, 10);

        weighted_graph_algorithms ga = new WGraph_Algo(g);

        weighted_graph g1 = ga.getGraph();

        assertEquals(g1.nodeSize(), g.nodeSize());
        assertEquals(g1.edgeSize(), g.edgeSize());

        g1 = ga.getGraph();
        g1 = ga.getGraph();

        assertEquals(g1.nodeSize(), g.nodeSize());
        assertEquals(g1.edgeSize(), g.edgeSize());
    }

    @Test
    void save() {
        weighted_graph g = createLinearGraph(10, 10);

        weighted_graph_algorithms ga = new WGraph_Algo(g);

        ga.save("");
    }




    public static weighted_graph createLinearGraph(int vertices, int _randomSeed) {

        weighted_graph g = new WGraph_DS();

        int i = 0;

        while (i < vertices)
            g.addNode(i++);

        i = 0;
        int _node1, _node2;

        while (i < vertices - 1) {
            double weight = 1; //(Math.random() * _randomSeed)

            _node1 = i;
            _node2 = ++i;

            g.connect(_node1, _node2, weight);
        }
        return g;
    }
}