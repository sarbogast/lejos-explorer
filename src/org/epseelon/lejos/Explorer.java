package org.epseelon.lejos;

import lejos.nxt.*;

/**
 * @author sarbogast
 * @version 5/12/10, 13:33
 */
public class Explorer {
    private TouchSensor collisionDetector;
    private UltrasonicSensor eyes;
    private Motor leftMotor;
    private Motor rightMotor;
    private Motor neck;

    public Explorer() {
        leftMotor = Motor.B;
        leftMotor.setSpeed(1000);
        rightMotor = Motor.C;
        rightMotor.setSpeed(1000);
        neck = Motor.A;
        collisionDetector = new TouchSensor(SensorPort.S1);
        eyes = new UltrasonicSensor(SensorPort.S2);
        eyes.continuous();

        Button.ESCAPE.addButtonListener(new ButtonListener() {
            public void buttonPressed(Button button) {
                System.exit(0);
            }

            public void buttonReleased(Button button) {
            }
        });
    }

    public void go() {
        goForward();
        while (true) {
            if (collisionDetector.isPressed() || eyes.getDistance() < 20) {
                halt();
                if (collisionDetector.isPressed()) {
                    sayOoof();
                    stepBack();
                }
                int distanceLeft = lookAtAngle(-90);
                int distanceRight = lookAtAngle(90);
                if (distanceRight >= distanceLeft) {
                    turnRight();
                } else {
                    turnLeft();
                }
                goForward();
            }
        }
    }

    private void turnRight() {
        leftMotor.rotate(275, true);
        rightMotor.rotate(-275, false);
    }

    private void turnLeft() {
        leftMotor.rotate(-275, true);
        rightMotor.rotate(275, false);
    }

    private int lookAtAngle(int degrees) {
        neck.rotate(degrees);
        int distance = eyes.getDistance();
        neck.rotate(-degrees);
        return distance;
    }

    private void sayOoof() {
        Sound.playTone(440, 200);
    }

    private void halt() {
        leftMotor.stop();
        rightMotor.stop();
    }

    private void stepBack() {
        leftMotor.rotate(-360, true);
        rightMotor.rotate(-360, false);
    }

    private void goForward() {
        leftMotor.forward();
        rightMotor.forward();
    }

    public static void main(String[] args) {
        Explorer explorer = new Explorer();
        explorer.go();
    }
}
