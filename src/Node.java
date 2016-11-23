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
}