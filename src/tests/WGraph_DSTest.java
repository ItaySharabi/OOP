package tests;

import ex1.WGraph_DS;
import ex1.weighted_graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {

    int _exceptionCount = 0;

    @Test
    void getNode() {

        weighted_graph g = createLinearGraph(10, 10);

        assertNotNull(g.getNode(0));
        g.removeNode(0);
        g.addNode(0);
        assertNotNull(g.getNode(0));
        g.removeNode(0);
        g.addNode(0);
        g.addNode(0);
        assertNotNull(g.getNode(0));
        g.removeNode(0);
        assertNull(g.getNode(0));
        g.addNode(0);
        g.removeNode(0);
        g.removeNode(0);
        assertNull(g.getNode(0));

    }

    @Test
    void getEdge() {
        weighted_graph g = createLinearGraph(10, 10);

        int node1 = (int) (Math.random() * 7) + 1;
        int node2 = (int) (Math.random() * 10) + 1;

        if (g.hasEdge(node1, node2)) {
            double weight = g.getEdge(node1, node2);

            System.out.println(node1 + ", " + node2);
            assertEquals(weight, g.getEdge(node1, node2));
            g.removeEdge(node1, node2);
            assertNotEquals(weight, g.getEdge(node1, node2));
            assertEquals(-1, g.getEdge(node1, node2));
            g.connect(node1, node2, weight);
            assertEquals(weight, g.getEdge(node1, node2));
            g.removeEdge(node1, node2);
            g.connect(node1, node2, (Math.random() * 20) + 1);
            assertNotEquals(weight, g.getEdge(node1, node2));
        }
    }

    @Test
    void hasEdge() {

        weighted_graph g = createLinearGraph(10, 10);

        int node1 = (int) (Math.random() * 7) + 1;
        int node2 = (int) (Math.random() * 10) + 1;

        if (g.hasEdge(node1, node2)) {
            double weight = g.getEdge(node1, node2);

            System.out.println(node1 + ", " + node2);
            assertEquals(weight, g.getEdge(node1, node2));
            g.removeEdge(node1, node2);
            assertNotEquals(weight, g.getEdge(node1, node2));
            assertEquals(-1, g.getEdge(node1, node2));
            g.connect(node1, node2, weight);
            assertEquals(weight, g.getEdge(node1, node2));
            g.removeEdge(node1, node2);
            g.connect(node1, node2, (Math.random() * 20) + 1);
            assertNotEquals(weight, g.getEdge(node1, node2));
        }
    }

    @Test
    void removeNode() {
        weighted_graph g = createLinearGraph(10, 10);

        int i = 0;
        int node1 = 0, node2 = 0;

        while(i < 100) {

                node1 = (int) (Math.random() * 10) + 1;
                node2 = (int) (Math.random() * 10) + 1;

            if (i++%2 == 0) {
                g.addNode(node1);
                g.removeNode(node1);
                assertNull(g.getNode(node1));
            }
            else {
                g.removeNode(node2);
                g.removeNode(node2);
                assertNull(g.getNode(node2));
            }




        }

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

    public static weighted_graph myGraph() {

        weighted_graph g = new WGraph_DS();

        for (int i = 0; i < 10; i++)
            g.addNode(i);

        g.connect(1, 2, 1);
        g.connect(2, 4, 1);
        g.connect(3, 5, 1);
        g.connect(4, 5, 1);
        g.connect(4, 6, 1);
        g.connect(6, 7, 1);
        g.connect(6, 8, 1);
        g.connect(8, 9, 1);
        g.connect(0, 9, 1);

        return g;
    }
}
