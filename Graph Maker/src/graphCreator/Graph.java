package graphCreator;

/*Kevin L. Lemus Serrano
 * CMSC315 Project 4
 * DUE date 5/7/24
 * Description:  The graph class represent a graph.
 * the vertices field represents a list of the vertices and their names
 * the adjencencyList has a Vertex for the key and a List of vertices for the destination
 * 
 * THE DFS AND BDS METHODS DO NOT WORK EVERYTIME A VERTEX IS ADDED IT ALWAYS COUNTS IT AS ADJENCENT
 */



import java.util.*;

public class Graph {
    private Map<String, Vertex> vertices;
    private Map<Vertex, List<Vertex>> adjacencyList;

    public Graph() {
        vertices = new HashMap<>();
        adjacencyList = new HashMap<>();
    }

    public void addVertex(Vertex vertex) {
        vertices.put(vertex.getName(), vertex);
        adjacencyList.put(vertex, new ArrayList<>());
    }

    public void addEdge(Vertex source, Vertex destination) {
        if (!vertices.containsValue(source) || !vertices.containsValue(destination))
            throw new IllegalArgumentException("Source or destination vertex does not exist in the graph.");

        adjacencyList.get(source).add(destination);
    }

    public void removeEdge(Vertex source, Vertex destination) {
        List<Vertex> neighbors = adjacencyList.get(source);
        if (neighbors != null) {
            neighbors.remove(destination);
        }
    }

    public boolean hasCycle() {
        Set<Vertex> visited = new HashSet<>();
        Set<Vertex> recursionStack = new HashSet<>();

        for (Vertex vertex : vertices.values()) {
            if (hasCycleUtil(vertex, visited, recursionStack))
                return true;
        }

        return false;
    }

    private boolean hasCycleUtil(Vertex vertex, Set<Vertex> visited, Set<Vertex> recursionStack) {
        if (recursionStack.contains(vertex))
            return true;

        if (visited.contains(vertex))
            return false;

        visited.add(vertex);
        recursionStack.add(vertex);

        List<Vertex> neighbors = adjacencyList.get(vertex);
        for (Vertex neighbor : neighbors) {
            if (hasCycleUtil(neighbor, visited, recursionStack))
                return true;
        }

        recursionStack.remove(vertex);
        return false;
    }

    public boolean isConnected() {
        // If there are no vertices, consider it connected
        if (vertices.isEmpty()) {
            return true;
        }

        // Perform DFS from the first vertex
        Set<Vertex> visited = new HashSet<>();
        dfs(vertices.values().iterator().next(), visited);

        // Check if all vertices are visited
        return visited.size() == vertices.size();
    }

    private void dfs(Vertex vertex, Set<Vertex> visited) {
        visited.add(vertex);
        
        List<Vertex> neighbors = adjacencyList.get(vertex);
        if (neighbors != null) {
            for (Vertex neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, visited);
                }
            }
        }
    }

    public List<Vertex> depthFirstSearch() {
        List<Vertex> result = new ArrayList<>();
        Set<Vertex> visited = new HashSet<>();
        
        for (Vertex vertex : vertices.values()) {
            if (!visited.contains(vertex)) {
                dfs(vertex, visited, result);
            }
        }
        return result;
    }

    private void dfs(Vertex vertex, Set<Vertex> visited, List<Vertex> result) {
        visited.add(vertex);
        result.add(vertex);
        
        List<Vertex> neighbors = adjacencyList.get(vertex);
        if (neighbors != null) {
            for (Vertex neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    dfs(neighbor, visited, result);
                }
            }
        }
    }

    public List<Vertex> breadthFirstSearch() {
        List<Vertex> result = new ArrayList<>();
        Queue<Vertex> queue = new LinkedList<>();
        Set<Vertex> visited = new HashSet<>();

        for (Vertex vertex : vertices.values()) {
            if (!visited.contains(vertex)) {
                queue.offer(vertex);
                visited.add(vertex);
                while (!queue.isEmpty()) {
                    Vertex current = queue.poll();
                    result.add(current);
                    List<Vertex> neighbors = adjacencyList.get(current);
                    if (neighbors != null) {
                        for (Vertex neighbor : neighbors) {
                            if (!visited.contains(neighbor)) {
                                queue.offer(neighbor);
                                visited.add(neighbor);
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public Map<String, Vertex> getVertices() {
        return this.vertices;
    }

    public Map<Vertex, List<Vertex>> getAdjacencyList() {
        return this.adjacencyList;
    }

    public Vertex getVertex(String name) {
        for (Map.Entry<String, Vertex> entry : vertices.entrySet()) {
            if (entry.getKey().equals(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void removeVertex(Vertex vertexToDelete) {
        vertices.remove(vertexToDelete.getName());
        adjacencyList.remove(vertexToDelete);
        // Remove edges connected to the vertexToDelete
        for (List<Vertex> neighbors : adjacencyList.values()) {
            neighbors.removeIf(vertex -> vertex.equals(vertexToDelete));
        }
    }
}