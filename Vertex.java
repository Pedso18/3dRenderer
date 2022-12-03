public class Vertex {

    private double x;
    private double y;
    private double z;

    Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vertex(Vertex v) {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
    }

    Vertex() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public void setLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double[] getLocation() {

        return new double[] { x, y, z };

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void printValues() {
        System.out.println("x: " + x);
        System.out.println("y: " + y);
        System.out.println("z: " + z);
    }

}