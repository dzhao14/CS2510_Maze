//CS2510 Fall 2016
//Assignment 11
//Zhao, David
//dzhao
//Carr, Kenneth "Theo"
//kcarr

// TO GENERATE MAZE: uncomment big-bang at the bottom of the Examples class

import java.util.ArrayList;
import java.util.HashMap;
import tester.*;
import java.util.Collection;

import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

public class Examples {
    Node n0;
    Node n1;
    Node n2;
    Node n3;
    Node n4;
    Edge e0;
    Edge e1;
    Edge e2;
    ArrayList<Edge> el0;
    ArrayList<Edge> el1;
    MazeWorld mw;
    HashMap<Node, Node> hash1;
    HashMap<Node, Node> hash2;
    Collection<Node> c;
    WorldScene bg;
    Player p;
    
    // initialize objects
    void init() {
        this.n0 = new Node(0, 0);
        this.n1 = new Node(0, 1);
        this.n2 = new Node(0, 2);
        this.n3 = new Node(MazeWorld.WIDTH - 1, MazeWorld.HEIGHT - 1);
        this.n4 = new Node(1, 2);
        this.e0 = new Edge(n0, n1, 0);
        this.e1 = new Edge(n1, n2, 1);
        this.e2 = new Edge(n2, n4, 5);
        this.el0 = new ArrayList<Edge>();
        this.el0.add(this.e0);
        this.el0.add(this.e1);
        this.el1 = new ArrayList<Edge>();
        this.el1.add(this.e1);
        this.el1.add(this.e0);
        this.mw = new MazeWorld(false);
        this.hash1 = new HashMap<Node, Node>();
        this.hash1.put(this.n0, this.n0);
        this.hash1.put(this.n1, this.n1);
        this.hash1.put(this.n2, this.n2);
        this.hash2 = new HashMap<Node, Node>();
        this.hash2.put(this.n0, this.n0);
        this.hash2.put(this.n0, this.n1);
        this.hash2.put(this.n0, this.n2);
        this.c = new ArrayList<Node>();
        this.bg = new WorldScene(MazeWorld.WIDTH * Node.CELL_SIZE, 
                MazeWorld.HEIGHT * Node.CELL_SIZE);
        this.p = new Player(this.n0);
        
    }
    
    
    // test createNodeTable
    void testCreateNodeTable(Tester t) {
        this.init();
        ArrayList<ArrayList<Node>> table = this.mw.createNodeTable();
        for (int x = 0 ; x < MazeWorld.WIDTH ; x++) {
            for (int y = 0; y < MazeWorld.HEIGHT ; y++) {
                t.checkExpect(table.get(x).get(y).x, x);
                t.checkExpect(table.get(x).get(y).y, y);
            }
        }
    }
    
    // test linkNodeTable
    void testLinkNodeTable(Tester t) {
        this.init();
        ArrayList<ArrayList<Node>> table = this.mw.linkNodeTable(this.mw.createNodeTable());
        for (int x = 1 ; x < MazeWorld.WIDTH - 1; x++) {
            for (int y = 1 ; y < MazeWorld.HEIGHT - 1; y++) {
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
    }
    
    // test createMinSpanningTree and its helpers
    void testCreateMinSpanningTree(Tester t) {
        this.init();
        ArrayList<ArrayList<Node>> table = this.mw.linkNodeTable(this.mw.createNodeTable());
        ArrayList<Edge> sortedEdges = this.mw.sortEdges(table);
        ArrayList<Edge> out = this.mw.createMinSpanningTree(sortedEdges, table);
        t.checkExpect(out.size(), MazeWorld.WIDTH * MazeWorld.HEIGHT - 1);
    }
    
    // test createMaze
    void testCreateMaze(Tester t) {
        this.init();
        t.checkExpect(this.mw.board, null);
        this.mw.createMaze();
        t.checkExpect(this.mw.board.size(), MazeWorld.HEIGHT * MazeWorld.WIDTH);
    }
    

    
    // test union
    void testUnion(Tester t) {
//        this.init();
//        this.mw.createMaze();
//        ArrayList<Node> ln = new ArrayList<Node>();
//        for (Node c : this.hash1.values()) {
//            ln.add(c);
//        }
//        t.checkExpect(ln.contains(this.n0), true);
//        t.checkExpect(ln.contains(this.n1), true);
//        t.checkExpect(ln.contains(this.n2), true);
//        
//        this.init();
//        this.mw.union(this.hash1, this.e0);
//        ln = new ArrayList<Node>();
//        for (Node c : this.hash1.values()) {
//            ln.add(c);
//        }
//        t.checkExpect(ln.contains(this.n1), false);
//        
//        this.mw.union(this.hash1, this.e1);
//        ln = new ArrayList<Node>();
//        for (Node c : this.hash1.values()) {
//            ln.add(c);
//        }
//        t.checkExpect(ln.contains(this.n2), false);
    }
    
    
    


    // test table2List
    void testTable2List(Tester t) {
        this.init();
        ArrayList<ArrayList<Node>> table = new ArrayList<ArrayList<Node>>();
        for (int x = 0 ; x < MazeWorld.WIDTH ; x++) {
            table.add(new ArrayList<Node>());
            for (int y = 0 ; y < MazeWorld.HEIGHT ; y++) {
                table.get(x).add(new Node(x, y));
            }
        }
        t.checkExpect(table.size(), MazeWorld.WIDTH);
        t.checkExpect(table.get(1).size(), MazeWorld.HEIGHT);
        t.checkExpect(this.mw.table2List(table).size(), MazeWorld.WIDTH * MazeWorld.HEIGHT);
    }
    
    // test makeScene
    void testMakeScene(Tester t) {
        this.init();
        this.mw.createMaze();
        WorldScene bg = new WorldScene(MazeWorld.WIDTH * Node.CELL_SIZE, 
                MazeWorld.HEIGHT * Node.CELL_SIZE);
        for (Node n : mw.board) {
            n.drawNode(bg);
        }
        t.checkExpect(this.mw.makeScene().height, MazeWorld.HEIGHT * Node.CELL_SIZE);
        t.checkExpect(this.mw.makeScene().width, MazeWorld.WIDTH * Node.CELL_SIZE);
    }
    
    // test drawNode
    void testDrawNode(Tester t) {
        this.init();
        WorldScene img = new WorldScene(MazeWorld.WIDTH * Node.CELL_SIZE, 
                MazeWorld.HEIGHT * Node.CELL_SIZE);
        img.placeImageXY(n0.determineRect(),
                n0.x * Node.CELL_SIZE + Node.CELL_SIZE / 2, 
                n0.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);
        img.placeImageXY(new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, 
                OutlineMode.OUTLINE, Color.BLACK), 
                n0.x * Node.CELL_SIZE + Node.CELL_SIZE / 2, 
                n0.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);

        for (Edge e : n0.edges) {
            img.placeImageXY(n0.determineEdge(e),
                    Node.CELL_SIZE * (e.from.x + e.to.x) / 2 + Node.CELL_SIZE / 2,
                    Node.CELL_SIZE * (e.from.y + e.to.y) / 2 + Node.CELL_SIZE / 2);
        }
        
        t.checkExpect(this.bg, new WorldScene(MazeWorld.WIDTH * Node.CELL_SIZE, 
                MazeWorld.HEIGHT * Node.CELL_SIZE));
        this.n0.drawNode(this.bg);
        t.checkExpect(this.bg, img);
        
    }
    
    
    // test determineEdge
    void testDetermineEdge(Tester t) {
        t.checkExpect(this.n4.determineEdge(this.e2),
                new LineImage(new Posn(0, Node.CELL_SIZE - 2), Color.WHITE));
        t.checkExpect(this.n0.determineEdge(this.e0),
                new LineImage(new Posn(Node.CELL_SIZE - 2, 0), Color.WHITE));
    }
    
    // test determineRect
    void testDetermineRect(Tester t) {
        t.checkExpect(this.n0.determineRect(), new RectangleImage(Node.CELL_SIZE,
                Node.CELL_SIZE, OutlineMode.SOLID, Color.BLUE));
        t.checkExpect(this.n1.determineRect(), new RectangleImage(Node.CELL_SIZE, 
                Node.CELL_SIZE, OutlineMode.OUTLINE, Color.BLACK));
        t.checkExpect(this.n3.determineRect(), new RectangleImage(Node.CELL_SIZE, 
                Node.CELL_SIZE, OutlineMode.SOLID, Color.GREEN));
    }
    
    // test Player's canmove functions
    void testCanMove(Tester t) {
        this.init();
        t.checkExpect(this.p.canMoveDown(), false);
        t.checkExpect(this.p.canMoveUp(), false);
        t.checkExpect(this.p.canMoveLeft(), false);
        t.checkExpect(this.p.canMoveRight(), false);
        this.n0.edges.add(this.e0);
        t.checkExpect(this.p.canMoveDown(), true);
        this.n0.edges.add(new Edge(n0, new Node(0, -1)));
        t.checkExpect(this.p.canMoveUp(), true);
        this.n0.edges.add(new Edge(n0, new Node(1, 0)));
        t.checkExpect(this.p.canMoveRight(), true);
        this.n0.edges.add(new Edge(n0, new Node(-1, 0)));
        t.checkExpect(this.p.canMoveLeft(), true);
        this.n0.edges.remove(0);
        this.n0.edges.add(this.e0);
        t.checkExpect(this.p.canMoveDown(), true);
    }
    
    
    // Test the game
    void testGame(Tester t) {
        MazeWorld mz = new MazeWorld();
        mz.bigBang((MazeWorld.WIDTH) * Node.CELL_SIZE, 
                (MazeWorld.HEIGHT) * Node.CELL_SIZE, .1);
    }
}
//