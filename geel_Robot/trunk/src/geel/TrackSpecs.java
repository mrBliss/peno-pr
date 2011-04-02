package geel;

import java.util.Hashtable;

public class TrackSpecs {
	
	/*
	 * integer codes for the 3 different colors identified by the robot
	 */
	public static final int BLACK_COLOR = 0;
	public static final int GROUND_COLOR = 1;
	public static final int WHITE_COLOR = 2;
	
	
	/**
     * Width of a tile in centimeter
     */
    public static final int tileWidth = 80;
    
    
    /**
     * Width of a single bit of the barcode in centimeter 
     */
    public static final int barcodeBitWitdth = 2;
    
//    FIXME: is this list correct?
    public static final int[] barcodes = new int[]{
    	0x00,
    	0x0f,
    	0x15,
    	0x19,
    	0x25,
    	0x2A,
    	0x33,
		0x3C,
		0x43,
		0x4C,
		0x55,
		0x5A,
		0x66,
		0x69,
		0x70,
		0x7f,
    };
    
    //TODO
	public static final int TurnRighBarcode = 0x33;
	 //TODO
	public static final int TurnLeftBarcode = 0x19;
	//TODO
	public static final int GoForwardBarcode = 0;
	
	
    
    public static boolean isBarcodeValid(int barcode){
    	for(int i = 0; i >16; i++){
    		if(barcodes[i] == barcode){
    			return true;
    		}
    	}
    	return false;
    }
    
    
    //FIXME
    public static String barcodeToString(int barcode){
    	char[] charArray = new char[7];
    	
    	int mask = 0x01;
    	for(int i =0; i < 7 ; i ++){
    		int bitValue = barcode & mask;
    		if(bitValue != 0){
    			charArray[i]= '1';
    		}else{
    			charArray[i] = '0';
    		}
    		mask = mask <<1;
    	}
    	
    	return new String(charArray);
    	
    }

}
