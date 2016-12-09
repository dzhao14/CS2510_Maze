//CS2510 Fall 2016
//Assignment 11
//Zhao, David
//dzhao
//Carr, Kenneth "Theo"
//kcarr

// TO GENERATE MAZE: uncomment big-bang at the bottom of the Examples class

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

import tester.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;

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
        for (int x = 0; x < MazeWorld.WIDTH; x++) {
            for (int y = 0; y < MazeWorld.HEIGHT; y++) {
                t.checkExpect(table.get(x).get(y).x, x);
                t.checkExpect(table.get(x).get(y).y, y);
            }
        }
    }

    // test linkNodeTable
    void testLinkNodeTable(Tester t) {
        this.init();
        ArrayList<ArrayList<Node>> table = this.mw.linkNodeTable(this.mw.createNodeTable());
        for (int x = 1; x < MazeWorld.WIDTH - 1; x++) {
            for (int y = 1; y < MazeWorld.HEIGHT - 1; y++) {
                t.checkExpect(table.get(x).get(y).edges.size(), 2);
            }
        }
    }

    // test sortEdges and its helper
    void testSortEdges(Tester t) {
        this.init();
        ArrayList<Edge> l = this.mw.sortEdges(this.mw.linkNodeTable(this.mw.createNodeTable()));
        for (int i = 1; i < l.size(); i++) {
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
        this.init();
        this.mw.createMaze();
        ArrayList<Node> ln = new ArrayList<Node>();
        for (Node c : this.hash1.values()) {
            ln.add(c);
        }
        t.checkExpect(ln.contains(this.n0), true);
        t.checkExpect(ln.contains(this.n1), true);
        t.checkExpect(ln.contains(this.n2), true);

        this.init();
        this.mw.union(this.hash1, this.e0);
        ln = new ArrayList<Node>();
        for (Node c : this.hash1.values()) {
            ln.add(c);
        }
        t.checkExpect(ln.contains(this.n1), false);

        this.mw.union(this.hash1, this.e1);
        ln = new ArrayList<Node>();
        for (Node c : this.hash1.values()) {
            ln.add(c);
        }
        t.checkExpect(ln.contains(this.n2), false);
    }

    // more examples
    Node node00;;
    Node node10;
    Node node01;
    Node node11;
    Node node20;
    Node node21;
    Node node22;
    Node node12;
    Node node02;
    ArrayList<Node> tempBoard;

    // another initializer
    void initNodes() {
        this.node00 = new Node(0, 0);
        this.node10 = new Node(1, 0);
        this.node01 = new Node(0, 1);
        this.node11 = new Node(1, 1);
        this.node20 = new Node(2, 0);
        this.node21 = new Node(2, 1);
        this.node22 = new Node(2, 2);
        this.node12 = new Node(1, 2);
        this.node02 = new Node(0, 2);
    }

    // test determineHead
    void testDetermineHead(Tester t) {
        this.initNodes();
        MazeWorld world1 = new MazeWorld(false);
        HashMap<Node, Node> hasher = new HashMap<Node, Node>();
        hasher.put(this.node00, this.node00);
        hasher.put(this.node10, this.node11);
        hasher.put(this.node01, this.node11);
        hasher.put(this.node11, this.node11);
        t.checkExpect(world1.determineHead(hasher, node11), node11);
        t.checkExpect(world1.determineHead(hasher, node01), node11);
        t.checkExpect(world1.determineHead(hasher, node00), node00);
        t.checkExpect(world1.determineHead(hasher, node10), node11);

    }

    // test updateNodeEdges
    void testUpdateNodeEdges(Tester t) {
        MazeWorld world = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world.createNodeTable();
        ArrayList<ArrayList<Node>> table1 = world.linkNodeTable(table);
        ArrayList<Edge> l = new ArrayList<Edge>();
        for (int x = 0; x < MazeWorld.WIDTH; x++) {
            for (int y = 0; y < MazeWorld.HEIGHT; y++) {
                for (Edge e : table1.get(x).get(y).edges) {
                    l.add(e);
                }
            }
        }
        ArrayList<Node> out = world.updateNodeEdges(table, l);
        t.checkExpect(out.get(0).edges.size(), 2);
        t.checkExpect(out.get(1).edges.size(), 3);
        t.checkExpect(out.get(MazeWorld.HEIGHT * 2 + 1).edges.size(), 4);

    }

    // test table2List
    void testTable2List(Tester t) {
        this.init();
        ArrayList<ArrayList<Node>> table = new ArrayList<ArrayList<Node>>();
        for (int x = 0; x < MazeWorld.WIDTH; x++) {
            table.add(new ArrayList<Node>());
            for (int y = 0; y < MazeWorld.HEIGHT; y++) {
                table.get(x).add(new Node(x, y));
            }
        }
        t.checkExpect(table.size(), MazeWorld.WIDTH);
        t.checkExpect(table.get(1).size(), MazeWorld.HEIGHT);
        t.checkExpect(this.mw.table2List(table).size(), MazeWorld.WIDTH * MazeWorld.HEIGHT);
    }

    // test createPlayer
    void testCreatePlayer(Tester t) {
        this.init();
        MazeWorld world1 = new MazeWorld(false);
        t.checkExpect(world1.player, null);
        world1.board = new ArrayList<Node>();
        world1.board.add(n0);
        world1.createPlayer();
        t.checkExpect(world1.player, new Player(n0));
    }

    // test movePlayerDown
    void testMovePlayerDown(Tester t) {

        MazeWorld world1 = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world1.createNodeTable();
        ArrayList<Node> list = world1.table2List(table);
        world1.board = list;

        world1.player = new Player(world1.board.get(0));
        world1.player.x = 0;
        world1.player.y = 0;

        t.checkExpect(world1.player.on, world1.board.get(0));
        t.checkExpect(world1.player.y, 0);
        t.checkExpect(world1.board.get(1).seen, false);
        t.checkExpect(world1.seen.contains(world1.board.get(1)), false);

        world1.movePlayerDown();
        t.checkExpect(world1.player.on, world1.board.get(1));
        t.checkExpect(world1.player.y, 1);
        t.checkExpect(world1.board.get(1).seen, true);
        t.checkExpect(world1.seen.contains(world1.board.get(1)), true);

        world1.wl.add(world1.board.get(3));
        world1.movePlayerDown();
        t.checkExpect(world1.player.on, world1.board.get(1));
        t.checkExpect(world1.player.y, 1);
        t.checkExpect(world1.board.get(1).seen, true);
        t.checkExpect(world1.seen.contains(world1.board.get(1)), true);

    }

    // test movePlayerUp
    void testMovePlayerUp(Tester t) {
        MazeWorld world1 = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world1.createNodeTable();
        ArrayList<Node> list = world1.table2List(table);
        world1.board = list;

        world1.player = new Player(world1.board.get(1));
        world1.player.x = 0;
        world1.player.y = 1;

        t.checkExpect(world1.player.on, world1.board.get(1));
        t.checkExpect(world1.player.y, 1);
        t.checkExpect(world1.board.get(0).seen, false);
        t.checkExpect(world1.seen.contains(world1.board.get(0)), false);

        world1.movePlayerUp();
        t.checkExpect(world1.player.on, world1.board.get(0));
        t.checkExpect(world1.player.y, 0);
        t.checkExpect(world1.board.get(0).seen, true);
        t.checkExpect(world1.seen.contains(world1.board.get(0)), true);

        world1.wl.add(world1.board.get(3));
        world1.movePlayerUp();
        t.checkExpect(world1.player.on, world1.board.get(0));
        t.checkExpect(world1.player.y, 0);
        t.checkExpect(world1.board.get(0).seen, true);
        t.checkExpect(world1.seen.contains(world1.board.get(0)), true);
    }

    // test movePlayerLeft
    void testMovePlayerLeft(Tester t) {
        MazeWorld world1 = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world1.createNodeTable();
        ArrayList<Node> list = world1.table2List(table);
        world1.board = list;

        world1.player = new Player(world1.board.get(MazeWorld.HEIGHT));
        world1.player.x = 1;
        world1.player.y = 0;

        t.checkExpect(world1.player.on, world1.board.get(MazeWorld.HEIGHT));
        t.checkExpect(world1.player.x, 1);
        t.checkExpect(world1.board.get(0).seen, false);
        t.checkExpect(world1.seen.contains(world1.board.get(0)), false);

        world1.movePlayerLeft();
        t.checkExpect(world1.player.on, world1.board.get(0));
        t.checkExpect(world1.player.x, 0);
        t.checkExpect(world1.board.get(0).seen, true);
        t.checkExpect(world1.seen.contains(world1.board.get(0)), true);

        world1.wl.add(world1.board.get(3));
        world1.movePlayerLeft();
        t.checkExpect(world1.player.on, world1.board.get(0));
        t.checkExpect(world1.player.x, 0);
        t.checkExpect(world1.board.get(0).seen, true);
        t.checkExpect(world1.seen.contains(world1.board.get(0)), true);
    }

    // test movePlayerRight
    void testMovePlayerRight(Tester t) {
        MazeWorld world1 = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world1.createNodeTable();
        ArrayList<Node> list = world1.table2List(table);
        world1.board = list;

        world1.player = new Player(world1.board.get(0));
        world1.player.x = 0;
        world1.player.y = 0;

        t.checkExpect(world1.player.on, world1.board.get(0));
        t.checkExpect(world1.player.x, 0);
        t.checkExpect(world1.board.get(MazeWorld.HEIGHT).seen, false);
        t.checkExpect(world1.seen.contains(world1.board.get(MazeWorld.HEIGHT)), false);

        world1.movePlayerRight();
        t.checkExpect(world1.player.on, world1.board.get(MazeWorld.HEIGHT));
        t.checkExpect(world1.player.x, 1);
        t.checkExpect(world1.board.get(MazeWorld.HEIGHT).seen, true);
        t.checkExpect(world1.seen.contains(world1.board.get(MazeWorld.HEIGHT)), true);

        world1.wl.add(world1.board.get(3));
        world1.movePlayerRight();
        t.checkExpect(world1.player.on, world1.board.get(MazeWorld.HEIGHT));
        t.checkExpect(world1.player.x, 1);
        t.checkExpect(world1.board.get(MazeWorld.HEIGHT).seen, true);
        t.checkExpect(world1.seen.contains(world1.board.get(MazeWorld.HEIGHT)), true);
    }

    // test slowGraphSearch
    void testSlowGraphSearch(Tester t) {
        MazeWorld world = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world.createNodeTable();
        ArrayList<Node> list = world.table2List(table);
        world.board = list;
        t.checkExpect(world.wl.contains(world.board.get(0)), false);
        world.slowGraphSearch();
        t.checkExpect(world.wl.contains(world.board.get(0)), true);
    }

    // test slowGraphSearchHelp
    void testSlowGraphSearchHelp(Tester t) {
        MazeWorld world = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world.createNodeTable();
        ArrayList<Node> list = world.table2List(table);
        world.board = list;
        t.checkExpect(world.slowGraphSearchHelp(), false);
        world.slowGraphSearch();
        world.wl.addFirst(new Node(MazeWorld.WIDTH - 1, MazeWorld.HEIGHT - 1));
        t.checkExpect(world.slowGraphSearchHelp(), true);

    }

    // GraphTraverseType Examples
    GraphTraverseType<Node> bfsNode = new BFS<Node>();
    GraphTraverseType<Node> dfsNode = new DFS<Node>();
    
    // test determineSolution
    void testDetermineSolution(Tester t) {
        MazeWorld world = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world.linkNodeTable(world.createNodeTable());
        ArrayList<Edge> sortedEdges = world.sortEdges(table);
        ArrayList<Edge> spanningTreeEdges = world.createMinSpanningTree(sortedEdges, table);
        world.board = world.updateNodeEdges(table, spanningTreeEdges);
        ArrayList<Node> tempSeen = new ArrayList<Node>();
        tempSeen.add(world.board.get(0));
        world.seen = tempSeen;
        
        t.checkExpect(world.rightMoves, 0);
        t.checkExpect(world.wrongMoves, 0);
        world.determineSolution();
        t.checkExpect(world.rightMoves, 1);
        t.checkExpect(world.wrongMoves, 0);
        
        tempSeen.add(this.n0);
        world.rightMoves = 0;
        world.wrongMoves = 0;
        t.checkExpect(world.rightMoves, 0);
        t.checkExpect(world.wrongMoves, 0);
        world.determineSolution();
        t.checkExpect(world.rightMoves, 1);
        t.checkExpect(world.wrongMoves, 1);        
    }

    // test fastGraphSearch
    void testFastGraphSearch(Tester t) {
        MazeWorld world = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world.linkNodeTable(world.createNodeTable());
        ArrayList<Edge> sortedEdges = world.sortEdges(table);
        ArrayList<Edge> spanningTreeEdges = world.createMinSpanningTree(sortedEdges, table);
        world.board = world.updateNodeEdges(table, spanningTreeEdges);
        ArrayList<Node> tempSeen = new ArrayList<Node>();
        tempSeen.add(world.board.get(0));
        t.checkExpect(world.fastGraphSearch(this.n0, new ArrayList<Node>(), this.bfsNode), false);
        t.checkExpect(
                world.fastGraphSearch(world.board.get(0), new ArrayList<Node>(), this.bfsNode),
                true);
        t.checkExpect(world.fastGraphSearch(this.n0, tempSeen, this.bfsNode), false);
        t.checkExpect(world.fastGraphSearch(world.board.get(0), tempSeen, this.bfsNode), false);

    }

    // test toggleColors
    void testToggleColors(Tester t) {
        MazeWorld world = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world.createNodeTable();
        ArrayList<Node> list = world.table2List(table);
        world.board = list;
        t.checkExpect(world.board.get(1).isHidden, false);
        world.toggleColors();
        t.checkExpect(world.board.get(1).isHidden, true);
    }

    // test toggleScore
    void testToggleScore(Tester t) {
        MazeWorld world = new MazeWorld(false);
        t.checkExpect(world.showScore, false);
        world.toggleScore();
        t.checkExpect(world.showScore, true);
    }

    // test drawEnd
    void testDrawEnd(Tester t) {
        MazeWorld world = new MazeWorld(false);
        world.rightMoves = 29;
        world.wrongMoves = 15;
        WorldScene testScene = new WorldScene(MazeWorld.WIDTH * Node.CELL_SIZE,
                MazeWorld.HEIGHT * Node.CELL_SIZE);
        WorldScene scene = new WorldScene(MazeWorld.WIDTH * Node.CELL_SIZE,
                MazeWorld.HEIGHT * Node.CELL_SIZE);
        scene.placeImageXY(
                new RectangleImage(MazeWorld.WIDTH * Node.CELL_SIZE,
                        MazeWorld.HEIGHT * Node.CELL_SIZE, OutlineMode.SOLID, Color.WHITE),
                MazeWorld.WIDTH * Node.CELL_SIZE / 2, MazeWorld.HEIGHT * Node.CELL_SIZE / 2);
        scene.placeImageXY(
                new TextImage("Nice job!", MazeWorld.HEIGHT * Node.CELL_SIZE / 8, Color.RED),
                MazeWorld.WIDTH * Node.CELL_SIZE / 2, MazeWorld.HEIGHT * Node.CELL_SIZE / 8);
        scene.placeImageXY(
                new TextImage("Took " + (world.rightMoves + world.wrongMoves) + " moves",
                        MazeWorld.HEIGHT * Node.CELL_SIZE / 8, Color.RED),
                MazeWorld.WIDTH * Node.CELL_SIZE / 2, MazeWorld.HEIGHT * Node.CELL_SIZE / 2);
        scene.placeImageXY(
                new TextImage(world.wrongMoves + " Wrong moves",
                        MazeWorld.HEIGHT * Node.CELL_SIZE / 8, Color.RED),
                MazeWorld.WIDTH * Node.CELL_SIZE / 2, MazeWorld.HEIGHT * Node.CELL_SIZE / 4 * 3);
        world.drawEnd(testScene);
        t.checkExpect(testScene, testScene);
    }

    // test onTick
    void testOnTick(Tester t) {
        MazeWorld world = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world.createNodeTable();
        ArrayList<Node> list = world.table2List(table);
        world.board = list;
        world.wl.add(world.board.get(0));
        t.checkExpect(world.wl.size(), 1);
        t.checkExpect(world.slowGraphSearchHelp(), false);

        world.onTick();

        t.checkExpect(world.wl.size(), 0);
        world.wl.add(world.board.get(1));
        t.checkExpect(world.gameStop, false);
        t.checkExpect(world.showScore, false);
        t.checkExpect(world.wl.contains(world.board.get(0)), false);
        world.board.get(MazeWorld.WIDTH * MazeWorld.HEIGHT - 1).seen = true;

        world.onTick();

        t.checkExpect(world.gameStop, true);
        t.checkExpect(world.showScore, true);
        t.checkExpect(world.wl, new ArrayDeque<Node>());

    }

    // test onKeyEvent
    void testOnKeyEvent(Tester t) {
        MazeWorld world = new MazeWorld(false);
        ArrayList<ArrayList<Node>> table = world.linkNodeTable(world.createNodeTable());
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (int x = 0; x < MazeWorld.WIDTH; x++) {
            for (int y = 0; y < MazeWorld.HEIGHT; y++) {
                for (Edge e : table.get(x).get(y).edges) {
                    edges.add(e);
                }
            }
        }
        ArrayList<Node> tableLinked = world.updateNodeEdges(table, edges);
        world.board = tableLinked;
        world.player = new Player(world.board.get(MazeWorld.HEIGHT));
        world.player.x = 1;
        world.onKeyEvent("q");
        t.checkExpect(world.player.x, 1);
        world.onKeyEvent("left");
        t.checkExpect(world.player.x, 0);
        
        world.player = new Player(world.board.get(0));
        world.player.y = 0;
        world.onKeyEvent("q");
        t.checkExpect(world.player.y, 0);
        world.onKeyEvent("down");
        t.checkExpect(world.player.y, 1);
        
        world.player = new Player(world.board.get(1));
        world.player.y = 1;
        world.onKeyEvent("q");
        t.checkExpect(world.player.y, 1);
        world.onKeyEvent("up");
        t.checkExpect(world.player.y, 0);
        
        world.player = new Player(world.board.get(0));
        world.player.x = 0;
        world.onKeyEvent("q");
        t.checkExpect(world.player.x, 0);
        world.onKeyEvent("right");
        t.checkExpect(world.player.x, 1);
        
        t.checkExpect(world.func, this.dfsNode);
        world.onKeyEvent("q");
        t.checkExpect(world.func, this.dfsNode);
        world.onKeyEvent("b");
        t.checkExpect(world.func, this.bfsNode);
        
        world.onKeyEvent("q");
        t.checkExpect(world.func, this.bfsNode);
        world.onKeyEvent("d");
        t.checkExpect(world.func, this.dfsNode);
        
        t.checkExpect(world.board.get(1).isHidden, false);
        world.onKeyEvent("q");
        t.checkExpect(world.board.get(1).isHidden, false);
        world.onKeyEvent("h");
        t.checkExpect(world.board.get(1).isHidden, true);
        
        t.checkExpect(world.showScore, false);
        world.onKeyEvent("q");
        t.checkExpect(world.showScore, false);
        world.onKeyEvent("j");
        t.checkExpect(world.showScore, true);


        
        
    }

    // Deque examples to use for testing below
    Deque<Integer> deque;
    Deque<Node> dequeNode;

    // initializer for add/remove testing
    void initDeque() {
        this.deque = new ArrayDeque<Integer>();
        this.dequeNode = new ArrayDeque<Node>();
        deque.add(1);
        deque.addLast(2);
        deque.addLast(3);
    }

    // GraphTraverseType examples
    BFS<Integer> bfs = new BFS<Integer>();
    DFS<Integer> dfs = new DFS<Integer>();

    // test add/remove (for BFS)
    void testAddRemoveBFS(Tester t) {
        this.initDeque();
        t.checkExpect(this.deque.getFirst(), 1);
        t.checkExpect(this.deque.getLast(), 3);
        this.bfs.add(this.deque, 9);
        t.checkExpect(this.deque.getFirst(), 9);
        t.checkExpect(this.deque.getLast(), 3);
        t.checkExpect(this.bfs.remove(this.deque), 3);
        t.checkExpect(this.deque.getFirst(), 9);
        t.checkExpect(this.deque.getLast(), 2);
    }

    // test add/remove (for DFS)
    void testAddRemoveDFS(Tester t) {
        this.initDeque();
        t.checkExpect(this.deque.getFirst(), 1);
        t.checkExpect(this.deque.getLast(), 3);
        this.dfs.add(this.deque, 9);
        t.checkExpect(this.deque.getFirst(), 9);
        t.checkExpect(this.deque.getLast(), 3);
        t.checkExpect(this.dfs.remove(this.deque), 9);
        t.checkExpect(this.deque.getFirst(), 1);
        t.checkExpect(this.deque.getLast(), 3);
    }

    // test drawPlayer
    void testDrawPlayer(Tester t) {
        this.init();
        WorldScene background = new WorldScene(Node.CELL_SIZE * 2, Node.CELL_SIZE * 2);
        WorldScene image = new WorldScene(Node.CELL_SIZE * 2, Node.CELL_SIZE * 2);
        image.placeImageXY(new CircleImage(Node.CELL_SIZE * 3 / 10, OutlineMode.SOLID, Color.RED),
                this.p.x * Node.CELL_SIZE + Node.CELL_SIZE / 2,
                this.p.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);
        t.checkExpect(background, new WorldScene(Node.CELL_SIZE * 2, Node.CELL_SIZE * 2));
        p.drawPlayer(background);
        t.checkExpect(background, image);
    }

    // test hasPlayerWon
    void testHasPlayerWon(Tester t) {
        this.init();
        t.checkExpect(this.p.hasPlayerWon(), false);
        p.x = MazeWorld.WIDTH - 1;
        p.y = MazeWorld.HEIGHT - 1;
        t.checkExpect(this.p.hasPlayerWon(), true);
    }

    //////////////// HeapSort Method testing ///////////////////

    // another initializer
    ArrayList<Integer> l;
    HeapSort<Integer> hs = new HeapSort<Integer>();
    Comparator<Integer> intComp = new CompareInt();
    Comparator<Edge> edgeComp = new CompareEdges();

    void init2() {
        this.l = new ArrayList<Integer>();
        this.hs = new HeapSort<Integer>();
    }

    // test heapSort
    void testHeapSort(Tester t) {
        this.init2();
        this.l.add(7);
        this.l.add(8);
        this.l.add(6);
        this.l.add(4);
        this.l.add(9);
        this.l.add(3);
        t.checkExpect(l.get(0), 7);
        t.checkExpect(l.get(1), 8);
        t.checkExpect(l.get(4), 9);
        t.checkExpect(hs.heapSort(l, this.intComp).get(0), 3);
        t.checkExpect(hs.heapSort(l, this.intComp).get(1), 4);
        t.checkExpect(hs.heapSort(l, this.intComp).get(2), 6);
        t.checkExpect(hs.heapSort(l, this.intComp).get(3), 7);
        t.checkExpect(hs.heapSort(l, this.intComp).get(4), 8);
        t.checkExpect(hs.heapSort(l, this.intComp).get(5), 9);
    }

    // test buildHeap
    void testBuildHeap(Tester t) {
        this.init2();
        this.l.add(7);
        this.l.add(8);
        this.l.add(6);
        this.l.add(4);
        this.l.add(9);
        this.l.add(3);
        t.checkExpect(l.get(0), 7);
        t.checkExpect(l.get(1), 8);
        t.checkExpect(l.get(4), 9);
        hs.buildHeap(l, this.intComp);
        t.checkExpect(l.get(0), 9);
        t.checkExpect(l.get(1), 8);
        t.checkExpect(l.get(2), 6);
        t.checkExpect(l.get(3), 4);
        t.checkExpect(l.get(4), 7);
        t.checkExpect(l.get(5), 3);
    }

    // test swap
    void testSwap(Tester t) {
        this.init2();
        for (int i = 0; i < 6; i++) {
            l.add(i);
        }
        t.checkExpect(l.get(2), 2);
        t.checkExpect(l.get(5), 5);
        this.hs.swap(l, 2, 5);
        t.checkExpect(l.get(2), 5);
        t.checkExpect(l.get(5), 2);
    }

    // test downHeap
    void testDownHeap(Tester t) {
        this.init2();
        this.l.add(7);
        this.l.add(8);
        this.l.add(6);
        this.l.add(4);
        this.l.add(9);
        this.l.add(3);
        t.checkExpect(l.get(0), 7);
        t.checkExpect(l.get(1), 8);
        t.checkExpect(l.get(4), 9);
        this.hs.downHeap(0, l, this.intComp, l.size());
        t.checkExpect(l.get(0), 8);
        t.checkExpect(l.get(1), 9);
        t.checkExpect(l.get(2), 6);
        t.checkExpect(l.get(3), 4);
        t.checkExpect(l.get(4), 7);
        t.checkExpect(l.get(5), 3);
    }

    // test heapOrder
    void testOrderHeap(Tester t) {
        this.init();
        this.init2();
        this.l.add(7);
        this.l.add(8);
        this.l.add(6);
        this.l.add(4);
        this.l.add(9);
        this.l.add(3);
        hs.buildHeap(l, this.intComp);
        t.checkExpect(l.get(0), 9);
        t.checkExpect(l.get(1), 8);
        t.checkExpect(l.get(2), 6);
        t.checkExpect(l.get(3), 4);
        t.checkExpect(l.get(4), 7);
        t.checkExpect(l.get(5), 3);
        hs.heapOrder(l, this.intComp);
        t.checkExpect(l.get(0), 3);
        t.checkExpect(l.get(1), 4);
        t.checkExpect(l.get(2), 6);
        t.checkExpect(l.get(3), 7);
        t.checkExpect(l.get(4), 8);
        t.checkExpect(l.get(5), 9);
    }

    // test compare (in Comparator interface)
    void testCompare(Tester t) {
        this.init();
        t.checkExpect(this.intComp.compare(5, 6), -1);
        t.checkExpect(this.intComp.compare(6, 6), 0);
        t.checkExpect(this.intComp.compare(7, 6), 1);
        t.checkExpect(this.edgeComp.compare(e1, e0), 1);
        t.checkExpect(this.edgeComp.compare(e0, e1), -1);
        t.checkExpect(this.edgeComp.compare(e0, e0), 0);
    }

    ////////// More MazeWorld testing ///////////
    // testing makeScene
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
        img.placeImageXY(n0.determineRect(), n0.x * Node.CELL_SIZE + Node.CELL_SIZE / 2,
                n0.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);
        img.placeImageXY(
                new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, OutlineMode.OUTLINE,
                        Color.BLACK),
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
        t.checkExpect(this.n0.determineRect(),
                new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, OutlineMode.SOLID, Color.BLUE));
        t.checkExpect(this.n1.determineRect(), new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE,
                OutlineMode.OUTLINE, Color.BLACK));
        t.checkExpect(this.n3.determineRect(),
                new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, OutlineMode.SOLID, Color.GREEN));
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

    // // Test the game
    // void testGame(Tester t) {
    // MazeWorld mz = new MazeWorld();
    // mz.bigBang((MazeWorld.WIDTH) * Node.CELL_SIZE,
    // (MazeWorld.HEIGHT) * Node.CELL_SIZE, .1);
    // }
}