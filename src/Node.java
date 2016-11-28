//CS2510 Fall 2016
//Assignment 11
//Zhao, David
//dzhao
//Carr, Kenneth "Theo"
//kcarr

import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

class Node {
    // Represents the width and height of this cell when drawn
    static final int CELL_SIZE = 10;
    // Represents the x,y position on the board
    int x;
    int y;
    //Represents this node's edges
    ArrayList<Edge> edges = new ArrayList<Edge>();    
    
    Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    void drawNode(WorldScene bg) {
        if (this.x == 0 && this.y == 0) {
            bg.placeImageXY(new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, 
                    OutlineMode.SOLID, Color.BLUE), 
                    this.x * Node.CELL_SIZE + Node.CELL_SIZE / 2, 
                    this.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);
            bg.placeImageXY(new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, 
                    OutlineMode.OUTLINE, Color.BLACK), 
                    this.x * Node.CELL_SIZE + Node.CELL_SIZE / 2, 
                    this.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);
        }
        else if (this.x == MazeWorld.WIDTH - 1 && this.y == MazeWorld.HEIGHT - 1) {
            bg.placeImageXY(new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, 
                    OutlineMode.SOLID, Color.GREEN), 
                    this.x * Node.CELL_SIZE + Node.CELL_SIZE / 2, 
                    this.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);
            bg.placeImageXY(new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, 
                    OutlineMode.OUTLINE, Color.BLACK), 
                    this.x * Node.CELL_SIZE + Node.CELL_SIZE / 2, 
                    this.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);
        }
        else {
            bg.placeImageXY(new RectangleImage(Node.CELL_SIZE, Node.CELL_SIZE, 
                    OutlineMode.OUTLINE, Color.BLACK), 
                    this.x * Node.CELL_SIZE + Node.CELL_SIZE / 2, 
                    this.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);
        }

        for (Edge e : this.edges) {
            if (e.from.y == e.to.y) { 
                bg.placeImageXY(new LineImage(new Posn(0, Node.CELL_SIZE), Color.WHITE), 
                        Node.CELL_SIZE * (e.from.x + e.to.x) / 2 + Node.CELL_SIZE / 2, 
                        Node.CELL_SIZE * (e.from.y + e.to.y) / 2 + Node.CELL_SIZE / 2);
            }
            else if (e.from.x == e.to.x) {
                bg.placeImageXY(new LineImage(new Posn(Node.CELL_SIZE, 0), Color.WHITE), 
                        Node.CELL_SIZE * (e.from.x + e.to.x) / 2 + Node.CELL_SIZE / 2, 
                        Node.CELL_SIZE * (e.from.y + e.to.y) / 2 + Node.CELL_SIZE / 2);
            }
        }
    }
}