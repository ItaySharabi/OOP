package tests;

import ex1.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {

    @Test
    void isConnected() {
        weighted_graph g0 = graph_creator(0,0,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = graph_creator(1,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = graph_creator(2,0,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertFalse(ag0.isConnected());

        g0 = graph_creator(2,1,1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = graph_creator(10,30,1);
        ag0.init(g0);
        boolean b = ag0.isConnected();
        assertEquals(30, g0.edgeSize());
        assertTrue(b);
        g0.removeNode(12);
        g0.addNode(12);
        ag0.init(g0);
        b = ag0.isConnected();
        assertFalse(b);
    }

    @Test
    void init() {
        weighted_graph g = graph_creator(10, 30, 10);

        weighted_graph_algorithms ga = new WGraph_Algo(g);

        weighted_graph g1 = ga.getGraph();

        assertEquals(g.edgeSize(), g1.edgeSize());

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
        weighted_graph g0 = graph_creator(3,3,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(g0.equals(ag0.copy()));
    }

    @Test
    void shortestPathDist() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());
        double d = ag0.shortestPathDist(0,10);
        assertEquals(5.1, d);
    }

    @Test
    void shortestPath() {
        weighted_graph g0 = small_graph();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        List<node_info> sp = ag0.shortestPath(0,10);
        double[] checkTag = {0.0, 1.0, 2.0, 3.1, 5.1};
        int[] checkKey = {0, 1, 5, 7, 10};
        int i = 0;
        for(node_info n: sp) {
            assertEquals(n.getTag(), checkTag[i]);
            assertEquals(n.getKey(), checkKey[i]);
            i++;
        }
    }

    @Test
    void save_load() {
        weighted_graph g0 = graph_creator(10,30,10);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);

        ag0.save("g0.obj");

        String str = "g0.obj";
        ag0.save(str);

        weighted_graph g1 = graph_creator(10,30,1);
        ag0.load("g0.obj");
        assertEquals(g0,g1);
        g0.removeNode(0);
        assertNotEquals(g0,g1);
    }

    /**
     * This method was taken from our test package on our github.
     * creates a small graph.
     * Runtime: O(1).
     * @return a weighted_graph object.
     */
    private weighted_graph small_graph() {
        weighted_graph g0 = graph_creator(11,0,1);
        g0.connect(0,1,1);
        g0.connect(0,2,2);
        g0.connect(0,3,3);

        g0.connect(1,4,17);
        g0.connect(1,5,1);
        g0.connect(2,4,1);
        g0.connect(3, 5,10);
        g0.connect(3,6,100);
        g0.connect(5,7,1.1);
        g0.connect(6,7,10);
        g0.connect(7,10,2);
        g0.connect(6,8,30);
        g0.connect(8,10,10);
        g0.connect(4,10,30);
        g0.connect(3,9,10);
        g0.connect(8,10,10);

        return g0;
    }

    /**
     * This method was take from our Github repository, from the tests package.
     * This method creates and returns an weighted, undirected graph of size v_size,
     * with e_size edges, each one with a random number by seed
     * @param v_size - vertices number
     * @param e_size - edge number
     * @param seed - generating random number
     * @return weighted, undirected graph
     */
    public static weighted_graph graph_creator(int v_size, int e_size, int seed) {
        weighted_graph g = new WGraph_DS();

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
        return g;
    }

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