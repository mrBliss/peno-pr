package geel.BTGW.infrastructure;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

/**
 * The BTGWConnection class is a parent class for backend connections associated with the
 * Bluetooth Gateway. While the BTGateway handles protocol and messaging, it still needs 
 * physical connections over which to transmit those messages. Those connections are 
 * setup using derivated classes of this class.
 * 
 * A derived class should implement only 2 methods: realconnect() and realdisconnect().
 * 
 * realconnect should setup a physical connection to a remote location and set the 
 * inputStream and outputStream fields using setInputStream() and setOutputStream(). 
 * After the connection has been established, the status should be set to 
 * STATUS_CONNECTED by using setStatus().
 * 
 * realdisconnect does the opposite of realconnect and should tear down a previously 
 * setup connection. After tearing down the connection, set the status to STATUS_DISCONNECTED.
 * 
 * Both realconnect() and realdisconnect() run in a threaded environment and should not worry
 * about blocking. Multiple (simultaneous) calls to disconnect() and connect() are handled by
 * this superclass and should also not be worried about.
 * 
 * This class also implements the DataChanged paradigm and allows other objects to register as
 * a listener for changes to the status field. This is also automagically handled by the setStatus()
 * implementation.
 * 
 * @author Steven Van Acker
 *
 */

public abstract class BTGWConnection {
	public static final int STATUS_DISCONNECTED = 0;
	public static final int STATUS_CONNECTING = 1;
	public static final int STATUS_CONNECTED = 2;
	public static final int STATUS_DISCONNECTING = 3;
	
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	
	private Thread connThread, disconnThread;
	
	private int status;
	private boolean autoReconnect = false;
	
	private ArrayList $listeners = new ArrayList();
		
	public BTGWConnection() {
		setStatus(STATUS_DISCONNECTED);
	}
	
	public synchronized void connect() {
		// Don't do anything in case we're already connected
		if(getStatus() == STATUS_CONNECTED) {
			return;
		}
		
		// if we are already connecting, then don't do anything
		if(connThread != null && connThread.isAlive()) {
			return;
		}
		
		connThread = new Thread() {
			public void run() {
				// In case we are trying to disconnect, wait untill the
				// operation has finished.
				while(getStatus() == STATUS_DISCONNECTING);
				
				// Now we are trying to connect
				setStatus(STATUS_CONNECTING);
				
				// Call specific code to setup the connection
				realConnect();
			}
		};
		
		connThread.start();
	}
	
	public abstract void realConnect();
	
	public synchronized void disconnect() {
		// Don't do anything in case we're already disconnected
		if(getStatus() == STATUS_DISCONNECTED) {
			return;
		}

		// if we are already disconnecting, then don't do anything
		if(disconnThread != null && disconnThread.isAlive()) {
			return;
		}
		
		disconnThread = new Thread() {
			public void run() {
				// In case we are trying to connect, wait untill the
				// operation has finished.
				while(getStatus() == STATUS_CONNECTING);
				
				// Now we are trying to disconnect
				setStatus(STATUS_DISCONNECTING);
				
				// Call specific code to break down the connection
				realDisconnect();
				setInputStream(null);
				setOutputStream(null);
			}
		};
		
		disconnThread.start();
	}
	
	public abstract void realDisconnect();
	
	public synchronized void autoReconnect() {
		if(getAutoReconnect()) {
			disconnect();
			connect();
		}
	}
	
	public int getStatus() {
		return status;
	}

	protected synchronized void setStatus(int status) {
		this.status = status;
		notifyListeners();
	}
	
	protected synchronized void setAutoReconnect(boolean autoReconnect) {
		this.autoReconnect = autoReconnect;
	}

	public boolean getAutoReconnect() {
		return autoReconnect;
	}
	
	public DataInputStream getInputStream() {
		return inputStream;
	}

	protected synchronized void setInputStream(DataInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public DataOutputStream getOutputStream() {
		return outputStream;
	}

	protected synchronized void setOutputStream(DataOutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	public String getStatusString() {
		switch(getStatus()) {
		    case STATUS_CONNECTED:     return "Connected.";
		    case STATUS_CONNECTING:    return "Connecting...";
		    case STATUS_DISCONNECTED:  return "Disconnected.";
		    case STATUS_DISCONNECTING: return "Disconnecting...";
		}
		
		return "Unknown status!";
	}
	
    protected void notifyListeners() {
    	for(int i = 0; i < $listeners.size(); i++) {
    		((IDataChangeListener)$listeners.get(i)).dataChanged(this);
    	}
    }
    
    public void addListener(IDataChangeListener l) {
    	$listeners.add(l);
    }


}
