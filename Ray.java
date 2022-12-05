public class Ray {
    Vector3 startingPoint;
    Vector3 direction;
    double t;

    Ray(Vector3 startingPoint, Vector3 direction, double t) {
        this.startingPoint = startingPoint;
        this.direction = direction;
        this.t = t;
    }

    public double getT() {
        return t;
    }

    public Vector3 getDirection() {
        return direction;
    }

    public void setStartingPoint(Vector3 startingPoint) {
        this.startingPoint = startingPoint;
    }

    public void setT(double t) {
        this.t = t;
    }

    public void setDirection(Vector3 direction) {
        this.direction = direction;
    }

}
