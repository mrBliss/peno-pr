package geel.barcodes;

/**
 * This class uses 7 bit barcodes to encode 4 bits of information
 * with 3 bits redundancy.
 * 
 * the encoding scheme is constructed such that any 7 bit sequence
 * has at most a hamming distance of 1 to a valid bit sequence.
 * Thus allowing the correction of a one bit error in an unknown position.
 * 
 * 
 * 
 * @author jeroendv
 *
 */
public class BarcodeDecoder {
	
	public final static int BARCODELENGTH = 7;
	
    
    /**
     * an array of the 16 valid barcodes
     */
    public static final BitSequence[] VALID_BARCODES = new BitSequence[]{
    	new BitSequence("0000000"), // 0  0
    	new BitSequence("0001111"), // 1  1
    	new BitSequence("0010110"), // 2  2
    	new BitSequence("0011001"), // 3  3
    	new BitSequence("0100101"), // 4  4
    	new BitSequence("0101010"), // 5  5
    	new BitSequence("0110011"), // 6  6
    	new BitSequence("0111100"), // 7  7
    	new BitSequence("1000011"), // 8  8
    	new BitSequence("1001100"), // 9  9
    	new BitSequence("1010101"), // A 10
    	new BitSequence("1011010"), // B 11
    	new BitSequence("1100110"), // C 12
    	new BitSequence("1101001"), // D 13
    	new BitSequence("1110000"), // E 14
    	new BitSequence("1111111"), // F 15
    };	
	
    /**
     * check if a certain bit sequence is valid
     * 
     * @param barcode 
     * @return true 
     */
    public static boolean isBarcodeValid(BitSequence barcode){
    	if(barcode.length != BARCODELENGTH){
    		throw new IllegalArgumentException("a barcode must be a bit sequence of 7 bits"); 
    	}
    	
    	for(byte i = 0; i < BarcodeDecoder.VALID_BARCODES.length; i++){
    		if( BarcodeDecoder.VALID_BARCODES[i].equals(barcode) ){
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    
    
    /**
     * nearest neighbor decoder,
     * which maps any of the 128 possible 7 bit sequences to 
     * one of the valid barcodes through a one bit change.
     * 
     * @param barcode
     * @return
     */
    public static BitSequence nearestNeighbor(BitSequence barcode){
    	if(barcode.length != BARCODELENGTH){
    		throw new IllegalArgumentException("a barcode must be a bit sequence of 7 bits"); 
    	}
    	
    	if(isBarcodeValid(barcode)){
    		return barcode;
    	}else{
    		for( int i=0 ; i<BARCODELENGTH ; i++){
    			BitSequence perturbedBarcode = new BitSequence(barcode);
    			perturbedBarcode.flipBit(i);
    			
    			if(isBarcodeValid(perturbedBarcode)){
    				return perturbedBarcode;
    			}
    		}
    	}
    	
    	throw new IllegalStateException("there must be a bug! this should never happen, " +
    			"every 7 bit sequence should have a valid barcode as neighbor");
    }
    
    
	
	

}
