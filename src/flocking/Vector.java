package flocking;

public class Vector {
    private float x;
    private float y;
    private float z;

    Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    private void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    float mag() {
        return (float) Math.sqrt((double) (this.x * this.x + this.y * this.y + this.z * this.z));
    }

    private float magSq() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector add(Vector v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    void sub(Vector v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    static Vector sub(Vector v1, Vector v2) {
        return sub(v1, v2, null);
    }

    private static Vector sub(Vector v1, Vector v2, Vector target) {
        if (target == null) {
            target = new Vector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        } else {
            target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
        }

        return target;
    }

    void mult(float n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }

    void div(float n) {
        this.x /= n;
        this.y /= n;
        this.z /= n;
    }

    static float dist(Vector v1, Vector v2) {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        float dz = v1.z - v2.z;
        return (float) Math.sqrt((double) (dx * dx + dy * dy + dz * dz));
    }

    public float heading() {
        return (float) Math.atan2((double) this.y, (double) this.x);
    }

    void normalize() {
        float m = this.mag();
        if (m != 0.0F && m != 1.0F) this.div(m);

    }

    void limit(float max) {
        if (this.magSq() > max * max) {
            this.normalize();
            this.mult(max);
        }

    }

    public String toString() {
        return "[ " + this.x + ", " + this.y + ", " + this.z + " ]";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        } else {
            Vector p = (Vector) obj;
            return this.x == p.x && this.y == p.y && this.z == p.z;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
