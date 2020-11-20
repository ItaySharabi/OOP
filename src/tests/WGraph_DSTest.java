package tests;

import ex1.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {

    weighted_graph g;
    weighted_graph_algorithms ga;

    /**
     * This graph building method was taken from our OOP course github.
     * Before each test, recreate this graph.
     */
    @BeforeEach
    public void makeGraph() {
        g = new WGraph_DS();
        int v_size = 10, e_size = 10, seed = 10;


        for(int i=0;i<v_size;i++)
            g.addNode(i);

        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {
            double weight = Math.random()*seed;
            int a = (int)(Math.random()*v_size);
            int b = (int)(Math.random()*v_size);

            int i = nodes[a];
            int j = nodes[b];
            g.connect(i,j, weight);
        }
        ga = new WGraph_Algo(g);
    }

    @Test
    void getNode() {

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

        int node1 = (int) (Math.random() * 7) + 1;
        int node2 = (int) (Math.random() * 10) + 1;

        g.connect(node1, node2, 1);

        if (g.hasEdge(node1, node2)) {
            double weight = g.getEdge(node1, node2);

            System.out.println(node1 + ", " + node2);
            assertEquals(weight, g.getEdge(node1, node2));

            g.removeEdge(node1, node2);
            assertNotEquals(weight, g.getEdge(node1, node2));
            assertEquals(-1, g.getEdge(node1, node2));

            g.connect(node1, node2, weight);
            g.connect(node1, node2, weight+1);
            assertEquals(weight+1, g.getEdge(node1, node2));

            g.removeEdge(node1, node2);
            g.connect(node1, node2, (Math.random() * 20) + 1);
            assertNotEquals(weight, g.getEdge(node1, node2));

        }
    }

    @Test
    void hasEdge() {

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

        int i = 0;
        int node1 = 0, node2 = 0;

        while(i < 50) {

                node1 = (int) (Math.random() * 10) + 1;
                node2 = (int) (Math.random() * 10) + 1;

            if ((i++)%2 == 0) {
                g.addNode(node1);
                g.removeNode(node1);
                g.addNode(node1);
                assertNotNull(g.getNode(node1));
            }
            else {
                g.removeNode(node2);
                g.removeNode(node2);
                assertNull(g.getNode(node2));
            }
        }

    }


    /**
     * This method was take from our Github repository, from the tests package.
     * @param g
     * @returns a sorted array of the graph's nodes.
     * @Runtime: Omega(n*log(n) + n) for sort operation. n = |V|.
     */
    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
}



