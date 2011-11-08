package geel.behaviours;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.subsumption.Behavior;

public class Manual implements Behavior {

    private DataInputStream str;
    private DataOutputStream stro;
    private Motor rightMotor;
    private Motor leftMotor;
    private boolean active = false;

    public Manual(Motor rightMotor, Motor leftMotor) {
        System.out.println("klaar om te ontvangen");
        BTConnection conn = Bluetooth.waitForConnection();
        str = conn.openDataInputStream();
        stro = conn.openDataOutputStream();
        System.out.println("data stream open");
        System.setOut(new PrintStream(stro));
        this.rightMotor = rightMotor;
        this.leftMotor = leftMotor;
    }

    public boolean takeControl() {
        if (active) {
            return true;
        }
        try {
            while (str.available() > 0) {
                byte br = str.readByte();
                if (br == 10) {
                    active = true;
                    return true;
                }
                if (br == 12) {
                    panic();
                    return false;
                }
            }
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

    public void action() {
        active = true;
        leftMotor.stop();
        rightMotor.stop();
        System.out.println("Start manual");
        boolean cont = true;
        while (cont) {
            try {
                byte bt = str.readByte();
                if (bt == 0) {
                    rightMotor.stop();
                    leftMotor.stop();
                } else if (bt == 11) {
                    cont = false;
                } else if (bt == 20) {
                    byte ml = str.readByte();
                    byte mr = str.readByte();
                    mCommand(leftMotor, ml);
                    mCommand(rightMotor, mr);
                } else if (bt / 10 == 3) {
                    toeter(bt % 10);
                } else if (bt == 21) {
                    byte ml = str.readByte();
                    byte mr = str.readByte();
                    Sound.playTone(ml * 10, mr * 10);
                }
            } catch (Exception ex) {
                Sound.buzz();
                System.out.println("klaar om te ontvangen");
                BTConnection conn = Bluetooth.waitForConnection();
                str = conn.openDataInputStream();
                System.out.println("data stream open");
            }
        }
        System.out.println("Exit manual");
        active = false;
    }

    public void suppress() {
        // should be impossible
    }

    private void mCommand(Motor motor, byte command) {
        if (command == 100) {
            return;
        } else if (command == 101) {
            motor.reverseDirection();
        } else if (command == 102) {
            motor.forward();
        } else if (command == 103) {
            motor.backward();
        } else if (100 > command && command >= 0) {
            motor.setSpeed(10 * command);
            motor.forward();
        } else if (-100 <= command && command < 0) {
            motor.setSpeed((command * -10));
            motor.backward();
        }
    }

    private void toeter(int i) {
        if (i == 0) {
            Sound.beep();
        } else if (i == 1) {
            Sound.beepSequence();
        } else if (i == 2) {
            Sound.beepSequenceUp();
        } else if (i == 3) {
            Sound.buzz();
        }
    }

    private void panic() {
        try {
            rightMotor.backward();
            leftMotor.backward();
            Thread.sleep(400);
            rightMotor.forward();
            Thread.sleep(400);
            leftMotor.forward();

        } catch (InterruptedException ex) {
        }
    }
}
