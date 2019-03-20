package flocking;

import configuration.Configuration;

import java.util.List;


public class Bird {

    private Vector position;
    private Vector velocity;
    private Vector acceleration;

    public Bird(float x, float y, float angle) {
        acceleration = new Vector(0, 0);
        velocity = new Vector((float) Math.cos(angle), (float) Math.sin(angle));
        position = new Vector(x, y);
    }

    public void run(List<Bird> birds, Vector mouseClick) {
        flock(birds, mouseClick);
        update();
        borders();
    }

    private void applyForce(Vector force) {
        acceleration.add(force);
    }

    private void flock(List<Bird> Birds, Vector mouseClick) {
        Vector sep = separate(Birds);
        Vector ali = align(Birds);
        Vector coh = cohesion(Birds);
        Vector mos = mouse(mouseClick);

        sep.mult(1.0f);
        ali.mult(0.4f);
        coh.mult(0.2f);
        mos.mult(3.0f);

        applyForce(sep);
        applyForce(ali);
        applyForce(coh);
        applyForce(mos);
    }

    private void update() {
        velocity.add(acceleration);
        velocity.limit(Configuration.maxSpeed);
        position.add(velocity);
        acceleration.mult(0);
    }

    private Vector mouse(Vector mouseClick) {
        Vector steer = new Vector(0, 0);

        float d = Vector.dist(this.position, mouseClick);
        if ((d > 0) && (d < Configuration.MOUSE_SEPARATION * 2)) {
            Vector diff = Vector.sub(this.position, mouseClick);
            diff.normalize();
            diff.div(d);
            steer.add(diff);
        }

        return adjustSteerVector(steer);
    }

    private Vector seek(Vector target) {
        Vector desired = Vector.sub(target, position);

        return adjustVectorSum(desired);
    }

    private void borders() {
        if (position.getX() < -Configuration.radiusBird) position.setX(Configuration.width + Configuration.radiusBird);
        if (position.getY() < -Configuration.radiusBird) position.setY(Configuration.height + Configuration.radiusBird);
        if (position.getX() > Configuration.width + Configuration.radiusBird) position.setX(-Configuration.radiusBird);
        if (position.getY() > Configuration.height + Configuration.radiusBird) position.setY(-Configuration.radiusBird);
    }

    public Vector separate(List<Bird> birds) {
        Vector steer = new Vector(0, 0, 0);
        int count = 0;
        for (Bird other : birds) {
            float d = Vector.dist(position, other.position);
            if ((d > 0) && (d < Configuration.desiredSeparation)) {
                Vector diff = Vector.sub(position, other.position);
                diff.normalize();
                diff.div(d);
                steer.add(diff);
                count++;
            }
        }

        if (count > 0) {
            steer.div((float) count);
        }

        return adjustSteerVector(steer);
    }

    private Vector adjustSteerVector(Vector steer) {
        if (steer.mag() > 0) {
            steer.normalize();
            steer.mult(Configuration.maxSpeed);
            steer.sub(velocity);
            steer.limit(Configuration.maxForce);
        }
        return steer;
    }

    private Vector align(List<Bird> birds) {
        Vector sum = new Vector(0, 0);
        int count = 0;
        for (Bird other : birds) {
            float d = Vector.dist(position, other.position);
            if ((d > 0) && (d < Configuration.neighborDist)) {
                sum.add(other.velocity);
                count++;
            }
        }
        if (count > 0) {
            sum.div((float) count);
            return adjustVectorSum(sum);
        } else {
            return new Vector(0, 0);
        }
    }

    private Vector adjustVectorSum(Vector sum) {
        sum.normalize();
        sum.mult(Configuration.maxSpeed);
        Vector steer = Vector.sub(sum, velocity);
        steer.limit(Configuration.maxForce);
        return steer;
    }

    private Vector cohesion(List<Bird> birds) {
        Vector sum = new Vector(0, 0);
        int count = 0;
        for (Bird other : birds) {
            float d = Vector.dist(position, other.position);
            if ((d > 0) && (d < Configuration.neighborDist)) {
                sum.add(other.position);
                count++;
            }
        }
        if (count > 0) {
            sum.div(count);
            return seek(sum);
        } else {
            return new Vector(0, 0);
        }
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }
}

