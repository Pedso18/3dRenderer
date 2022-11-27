public class Shape {

    Vertex[] vertices;

    Shape(Vertex[] vertices) {
        this.vertices = vertices;
    }

    Shape() {
        vertices = new Vertex[0];
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public void addVertex(Vertex vertex) {
        Vertex[] newVertices = new Vertex[vertices.length + 1];
        for (int i = 0; i < vertices.length; i++) {
            if ((double) vertex.getZ() <= vertices[i].getZ() || vertices[i] == null) {
                newVertices[i] = vertex;
                for (i = i + 0; i < vertices.length; i++) {
                    newVertices[i + 1] = vertices[i];
                }
                vertices = newVertices;
                return;
            } else {
                newVertices[i] = vertices[i];
            }
        }

        newVertices[newVertices.length - 1] = vertex;
        vertices = newVertices;

    }

}
