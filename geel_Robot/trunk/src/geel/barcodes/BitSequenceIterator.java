package geel.barcodes;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An interator that will iterate over all the the possible bit sequences of a given length
 * 
 * @author jeroendv
 *
 */
public class BitSequenceIterator implements Iterator<BitSequence> {
	
	
	/*
	 * The last the bit sequence in the iteration
	 * stored as an int
	 * 
	 * which is always the onemask of given length
	 * prefixed with zero's to get the integer container value
	 */
	private int end;
	
	/*
	 * the next bitsequence to be returned by hasNext()
	 * stored as an int
	 * 
	 * the first bit sequence is always the zeromask
	 */
	private int next;
	
	/*
	 * the length of the bitsequences over which we are iterating
	 */
	private int length;
	
	/*
	 * flag indicating whether or not there are still element left in the
	 * iteration list
	 * This is set to false, once hasNext() return the last element this.end
	 */
	private boolean hasNext = true;
	
	public BitSequenceIterator(int length){
		if(length > BitSequence.MAXLENGTH){
			throw new IllegalArgumentException("the length of a bit sequence can't be more then "+BitSequence.MAXLENGTH);
		}
		
		if(length < BitSequence.MINLENGTH){
			throw new IllegalArgumentException("the length of a bit sequence can't be less then "+ BitSequence.MINLENGTH);
		}
		
		
		this.length = length;
		this.next = BitSequence.ZEROMASK;
		this.end = BitSequence.ONEMASK >>>(BitSequence.MAXLENGTH-length);
		
	}
	
	@Override
	public boolean hasNext() {
		return this.hasNext;
	}

	@Override
	public BitSequence next() {
		if(this.hasNext == false){
			throw new NoSuchElementException("the last element of has already be returned by the previous next() method call");
		}
		
		//construct the current BitSequence object
		BitSequence currentBitSequence = new BitSequence(this.length, this.next);
		
		//update next value
		if(this.next == this.end){
			this.hasNext = false;
		}else{
			this.next++;
		}

		return currentBitSequence;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("the remove operation is not supported by this Iterator.");
		
	}
}
