package geel;

import geel.barcodes.BitSequence;
import geel.barcodes.BarcodeDecoder;

import java.util.Hashtable;

/**
 * @author jeroendv
 *
 */
public class TrackSpecs {

	
	
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
     * list of tile types together with a reference to the barcode that identifies it
     */
    public static enum Tile{
    	LEFT_TURN(BarcodeDecoder.VALID_BARCODES[6]),
    	RIGHT_TURN(BarcodeDecoder.VALID_BARCODES[3]),
    	STRAIGHT_ON(BarcodeDecoder.VALID_BARCODES[1]),
    	RAMP_UP(null),
    	RAMP_DOWN(null),
    	CHOKEPOINT(null);
    	
    	
    	/*
    	 * barcode that identifies a Tile
    	 */
    	public final BitSequence barcode;
    	
    	private Tile(BitSequence barcode){
    		this.barcode =  barcode;
    	}
    }
    
    
	
	
    

    
    
    


}
