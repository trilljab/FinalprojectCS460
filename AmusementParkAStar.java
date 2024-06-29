import java.util.*;

class Node {
    int x, y, gCost = Integer.MAX_VALUE, hCost = 0;
    Node parent;
    String description;

    public Node(int x, int y, String description) {
        this.x = x;
        this.y = y;
        this.description = description;
    }

    public int getFCost() {
        return gCost + hCost;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node other = (Node) obj;
            return this.x == other.x && this.y == other.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return description + " (" + x + ", " + y + ")";
    }
}

public class AmusementParkAStar {

    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private int[][] parkMap;
    private Node[][] nodes;

    public AmusementParkAStar(int[][] parkMap, Node[][] nodes) {
        this.parkMap = parkMap;
        this.nodes = nodes;
    }

    public List<Node> findPath(Node start, Node end) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(Node::getFCost));
        Set<Node> closedList = new HashSet<>();
        start.gCost = 0;
        start.hCost = calculateDistance(start, end);
        openList.add(start);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            if (currentNode.equals(end)) return reconstructPath(currentNode);
            closedList.add(currentNode);

            for (int[] direction : DIRECTIONS) {
                int nx = currentNode.x + direction[0], ny = currentNode.y + direction[1];
                if (isValid(nx, ny) && !closedList.contains(nodes[nx][ny])) {
                    Node neighbor = nodes[nx][ny];
                    int newGCost = currentNode.gCost + 1;

                    if (newGCost < neighbor.gCost || !openList.contains(neighbor)) {
                        neighbor.gCost = newGCost;
                        neighbor.hCost = calculateDistance(neighbor, end);
                        neighbor.parent = currentNode;
                        openList.add(neighbor);
                    }
                }
            }
        }
        return Collections.emptyList(); // No path found
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < parkMap.length && y >= 0 && y < parkMap[0].length && parkMap[x][y] == 0;
    }

    private int calculateDistance(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private List<Node> reconstructPath(Node endNode) {
        List<Node> path = new ArrayList<>();
        for (Node node = endNode; node != null; node = node.parent) path.add(node);
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        int[][] parkMap = {
                {0, 1, 0, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 0, 0, 1, 0},
                {0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0}
        };

        Node[][] nodes = {
                {new Node(0, 0, "Entrance"), new Node(0, 1, "Wall"), new Node(0, 2, "Gift Shop"), new Node(0, 3, "Food Stall"), new Node(0, 4, "Ferris Wheel")},
                {new Node(1, 0, "Rest Area"), new Node(1, 1, "Wall"), new Node(1, 2, "Carousel"), new Node(1, 3, "Wall"), new Node(1, 4, "Roller Coaster")},
                {new Node(2, 0, "Haunted House"), new Node(2, 1, "Open Area"), new Node(2, 2, "Open Area"), new Node(2, 3, "Wall"), new Node(2, 4, "Bumper Cars")},
                {new Node(3, 0, "Restroom"), new Node(3, 1, "Wall"), new Node(3, 2, "Arcade"), new Node(3, 3, "Open Area"), new Node(3, 4, "Swing Ride")},
                {new Node(4, 0, "Water Park"), new Node(4, 1, "Open Area"), new Node(4, 2, "Open Area"), new Node(4, 3, "Wall"), new Node(4, 4, "Exit")}
        };

        AmusementParkAStar astar = new AmusementParkAStar(parkMap, nodes);
        Node start = nodes[0][0];
        Node end = nodes[4][4];
        List<Node> path = astar.findPath(start, end);

        if (!path.isEmpty()) {
            System.out.println("FUN PATH FOUND:");
            int totalDistance = 0;
            Node prev = null;
            for (Node node : path) {
                if (prev != null) {
                    int distance = astar.calculateDistance(prev, node);
                    totalDistance += distance;
                    System.out.println(node + " (Distance: " + distance + ")");
                } else {
                    System.out.println(node);
                }
                prev = node;
            }
            System.out.println("Total Distance: " + totalDistance);
        } else {
            System.out.println("No path found. Try a different adventure route!");
        }
    }
}