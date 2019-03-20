package configuration;

import javafx.scene.image.Image;

public class Configuration {
    public static final int width = 1200;
    public static final int height = 600;
    public static final int repaintClock = 40;

    public static final int MOUSE_SEPARATION = 30;
    public static final float radiusBird = 16.0f;
    public static final float maxSpeed = 2;
    public static final float maxForce = 0.03f;
    public static final float desiredSeparation = 25.0f;
    public static final float neighborDist = 50;

    public static final float TWO_PI = 6.2831855F;

    public static final Image birdImage = new Image("resources/bird.png");

    private int numberOfBirds;

    public Configuration(int numberOfBirds) {
        this.numberOfBirds = numberOfBirds;
    }

    public int getNumberOfBirds() {
        return numberOfBirds;
    }
}
