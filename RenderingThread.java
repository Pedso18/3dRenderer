import java.awt.Color;
import javax.swing.JPanel;

public class RenderingThread extends Thread {

    Double[][] lastZRayCoords;
    int[] cameraPos;
    int[] defaultFrameSize;
    Vector3 light;
    Shape[] shapes;
    int[] cameraMovement;
    Color[][] framePixels;
    Shape shape;
    JPanel panel;
    boolean isEven;

    Vector3 zAxis = new Vector3(0, 0, 1);

    RenderingThread(Double[][] lastZRayCoords, int[] cameraPos,
            int[] defaultFrameSize, Vector3 light,
            Shape[] shapes, int[] cameraMovement, Color[][] framePixels, JPanel panel, boolean isEven) {

        this.lastZRayCoords = lastZRayCoords;
        this.cameraPos = cameraPos;
        this.defaultFrameSize = defaultFrameSize;
        this.light = light;
        this.shapes = shapes;
        this.cameraMovement = cameraMovement;
        this.framePixels = framePixels;
        this.panel = panel;
        this.isEven = isEven;
    }

    @Override
    public void run() {

        for (Shape shape : shapes) {

            this.shape = shape;
            Vector3[] vertices = shape.getVertices();

            for (int i = 0; i < 4; i++) {

                Vector3 v1, v2, v3, v4;
                v1 = getPerspectiveOffset(vertices[i], shape.getStatic());
                v2 = getPerspectiveOffset(vertices[i + 4], shape.getStatic());
                v3 = getPerspectiveOffset(vertices[(i + 1) % 4 + 4], shape.getStatic());
                v4 = getPerspectiveOffset(vertices[(i + 1) % 4], shape.getStatic());

                if (v1.getX() > -200 && v2.getX() > -200 && v3.getX() > -200 && v1.getX() < defaultFrameSize[0] + 200
                        && v2.getX() < defaultFrameSize[0] + 200 && v3.getX() < defaultFrameSize[0] + 200
                        && v1.getY() > -200
                        && v2.getY() > -200 && v3.getY() > -200 && v1.getY() < defaultFrameSize[1] + 200
                        && v2.getY() < defaultFrameSize[1] + 200 && v3.getY() < defaultFrameSize[1] + 200) {
                    rayTriangleIntersection(v1, v2, v3, shape.color);
                }
                if (v1.getX() > -200 && v4.getX() > -200 && v3.getX() > -200 && v1.getX() < defaultFrameSize[0] + 200
                        && v4.getX() < defaultFrameSize[0] + 200 && v3.getX() < defaultFrameSize[0] + 200
                        && v1.getY() > -200
                        && v4.getY() > -200 && v3.getY() > -200 && v1.getY() < defaultFrameSize[1] + 200
                        && v4.getY() < defaultFrameSize[1] + 200 && v3.getY() < defaultFrameSize[1] + 200) {
                    rayTriangleIntersection(v3, v4, v1, shape.color);
                }

            }

            for (int i = 0; i < 2; i++) {

                Vector3 v1, v2, v3, v4;
                v1 = getPerspectiveOffset(vertices[0 + 4 * i], shape.getStatic());
                v2 = getPerspectiveOffset(vertices[1 + 4 * i], shape.getStatic());
                v3 = getPerspectiveOffset(vertices[2 + 4 * i], shape.getStatic());
                v4 = getPerspectiveOffset(vertices[3 + 4 * i], shape.getStatic());

                if (v1.getX() > -200 && v2.getX() > -200 && v3.getX() > -200 && v1.getX() < defaultFrameSize[0] + 200
                        && v2.getX() < defaultFrameSize[0] + 200 && v3.getX() < defaultFrameSize[0] + 200
                        && v1.getY() > -200
                        && v2.getY() > -200 && v3.getY() > -200 && v1.getY() < defaultFrameSize[1] + 200
                        && v2.getY() < defaultFrameSize[1] + 200 && v3.getY() < defaultFrameSize[1] + 200) {
                    rayTriangleIntersection(v1, v2, v3, shape.color);
                }
                if (v1.getX() > -200 && v4.getX() > -200 && v3.getX() > -200 && v1.getX() < defaultFrameSize[0] + 200
                        && v4.getX() < defaultFrameSize[0] + 200 && v3.getX() < defaultFrameSize[0] + 200
                        && v1.getY() > -200
                        && v4.getY() > -200 && v3.getY() > -200 && v1.getY() < defaultFrameSize[1] + 200
                        && v4.getY() < defaultFrameSize[1] + 200 && v3.getY() < defaultFrameSize[1] + 200) {
                    rayTriangleIntersection(v3, v4, v1, shape.color);
                }

            }

            panel.repaint();

        }
    }

    void setXYMinMax(int[] miniMaxX, int[] miniMaxY, Vector3 b, Vector3 c) {
        if ((int) b.getX() > miniMaxX[1]) {
            miniMaxX[1] = (int) b.getX();
        }

        if ((int) c.getX() > miniMaxX[1]) {
            miniMaxX[1] = (int) c.getX();
        }

        if ((int) b.getY() > miniMaxY[1]) {
            miniMaxY[1] = (int) b.getY();
        }

        if ((int) c.getY() > miniMaxY[1]) {
            miniMaxY[1] = (int) c.getY();
        }

        if ((int) b.getX() < miniMaxX[0]) {
            miniMaxX[0] = (int) b.getX();
        }

        if ((int) c.getX() < miniMaxX[0]) {
            miniMaxX[0] = (int) c.getX();
        }

        if ((int) b.getY() < miniMaxY[0]) {
            miniMaxY[0] = (int) b.getY();
        }

        if ((int) c.getY() < miniMaxY[0]) {
            miniMaxY[0] = (int) c.getY();
        }
    }

    void rayTriangleIntersection(Vector3 a, Vector3 b, Vector3 c,
            Color shapeColor) {

        int[] miniMaxX = { (int) a.getX(), (int) a.getX() };
        int[] miniMaxY = { (int) a.getY(), (int) a.getY() };

        setXYMinMax(miniMaxX, miniMaxY, b, c);

        for (int x = miniMaxX[0]; x < miniMaxX[1]; x++) {

            if (x % 2 == 0 && !isEven) {
                continue;
            }

            boolean hasLastHit = false;

            for (int y = miniMaxY[0]; y < miniMaxY[1]; y++) {

                if (y % 2 == 0 && !isEven) {
                    continue;
                }

                Ray camRay = new Ray(new Vector3(x, // makes sure that rays will only
                        y, cameraPos[2]), zAxis, -1); // be calculated if close to the triangle. To
                // render reflections
                // in the future I can get a line from the camera to the rendered
                // dot

                Vector3 triEdge1 = vectorSubtraction(b, a);
                Vector3 triEdge2 = vectorSubtraction(c, a);
                Vector3 triFlatNormal = crossProduct(triEdge1, triEdge2);

                Vector3 triPlaneNormal = triFlatNormal;
                Vector3 triPlanePointOn = a;

                double nDotD = dotProduct(triPlaneNormal, camRay.getDirection());

                if (Math.abs(nDotD) <= 0.0001) {
                    return;
                }

                double nDotPs = dotProduct(triPlaneNormal, vectorSubtraction(triPlanePointOn,
                        camRay.startingPoint));
                camRay.setT(nDotPs / nDotD);

                Vector3 planePoint = vectorAddition(camRay.startingPoint,
                        vectorMultiplication(camRay.getDirection(), camRay.t));

                if (planePoint.getX() >= 0 && planePoint.getX() < defaultFrameSize[0] &&
                        planePoint.getY() >= 0 && planePoint.getY() < defaultFrameSize[1]) {
                    if (lastZRayCoords[(int) planePoint.getX()][(int) planePoint.getY()] != null
                            && lastZRayCoords[(int) planePoint.getX()][(int) planePoint.getY()] >= planePoint.getZ()) {
                        continue;
                    }
                } else {
                    continue;
                }

                Vector3 bToCEdge = vectorSubtraction(c, b);
                Vector3 cToAEdge = vectorSubtraction(a, c);

                Vector3 aToPoint = vectorSubtraction(planePoint, a);
                Vector3 bToPoint = vectorSubtraction(planePoint, b);
                Vector3 cToPoint = vectorSubtraction(planePoint, c);

                Vector3 aTestVec = crossProduct(triEdge1, aToPoint);
                Vector3 bTestVec = crossProduct(bToCEdge, bToPoint);
                Vector3 cTestVec = crossProduct(cToAEdge, cToPoint);

                boolean aTestVecMatchesNormal = dotProduct(aTestVec, triFlatNormal) > 0;
                boolean bTestVecMatchesNormal = dotProduct(bTestVec, triFlatNormal) > 0;
                boolean cTestVecMatchesNormal = dotProduct(cTestVec, triFlatNormal) > 0;

                if (aTestVecMatchesNormal && bTestVecMatchesNormal && cTestVecMatchesNormal) {

                    hasLastHit = true;

                    lastZRayCoords[(int) planePoint.getX()][(int) planePoint.getY()] = planePoint.getZ();

                    double distanceToLight = getDistance(planePoint, light);

                    int red = shape.getColor().getRed() * 400 / ((int) distanceToLight + 1);
                    int green = shape.getColor().getGreen() * 400 / ((int) distanceToLight + 1);
                    int blue = shape.getColor().getBlue() * 400 / ((int) distanceToLight + 1);

                    if (red > 255) {
                        red = 255;
                    }
                    if (green > 255) {
                        green = 255;
                    }
                    if (blue > 255) {
                        blue = 255;
                    }

                    framePixels[x][y] = new Color(red, green, blue);

                } else if (hasLastHit) {
                    break;
                }
            }

        }
    }

    Vector3 getPerspectiveOffset(Vector3 vertex, boolean isStatic) {

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

    Vector3 vectorSubtraction(Vector3 v1, Vector3 v2) {

        Vector3 res = new Vector3();

        res.setX(v1.getX() - v2.getX());
        res.setY(v1.getY() - v2.getY());
        res.setZ(v1.getZ() - v2.getZ());

        return res;

    }

    Vector3 vectorAddition(Vector3 v1, Vector3 v2) {

        Vector3 res = new Vector3();

        res.setX(v1.getX() + v2.getX());
        res.setY(v1.getY() + v2.getY());
        res.setZ(v1.getZ() + v2.getZ());

        return res;

    }

    Vector3 vectorMultiplication(Vector3 v1, double value) {

        Vector3 res = new Vector3();

        res.setX(v1.getX() * value);
        res.setY(v1.getY() * value);
        res.setZ(v1.getZ() * value);

        return res;

    }

    Vector3 normalizeVector(Vector3 v) {
        double l = getVectorLength(v);
        v.setLocation(v.getX() / l, v.getY() / l, v.getZ() / l);
        return v;
    }

    double getVectorLength(Vector3 vector) {
        return Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2) + Math.pow(vector.getZ(), 2));
    }

    double dotProduct(Vector3 v1, Vector3 v2) {

        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();

    }

    Vector3 crossProduct(Vector3 v1, Vector3 v2) {
        Vector3 s = new Vector3();

        s.setX(v1.getY() * v2.getZ() - v1.getZ() * v2.getY());
        s.setY(v1.getZ() * v2.getX() - v1.getX() * v2.getZ());
        s.setZ(v1.getX() * v2.getY() - v1.getY() * v2.getX());

        return s;
    }

    double getDistance(Vector3 a, Vector3 b) {
        return Math.sqrt(
                Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2) + Math.pow(a.getZ() - b.getZ(), 2));
    }
}
