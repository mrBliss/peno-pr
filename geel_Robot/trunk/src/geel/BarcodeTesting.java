package geel;

import geel.barcodes.BitSequence;
import geel.barcodes.BarcodeDecoder;
import geel.barcodes.BitSequenceIterator;

import java.util.ArrayList;
import java.util.Iterator;

public class BarcodeTesting {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	TestAllBarcodesValidity();


		
		
		
		
	}

	/**
	 * 
	 */
	private static void TestAllBarcodesValidity() {
		//run over all possible 7 bit barcodes and test there validity
		Iterator<BitSequence> it = new BitSequenceIterator(7);
		int nbvalidBarcodes = 0;
		int nbInvalidBarcodes = 0;
		while( it.hasNext() ){
			BitSequence barcode = it.next();
			System.out.print(barcode);
			if(BarcodeDecoder.isBarcodeValid(barcode)){
				System.out.println(" is valid");
				nbvalidBarcodes ++;
			}else{
				System.out.println(" is decoded to "+ BarcodeDecoder.nearestNeighbor(barcode));
				nbInvalidBarcodes ++;
			}
		}
		
		System.out.println();
		System.out.println("There were "+nbvalidBarcodes+ " valid barcodes");
		System.out.println("and "+nbInvalidBarcodes+" invalid onces");
	}

	/**
	 * 
	 */
	private static void TestValidBarcodes() {
		for(int i = 0; i <BarcodeDecoder.VALID_BARCODES.length; i++){
			
			BitSequence barcode = BarcodeDecoder.VALID_BARCODES[i];
			System.out.println(barcode);
			System.out.println(BarcodeDecoder.isBarcodeValid(barcode));
			
		}
	}

}
