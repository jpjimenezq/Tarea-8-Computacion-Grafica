public class Punto {
    public double x;
    public double y;
    public double z;

    public Punto() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public Punto(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
