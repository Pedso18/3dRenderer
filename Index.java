import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Index {

    static JFrame frame;
    static private JPanel panel;
    static int[] defaultFrameSize = { 800, 500 };
    static int[] cameraPos = { defaultFrameSize[0] / 2, defaultFrameSize[1] / 2, 100 };
    static int[] cameraMovement = { 0, 0, 0 };
    static Vector3 viewportPos = new Vector3(0, 0, 1);
    static int[] viewportSize = new int[] { defaultFrameSize[0], defaultFrameSize[1] };

    static int renderDistance = 200;
    static int renderQuality = 1; // the closer to 1 the better

    static Vector3 xAxis = new Vector3(1, 0, 0);
    static Vector3 yAxis = new Vector3(0, 1, 0);
    static Vector3 zAxis = new Vector3(0, 0, 1);

    static Vector3 light = new Vector3(0, 0, 0);

    volatile static Double[][] lastZRayCoords = new Double[defaultFrameSize[0]][defaultFrameSize[1]];
    volatile static Color[][] framePixels = new Color[defaultFrameSize[0]][defaultFrameSize[1]];

    static int maxFps = 15;
    static boolean canRenderNewFrame = true;

    public static void main(String[] args) {

        Shape[] shapes = new Shape[3];

        Shape cube = new Shape();
        Shape cube2 = new Shape();
        Shape ground = new Shape();

        cube.addVector3(new Vector3(-50, 50, 0));
        cube.addVector3(new Vector3(50, 50, 0));
        cube.addVector3(new Vector3(50, -50, 0));
        cube.addVector3(new Vector3(-50, -50, 0));

        cube.addVector3(new Vector3(-50, 50, -100));
        cube.addVector3(new Vector3(50, 50, -100));
        cube.addVector3(new Vector3(50, -50, -100));
        cube.addVector3(new Vector3(-50, -50, -100));

        shapes[0] = cube;
        shapes[0].setColor(new Color(20, 20, 255));

        cube2.addVector3(new Vector3(-50, 50, -200));
        cube2.addVector3(new Vector3(50, 50, -200));
        cube2.addVector3(new Vector3(50, -50, -200));
        cube2.addVector3(new Vector3(-50, -50, -200));

        cube2.addVector3(new Vector3(-50, 50, -300));
        cube2.addVector3(new Vector3(50, 50, -300));
        cube2.addVector3(new Vector3(50, -50, -300));
        cube2.addVector3(new Vector3(-50, -50, -300));

        shapes[1] = cube2;
        shapes[1].setColor(new Color(255, 20, 20));

        ground.addVector3(new Vector3(-500, 300, 500));
        ground.addVector3(new Vector3(500, 300, 500));
        ground.addVector3(new Vector3(500, 300, 500));
        ground.addVector3(new Vector3(-500, 300, 500));

        ground.addVector3(new Vector3(-500, 300, -5000));
        ground.addVector3(new Vector3(500, 300, -5000));
        ground.addVector3(new Vector3(500, 300, -5000));
        ground.addVector3(new Vector3(-500, 300, -5000));

        ground.setStatic(true);

        shapes[2] = ground;
        shapes[2].setColor(new Color(20, 255, 20));

        initGUI(shapes);

        while (true) {
            try {
                Thread.sleep(1000 / maxFps);
                canRenderNewFrame = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    static public void initGUI(Shape[] shapes) {

        frame = new JFrame(); // default layout manager is BorderLayout
        frame.setSize(defaultFrameSize[0], defaultFrameSize[1]);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 65) { // a
                    cameraPos[0] -= 6;
                    cameraMovement[0] -= 6;
                    generateFrame(shapes, false);
                } else if (e.getKeyCode() == 68) { // d
                    cameraPos[0] += 6;
                    cameraMovement[0] += 6;
                    generateFrame(shapes, false);
                } else if (e.getKeyCode() == 69) { // e
                    cameraPos[1] -= 6;
                    cameraMovement[1] -= 6;
                    generateFrame(shapes, false);
                } else if (e.getKeyCode() == 81) { // q
                    cameraPos[1] += 6;
                    cameraMovement[1] += 6;
                    generateFrame(shapes, false);
                } else if (e.getKeyCode() == 87) { // w
                    cameraMovement[2] -= 5;
                    cameraPos[2] -= 5;
                    generateFrame(shapes, false);
                } else if (e.getKeyCode() == 83) { // s
                    cameraMovement[2] += 5;
                    cameraPos[2] += 5;
                    generateFrame(shapes, false);
                } else if (e.getKeyCode() == 88) {
                    double angle = -0.3;
                    for (int s = 0; s < shapes.length; s++) {
                        if (shapes[s].getStatic() == false) {
                            Vector3 middlePoint = getShapeMiddlePoint(shapes[s]);
                            Vector3[] vertices = shapes[s].getVertices();
                            shapes[s].addXAxisRotation(angle);
                            for (int i = 0; i < vertices.length; i++) {
                                rotateXAxis(vertices[i], angle, (int) middlePoint.getX(), (int) middlePoint.getY(),
                                        (int) middlePoint.getZ());
                            }
                        }
                    }
                    generateFrame(shapes, false);
                } else if (e.getKeyCode() == 89) {
                    double angle = -0.3;
                    for (int s = 0; s < shapes.length; s++) {
                        if (shapes[s].getStatic() == false) {
                            Vector3 middlePoint = getShapeMiddlePoint(shapes[s]);
                            Vector3[] vertices = shapes[s].getVertices();
                            shapes[s].addYAxisRotation(angle);
                            for (int i = 0; i < vertices.length; i++) {
                                rotateYAxis(vertices[i], angle, (int) middlePoint.getX(), (int) middlePoint.getY(),
                                        (int) middlePoint.getZ());
                            }
                        }
                    }
                    generateFrame(shapes, false);
                } else if (e.getKeyCode() == 90) {
                    double angle = -0.3;
                    for (int s = 0; s < shapes.length; s++) {
                        if (shapes[s].getStatic() == false) {
                            Vector3 middlePoint = getShapeMiddlePoint(shapes[s]);
                            Vector3[] vertices = shapes[s].getVertices();
                            shapes[s].addZAxisRotation(angle);
                            for (int i = 0; i < vertices.length; i++) {
                                rotateZAxis(vertices[i], angle, (int) middlePoint.getX(), (int) middlePoint.getY(),
                                        (int) middlePoint.getZ());
                            }
                        }
                    }
                    generateFrame(shapes, false);
                } else if (e.getKeyCode() == 37) {
                    for (int a = 0; a < shapes.length; a++) {
                        Vector3[] vertices = shapes[a].getVertices();
                        double angle = -45;
                        shapes[0].addYAxisRotation(angle);
                        for (int i = 0; i < vertices.length; i++) {
                            rotateYAxis(vertices[i], angle, cameraPos[0], cameraPos[1], cameraPos[2]);
                        }
                    }
                    generateFrame(shapes, false);
                } else if (e.getKeyCode() == 39) {
                    for (int a = 0; a < shapes.length; a++) {
                        Vector3[] vertices = shapes[a].getVertices();
                        double angle = 45;
                        shapes[0].addYAxisRotation(angle);
                        for (int i = 0; i < vertices.length; i++) {
                            rotateYAxis(vertices[i], angle, cameraPos[0], cameraPos[1], cameraPos[2]);
                        }
                    }
                    generateFrame(shapes, false);
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                generateFrame(shapes, true);
            }

        });

        panel = new JPanel() {

            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0));
                g.fillRect(0, 0, defaultFrameSize[0], defaultFrameSize[1]);
                for (int x = 0; x < defaultFrameSize[0]; x += renderQuality) {
                    for (int y = 0; y < defaultFrameSize[1]; y += renderQuality) {
                        if (framePixels[x][y] != null) {
                            g.setColor(framePixels[x][y]);
                            g.fillRect(x, y, renderQuality, renderQuality);
                        }
                    }
                }
            }

        };

        generateFrame(shapes, true);

        frame.add(panel);
        frame.setVisible(true);

    }

    static void rotateZAxis(Vector3 vertex, double angle, int... rotateAround) {

        int px = rotateAround[0];
        int py = rotateAround[1];

        double newX = Math.cos(angle) * (vertex.getX() - px) + -Math.sin(angle) * (vertex.getY() - py) + 0;
        double newY = Math.sin(angle) * (vertex.getX() - px) + Math.cos(angle) * (vertex.getY() - py) + 0;

        newX += px;
        newY += py;

        vertex.setLocation(newX, newY, vertex.getZ());

    }

    static void rotateXAxis(Vector3 vertex, double angle, int... rotateAround) {

        int py = rotateAround[1];
        int pz = rotateAround[2];

        double newY = 0 + Math.cos(angle) * (vertex.getY() - py) + -Math.sin(angle) * (vertex.getZ() - pz);
        double newZ = 0 + Math.sin(angle) * (vertex.getY() - py) + Math.cos(angle) * (vertex.getZ() - pz);

        newY += py;
        newZ += pz;

        vertex.setLocation(vertex.getX(), newY, newZ);

    }

    static void connectVertices(int index1, int index2, Vector3[] vertices, Graphics g) {
        Vector3 v1 = getPerspectiveOffset(vertices[index1], false);
        Vector3 v2 = getPerspectiveOffset(vertices[index2], false);
        if (v1.getZ() > cameraPos[2] && v2.getZ() > cameraPos[2]) {
            return;
        }
        g.drawLine((int) v1.getX(), (int) v1.getY(), (int) v2.getX(), (int) v2.getY());
    }

    static void drawFace(int index1, int index2, int index3, int index4, Vector3[] vertices, Graphics g,
            boolean shouldFill) {
        Vector3 v1 = getPerspectiveOffset(vertices[index1], false);
        Vector3 v2 = getPerspectiveOffset(vertices[index2], false);
        Vector3 v3 = getPerspectiveOffset(vertices[index3], false);
        Vector3 v4 = getPerspectiveOffset(vertices[index4], false);

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

    static void generateFrame(Shape[] shapes, boolean forceRender) {

        if (!canRenderNewFrame && !forceRender) {
            return;
        }

        canRenderNewFrame = false;

        lastZRayCoords = new Double[defaultFrameSize[0]][defaultFrameSize[1]];
        framePixels = new Color[defaultFrameSize[0]][defaultFrameSize[1]];

        RenderingThread[] renderingThreads = new RenderingThread[2];

        renderingThreads[0] = new RenderingThread(lastZRayCoords, cameraPos, defaultFrameSize,
                light, shapes, cameraMovement, framePixels, panel, true);

        renderingThreads[1] = new RenderingThread(lastZRayCoords, cameraPos, defaultFrameSize,
                light, shapes, cameraMovement, framePixels, panel, false);

        renderingThreads[0].start();
        renderingThreads[1].start();

    }

    static void rotateYAxis(Vector3 vertex, double angle, int... rotateAround) {

        int px = rotateAround[0];
        int pz = rotateAround[2];

        double newX = Math.cos(angle) * (vertex.getX() - px) + 0 + Math.sin(angle) * (vertex.getZ() - pz);
        double newZ = -Math.sin(angle) * (vertex.getX() - px) + 0 + Math.cos(angle) * (vertex.getZ() - pz);

        newX += px;
        newZ += pz;

        vertex.setLocation(newX, vertex.getY(), newZ);

    }

    static Vector3 getPerspectiveOffset(Vector3 vertex, boolean isStatic) {

        Vector3 currVector3 = new Vector3(vertex.getX(), vertex.getY(), vertex.getZ());

        double z = Math.abs(currVector3.getZ());

        currVector3.setX(
                (currVector3.getX() - (isStatic == true ? 0 : cameraMovement[0])) / ((z + cameraPos[2]) / 1000 + 1)
                        + defaultFrameSize[0] / 2);
        currVector3.setY(
                (currVector3.getY() - (isStatic == true ? 0 : cameraMovement[1])) / ((z + cameraPos[2]) / 1000 + 1)
                        + defaultFrameSize[1] / 2);

        return currVector3;

    }

    static Vector3 vectorSubtraction(Vector3 v1, Vector3 v2) {

        Vector3 res = new Vector3();

        res.setX(v1.getX() - v2.getX());
        res.setY(v1.getY() - v2.getY());
        res.setZ(v1.getZ() - v2.getZ());

        return res;

    }

    static Vector3 vectorAddition(Vector3 v1, Vector3 v2) {

        Vector3 res = new Vector3();

        res.setX(v1.getX() + v2.getX());
        res.setY(v1.getY() + v2.getY());
        res.setZ(v1.getZ() + v2.getZ());

        return res;

    }

    static Vector3 vectorMultiplication(Vector3 v1, double value) {

        Vector3 res = new Vector3();

        res.setX(v1.getX() * value);
        res.setY(v1.getY() * value);
        res.setZ(v1.getZ() * value);

        return res;

    }

    static double getVectorLength(Vector3 vector) {
        return Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2) + Math.pow(vector.getZ(), 2));
    }

    static Vector3 normalizeVector(Vector3 v) {
        double l = getVectorLength(v);
        v.setLocation(v.getX() / l, v.getY() / l, v.getZ() / l);
        return v;
    }

    static double dotProduct(Vector3 v1, Vector3 v2) {

        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();

    }

    static Vector3 crossProduct(Vector3 v1, Vector3 v2) {
        Vector3 s = new Vector3();

        s.setX(v1.getY() * v2.getZ() - v1.getZ() * v2.getY());
        s.setY(v1.getZ() * v2.getX() - v1.getX() * v2.getZ());
        s.setZ(v1.getX() * v2.getY() - v1.getY() * v2.getX());

        return s;
    }

    static double getDistance(Vector3 a, Vector3 b) {
        return Math.sqrt(
                Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2) + Math.pow(a.getZ() - b.getZ(), 2));
    }

    static Vector3 getShapeMiddlePoint(Shape s) {
        int mx = (int) ((s.getBiggestXPoint().getX() + s.getSmallestXPoint().getX()) / 2);
        int my = (int) ((s.getBiggestYPoint().getY() + s.getSmallestYPoint().getY()) / 2);
        int mz = (int) ((s.getBiggestZPoint().getZ() + s.getSmallestZPoint().getZ()) / 2);

        return new Vector3(mx, my, mz);
    }

}
