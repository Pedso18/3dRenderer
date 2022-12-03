public class Ray {
    Vertex startingPoint;
    Vertex direction;
    double t;

    Ray(Vertex startingPoint, Vertex direction, double t) {
        this.startingPoint = startingPoint;
        this.direction = direction;
        this.t = t;
    }

    public double getT() {
        return t;
    }

    public Vertex getDirection() {
        return direction;
    }

    public void setStartingPoint(Vertex startingPoint) {
        this.startingPoint = startingPoint;
    }

    public void setT(double t) {
        this.t = t;
    }

    public void setDirection(Vertex direction) {
        this.direction = direction;
    }

}
