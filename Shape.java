import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Shape {

    private BufferedImage texture;

    private Vector3[] vertices;
    private double[] rotation;

    private Integer indexToSmallestX;
    private Integer indexToSmallestY;
    private Integer indexToSmallestZ;

    private Integer indexToBiggestX;
    private Integer indexToBiggestY;
    private Integer indexToBiggestZ;

    private String pathToTexture = null;

    private boolean isStatic;

    private Face[] faces;

    private Color color;

    Shape(Vector3[] vertices) {
        this.vertices = vertices;
        rotation = new double[3];
    }

    Shape(Vector3 blf, Vector3 brf, Vector3 trf, Vector3 tlf, Vector3 blb, Vector3 brb, Vector3 trb, Vector3 tlb) {
        rotation = new double[3];
        vertices = new Vector3[8];
        vertices[0] = blf;
        vertices[1] = brf;
        vertices[2] = trf;
        vertices[3] = tlf;
        vertices[4] = blb;
        vertices[5] = brb;
        vertices[6] = trb;
        vertices[7] = tlb;
        faces = new Face[6]; // front, side-left, side-right, top, bottom, back
        faces[0] = new Face(tlf, trf, blf, brf);
        faces[1] = new Face(tlb, tlf, blb, blf);
        faces[2] = new Face(trf, trb, brf, brb);
        faces[3] = new Face(tlf, trf, tlb, trb);
        faces[4] = new Face(blf, brf, blb, brb);
        faces[5] = new Face(tlb, trb, blb, brb);

    }

    Shape() {
        vertices = new Vector3[0];
        rotation = new double[3];
    }

    public void setVertices(Vector3[] vertices) {
        this.vertices = vertices;
    }

    public double[] getRotation() {
        return rotation;
    }

    public Color getColor() {
        return color;
    }

    public Face[] getFaces() {
        return faces;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public boolean getStatic() {
        return isStatic;
    }

    public void setPathToTexture(String pathToTexture) {
        this.pathToTexture = pathToTexture;

        try {
            this.texture = ImageIO.read(new File(pathToTexture));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public BufferedImage getTexture() {
        return this.texture;
    }

    public String getPathToTexture() {
        return this.pathToTexture;
    }

    public Vector3 getSmallestYPoint() {

        if (this.indexToSmallestY != null) {
            return new Vector3(vertices[indexToSmallestY]);
        }

        int smallestYPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[smallestYPointIndex].getY() > vertices[i].getY()) {
                smallestYPointIndex = i;
            }
        }

        indexToSmallestY = smallestYPointIndex;
        return new Vector3(vertices[smallestYPointIndex]);

    }

    public Vector3 getBiggestYPoint() {

        if (this.indexToBiggestY != null) {
            return new Vector3(vertices[indexToBiggestY]);
        }

        int biggestYPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[biggestYPointIndex].getY() < vertices[i].getY()) {
                biggestYPointIndex = i;
            }
        }

        indexToBiggestY = biggestYPointIndex;
        return new Vector3(vertices[biggestYPointIndex]);

    }

    public Vector3 getSmallestXPoint() {

        if (this.indexToSmallestX != null) {
            return new Vector3(vertices[indexToSmallestX]);
        }

        int smallestXPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[smallestXPointIndex].getX() > vertices[i].getX()) {
                smallestXPointIndex = i;
            }
        }

        indexToSmallestX = smallestXPointIndex;
        return new Vector3(vertices[smallestXPointIndex]);

    }

    public Vector3 getBiggestXPoint() {

        if (this.indexToBiggestX != null) {
            return new Vector3(vertices[indexToBiggestX]);
        }

        int biggestXPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[biggestXPointIndex].getX() < vertices[i].getX()) {
                biggestXPointIndex = i;
            }
        }

        indexToBiggestX = biggestXPointIndex;
        return new Vector3(vertices[biggestXPointIndex]);

    }

    public Vector3 getSmallestZPoint() {

        if (this.indexToSmallestZ != null) {
            return new Vector3(vertices[indexToSmallestZ]);
        }

        int smallestZPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[smallestZPointIndex].getZ() > vertices[i].getZ()) {
                smallestZPointIndex = i;
            }
        }

        indexToSmallestZ = smallestZPointIndex;
        return new Vector3(vertices[smallestZPointIndex]);

    }

    public Vector3 getBiggestZPoint() {

        if (this.indexToBiggestZ != null) {
            return new Vector3(vertices[indexToBiggestZ]);
        }

        int biggestZPointIndex = 0;

        for (int i = 1; i < vertices.length; i++) {
            if (vertices[biggestZPointIndex].getZ() < vertices[i].getZ()) {
                biggestZPointIndex = i;
            }
        }

        indexToBiggestZ = biggestZPointIndex;
        return new Vector3(vertices[biggestZPointIndex]);

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

    public Vector3[] getVertices() {
        return vertices;
    }

    public void addVector3(Vector3 vertex) {

        indexToSmallestX = null;
        indexToSmallestY = null;
        indexToSmallestZ = null;

        indexToBiggestX = null;
        indexToBiggestY = null;
        indexToBiggestZ = null;

        Vector3[] newVertices = new Vector3[vertices.length + 1];
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
