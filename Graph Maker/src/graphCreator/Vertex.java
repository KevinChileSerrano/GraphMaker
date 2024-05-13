package graphCreator;

/*Kevin L. Lemus Serrano
 * Description:  The verex class is an immutable class that defines 
 * a vertex of the graph and contains the x and y coordinates of the vertex along with its name.
 * 
 */

public class Vertex {
    private final String name;
    private final double x;
    private final double y;

    public Vertex(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    public String getName() {
        return name;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
	
}
