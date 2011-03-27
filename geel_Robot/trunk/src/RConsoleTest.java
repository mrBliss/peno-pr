
import lejos.nxt.comm.RConsole;

/**
 * Test of the RConsole class, which allows you to print strings messages to a
 * remote console over bluetooth.
 * 
 * 
 * @author jeroendv
 * 
 */
public class RConsoleTest {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// wait for remote blutooth console to connect
		final int timeout = 0; // wait Indefinitely
		RConsole.openBluetooth(timeout);
		
		// blocks until bluetooth connection is created by computer
		
		// print some test stuff
		RConsole.println("I'm alive :-)");
		RConsole.println("let sleep for 5 seconds");
		long T1 = System.currentTimeMillis();
		Thread.sleep(5);
		long sleepDuration = System.currentTimeMillis() - T1;
		RConsole.println("back!");
		RConsole.println("elapsed time: " + sleepDuration);
		
		
		// properly close the Remote console to ensure nothing gets lost
		// very important !!
		RConsole.close();
	}

}
