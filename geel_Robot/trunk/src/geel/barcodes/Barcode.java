package geel.barcodes;

/**
 * The barcode class represents a sequence of 7 bits.
 * 
 * internally barcodes are stored as java integer primitives.
 * the reason is that an 'int' corresponds to exactly one word in memory 
 * and thus allow easy bit manipulation.
 * 
 * more specifically a barcode is stored in the 7 least significant bits
 * of the integer( ie. 7 right most bits)
 * 
 * @author jeroendv
 *
 */
public class Barcode {
	
	private int barcode;
	
	/**
	 * create a barcode instance representing the bitsequence
	 * 0000000
	 */
	public Barcode() {
		this.barcode = 0;
	}
	
	/**
	 * create a barcode object with a given integer representation
	 * 
	 * @param barcode
	 */
	public Barcode(int barcode){
		this.barcode = barcode;
	}
	
	/**
	 * create a barcode from its string representation
	 * 
	 * @param barcode
	 */
	public Barcode(String barcode){
		this.barcode = Integer.parseInt(barcode, 2);
	}
	
	public Barcode(Barcode barcode) {
		this.barcode = barcode.barcode;
	}

	private final static int BITMASK = 1;
	
	
	/**
	 * set a bit to value 1
	 * 
	 * @param pos position of the bit
	 *  0 is the first bit
	 *  6 is the last bit
	 */
	public void setBit(int pos){
		this.barcode = this.barcode | (BITMASK<<pos);
	}
	
	/**
	 * set the value of a certain bit to 0 or 1
	 * 
	 * @param pos position of the bit to be set
	 *  0 is the first bit
	 *  6 is the last bit
	 *  
	 * @param value 0 or 1
	 *  (if value != 0 then the bit will be set to 1)
	 */
	public void setBit(int pos, int value) {
		if (value == 0 ){
			this.unsetBit(pos);
		}else{
			this.setBit(pos);
		}
		
	}
	
	/**
	 * set a bit to value 0
	 * 
	 * @param pos position of the bit
	 *  0 is the first bit
	 *  6 is the last bit
	 */
	public void unsetBit(int pos){
		this.barcode = this.barcode & ~(BITMASK<<pos);
		
	}
	
	/**
	 * flip the value of a bit 
	 * 
	 * @param index position of the bit
	 *  0 is the first bit
	 *  6 is the last bit
	 */
	public void flipBit(int index){
		this.barcode = this.barcode ^ (BITMASK<<index);
	}
	
	/**
	 * return the value of the bit on a given position 
	 * 
	 * @param index position of the bit
	 *  0 is the first bit
	 *  6 is the last bit
	 *  
	 * @return 0 or 1
	 */
	public int getBit(int index){
		return (this.barcode>>> index) & BITMASK;
	}
	
	
    /**
     * return a String representation of this barcode
     */
	@Override
    public String toString(){
    	char[] charArray = new char[7];
    	
    	for(byte i =0; i < 7 ; i ++){
    		int bitValue = this.getBit(i);
    		if(bitValue == 1){
    			charArray[6-i]= '1';
    		}else{
    			charArray[6-i] = '0';
    		}
    	}
    	
    	return new String(charArray);
    	
    }
	
    @Override
    public boolean equals(Object other) {
    	return (other instanceof Barcode) && ((Barcode)other).barcode == this.barcode;
    }



}
