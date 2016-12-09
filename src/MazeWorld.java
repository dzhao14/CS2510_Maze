import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import javalib.impworld.*;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;


// Represents a maze
class MazeWorld extends World {
    
    // the total height of the maze in pixels 
    static final int HEIGHT = 60;
    // the total width of the maze in pixels
    static final int WIDTH = 100;
    
    //A list of nodes that represent the maze
    ArrayList<Node> board;
    
    //Stores the player for this maze
    Player player;
    // Stores the worklist for graph searching
    Deque<Node> wl = new ArrayDeque<Node>();
    // Stores the seen list for graph searching
    ArrayList<Node> seen = new ArrayList<Node>();
    // Stores the function object for graph searching
    GraphTraverseType<Node> func = new DFS<Node>();
    // Stores whether the maze has been solved AND the solution path has been shown
    boolean gameStop = false;
    // Stores the number of wrong moves taken during this game. 
    // Going over the same wrong square more than
    // once doesn't add to this score.
    int wrongMoves = 0;
    // Stores the number of right moves taken during this game to solve the maze. 
    // Going over the same right square
    // more than once won't add to this score.
    int rightMoves = 0;
    // Stores whether or not to show the score screen when the maze is finished
    boolean showScore = false;
    
    MazeWorld() {
        this.createMaze();
        this.createPlayer();
    }
    
    // Testing constructor
    MazeWorld(boolean b) {
        // this constructor is used for testing, to avoid generating a new maze each time
    }
    
    //Reset
    void reset() {
        this.createMaze();
        this.player = new Player(this.board.get(0));
        this.wl = new ArrayDeque<Node>();
        this.seen = new ArrayList<Node>();
        this.gameStop = false;
        this.wrongMoves = 0;
        this.rightMoves = 0;
        this.showScore = false;
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
        
        HeapSort<Edge> hSort = new HeapSort<Edge>();
        hSort.heapSort(l, new CompareEdges());
        return l;
        
    }
    
    // create a minimum spanning tree from the given sorted list of edges
    ArrayList<Edge> createMinSpanningTree(ArrayList<Edge> l, ArrayList<ArrayList<Node>> table) {
        HashMap<Node, Node> map = new HashMap<Node, Node>();
        for (int x = 0 ; x < MazeWorld.WIDTH ; x++) {
            for (int y = 0 ; y < MazeWorld.HEIGHT ; y++) {
                map.put(table.get(x).get(y), table.get(x).get(y));
            }
        }
        
        ArrayList<Edge> out = new ArrayList<Edge>();
        
        while (out.size() < MazeWorld.WIDTH * MazeWorld.HEIGHT - 1) {
            Edge e = l.get(0);
            if (this.determineHead(map, e.to) == this.determineHead(map, e.from)) {
                l.remove(0);
            }
            else {
                out.add(e);
                this.union(map, e);
                l.remove(0);
            }
        }
        return out;
    }
    
    // update the hashmap once a connection between two nodes is made
    void union(HashMap<Node, Node> map, Edge e) {
        map.put(this.determineHead(map, e.to), map.get(e.from));
    }
    
    // determine the head of the group in this hasmap given a node in the hashmap
    Node determineHead(HashMap<Node, Node> map, Node n) {
        Node val = map.get(n);
        while (val != n) {
            n = val;
            val = map.get(n);
        }
        return n;
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
        if (this.wl.size() == 0) {
            this.player.on = this.board.get(this.player.x * MazeWorld.HEIGHT + this.player.y - 1);
            this.player.y = this.player.y - 1;
            this.player.on.seen = true;
            if (!this.seen.contains(this.player.on)) {
                this.seen.add(this.player.on);
            }
        }
    }
    
    //Move the player down. Assumes the player can move down
    void movePlayerDown() {
        if (this.wl.size() == 0) {
            this.player.on = this.board.get(this.player.x * MazeWorld.HEIGHT + this.player.y + 1);
            this.player.y = this.player.y + 1;
            this.player.on.seen = true;
            if (!this.seen.contains(this.player.on)) {
                this.seen.add(this.player.on);
            }
        }
    }
    
    //Move the player left. Assumes the player can move left
    void movePlayerLeft() {
        if (this.wl.size() == 0) {
            this.player.on = this.board.get((this.player.x - 1) * MazeWorld.HEIGHT + this.player.y);
            this.player.x = this.player.x - 1;
            this.player.on.seen = true;
            if (!this.seen.contains(this.player.on)) {
                this.seen.add(this.player.on);
            }
        }
    }
    
    //Move the player right. Assumes the player can move right
    void movePlayerRight() {
        if (this.wl.size() == 0) {
            this.player.on = this.board.get((this.player.x + 1) * MazeWorld.HEIGHT + this.player.y);
            this.player.x = this.player.x + 1;
            this.player.on.seen = true;
            if (!this.seen.contains(this.player.on)) {
                this.seen.add(this.player.on);
            } 
        }
    }
    
    //Search the graph
    void slowGraphSearch() {
        this.wl = new ArrayDeque<Node>();
        this.seen = new ArrayList<Node>();
        this.gameStop = false;
        this.wrongMoves = 0;
        this.rightMoves = 0;
        this.player = new Player(this.board.get(0));
        for (Node n : this.board) {
            n.seen = false;
            n.solution = false;
        }
        func.add(this.wl, this.board.get(0));
    }
    
    //Search the graph
    boolean slowGraphSearchHelp() {        
        if (this.wl.size() != 0) {
            Node cur = this.func.remove(this.wl);
            if (cur.x == MazeWorld.WIDTH - 1 && cur.y == MazeWorld.HEIGHT - 1) {
                cur.seen = true;
                return true;
            }

            if (!this.seen.contains(cur)) {
                for (Edge e : cur.edges) {
                    if (!this.seen.contains(e.to)) {
                        this.func.add(this.wl, e.to);
                    }
                    
                }
                if (!this.seen.contains(cur)) {
                    this.seen.add(cur);
                }

                cur.seen = true;
            }
        }
        return false;
        
    }
    
    //Calculates the solution to the maze
    void determineSolution() {
        ArrayList<Node> tempSeen = new ArrayList<Node>();
        for (Node n : this.seen) {
            int index = this.seen.indexOf(n);
            for (int i = 0 ; i < index ; i++) {
                tempSeen.add(this.seen.get(i));                
            }
            if (this.fastGraphSearch(n, tempSeen, this.func)) {
                n.solution = true;
                this.rightMoves++;
            }
            else {
                this.wrongMoves++;
            }
            tempSeen = new ArrayList<Node>();
        }
    }
    
    //Search the graph completely
    boolean fastGraphSearch(Node n, ArrayList<Node> seen, GraphTraverseType<Node> func) {
        Deque<Node> wl = new ArrayDeque<Node>();
        func.add(wl, n);
        while (wl.size() != 0) {
            Node cur = this.func.remove(wl);
            if (cur.x == MazeWorld.WIDTH - 1 && cur.y == MazeWorld.HEIGHT - 1) {
                return true;
            }

            if (!seen.contains(cur)) { 
                for (Edge e : cur.edges) {
                    if (!seen.contains(e.to)) {
                        this.func.add(wl, e.to);
                    }                    
                }
                if (!seen.contains(cur)) {
                    seen.add(cur);
                }
            }
        }
        return false;
    }
    
    //Hide / show (toggle) the coloring of the seen nodes in the maze
    void toggleColors() {
        for (int i = 1 ; i < this.board.size() - 1 ; i++) {
            this.board.get(i).isHidden = !this.board.get(i).isHidden;
        }
    }
    
    // Hide / show (toggle) the ending score screen
    void toggleScore() {
        this.showScore = !this.showScore;
    }
    
    // If the maze has been solved display stats and end message
    void drawEnd(WorldScene bg) {
        bg.placeImageXY(new RectangleImage(MazeWorld.WIDTH * Node.CELL_SIZE, 
                MazeWorld.HEIGHT * Node.CELL_SIZE,
                OutlineMode.SOLID, Color.WHITE), 
                MazeWorld.WIDTH * Node.CELL_SIZE / 2, MazeWorld.HEIGHT * Node.CELL_SIZE / 2);
        bg.placeImageXY(new TextImage("Nice job!", 
                MazeWorld.HEIGHT * Node.CELL_SIZE / 8, Color.RED), 
                MazeWorld.WIDTH * Node.CELL_SIZE / 2, MazeWorld.HEIGHT * Node.CELL_SIZE / 8);
        bg.placeImageXY(new TextImage("Took " + (this.rightMoves + this.wrongMoves) + " moves", 
                MazeWorld.HEIGHT * Node.CELL_SIZE / 8, Color.RED), 
                MazeWorld.WIDTH * Node.CELL_SIZE / 2, MazeWorld.HEIGHT * Node.CELL_SIZE / 2);
        bg.placeImageXY(new TextImage("" + this.wrongMoves + "wrong moves",
                MazeWorld.HEIGHT * Node.CELL_SIZE / 8, Color.RED), 
                MazeWorld.WIDTH * Node.CELL_SIZE / 2, MazeWorld.HEIGHT * Node.CELL_SIZE / 4 * 3);
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
        if (this.gameStop && this.showScore) {
            this.drawEnd(bg);
        }

        return bg;
    }
    
    // onTick
    public void onTick() {
        if (this.wl.size() != 0 && 
                !this.board.get(MazeWorld.WIDTH * MazeWorld.HEIGHT - 1).seen) {
            this.slowGraphSearchHelp();
        }
        if (this.board.get(MazeWorld.WIDTH * MazeWorld.HEIGHT - 1).seen && !this.gameStop) {
            this.determineSolution();
            this.gameStop = true;
            this.showScore = true;
            this.wl = new ArrayDeque<Node>();
        }        
    }
    
    // onKey
    public void onKeyEvent(String ke) {
        if (ke.equals("r")) {
            this.reset();
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
            this.func = new BFS<Node>();
            this.slowGraphSearch();
        }
        else if (ke.equals("d")) {
            this.func = new DFS<Node>();
            this.slowGraphSearch();
        }
        else if (ke.equals("h")) {
            this.toggleColors();
        }
        else if (ke.equals("j")) {
            this.toggleScore();
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
