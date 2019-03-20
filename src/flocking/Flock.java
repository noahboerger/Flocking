package flocking;

import java.util.ArrayList;
import java.util.List;

public class Flock {
    private List<Bird> birds;

    public Flock() {
        birds = new ArrayList<>();
    }

    public void run(Vector mouseClick) {
        for (Bird b : birds) {
            b.run(birds, mouseClick);
        }
    }

    public Bird getNearestBird(float x, float y) {
        Bird starter = birds.get(0);
        Bird nearestBird = starter;
        float actMinDistance = calculateDistanceTo(x, y, starter);
        for (Bird bird : birds) {
            float calculatedDistance = calculateDistanceTo(x, y, bird);
            if (calculatedDistance < actMinDistance) {
                actMinDistance = calculatedDistance;
                nearestBird = bird;
            }
        }
        return nearestBird;
    }

    private float calculateDistanceTo(float x, float y, Bird bird) {
        return (float) Math.sqrt(((x - bird.getPosition().getX()) * (x - bird.getPosition().getX())) + ((y - bird.getPosition().getY()) * (y - bird.getPosition().getY())));
    }

    public void addBird(Bird b) {
        birds.add(b);
    }

    public List<Bird> getBirds() {
        return birds;
    }
}