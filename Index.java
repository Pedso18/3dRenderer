import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class Index {

    static JFrame frame;
    static private JPanel panel;
    static int[] defaultFrameSize = { 800, 500 };
    static int[] cameraPos = { defaultFrameSize[0] / 2, defaultFrameSize[1] / 2, 2000 };
    static int[] cameraMovement = { 0, 0, 0 };
    static int renderDistance = 200;

    static Vertex xAxis = new Vertex(1, 0, 0);
    static Vertex yAxis = new Vertex(0, 1, 0);
    static Vertex zAxis = new Vertex(0, 0, 1);

    public static void main(String[] args) {

        Shape[] shapes = new Shape[1];

        Shape cube = new Shape();
        Shape parallelepiped = new Shape();

        cube.addVertex(new Vertex(350, 300, 0));
        cube.addVertex(new Vertex(450, 300, 0));
        cube.addVertex(new Vertex(450, 200, 0));
        cube.addVertex(new Vertex(350, 200, 0));

        cube.addVertex(new Vertex(350, 300, -100));
        cube.addVertex(new Vertex(450, 300, -100));
        cube.addVertex(new Vertex(450, 200, -100));
        cube.addVertex(new Vertex(350, 200, -100));

        // parallelepiped.addVertex(new Vertex(550, 300, 0));
        // parallelepiped.addVertex(new Vertex(650, 300, 0));
        // parallelepiped.addVertex(new Vertex(650, 200, 0));
        // parallelepiped.addVertex(new Vertex(550, 200, 0));

        // parallelepiped.addVertex(new Vertex(550, 300, -100));
        // parallelepiped.addVertex(new Vertex(650, 300, -100));
        // parallelepiped.addVertex(new Vertex(650, 200, -100));
        // parallelepiped.addVertex(new Vertex(550, 200, -100));

        shapes[0] = cube;
        // shapes[1] = parallelepiped;

        initGUI(shapes);

    }

    static public void initGUI(Shape[] shapes) {

        Random rand = new Random();

        frame = new JFrame(); // default layout manager is BorderLayout
        frame.setSize(defaultFrameSize[0], defaultFrameSize[1]);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 65) { // a
                    cameraPos[0] -= 2;
                    cameraMovement[0] -= 2;
                    panel.repaint();
                } else if (e.getKeyCode() == 68) { // d
                    cameraPos[0] += 2;
                    cameraMovement[0] += 2;
                    panel.repaint();
                } else if (e.getKeyCode() == 69) { // e
                    cameraPos[1] -= 2;
                    cameraMovement[1] -= 2;
                    panel.repaint();
                } else if (e.getKeyCode() == 81) { // q
                    cameraPos[1] += 2;
                    cameraMovement[1] += 2;
                    panel.repaint();
                } else if (e.getKeyCode() == 87) { // w
                    cameraMovement[2] -= 5;
                    cameraPos[2] -= 5;
                    panel.repaint();
                } else if (e.getKeyCode() == 83) { // s
                    cameraMovement[2] += 5;
                    cameraPos[2] += 5;
                    panel.repaint();
                } else if (e.getKeyCode() == 88) {
                    Vertex[] vertices = shapes[0].getVertices();
                    double angle = -0.3;
                    shapes[0].addXAxisRotation(angle);
                    for (int i = 0; i < vertices.length; i++) {
                        rotateXAxis(vertices[i], angle, 400, 250, -50);
                    }
                    panel.repaint();
                } else if (e.getKeyCode() == 89) {
                    Vertex[] vertices = shapes[0].getVertices();
                    double angle = -0.3;
                    shapes[0].addYAxisRotation(angle);
                    for (int i = 0; i < vertices.length; i++) {
                        rotateYAxis(vertices[i], angle, 400, 250, -50);
                    }
                    panel.repaint();
                } else if (e.getKeyCode() == 90) {
                    Vertex[] vertices = shapes[0].getVertices();
                    double angle = -0.3;
                    shapes[0].addZAxisRotation(angle);
                    for (int i = 0; i < vertices.length; i++) {
                        rotateZAxis(vertices[i], angle, 400, 250, -50);
                    }
                    panel.repaint();
                } else if (e.getKeyCode() == 37) {
                    for (int a = 0; a < shapes.length; a++) {
                        Vertex[] vertices = shapes[a].getVertices();
                        double angle = -0.05;
                        shapes[0].addYAxisRotation(angle);
                        for (int i = 0; i < vertices.length; i++) {
                            rotateYAxis(vertices[i], angle, cameraPos[0], cameraPos[1], cameraPos[2]);
                        }
                    }
                    panel.repaint();
                } else if (e.getKeyCode() == 39) {
                    for (int a = 0; a < shapes.length; a++) {
                        Vertex[] vertices = shapes[a].getVertices();
                        double angle = 0.05;
                        shapes[0].addYAxisRotation(angle);
                        for (int i = 0; i < vertices.length; i++) {
                            rotateYAxis(vertices[i], angle, cameraPos[0], cameraPos[1], cameraPos[2]);
                        }
                    }
                    panel.repaint();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });

        panel = new JPanel() {

            protected void paintComponent(Graphics g) {

                g.setColor(new Color(0, 0, 0));
                g.fillRect(0, 0, defaultFrameSize[0], defaultFrameSize[1]);

                for (int s = shapes.length - 1; s >= 0; s--) {

                    Shape shape = shapes[s];

                    rayRendering(shape, 50000, g);

                    // Vertex[] vertices = shape.getVertices();

                    // // System.out.println("in shape: " + s);

                    // for (int i = 0; i < 4; i++) {

                    // g.setColor(new Color(rand.nextInt(200) + 56, rand.nextInt(200) + 56,
                    // rand.nextInt(200) + 56));

                    // drawFace(i, i + 4, (i + 1) % 4 + 4, (i + 1) % 4, vertices, g, false);
                    // // drawFace(0, 4, 5, 1, vertices, g, false);

                    // }

                    // for (int i = 0; i < 2; i++) {

                    // g.setColor(new Color(rand.nextInt(200) + 56, rand.nextInt(200) + 56,
                    // rand.nextInt(200) + 56));
                    // drawFace(0 + 4 * i, 1 + 4 * i, 2 + 4 * i, 3 + 4 * i, vertices, g, false);

                    // }

                }

            }

        }; // default layout manager is FlowLayout

        frame.add(panel);
        frame.setVisible(true);

    }

    static void rotateZAxis(Vertex vertex, double angle, int... rotateAround) {

        int px = rotateAround[0];
        int py = rotateAround[1];

        double newX = Math.cos(angle) * (vertex.getX() - px) + -Math.sin(angle) * (vertex.getY() - py) + 0;
        double newY = Math.sin(angle) * (vertex.getX() - px) + Math.cos(angle) * (vertex.getY() - py) + 0;

        newX += px;
        newY += py;

        vertex.setLocation(newX, newY, vertex.getZ());

    }

    static void rotateXAxis(Vertex vertex, double angle, int... rotateAround) {

        int py = rotateAround[1];
        int pz = rotateAround[2];

        double newY = 0 + Math.cos(angle) * (vertex.getY() - py) + -Math.sin(angle) * (vertex.getZ() - pz);
        double newZ = 0 + Math.sin(angle) * (vertex.getY() - py) + Math.cos(angle) * (vertex.getZ() - pz);

        newY += py;
        newZ += pz;

        vertex.setLocation(vertex.getX(), newY, newZ);

    }

    static void connectVertices(int index1, int index2, Vertex[] vertices, Graphics g) {
        Vertex v1 = getPerspectiveOffset(vertices[index1]);
        Vertex v2 = getPerspectiveOffset(vertices[index2]);
        if (v1.getZ() > cameraPos[2] && v2.getZ() > cameraPos[2]) {
            return;
        }
        g.drawLine((int) v1.getX(), (int) v1.getY(), (int) v2.getX(), (int) v2.getY());
    }

    static void drawFace(int index1, int index2, int index3, int index4, Vertex[] vertices, Graphics g,
            boolean shouldFill) {
        Vertex v1 = getPerspectiveOffset(vertices[index1]);
        Vertex v2 = getPerspectiveOffset(vertices[index2]);
        Vertex v3 = getPerspectiveOffset(vertices[index3]);
        Vertex v4 = getPerspectiveOffset(vertices[index4]);

        if (v1.getZ() > cameraPos[2] && v2.getZ() > cameraPos[2] && v3.getZ() > cameraPos[2]
                && v4.getZ() > cameraPos[2]) {
            return;
        }

        Polygon tri1 = new Polygon(new int[] { (int) v1.getX(), (int) v2.getX(), (int) v3.getX() },
                new int[] { (int) v1.getY(), (int) v2.getY(), (int) v3.getY() }, 3);
        Polygon tri2 = new Polygon(new int[] { (int) v3.getX(), (int) v4.getX(), (int) v1.getX() },
                new int[] { (int) v3.getY(), (int) v4.getY(), (int) v1.getY() }, 3);

        g.drawPolygon(tri1);
        g.drawPolygon(tri2);

        if (shouldFill) {
            g.fillPolygon(new Polygon(new int[] { (int) v1.getX(), (int) v2.getX(), (int) v3.getX(), (int) v4.getX() },
                    new int[] { (int) v1.getY(), (int) v2.getY(), (int) v3.getY(), (int) v4.getY() }, 4));
        }
    }

    static void rayRendering(Shape shape, int amountOfRays, Graphics g) {

        Vertex[] vertices = shape.getVertices();

        for (int i = 0; i < 4; i++) {

            Vertex v1, v2, v3, v4;
            v1 = getPerspectiveOffset(vertices[i]);
            v2 = getPerspectiveOffset(vertices[i + 4]);
            v3 = getPerspectiveOffset(vertices[(i + 1) % 4 + 4]);
            v4 = getPerspectiveOffset(vertices[(i + 1) % 4]);

            rayTriangleIntersection(v1, v2, v3, amountOfRays, g);
            rayTriangleIntersection(v3, v4, v1, amountOfRays, g);

        }

        for (int i = 0; i < 2; i++) {

            Vertex v1, v2, v3, v4;
            v1 = getPerspectiveOffset(vertices[0 + 4 * i]);
            v2 = getPerspectiveOffset(vertices[1 + 4 * i]);
            v3 = getPerspectiveOffset(vertices[2 + 4 * i]);
            v4 = getPerspectiveOffset(vertices[3 + 4 * i]);

            rayTriangleIntersection(v1, v2, v3, amountOfRays, g);
            rayTriangleIntersection(v3, v4, v1, amountOfRays, g);

        }
    }

    static void rayTriangleIntersection(Vertex a, Vertex b, Vertex c, int amountOfRays, Graphics g) {

        Random rand = new Random();

        for (int i = 0; i < amountOfRays; i++) {

            Ray camRay;
            camRay = new Ray(new Vertex(rand.nextInt(defaultFrameSize[0] + 1), rand.nextInt(defaultFrameSize[0] + 1),
                    cameraPos[2]), zAxis, -1);

            Vertex triEdge1 = vectorSubtraction(b, a);
            Vertex triEdge2 = vectorSubtraction(c, a);
            Vertex triFlatNormal = crossProduct(triEdge1, triEdge2);

            Vertex triPlaneNormal = triFlatNormal;
            Vertex triPlanePointOn = a;

            double nDotD = dotProduct(triPlaneNormal, camRay.getDirection());

            if (Math.abs(nDotD) <= 0.0001) {
                // System.out.println("returned");
                return;
            }

            double nDotPs = dotProduct(triPlaneNormal, vectorSubtraction(triPlanePointOn, camRay.startingPoint));
            camRay.setT(nDotPs / nDotD);

            Vertex planePoint = vectorAddition(camRay.startingPoint,
                    vectorMultiplication(camRay.getDirection(), camRay.t));

            Vertex aToBEdge = vectorSubtraction(b, a);
            Vertex bToCEdge = vectorSubtraction(c, b);
            Vertex cToAEdge = vectorSubtraction(a, c);

            Vertex aToPoint = vectorSubtraction(planePoint, a);
            Vertex bToPoint = vectorSubtraction(planePoint, b);
            Vertex cToPoint = vectorSubtraction(planePoint, c);

            Vertex aTestVec = crossProduct(aToBEdge, aToPoint);
            Vertex bTestVec = crossProduct(bToCEdge, bToPoint);
            Vertex cTestVec = crossProduct(cToAEdge, cToPoint);

            boolean aTestVecMatchesNormal = dotProduct(aTestVec, triFlatNormal) > 0;
            boolean bTestVecMatchesNormal = dotProduct(bTestVec, triFlatNormal) > 0;
            boolean cTestVecMatchesNormal = dotProduct(cTestVec, triFlatNormal) > 0;

            // System.out.println(aTestVecMatchesNormal);
            // System.out.println(bTestVecMatchesNormal);
            // System.out.println(cTestVecMatchesNormal);

            if (aTestVecMatchesNormal && bTestVecMatchesNormal && cTestVecMatchesNormal) {
                // System.out.println("intersected");
                g.setColor(new Color(rand.nextInt(200) + 56, rand.nextInt(200) + 56,
                        rand.nextInt(200) + 56));
                g.drawRect((int) planePoint.getX(), (int) planePoint.getY(), 1, 1);
            }

        }
    }

    static void rotateYAxis(Vertex vertex, double angle, int... rotateAround) {

        int px = rotateAround[0];
        int pz = rotateAround[2];

        double newX = Math.cos(angle) * (vertex.getX() - px) + 0 + Math.sin(angle) * (vertex.getZ() - pz);
        double newZ = -Math.sin(angle) * (vertex.getX() - px) + 0 + Math.cos(angle) * (vertex.getZ() - pz);

        newX += px;
        newZ += pz;

        vertex.setLocation(newX, vertex.getY(), newZ);

    }

    static Vertex getPerspectiveOffset(Vertex vertex) {

        double offsetPerZForX = (cameraPos[0] - vertex.getX()) / 6;
        if (offsetPerZForX < 0)
            offsetPerZForX *= -1;

        double offsetPerZForY = (cameraPos[1] - vertex.getY()) / 6;
        if (offsetPerZForY < 0)
            offsetPerZForY *= -1;

        Vertex currVertex = new Vertex(vertex.getX(), vertex.getY(), vertex.getZ());

        if (currVertex.getX() <= cameraPos[0]) {

            currVertex.setX(
                    currVertex.getX() - cameraMovement[0]
                            + offsetPerZForX * (-currVertex.getZ() + cameraMovement[2]) / 100);

        } else if (currVertex.getX() > cameraPos[0]) {

            currVertex.setX(
                    currVertex.getX() - cameraMovement[0]
                            - offsetPerZForX * (-currVertex.getZ() + cameraMovement[2]) / 100);

        }

        if (currVertex.getY() <= cameraPos[1]) {

            currVertex.setY(
                    currVertex.getY() - cameraMovement[1]
                            + offsetPerZForY * (-currVertex.getZ() + cameraMovement[2]) / 100);

        } else if (currVertex.getY() > cameraPos[1]) {

            currVertex.setY(
                    currVertex.getY() - cameraMovement[1]
                            - offsetPerZForY * (-currVertex.getZ() + cameraMovement[2]) / 100);

        }

        return currVertex;

    }

    static Vertex vectorSubtraction(Vertex v1, Vertex v2) {

        Vertex res = new Vertex();

        res.setX(v1.getX() - v2.getX());
        res.setY(v1.getY() - v2.getY());
        res.setZ(v1.getZ() - v2.getZ());

        return res;

    }

    static Vertex vectorAddition(Vertex v1, Vertex v2) {

        Vertex res = new Vertex();

        res.setX(v1.getX() + v2.getX());
        res.setY(v1.getY() + v2.getY());
        res.setZ(v1.getZ() + v2.getZ());

        return res;

    }

    static Vertex vectorMultiplication(Vertex v1, double value) {

        Vertex res = new Vertex();

        res.setX(v1.getX() * value);
        res.setY(v1.getY() * value);
        res.setZ(v1.getZ() * value);

        return res;

    }

    static double getVectorLength(Vertex vector) {
        return Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2) + Math.pow(vector.getZ(), 2));
    }

    static Vertex normalizeVector(Vertex v) {
        double l = getVectorLength(v);
        v.setLocation(v.getX() / l, v.getY() / l, v.getZ() / l);
        return v;
    }

    static double dotProduct(Vertex v1, Vertex v2) {

        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();

    }

    static Vertex crossProduct(Vertex v1, Vertex v2) {
        Vertex s = new Vertex();

        s.setX(v1.getY() * v2.getZ() - v1.getZ() * v2.getY());
        s.setY(v1.getZ() * v2.getX() - v1.getX() * v2.getZ());
        s.setZ(v1.getX() * v2.getY() - v1.getY() * v2.getX());

        return s;
    }

}
