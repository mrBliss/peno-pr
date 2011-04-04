package geel;

import geel.barcodes.Barcode;
import geel.barcodes.BarcodeDecoder;

import java.util.Hashtable;

/**
 * @author jeroendv
 *
 */
public class TrackSpecs {
	
	/**
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
    
    
    /**
     * barcode specification mapping barcode color to a bit value 
     *  0 = black
     *  1 = white
     */
    public static final byte  BLACK_BAR_VALUE = 0;
    public static final byte  WHITE_BAR_VALUE = 1;
    
    
    /**
     * 
     */
	public static final Barcode TurnRighBarcode = BarcodeDecoder.VALID_BARCODES[6];
	 //TODO
	public static final Barcode TurnLeftBarcode = BarcodeDecoder.VALID_BARCODES[3];
	//TODO
	public static final Barcode GoForwardBarcode = BarcodeDecoder.VALID_BARCODES[1];
	
	
    

    
    
    


}
