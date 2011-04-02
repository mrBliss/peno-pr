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
    	if(0x00 == barcode){
    		return "0000000";
    	}else if(0x0f == barcode){
    		return "0001111";
    	}else if( 0x15 == barcode){
    		return "0010110";
    	}else if(	0x19 == barcode){
    		return "0011001";
    	}else if (    	0x25 == barcode){
    		return "0100101";
    	}else if(    	0x2A == barcode){
    		return "0101010";
    	}else if(   	0x33 == barcode){
    		return "0110011";
    	}else if(		0x3C == barcode){
    		return "0111100";
		}else if(		0x43 == barcode){
			return "1000011";
		}else if(		0x4C == barcode){
			return "1001100";
		}else if(		0x55 == barcode){
			return "1010101";
		}else if(		0x5A == barcode){
			return "1011010";
		}else if(		0x66 == barcode){
			return "1100110";
		}else if(		0x69 == barcode){
			return "1101001";
		}else if(		0x70 == barcode){
			return "1110000";
		}else if(		0x7f == barcode){
			return "1111111";
		}
		else{
			return "xxxxxxx";
		}
    	
    }

}
