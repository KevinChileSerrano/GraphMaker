// GraphVisualizer.java
package graphCreator;

/*Kevin L. Lemus Serrano
 * Description:  The GraphVisulizer class created the GUI for the program
 * 
 */


import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class GraphVisualizer extends Application {

    private TextField messageTextField;
    private TextField vertex1TextField;
    private TextField vertex2TextField;
    private GraphPane graphPane;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        graphPane = new GraphPane();

        // Create buttons
        Button button1 = new Button("BFS");
        Button button2 = new Button("DFS");
        Button button3 = new Button("Check Connectivity");
        Button button4 = new Button("Check Cycles");

        // Add event handlers to buttons
        button1.setOnAction(event -> handleButton1Press());
        button2.setOnAction(event -> handleButton2Press());
        button3.setOnAction(event -> handleButton3Press());
        button4.setOnAction(event -> handleButton4Press());

        // Create bottom control panel
        HBox bottomControlPanel = new HBox(button1, button2, button3, button4);

        // Create message text field
        messageTextField = new TextField();
        messageTextField.setEditable(false);

        // Create text fields for vertex inputs
        vertex1TextField = new TextField();
        vertex2TextField = new TextField();
        Button connectButton = new Button("Connect");
        connectButton.setOnAction(event -> handleConnectButtonPress());

        // Create top control panel
        HBox topControlPanel = new HBox(10, vertex1TextField, vertex2TextField, connectButton);

        // Create container for control components
        VBox controlsContainer = new VBox(topControlPanel, bottomControlPanel, messageTextField);

        // Create border pane to hold the graph and control components
        BorderPane root = new BorderPane();
        root.setCenter(graphPane);
        root.setBottom(controlsContainer);

        // Create scene
        Scene scene = new Scene(root, 600, 400);

        // Set up primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Graph Visualization");
        primaryStage.show();
    }
    
    private void handleButton1Press() {
        StringBuilder result = new StringBuilder("Breadth First Search Result:\n");
        for (Vertex vertex : graphPane.getGraph().breadthFirstSearch()) {
            result.append(vertex.getName()).append(" ");
        }
        messageTextField.setText(result.toString());
    }
    
    private void handleButton2Press() {
        StringBuilder result = new StringBuilder("Depth First Search Result:\n");
        for (Vertex vertex : graphPane.getGraph().depthFirstSearch()) {
            result.append(vertex.getName()).append(" ");
        }
        messageTextField.setText(result.toString());
    }

    private void handleButton3Press() {
        if (graphPane.getGraph().isConnected()) {
            messageTextField.setText("The graph is connected.");
        } else {
            messageTextField.setText("The graph is not connected.");
        }
    }

    private void handleButton4Press() {
        if (graphPane.getGraph().hasCycle()) {
            messageTextField.setText("The graph has cycles.");
        } else {
            messageTextField.setText("The graph does not have cycles.");
        }
    }

    private void handleConnectButtonPress() {
        String vertex1Name = vertex1TextField.getText();
        String vertex2Name = vertex2TextField.getText();

        if (!vertex1Name.isEmpty() && !vertex2Name.isEmpty()) {
            // Find the corresponding vertices in the graphPane's vertexMap
            Vertex vertex1 = findVertexByName(vertex1Name);
            Vertex vertex2 = findVertexByName(vertex2Name);

            if (vertex1 != null && vertex2 != null) {
                // Add edges between the vertices in the graph
                graphPane.getGraph().addEdge(vertex1, vertex2);
                // Draw edges in the graphPane scene
                graphPane.drawEdges(false);
                messageTextField.setText("Vertices connected: " + vertex1Name + " - " + vertex2Name);
            } else {
                messageTextField.setText("One or both vertices not found.");
            }
        } else {
            messageTextField.setText("Please enter names for both vertices.");
        }
    }

    private Vertex findVertexByName(String name) {
        for (Vertex vertex : graphPane.getVertexMap().keySet()) {
            if (vertex.getName().equals(name)) {
                return vertex;
            }
        }
        return null;
    }
}
