import java.awt.Color;
import javalib.worldimages.*;
import javalib.impworld.*;

// Represents a player that exists in the maze
public class Player {
    
    // Represents the cood of this player on the board
    int x;
    int y;
    // Store the node on which this player stands
    Node on;
    
    Player(Node n) {
        this.x = 0;
        this.y = 0;
        this.on = n;
    }
    
    //Check if the player can move up
    boolean canMoveUp() {
        for (Edge e : this.on.edges) {
            if (e.to.x == this.x && e.to.y == this.y - 1) {
                return true;
            }
        }
        return false;
    }
    
    //Check if the player can move down
    boolean canMoveDown() {
        for (Edge e : this.on.edges) {
            if (e.to.x == this.x && e.to.y == this.y + 1) {
                return true;
            }
        }
        return false;
    }
    
    //Check if the player can move left
    boolean canMoveLeft() {
        for (Edge e : this.on.edges) {
            if (e.to.x == this.x - 1 && e.to.y == this.y) {
                return true;
            }
        }
        return false;
    }
    
    //Check if the player can move right
    boolean canMoveRight() {
        for (Edge e : this.on.edges) {
            if (e.to.x == this.x + 1 && e.to.y == this.y) {
                return true;
            }
        }
        return false;
    }
    
    // Draw the player on the background
    void drawPlayer(WorldScene bg) {
        bg.placeImageXY(new CircleImage(Node.CELL_SIZE * 3 / 10, OutlineMode.SOLID, Color.RED), 
                this.x * Node.CELL_SIZE + Node.CELL_SIZE / 2, 
                this.y * Node.CELL_SIZE + Node.CELL_SIZE / 2);
    }
    
    //Has the player reached the end of the maze?
    boolean hasPlayerWon() {
        return this.x == MazeWorld.WIDTH - 1 && this.y == MazeWorld.HEIGHT - 1;
    }
    
}
