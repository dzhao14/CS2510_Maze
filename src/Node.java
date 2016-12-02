//CS2510 Fall 2016
//Assignment 11
//Zhao, David
//dzhao
//Carr, Kenneth "Theo"
//kcarr

// TO GENERATE MAZE: uncomment big-bang at the bottom of the Examples class

import java.util.ArrayList;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

class Node {
    // Represents the width and height of this cell when drawn
    static final int CELL_SIZE = 14;
    // Represents the x,y position on the board
    int x;
    int y;
    //Represents this node's edges
    ArrayList<Edge> edges = new ArrayList<Edge>();
    // has this node been visited?
    boolean seen = false;
    
    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    //Draw the node on the given background
    void drawNode(WorldScene bg) {
        bg.placeImageXY(this.determineRect(),
                this.x * Node.CELL_SIZE + Node.CELL_SIZE / 2, 
                this.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);
        bg.placeImageXY(new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, 
                OutlineMode.OUTLINE, Color.BLACK), 
                this.x * Node.CELL_SIZE + Node.CELL_SIZE / 2, 
                this.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);

        for (Edge e : this.edges) {
            bg.placeImageXY(this.determineEdge(e),
                    Node.CELL_SIZE * (e.from.x + e.to.x) / 2 + Node.CELL_SIZE / 2,
                    Node.CELL_SIZE * (e.from.y + e.to.y) / 2 + Node.CELL_SIZE / 2);

        }
    }
    
    // determine the edge orientation based on the given edge
    WorldImage determineEdge(Edge e) {
        if (e.from.y == e.to.y) {
            return new LineImage(new Posn(0, Node.CELL_SIZE - 2), Color.WHITE);
        }
        else {
            return new LineImage(new Posn(Node.CELL_SIZE - 2, 0), Color.WHITE);
        }
    }
    
    // determine the correct color background for this node when drawn
    WorldImage determineRect() {
        if (this.x == 0 && this.y == 0) {
            return new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, 
                    OutlineMode.SOLID, Color.BLUE);
        }
        else if (this.x == MazeWorld.WIDTH - 1 && this.y == MazeWorld.HEIGHT - 1) {
            return new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, 
                    OutlineMode.SOLID, Color.GREEN);
        }
        else {
            return new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, 
                    OutlineMode.OUTLINE, Color.BLACK);
        }
    }
}