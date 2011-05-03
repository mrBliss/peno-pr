package geel.barcodes;

/**
 * This class represents a sequence of bits.
 * Due to internal representation the sequence can be at most 32 bits long.
 * 
 * @author jeroendv
 * 
 */
public class BitSequence {
	
	/*
	 * the bit sequence of 32 zero bits stored as a int
	 * 000000000000000000000000000000000
	 */
	final static int ZEROMASK = 0;
	
	/*
	 * the bit sequence of 32 one bits stored as an int
	 * 11111111111111111111111111111111
	 */
	final static int ONEMASK = ~ZEROMASK;
	
	/*
	 * the bit sequence of one one-bit prefixed with zero's 
	 * to make bit filtering and manipulation easy
	 * 00000000000000000000000000000001
	 */
	final static int BITMASK = 1;
	
	/**
	 * the maximum length of a bit sequence
	 */
	public static final int MAXLENGTH = 32;
	
	/**
	 * the minimum length of a bit sequence
	 */
	public static final int MINLENGTH = 1;
	
	
	

	/**
	 * length of the bit sequence
	 */
	public final int length;
	
	/**
	 * internally bit sequences are stored as java integer primitives. the reason is that
	 * all java bit manipulators use an int as input and output making this the container of choice
	 * for bit sequence representation and manipulation.
	 * a primitive java int corresponds to a 32 bit memory allocation, hence bit sequences 
	 * can only be 32 bits long.
	 * note that bit sequences are stored from right to left. ie. the least significant bit 
	 * (ie. right most bit) is the first bit in the sequence and the most significant bit
	 * (ie. the left most bit or sign bit) is the 32th bit of the sequence.
	 *  
	 * Which is opposite of the string representation from left to right where the first bit is 
	 * mentioned first and the last bit is mentioned last
	 * 
	 * @invar: only the left most length bit may differ from zero to form a bit sequence
	 * the remaining 32-length left most bits must always be zero!!
	 */
	private int bitSequence;
	

	

	/**
	 * create a bit sequence of given length equal to  000...000
	 * 
	 * @pre: length <= MAXLENGTH
	 */
	public BitSequence(int length) {
		if(length>MAXLENGTH){
			throw new IllegalArgumentException("the length of a bit sequence can't be more then "+MAXLENGTH);
		}
		
		if(length <MINLENGTH){
			throw new IllegalArgumentException("the length of a bit sequence can't be less then "+ MINLENGTH);
		}
		
		this.length = length;
		this.bitSequence = ZEROMASK;
	}


	/**
	 * create a bit sequence of given length, with a given integer container value
	 * 
	 * only the 'length' left most bits are selected and prefixed with zero's to create
	 * the bit sequence integer container value. 
	 * 
	 * @param barcode
	 */
	BitSequence(int length,int containerIntValue){
		this(length);
		
		/*
		 * a bit mask with ones as <length> right most bits
		 * prefixed with zeros.
		 * 
		 * the mask is useful to select only those bits that represent a bit in the bit sequence
		 */
		int bitSequenceMask = BitSequence.ONEMASK >>>(BitSequence.MAXLENGTH-length);
		
		this.bitSequence = containerIntValue & bitSequenceMask;
	}
	
	/**
	 * create a bit sequence from its string representation as returned by toString()
	 * note:
	 *  * that the string length may not exceed MAXLENGTH characters
	 *  * any non '0' character is interpreted as a '1' character
	 * 
	 * @param barcode
	 */
	public BitSequence(String barcode) {
		this(barcode.length());
		
		for( int i = 0; i <this.length ; i++){
			if(barcode.charAt(i)!='0'){
				this.setBit(i);
			}else{
				//do nothing: this.getBit(i) is already 0
			}
		}
	}

	public BitSequence(BitSequence barcode) {
		this.length = barcode.length;
		this.bitSequence = barcode.bitSequence;
	}


	/**
	 * set a bit to value 1
	 * 
	 * nothing is done if isValidIndex(pos) != true
	 * 
	 * @param pos zero valued index of the bit
	 */
	public void setBit(int pos) {
		
		if(this.isValidIndex(pos)){
			this.bitSequence = this.bitSequence | (BITMASK << pos);
		}
		
	}

	/**
	 * set the value of a certain bit to 0 or 1
	 * 
	 * nothing is done if isValidIndex(pos) != true
	 * 
	 * @param pos
	 *            position of the bit to be set 0 is the first bit 6 is the last
	 *            bit
	 * 
	 * @param value
	 * 0 or 1 
	 * (note: any value != 0 is mapped to 1)
	 */
	public void setBit(int pos, int value) {
		if (value == 0) {
			this.unsetBit(pos);
		} else {
			this.setBit(pos);
		}

	}

	/**
	 * set a bit to value 0
	 * 
	 * nothing is done if isValidIndex(pos) != true
	 * 
	 * @param pos zero based index
	 */
	public void unsetBit(int pos) {
		
		if(this.isValidIndex(pos)){
			this.bitSequence = this.bitSequence & ~(BITMASK << pos);
		}

	}
	
	/**
	 * check if a given index position of a bit is valid.
	 * 
	 * bit sequence index positions are zero based, therefor 
	 * 		this.length > pos >= 0 
	 * 
	 * @param pos
	 * @return
	 */
	public boolean isValidIndex(int pos){
		return pos<this.length && pos >= 0;
	}

	/**
	 * flip the value of a bit
	 * 
	 * nothing is done if isValidIndex(pos) != true
	 * 
	 * @param pos zero based index
	 */
	public void flipBit(int pos) {
		if( this.isValidIndex(pos) ){
			this.bitSequence = this.bitSequence ^ (BITMASK << pos);
		}
	}

	/**
	 * return the value of the bit on a given position
	 * 
	 * @param pos zero based index
	 * 
	 * @return 0 or 1
	 *  if this.isValidIndex(pos) == false then this method
	 *  always return 0.
	 */
	public int getBit(int pos) {
		if(this.isValidIndex(pos)){
			return (this.bitSequence >>> pos) & BITMASK;
		}
		else{
			return 0;
		}
	}

	/**
	 * return a String representation of this barcode
	 */
	@Override
	public String toString() {
		char[] charArray = new char[this.length];

		for (int i = 0; i < this.length; i++) {
			int bitValue = this.getBit(i);
			if (bitValue == 1) {
				charArray[i] = '1';
			} else {
				charArray[i] = '0';
			}
		}

		return new String(charArray);

	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof BitSequence)
				&& ((BitSequence) other).length == this.length
				&& ((BitSequence) other).bitSequence == this.bitSequence;
	}

}
