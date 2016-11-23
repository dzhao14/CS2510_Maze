import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

public class Examples {
    Node n0 = new Node(0, 0);
    Node n1 = new Node(0, 1);
    Node n2 = new Node(0, 2);
    Edge e0 = new Edge(n0, n1);
    Edge e1 = new Edge(n1, n2);
    ArrayList<Edge> el0;
    ArrayList<Edge> el1;
    MazeWorld mw;
    
    void init() {
        this.el0 = new ArrayList<Edge>();
        this.el0.add(this.e1);
        this.el0.add(this.e0);
        this.el1 = new ArrayList<Edge>();
        this.el1.add(this.e1);
        this.el1.add(this.e0);
        this.mw = new MazeWorld();
    }
    
    // test mergeSort for edges
    void testMergeSort(Tester t) {
        this.init();
        this.e0.weight = 0;
        this.e1.weight = 1;
        t.checkExpect(this.mw.sortEdgeHelp(this.el0), this.el1);
    }
}
