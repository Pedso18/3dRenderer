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

    static boolean hasHeld = false;
    static boolean isClicking = false;

    public static void main(String[] args) {

        Shape[] shapes = new Shape[3];

        Shape cube = new Shape(
                new Vector3(-50, 50, 0),
                new Vector3(50, 50, 0),
                new Vector3(50, -50, 0),
                new Vector3(-50, -50, 0),
                new Vector3(-50, 50, -100),
                new Vector3(50, 50, -100),
                new Vector3(50, -50, -100),
                new Vector3(-50, -50, -100));

        cube.setPathToTexture("./dirt.png");

        shapes[0] = cube;
        shapes[0].setColor(new Color(20, 20, 255));

        Shape cube2 = new Shape(
                new Vector3(-50, 50, -200),
                new Vector3(50, 50, -200),
                new Vector3(50, -50, -200),
                new Vector3(-50, -50, -200),
                new Vector3(-50, 50, -300),
                new Vector3(50, 50, -300),
                new Vector3(50, -50, -300),
                new Vector3(-50, -50, -300));

        shapes[1] = cube2;
        shapes[1].setColor(new Color(255, 20, 20));

        Shape ground = new Shape(
                new Vector3(-500, 300, 500),
                new Vector3(500, 300, 500),
                new Vector3(500, 300, 500),
                new Vector3(-500, 300, 500),
                new Vector3(-500, 300, -5000),
                new Vector3(500, 300, -5000),
                new Vector3(500, 300, -5000),
                new Vector3(-500, 300, -5000));

        ground.setStatic(true);

        shapes[2] = ground;
        shapes[2].setColor(new Color(255, 255, 255));

        // leftWall.addVector3(new Vector3(-500, 300, 500));
        // leftWall.addVector3(new Vector3(-500, -300, 500));
        // leftWall.addVector3(new Vector3(-500, -300, 500));
        // leftWall.addVector3(new Vector3(-500, 300, 500));

        // leftWall.addVector3(new Vector3(-500, 300, -5000));
        // leftWall.addVector3(new Vector3(-500, -300, -5000));
        // leftWall.addVector3(new Vector3(-500, -300, -5000));
        // leftWall.addVector3(new Vector3(-500, 300, -5000));

        // leftWall.setStatic(true);
        // leftWall.setColor(new Color(255, 255, 255));

        // shapes[3] = leftWall;

        // rightWall.addVector3(new Vector3(500, 300, 500));
        // rightWall.addVector3(new Vector3(500, -300, 500));
        // rightWall.addVector3(new Vector3(500, -300, 500));
        // rightWall.addVector3(new Vector3(500, 300, 500));

        // rightWall.addVector3(new Vector3(500, 300, -5000));
        // rightWall.addVector3(new Vector3(500, -300, -5000));
        // rightWall.addVector3(new Vector3(500, -300, -5000));
        // rightWall.addVector3(new Vector3(500, 300, -5000));

        // rightWall.setStatic(true);
        // rightWall.setColor(new Color(255, 255, 255));

        // shapes[4] = rightWall;

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
                if (isClicking) {
                    hasHeld = true;
                }
                isClicking = true;
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
                                rotateXAxis(vertices[i], angle, middlePoint.getX(), middlePoint.getY(),
                                        middlePoint.getZ());
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
                isClicking = false;
                if (hasHeld) {
                    hasHeld = false;
                    generateFrame(shapes, true);
                }
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

    static void rotateZAxis(Vector3 vertex, double angle, double... rotateAround) {

        double px = rotateAround[0];
        double py = rotateAround[1];

        double newX = Math.cos(angle) * (vertex.getX() - px) + -Math.sin(angle) * (vertex.getY() - py) + 0;
        double newY = Math.sin(angle) * (vertex.getX() - px) + Math.cos(angle) * (vertex.getY() - py) + 0;

        newX += px;
        newY += py;

        vertex.setLocation(newX, newY, vertex.getZ());

    }

    static void rotateXAxis(Vector3 vertex, double angle, double... rotateAround) {

        double py = rotateAround[1];
        double pz = rotateAround[2];

        double newY = 0 + Math.cos(angle) * (vertex.getY() - py) + -Math.sin(angle) * (vertex.getZ() - pz);
        double newZ = 0 + Math.sin(angle) * (vertex.getY() - py) + Math.cos(angle) * (vertex.getZ() - pz);

        newY += py;
        newZ += pz;

        vertex.setLocation(vertex.getX(), newY, newZ);

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

    static void rotateYAxis(Vector3 vertex, double angle, double... rotateAround) {

        double px = rotateAround[0];
        double pz = rotateAround[2];

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

    static Vector3 getShapeMiddlePoint(Shape s) {

        double mx = (s.getBiggestXPoint().getX() + s.getSmallestXPoint().getX()) / 2;
        double my = (s.getBiggestYPoint().getY() + s.getSmallestYPoint().getY()) / 2;
        double mz = (s.getBiggestZPoint().getZ() + s.getSmallestZPoint().getZ()) / 2;

        return new Vector3(mx, my, mz);
    }

}
