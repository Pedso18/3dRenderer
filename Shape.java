public class Shape {

    Vertex[] vertices;
    double[] rotation;

    Integer indexToSmallestX;
    Integer indexToSmallestY;
    Integer indexToSmallestZ;

    Integer indexToBiggestX;
    Integer indexToBiggestY;
    Integer indexToBiggestZ;

    Shape(Vertex[] vertices) {
        this.vertices = vertices;
        rotation = new double[3];
    }

    Shape() {
        vertices = new Vertex[0];
        rotation = new double[3];
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public double[] getRotation() {
        return rotation;
    }

    public Vertex getSmallestYPoint() {

        if (this.indexToSmallestY != null) {
            return new Vertex(vertices[indexToSmallestY]);
        }

        int smallestYPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[smallestYPointIndex].getY() > vertices[i].getY()) {
                smallestYPointIndex = i;
            }
        }

        indexToSmallestY = smallestYPointIndex;
        return new Vertex(vertices[smallestYPointIndex]);

    }

    public Vertex getBiggestYPoint() {

        if (this.indexToBiggestY != null) {
            return new Vertex(vertices[indexToBiggestY]);
        }

        int biggestYPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[biggestYPointIndex].getY() < vertices[i].getY()) {
                biggestYPointIndex = i;
            }
        }

        indexToBiggestY = biggestYPointIndex;
        return new Vertex(vertices[biggestYPointIndex]);

    }

    public Vertex getSmallestXPoint() {

        if (this.indexToSmallestX != null) {
            return new Vertex(vertices[indexToSmallestX]);
        }

        int smallestXPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[smallestXPointIndex].getX() > vertices[i].getX()) {
                smallestXPointIndex = i;
            }
        }

        indexToSmallestX = smallestXPointIndex;
        return new Vertex(vertices[smallestXPointIndex]);

    }

    public Vertex getBiggestXPoint() {

        if (this.indexToBiggestX != null) {
            return new Vertex(vertices[indexToBiggestX]);
        }

        int biggestXPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[biggestXPointIndex].getX() < vertices[i].getX()) {
                biggestXPointIndex = i;
            }
        }

        indexToBiggestX = biggestXPointIndex;
        return new Vertex(vertices[biggestXPointIndex]);

    }

    public Vertex getSmallestZPoint() {

        if (this.indexToSmallestZ != null) {
            return new Vertex(vertices[indexToSmallestZ]);
        }

        int smallestZPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[smallestZPointIndex].getZ() > vertices[i].getZ()) {
                smallestZPointIndex = i;
            }
        }

        indexToSmallestZ = smallestZPointIndex;
        return new Vertex(vertices[smallestZPointIndex]);

    }

    public Vertex getBiggestZPoint() {

        if (this.indexToBiggestZ != null) {
            return new Vertex(vertices[indexToBiggestZ]);
        }

        int biggestZPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[biggestZPointIndex].getZ() < vertices[i].getZ()) {
                biggestZPointIndex = i;
            }
        }

        indexToBiggestZ = biggestZPointIndex;
        return new Vertex(vertices[biggestZPointIndex]);

    }

    public void addXAxisRotation(double angle) {
        rotation[0] += angle;
    }

    public void addYAxisRotation(double angle) {
        rotation[1] += angle;
    }

    public void addZAxisRotation(double angle) {
        rotation[2] += angle;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public void addVertex(Vertex vertex) {

        indexToSmallestX = null;
        indexToSmallestY = null;
        indexToSmallestZ = null;

        indexToBiggestX = null;
        indexToBiggestY = null;
        indexToBiggestZ = null;

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
