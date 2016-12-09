import java.util.Random;

//Represents an edge of a graph
class Edge {
    Node from;
    Node to;
    int weight;
    
    Random rand = new Random();
    
    Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        this.weight = this.rand.nextInt();
    }
    
    Edge(Node from, Node to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
    
}

