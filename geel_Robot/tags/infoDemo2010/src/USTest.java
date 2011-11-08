
import lejos.nxt.*;

public class USTest
{
   public static void main(String[] args) {
       UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S4);
       while(true) {
               LCD.drawString("j" + sonic.getDistance() + "   ", 0 ,3);
       }
   }
}