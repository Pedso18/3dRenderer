import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class Index {

    static JFrame frame;
    static private JPanel panel;
    static int[] defaultFrameSize = { 800, 500 };
    static int[] cameraPos = { defaultFrameSize[0] / 2, defaultFrameSize[1] / 2, 1200 };
    static int[] cameraMovement = { 0, 0, 0 };

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

        // parallelepiped.addVertex(new Vertex(650, 200, -100));
        // parallelepiped.addVertex(new Vertex(450, 200, -100));
        // parallelepiped.addVertex(new Vertex(650, 300, -100));
        // parallelepiped.addVertex(new Vertex(450, 300, -100));

        // parallelepiped.addVertex(new Vertex(650, 200, -200));
        // parallelepiped.addVertex(new Vertex(450, 200, -200));
        // parallelepiped.addVertex(new Vertex(650, 300, -200));
        // parallelepiped.addVertex(new Vertex(450, 300, -200));

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
                    for (int i = 0; i < vertices.length; i++) {
                        rotateXAxis(vertices[i], -0.3, 400, 250, -50);
                    }
                    panel.repaint();
                } else if (e.getKeyCode() == 89) {
                    Vertex[] vertices = shapes[0].getVertices();
                    for (int i = 0; i < vertices.length; i++) {
                        rotateYAxis(vertices[i], -0.3, 400, 250, -50);
                    }
                    panel.repaint();
                } else if (e.getKeyCode() == 90) {
                    Vertex[] vertices = shapes[0].getVertices();
                    for (int i = 0; i < vertices.length; i++) {
                        rotateZAxis(vertices[i], -0.3, 400, 250, -50);
                    }
                    panel.repaint();
                } else if (e.getKeyCode() == 37) {
                    Vertex[] vertices = shapes[0].getVertices();
                    for (int i = 0; i < vertices.length; i++) {
                        rotateYAxis(vertices[i], -0.05, cameraPos[0], cameraPos[1], cameraPos[2]);
                    }
                    panel.repaint();
                } else if (e.getKeyCode() == 39) {
                    Vertex[] vertices = shapes[0].getVertices();
                    for (int i = 0; i < vertices.length; i++) {
                        rotateYAxis(vertices[i], 0.05, cameraPos[0], cameraPos[1], cameraPos[2]);
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

                    Vertex[] vertices = shape.getVertices();

                    g.setColor(new Color(rand.nextInt(200) + 56, rand.nextInt(200) + 56, rand.nextInt(200) + 56));

                    for (int i = 0; i < 4; i++) {
                        connectVertices(i, (i + 1) % 4, vertices, g);
                        connectVertices(i + 4, (i + 1) % 4 + 4, vertices, g);
                        connectVertices(i, i + 4, vertices, g);
                    }

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

}
