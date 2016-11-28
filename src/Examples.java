import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

public class Examples {
    Node n0;
    Node n1;
    Node n2;
    Edge e0;
    Edge e1;
    ArrayList<Edge> el0;
    ArrayList<Edge> el1;
    MazeWorld mw;
    
    void init() {
        this.n0 = new Node(0, 0);
        this.n1 = new Node(0, 1);
        this.n2 = new Node(0, 2);
        this.e0 = new Edge(n0, n1, 0);
        this.e1 = new Edge(n1, n2, 1);
        this.el0 = new ArrayList<Edge>();
        this.el0.add(this.e0);
        this.el0.add(this.e1);
        this.el1 = new ArrayList<Edge>();
        this.el1.add(this.e1);
        this.el1.add(this.e0);
        this.mw = new MazeWorld(false);
    }
    
    // test createNodeTable
    void testCreateNodeTable(Tester t) {
        this.init();
        ArrayList<ArrayList<Node>> table = this.mw.createNodeTable();
        for (int x = 0 ; x < this.mw.WIDTH ; x++) {
            for (int y = 0; y < this.mw.HEIGHT ; y++) {
                t.checkExpect(table.get(x).get(y).x, x);
                t.checkExpect(table.get(x).get(y).y, y);
            }
        }
    }
    
    // test linkNodeTable
    void testLinkNodeTable(Tester t) {
        this.init();
        ArrayList<ArrayList<Node>> table = this.mw.linkNodeTable(this.mw.createNodeTable());
        for (int x = 1 ; x < this.mw.WIDTH - 1; x++) {
            for (int y = 1 ; y < this.mw.HEIGHT - 1; y++) {
                t.checkExpect(table.get(x).get(y).edges.size(), 2);
            }
        }
    }
    
    // test sortEdges and its helper
    void testSortEdges(Tester t) {
        this.init();
        ArrayList<Edge> l = this.mw.sortEdges(this.mw.linkNodeTable(this.mw.createNodeTable()));
        for (int i = 1 ; i < l.size(); i++) {
            t.checkExpect(l.get(i - 1).weight <= l.get(i).weight, true);
        }
        t.checkNumRange(l.size(), this.mw.WIDTH * this.mw.HEIGHT, 
                this.mw.HEIGHT * this.mw.WIDTH * 2);
        t.checkExpect(this.mw.minimum(this.el0), this.e0);
        t.checkExpect(this.mw.minimum(this.el1), this.e0);
    }
    
    // test createMinSpanningTree and its helpers
    void testCreateMinSpanningTree(Tester t) {
        this.init();
        ArrayList<ArrayList<Node>> table = this.mw.linkNodeTable(this.mw.createNodeTable());
        ArrayList<Edge> sortedEdges = this.mw.sortEdges(table);
        ArrayList<Edge> out = this.mw.createMinSpanningTree(sortedEdges, table);
        t.checkExpect(out.size(), this.mw.WIDTH * this.mw.HEIGHT - 1);
    }
    
    // Test the game
    void testGame(Tester t) {
        MazeWorld mz = new MazeWorld();
        mz.bigBang((MazeWorld.WIDTH) * Node.CELL_SIZE, 
                (MazeWorld.HEIGHT) * Node.CELL_SIZE);
    }
}
