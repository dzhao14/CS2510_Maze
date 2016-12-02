//CS2510 Fall 2016
//Assignment 11
//Zhao, David
//dzhao
//Carr, Kenneth "Theo"
//kcarr

// TO GENERATE MAZE: uncomment big-bang at the bottom of the Examples class

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;

import javalib.impworld.*;


// Represents a maze
class MazeWorld extends World {
    
    // the total height of the maze in pixels 
    static final int HEIGHT = 10;
    // the total width of the maze in pixels
    static final int WIDTH = 10;
    
    //A list of nodes that represent the maze
    ArrayList<Node> board;
    
    //Stores the player for this maze
    Player player;
    
    MazeWorld() {
        this.createMaze();
        this.createPlayer();
    }
    
    // Testing constructor
    MazeWorld(boolean b) {
        // this constructor is used for testing, to avoid generating a new maze each time
    }
    
    // create an table of unconnected nodes
    ArrayList<ArrayList<Node>> createNodeTable() {
        ArrayList<ArrayList<Node>> table = new ArrayList<ArrayList<Node>>();
        
        for (int x = 0 ; x < MazeWorld.WIDTH ; x++) {
            table.add(new ArrayList<Node>());
            for (int y = 0 ; y < MazeWorld.HEIGHT ; y++) {
                table.get(x).add(new Node(x, y));
            }
        }
        return table;
    }
    
    // Link each node with its adjacent neighbor
    ArrayList<ArrayList<Node>> linkNodeTable(ArrayList<ArrayList<Node>> table) {
        for (int x = 0 ; x < MazeWorld.WIDTH ; x++) {
            for (int y = 0 ; y < MazeWorld.HEIGHT ; y++) {
                if (x != 0) {
                    table.get(x).get(y).edges.add(new Edge(table.get(x).get(y),
                            table.get(x - 1).get(y)));
                }
                if (y != MazeWorld.HEIGHT - 1) {
                    table.get(x).get(y).edges.add(new Edge(table.get(x).get(y), 
                            table.get(x).get(y + 1)));
                }            
            }
        }
        return table;
    }
    
    // return an ArrayList of edges ordered by weight from least to greatest
    ArrayList<Edge> sortEdges(ArrayList<ArrayList<Node>> table) {
        ArrayList<Edge> l = new ArrayList<Edge>();
        for (int x = 0 ; x < MazeWorld.WIDTH ; x++) {
            for (int y = 0 ; y < MazeWorld.HEIGHT ; y++) {
                for (Edge e : table.get(x).get(y).edges) {
                    l.add(e);
                }
            }
        }
        int size = l.size();
        ArrayList<Edge> out = new ArrayList<Edge>();
        for (int i = 0 ; i < size ; i++) {
            out.add(this.minimum(l));
        }
        
        return out;
    }
    

    
    // Returns the edge with the smallest weight
    // EFFECT: removes the smallest weight edge from the list 
    Edge minimum(ArrayList<Edge> l) {
        if (l.size() == 0) {
            throw new RuntimeException("Can't get min of empty list");
        }
        else {
            int min = 0;
            for (int i = 0 ; i < l.size() ; i++) {
                if (l.get(i).weight < l.get(min).weight) {
                    min = i;
                }
            }
            return l.remove(min);
        }
    }
    
    // create a minimum spanning tree from the given sorted list of edges
    ArrayList<Edge> createMinSpanningTree(ArrayList<Edge> l, ArrayList<ArrayList<Node>> table) {
        HashMap<Node, Node> representatives = new HashMap<Node, Node>();
        for (int x = 0 ; x < MazeWorld.WIDTH ; x++) {
            for (int y = 0 ; y < MazeWorld.HEIGHT ; y++) {
                representatives.put(table.get(x).get(y), table.get(x).get(y));
            }
        }
        
        ArrayList<Edge> out = new ArrayList<Edge>();
        
        while (!this.spanningTreeDone(representatives)) {
            Edge e = l.get(0);
            if (representatives.get(e.from) == representatives.get(e.to)) {
                l.remove(0);
            }
            else {
                out.add(e);
                this.union(representatives, e);
                l.remove(0);
            }
        }
        return out;
    }
    
    // check if this spanning tree is complete
    boolean spanningTreeDone(HashMap<Node, Node> map) {
        Collection<Node> c = map.values();
        ArrayList<Node> ln = new ArrayList<Node>();
        for (Node n : c) {
            ln.add(n);
        }
        Node n0 = ln.get(0);
        int size = ln.size();
        for (int i = 0 ; i < size; i++) {
            if (ln.get(0) == n0) {
                ln.remove(0);
            }
            else {
                return false;
            }
        }
        return true;
    }
    
    // update the hashmap once a connection between two nodes is made
    void union(HashMap<Node, Node> map, Edge e) {
        Node head = map.get(e.from);
        Node otherHead = map.get(e.to);
        for (Node key : map.keySet()) {
            if (map.get(key) == otherHead) {
                map.put(key, head);
            }
        }
    }
    
    // create a table of nodes that represent the maze
    // EFFECT: assign the this.board field
    void createMaze() {
        ArrayList<ArrayList<Node>> table = this.linkNodeTable(this.createNodeTable());
        ArrayList<Edge> sortedEdges = this.sortEdges(table);
        ArrayList<Edge> spanningTreeEdges = this.createMinSpanningTree(sortedEdges, table);
        this.board = this.updateNodeEdges(table, spanningTreeEdges);
    }
    
    // resets each node's edges field to now contain the only edges that properly represent
    // the created maze
    ArrayList<Node> updateNodeEdges(ArrayList<ArrayList<Node>> table, ArrayList<Edge> edges) {
        for (int x = 0 ; x < MazeWorld.WIDTH ; x++) {
            for (int y = 0 ; y < MazeWorld.HEIGHT ; y++) {
                table.get(x).get(y).edges = new ArrayList<Edge>();
            }
        }
        for (Edge e : edges) {
            table.get(e.from.x).get(e.from.y).edges.add(e);
            table.get(e.to.x).get(e.to.y).edges.add(new Edge(e.to, e.from, e.weight));
        }
        return this.table2List(table);
    }
    
    // Turns a table of nodes into an arraylist of nodes
    ArrayList<Node> table2List(ArrayList<ArrayList<Node>> table) {
        ArrayList<Node> list = new ArrayList<Node>();
        for (int x = 0 ; x < MazeWorld.WIDTH ; x++) {
            for (int y = 0 ; y < MazeWorld.HEIGHT ; y++) {
                list.add(table.get(x).get(y));
            }
        }
        return list;
    }
    
    //Create a player. Assumes that the board has already been created
    void createPlayer() {
        this.player = new Player(this.board.get(0));
    }
    
    //Move the player up. Assumes the player can move up
    void movePlayerUp() {
        this.player.on = this.board.get(this.player.x * MazeWorld.HEIGHT + this.player.y - 1);
        this.player.y = this.player.y - 1;
    }
    
    //Move the player down. Assumes the player can move down
    void movePlayerDown() {
        this.player.on = this.board.get(this.player.x * MazeWorld.HEIGHT + this.player.y + 1);
        this.player.y = this.player.y + 1;
    }
    
    //Move the player left. Assumes the player can move left
    void movePlayerLeft() {
        this.player.on = this.board.get((this.player.x - 1) * MazeWorld.HEIGHT + this.player.y);
        this.player.x = this.player.x - 1;
    }
    
    //Move the player right. Assumes the player can move right
    void movePlayerRight() {
        this.player.on = this.board.get((this.player.x + 1) * MazeWorld.HEIGHT + this.player.y);
        this.player.x = this.player.x + 1;
    }
    
    //Search the graph
    boolean graphSearch(Node n, GraphTraverseType<Node> func) {
        Deque<Node> wl = new ArrayDeque<Node>();
        ArrayList<Node> seen = new ArrayList<Node>();
        func.add(wl, n);
        
        while (wl.size() != 0) {
            Node cur = func.remove(wl);
            if (cur.x == MazeWorld.WIDTH - 1 && cur.y == MazeWorld.HEIGHT - 1) {
                return true;
            }
            
            if (seen.contains(cur)) {
                
            }
            else {
                for (Edge e : cur.edges) {
                    func.add(wl, e.to);
                }
                seen.add(cur);
            }
        }
        return false;
        
    }
    
    // Render the game
    public WorldScene makeScene() {
        WorldScene bg = new WorldScene(MazeWorld.WIDTH * Node.CELL_SIZE, 
                MazeWorld.HEIGHT * Node.CELL_SIZE);
        for (Node n : this.board) {
            n.drawNode(bg);
        }
        if (player != null) {
            this.player.drawPlayer(bg);
        }

        return bg;
    }
    
    // onTick
    public void onTick() {
        if (this.player.hasPlayerWon()) {
            this.onKeyEvent("r");
        }
    }
    
    // onKey
    public void onKeyEvent(String ke) {
        if (ke.equals("r")) {
            this.createMaze();
            this.createPlayer();
        }
        else if (ke.equals("left")) {
            if (this.player.canMoveLeft()) {
                this.movePlayerLeft();
            }
        }
        else if (ke.equals("right")) {
            if (this.player.canMoveRight()) {
                this.movePlayerRight();
            }
        }
        else if (ke.equals("down")) {
            if (this.player.canMoveDown()) {
                this.movePlayerDown();
            }
        }
        else if (ke.equals("up")) {
            if (this.player.canMoveUp()) {
                this.movePlayerUp();
            }
        }
        else if (ke.equals("b")) {
            if (this.graphSearch(this.board.get(0), new BFS<Node>())) {
                System.out.println("BooYeah!");
            }
        }
        else if (ke.equals("d")) {
            if (this.graphSearch(this.board.get(0), new DFS<Node>())) {
                System.out.println("BooYeah! DFS");
            }
        }
        else {
            return ;
        }
    }
    
}

// Represents the interface for graph traversing function objects
interface GraphTraverseType<T> {
    
    // Add an item to the worklist
    void add(Deque<T> wl, T item);
    // Remove an item from the worklist
    T remove(Deque<T> wl);
}

// Represents the function object for bfs
class BFS<T> implements GraphTraverseType<T> {
    
    // Add an item to the worklist
    public void add(Deque<T> wl, T item) {
        wl.addFirst(item);
    }
    
    // Remove an item from the worklist
    public T remove(Deque<T> wl) {
        return wl.removeLast();
    }
}

class DFS<T> implements GraphTraverseType<T> {
    
    // Add an item to the worklist
    public void add(Deque<T> wl, T item) {
        wl.addFirst(item);
    }
    
    // Remove an item from the worklist
    public T remove(Deque<T> wl) {
        return wl.removeFirst();
    }
}








