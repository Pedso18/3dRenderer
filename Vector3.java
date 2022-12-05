public class Vector3 {

    private double x;
    private double y;
    private double z;

    Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector3(Vector3 v) {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
    }

    Vector3() {
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