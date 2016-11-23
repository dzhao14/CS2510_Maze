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

// Represents a maze
class MazeWorld extends World {
    
    // the total height of the maze in pixels 
    static final int HEIGHT = 60;
    // the total width of the maze in pixels
    static final int WIDTH = 60;
    
    //A list of nodes that represent the maze
    ArrayList<Node> board;
    
    
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
        return this.sortEdgeHelp(l);
    }
    
    // merge sort this list of edges
    ArrayList<Edge> sortEdgeHelp(ArrayList<Edge> l) {
        if (l.size() == 0 || l.size() == 1) {
            return l;
        }
        else {
            ArrayList<Edge> temp = new ArrayList<Edge>();
            for (int i = 0 ; i < l.size() ; i++) {
                temp.add(l.get(i));
            }
            
            return this.merge(this.sortEdgeHelp(this.splitLeft(temp)), 
                    this.sortEdgeHelp(this.splitRight(temp)),
                    new ArrayList<Edge>());
        }
    }
    
    // get the left half of a given list of Edges
    ArrayList<Edge> splitLeft(ArrayList<Edge> l) {
        ArrayList<Edge> out = new ArrayList<Edge>();
        int mid = l.size() / 2;
        for (int i = 0 ; i < mid ; i++) {
            out.add(l.get(i));
        }
        return out;
    }
    
    // get the left half of a given list of Edges
    ArrayList<Edge> splitRight(ArrayList<Edge> l) {
        ArrayList<Edge> out = new ArrayList<Edge>();
        int mid = l.size() / 2;
        for (int i = mid ; i < l.size() ; i++) {
            out.add(l.get(i));
        }
        return out;
    }
    
    // merge two sorted list of edges into a sorted list of edges
    ArrayList<Edge> merge(ArrayList<Edge> l1, ArrayList<Edge> l2, ArrayList<Edge> acc) {
        if (l1.size() == 0) {
            acc.addAll(l2);
            return acc;
        }
        else if (l2.size() == 0) {
            return acc;
        }
        else {
            if (l1.get(0).weight > l2.get(0).weight) {
                acc.add(l2.get(0));
                l2.remove(0);
                return this.merge(l1, l2, acc);
            }
            else {
                acc.add(l1.get(0));
                l1.remove(0);
                return this.merge(l1, l2, acc);
            }            
        }
    }
    
    // stub for makescene
    public WorldScene makeScene() {
        return new WorldScene(100, 100);
    }
    
}










