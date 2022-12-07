import java.awt.Color;
import java.awt.Graphics;

public class RenderingThread extends Thread {

    Double[][] lastZRayCoords;
    int[] miniMaxX;
    int[] miniMaxY;
    int renderQuality;
    int[] cameraPos;
    int[] defaultFrameSize;
    Color shapeColor;
    Graphics g;
    Vector3 light;
    Vector3 a;
    Vector3 b;
    Vector3 c;

    Vector3 zAxis = new Vector3(0, 0, 1);

    RenderingThread(Double[][] lastZRayCoords, int[] miniMaxX, int[] miniMaxY, int renderQuality, int[] cameraPos,
            int[] defaultFrameSize, Color shapeColor, Graphics g, Vector3 light, Vector3 a, Vector3 b, Vector3 c) {

        this.lastZRayCoords = lastZRayCoords;
        this.miniMaxX = miniMaxX;
        this.miniMaxY = miniMaxY;
        this.renderQuality = renderQuality;
        this.cameraPos = cameraPos;
        this.defaultFrameSize = defaultFrameSize;
        this.shapeColor = shapeColor;
        this.g = g;
        this.light = light;
        this.a = a;
        this.b = b;
        this.c = c;

    }

    @Override
    public void run() {
        for (int x = miniMaxX[0]; x < miniMaxX[1]; x += renderQuality) {
            for (int y = miniMaxY[0]; y < miniMaxY[1]; y += renderQuality) {

                Ray camRay;
                camRay = new Ray(new Vector3(x, // makes sure that rays will only
                        y, cameraPos[2]), zAxis, -1); // be calculated if close to the triangle. To render reflections
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

                double nDotPs = dotProduct(triPlaneNormal, vectorSubtraction(triPlanePointOn, camRay.startingPoint));
                camRay.setT(nDotPs / nDotD);

                Vector3 planePoint = vectorAddition(camRay.startingPoint,
                        vectorMultiplication(camRay.getDirection(), camRay.t));

                if (planePoint.getX() >= 0 && planePoint.getX() < defaultFrameSize[0] &&
                        planePoint.getY() >= 0 && planePoint.getY() < defaultFrameSize[1]) {
                    if (lastZRayCoords[(int) planePoint.getX()][(int) planePoint.getY()] != null
                            && lastZRayCoords[(int) planePoint.getX()][(int) planePoint.getY()] > planePoint.getZ()) {
                        return;
                    }
                } else {
                    return;
                }

                Vector3 aToBEdge = vectorSubtraction(b, a);
                Vector3 bToCEdge = vectorSubtraction(c, b);
                Vector3 cToAEdge = vectorSubtraction(a, c);

                Vector3 aToPoint = vectorSubtraction(planePoint, a);
                Vector3 bToPoint = vectorSubtraction(planePoint, b);
                Vector3 cToPoint = vectorSubtraction(planePoint, c);

                Vector3 aTestVec = crossProduct(aToBEdge, aToPoint);
                Vector3 bTestVec = crossProduct(bToCEdge, bToPoint);
                Vector3 cTestVec = crossProduct(cToAEdge, cToPoint);

                boolean aTestVecMatchesNormal = dotProduct(aTestVec, triFlatNormal) > 0;
                boolean bTestVecMatchesNormal = dotProduct(bTestVec, triFlatNormal) > 0;
                boolean cTestVecMatchesNormal = dotProduct(cTestVec, triFlatNormal) > 0;

                if (aTestVecMatchesNormal && bTestVecMatchesNormal && cTestVecMatchesNormal) {

                    // lastZRayCoords[(int) planePoint.getX()][(int) planePoint.getY()] =
                    // planePoint.getZ();

                    double distanceToLight = getDistance(planePoint, light);

                    int red = shapeColor.getRed() * 400 / ((int) distanceToLight + 1);
                    int green = shapeColor.getGreen() * 400 / ((int) distanceToLight + 1);
                    int blue = shapeColor.getBlue() * 400 / ((int) distanceToLight + 1);

                    if (red > 255) {
                        red = 255;
                    }
                    if (green > 255) {
                        green = 255;
                    }
                    if (blue > 255) {
                        blue = 255;
                    }
                    System.out.println("collided");
                    g.setColor(new Color(red, green, blue));
                    g.fillRect((int) planePoint.getX(), (int) planePoint.getY(), renderQuality, renderQuality);
                }
            }

        }
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

    static Vector3 normalizeVector(Vector3 v) {
        double l = getVectorLength(v);
        v.setLocation(v.getX() / l, v.getY() / l, v.getZ() / l);
        return v;
    }

    static double getVectorLength(Vector3 vector) {
        return Math.sqrt(Math.pow(vector.getX(), 2) + Math.pow(vector.getY(), 2) + Math.pow(vector.getZ(), 2));
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
}
