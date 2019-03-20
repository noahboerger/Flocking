package gui;

import configuration.Configuration;
import flocking.Bird;
import flocking.Flock;
import flocking.Vector;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


class FlockingScene extends Scene {

    private Group root;
    private Flock flock;
    private Random random;

    private Bird showBehaviourBird;
    private Line line;
    private Circle neighborCircle;
    private Circle separationCircle;

    private Map<Bird, ImageView> imageViewToBird;
    private Circle mouseCircle;

    private Vector mouseClick = new Vector(0, 0);


    FlockingScene(Configuration configuration) {
        super(new Group(), Configuration.width, Configuration.height);

        root = (Group) getRoot();
        initializeSwarm(configuration);
        imageViewToBird = new HashMap<>();
        for (Bird bird : flock.getBirds()) {
            ImageView imageView = new ImageView();
            imageView.setImage(Configuration.birdImage);
            imageView.setLayoutX(bird.getPosition().getX());
            imageView.setLayoutY(bird.getPosition().getY());
            imageView.setRotate(bird.getVelocity().heading() / Configuration.TWO_PI * 360);
            root.getChildren().add(imageView);
            imageViewToBird.put(bird, imageView);
        }
        mouseCircle = new Circle(25);
        mouseCircle.setFill(null);
        mouseCircle.setStrokeWidth(2);
        mouseCircle.setStroke(Color.DARKORANGE);
        line = new Line();
        line.setFill(Color.DARKGREEN);
        neighborCircle = new Circle(Configuration.neighborDist);
        neighborCircle.setFill(null);
        neighborCircle.setStrokeWidth(3);
        neighborCircle.setStroke(Color.ORANGERED);

        separationCircle = new Circle(Configuration.desiredSeparation);
        separationCircle.setFill(null);
        separationCircle.setStrokeWidth(3);
        separationCircle.setStroke(Color.DARKGRAY);


        startRunning();

        addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {

            float x = (float) mouseEvent.getSceneX();
            float y = (float) mouseEvent.getSceneY();

            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (mouseClick.getX() == 0 && mouseClick.getY() == 0) {
                    mouseClick.setX(x);
                    mouseClick.setY(y);
                    mouseCircle.setCenterX(x);
                    mouseCircle.setCenterY(y);
                    root.getChildren().add(mouseCircle);
                } else {
                    mouseClick = new Vector(0, 0);
                    root.getChildren().remove(mouseCircle);
                }
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY) {

                if (showBehaviourBird == null) {
                    showBehaviourBird = flock.getNearestBird(x, y);
                    root.getChildren().add(line);
                    root.getChildren().add(neighborCircle);
                    root.getChildren().add(separationCircle);
                } else {
                    showBehaviourBird = null;
                    root.getChildren().remove(line);
                    root.getChildren().remove(neighborCircle);
                    root.getChildren().remove(separationCircle);
                }
            } else if (mouseEvent.getButton() == MouseButton.MIDDLE) {
                Bird bird = new Bird((int) x - 7, (int) y - 7, random());
                flock.addBird(bird);
                ImageView imageView = new ImageView();
                imageView.setImage(Configuration.birdImage);
                imageView.setLayoutX(bird.getPosition().getX() - 7);
                imageView.setLayoutY(bird.getPosition().getY() - 7);
                imageView.setRotate(bird.getVelocity().heading() / Configuration.TWO_PI * 360);
                root.getChildren().add(imageView);
                imageViewToBird.put(bird, imageView);
            }
        });
    }

    private void startRunning() {
        Thread thread = new Thread(() -> {
            Runnable updater = this::updateBirds;
            while (true) {
                try {
                    Thread.sleep(Configuration.repaintClock);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(updater);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void updateBirds() {
        flock.run(mouseClick);
        for (Bird bird : flock.getBirds()) {
            ImageView birdView = imageViewToBird.get(bird);
            birdView.setLayoutX(bird.getPosition().getX());
            birdView.setLayoutY(bird.getPosition().getY());
            birdView.setRotate(bird.getVelocity().heading() / Configuration.TWO_PI * 360);
            if (showBehaviourBird == null) {
                continue;
            }
            if (bird == showBehaviourBird) {
                line.setStartX(bird.getPosition().getX() + 7);
                line.setStartY(bird.getPosition().getY() + 7);
                line.setEndX(bird.getPosition().getX() + 7 + bird.getVelocity().getX() * 50);
                line.setEndY(bird.getPosition().getY() + 7 + bird.getVelocity().getY() * 50);

                neighborCircle.setCenterX(bird.getPosition().getX() + 7);
                neighborCircle.setCenterY(bird.getPosition().getY() + 7);

                separationCircle.setCenterX(bird.getPosition().getX() + 7);
                separationCircle.setCenterY(bird.getPosition().getY() + 7);
            }
        }
    }


    private void initializeSwarm(Configuration configuration) {
        flock = new Flock();
        for (int i = 0; i < configuration.getNumberOfBirds(); i++) {
            Bird bird = new Bird(Configuration.width / 2.0f, Configuration.height / 2.0f, random());
            flock.addBird(bird);
        }
    }

    private float random() {
        if (random == null) {
            random = new Random();
        }
        float value;
        do {
            value = random.nextFloat() * Configuration.TWO_PI;
        } while (value == Configuration.TWO_PI);

        return value;
    }
}

