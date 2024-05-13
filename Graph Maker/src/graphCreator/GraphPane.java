package graphCreator;

/*Kevin L. Lemus Serrano
 * CMSC315 Project 4
 * DUE date 5/7/24
 * Description:  The GraphVisualizer class is a JavaFX program for visualizing graphs. 
 * It allows users to create graphs, add vertices and edges, and perform searches to analyze graph properties like connectivity and cycles. 
 * It handles mouse clicks
 * THE BFS ANF BFS DO NOT WORK PROPERLY, EXPLINATION IN DOCUMENTATION
 * 
 */

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphPane extends Pane {
    private Graph graph;
    private Map<Vertex, Line> lines;
    private Map<Vertex, Circle> vertexMap;
    private char nextVertexName = 'A';

    public GraphPane() {
        this.graph = new Graph();
        this.vertexMap = new HashMap<>();
        this.lines = new HashMap();

        // Set mouse click event handler
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    // Left mouse click: create new vertex
                    handleLeftMouseClick(event);
                } else if (event.getButton() == MouseButton.SECONDARY) {
                    // Right mouse click: remove vertex and associated elements
                    handleRightMouseClick(event);
                }
            }
        });
    }

    private void handleLeftMouseClick(MouseEvent event) {
        if (nextVertexName > 'Z') return; // Limit to A-Z names

        double mouseX = event.getX();
        double mouseY = event.getY();

        // Create new vertex with name from 'A' to 'Z'
        String name = String.valueOf(nextVertexName++);
        Vertex newVertex = new Vertex(name, mouseX, mouseY);
        graph.addVertex(newVertex);

        // Draw the new vertex as a circle with its name
        Circle circle = new Circle(mouseX, mouseY, 5, Color.BLACK);
        javafx.scene.text.Text text = new javafx.scene.text.Text(name);
        text.setX(mouseX - 5); // Adjust the position to center the text
        text.setY(mouseY - 13); // Move the text above the vertex

        // Add to scene and map
        getChildren().addAll(circle, text);
        vertexMap.put(newVertex, circle);
    }

    private void handleRightMouseClick(MouseEvent event) {
        for (Map.Entry<Vertex, Circle> entry : vertexMap.entrySet()) {
            Circle circle = entry.getValue();
            if (circle.contains(event.getX(), event.getY())) {
                Vertex vertexToDelete = entry.getKey();

                
                // Remove vertex from the graph
                graph.removeVertex(vertexToDelete);

                // Remove associated visual elements
                removeVertexVisuals(vertexToDelete);

                break;
            }
        }
    }

    private void removeVertexVisuals(Vertex vertex) {
        Circle circleToRemove = vertexMap.get(vertex);
        if (circleToRemove != null) {
            getChildren().remove(circleToRemove);
            vertexMap.remove(vertex);
        }
        

        Line lineToRemove = lines.get(vertex);
        if (lineToRemove != null) {
            getChildren().remove(lineToRemove);
            lines.remove(vertex);
        }

        // Remove lines where this vertex is a destination
        lines.entrySet().removeIf(entry -> entry.getValue().getEndX() == circleToRemove.getCenterX() &&
                                             entry.getValue().getEndY() == circleToRemove.getCenterY());

        // Remove lines where this vertex is a source
        lines.entrySet().removeIf(entry -> entry.getValue().getStartX() == circleToRemove.getCenterX() &&
                                             entry.getValue().getStartY() == circleToRemove.getCenterY());
        
        // Remove associated text
        getChildren().removeIf(node -> node instanceof javafx.scene.text.Text &&
                                       ((Text) node).getX() == circleToRemove.getCenterX() - 5 &&
                                       ((Text) node).getY() == circleToRemove.getCenterY() - 13);
        drawEdges(true);
        drawEdges(false);
    }

    public Graph getGraph() {
        return graph;
    }

    public Map<Vertex, Circle> getVertexMap() {
        return vertexMap;
    }

    public void drawEdges(boolean deletion) {
        if (deletion) {
            // Remove all lines
            getChildren().removeIf(node -> node instanceof Line);
            getChildren().removeAll(lines.values());
            lines.clear();
             // Clear adjacency list in the graph
        }

        // Draw edges based on the graph's adjacency list
        for (Vertex source : graph.getVertices().values()) {
            for (Vertex destination : graph.getAdjacencyList().get(source)) {
                Circle sourceCircle = vertexMap.get(source);
                Circle destCircle = vertexMap.get(destination);

                if (sourceCircle != null && destCircle != null) {
                    Line line = new Line(
                            sourceCircle.getCenterX(), sourceCircle.getCenterY(),
                            destCircle.getCenterX(), destCircle.getCenterY()
                    );
                    getChildren().add(line);
                    lines.put(source, line);
                    
                }
            }
        }
        
    }
    
    public void clearAdjacencyList() {
        for (List<Vertex> adjacency : graph.getAdjacencyList().values()) {
            adjacency.clear();
        }
    }
}

