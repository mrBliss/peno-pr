package geel;

import geel.BTGW.infrastructure.BTGWConnection;
import geel.BTGW.infrastructure.BTGateway;
import geel.BTGW.robot.BTGWRealConnectionTaker;

/**
 * short test program to test the {@link RobotBTGWConfigurator} 
 * using a bogus Configurable object with 2 int parameters, one float and one boolean parameter
 * 
 * 
 * 
 * @author jeroendv
 *
 */
public class ConfigTest {
	
	
	public static void main(String[] args) {
		initilizeBTGateWay();
		
		RobotBTGWConfigurator.registerWithBTGW();
		
		RobotBTGWConfigurator.register(bogusConfigurableObject);
		
	}


	/**
	 * initialize a Bluetooth Gateway for communication between robot and pc and
	 * make it globally accessible through BTGateWay.getinstance()
	 * 
	 * note that this methods blocks until the gate way is ready for use
	 */
	public static void initilizeBTGateWay() {
		// create BT connection
		BTGWRealConnectionTaker btconn = new BTGWRealConnectionTaker();
		System.out.println("Waiting for BT...");
		btconn.connect();

		// wait till BT properly connected
		while (btconn.getStatus() != BTGWConnection.STATUS_CONNECTED) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}

		// make the BTGW globally accessible through BTGateWay.getinstance()
		BTGateway.addInstance(new BTGateway(btconn));
	}
	
	
	private static Configurable bogusConfigurableObject = new Configurable() {
		
		private final String testBoolId = "testBoolId";
		private boolean testBool = false;
		
		private final String testIntId1 = "testIntId1";
		private int testInt1 = 0;
		
		private final String testIntId2 = "testIntId2";
		private int testInt2 = 0;
		
		private final String testFloatId = "testFloatId";
		private float testFloat = 0;
		
		@Override
		public void setConfigurableInt(String id, int value)
				throws IllegalArgumentException {
			if(id.equals(testIntId1)){
				this.testInt1 = value;
			}else if(id.equals(testIntId2)){
				this.testInt2 = value;
			}
			else{
				throw new IllegalArgumentException("parameter '"+id+"' doesn't exists");
			}
			
		}
		
		@Override
		public void setConfigurableFloat(String id, float value)
				throws IllegalArgumentException {
			if(id.equals(testFloatId)){
				this.testFloat = value;
			}else{
				throw new IllegalArgumentException("parameter '"+id+"' doesn't exists");
			}
			
		}
		
		@Override
		public void setConfigurableBoolean(String id, boolean value)
				throws IllegalArgumentException {
			if(id.equals(testBoolId)){
				this.testBool = value;
			}else{
				throw new IllegalArgumentException("parameter '"+id+"' doesn't exists");
			}
			
		}
		
		@Override
		public String[] listConfigurableIntegers() {
			return new String[]{testIntId1,testIntId2};
		}
		
		@Override
		public String[] listConfigurableFloat() {
			return new String[]{testFloatId};
		}
		
		@Override
		public String[] listConfigurableBooleans() {
			return new String[]{testBoolId};
		}
		
		@Override
		public int getConfigurableInt(String id) throws IllegalArgumentException {
			if(id.equals(testIntId1)){
				return this.testInt1;
			}else if(id.equals(testIntId2)){
				return this.testInt2;
			}else{
				throw new IllegalArgumentException("parameter '"+id+"' doesn't exists");
			}
		}
		
		@Override
		public float getConfigurableFloat(String id)
				throws IllegalArgumentException {
			if(id.equals(testFloatId)){
				return this.testFloat;
			}else{
				throw new IllegalArgumentException("parameter '"+id+"' doesn't exists");
			}
		}
		
		@Override
		public boolean getConfigurableBoolean(String id)
				throws IllegalArgumentException {
			if(id.equals(testBoolId)){
				return this.testBool;
			}else{
				throw new IllegalArgumentException("parameter '"+id+"' doesn't exists");
			}
		}
	};

}
