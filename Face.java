public class Face {
    private Vector3[] vertices;

    Face(Vector3 topLeft, Vector3 topRight, Vector3 bottomLeft, Vector3 bottomRight) {
        vertices = new Vector3[4];
        vertices[0] = topLeft;
        vertices[1] = topRight;
        vertices[2] = bottomLeft;
        vertices[3] = bottomRight;
    }

    Vector3[] getVertices() {
        return vertices;
    }
}
