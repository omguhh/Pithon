/****************************************************************************************
 * Author: Yasmine Fadel
 * Written: 2013-10-25
 *
 *
 * HashTable implementation of Dictionary.
 * Uses MAD compression and Double Hashing.
 * 
 * There was a lot of repetition in the insert,remove and find methods - this could've
 * been improved by having one method that does all the key generation/compression
 * that also is capable of finding the keys. (It also would've made the program much
 * shorter)
 ****************************************************************************************/
package Snapple;
import java.util.Iterator;


public class HashDictionary implements Dictionary {
	
	
	private String [] N =new String [7]; //initial size of the hashtable
	private int ttlOps=0; //total operations
	private int ttlPrbs=0; //total probes
	private  HashCode hashcode;
	private float inputLoad;
	private final String SENTINEL = "*^&%$!" ;
	private int numOfElements=0;
		
		
	public HashDictionary() throws DictionaryException  { 
		throw new DictionaryException ("THIS WILL NOT PASS");
	}
	
	public HashDictionary(HashCode inputCode, float inputLoadFactor) {
		
		hashcode=inputCode;
		inputLoad=inputLoadFactor;
	
	} 
	
	
	 /**
     * Insertion method.
     * First check if the hashtable is close to exceeding the load amount (by calling the floatOverload method)
     * if it is then,rehash.
     * Else,generate the hashcode and continue to compressing it using MADcompression to minimize the number of 
     * collisions.
     * We get an index from the compression code and place the element there (if it the index is null or a sentinel/marker)
     * If the key has collided with another,then use double hashing to generate another hashkey + compress that key
     * in order to find another empty space.
     * 
     * Number of Elements ,probes and operations will be incremented.
     * 
     * @param String
     *            key. A key to be inserted into the HashTable.
     * 
     */
    
	
	
	@Override
	public void insert(String key)  {
		ttlOps++;
		
		
		if (floatOverload()) { rehash(); 	}
		 
	
		int hCode = hashcode.giveCode(key);
		int compressedCode = MADcompression(hCode);
	
		if (N[compressedCode]==null )	{
			N[compressedCode] = key;
			numOfElements++;
			return;	
		}
		
	
		int stepCode = doubleCompression(hCode);
		for(int p = 0 ; p < N.length -1 ; p++)	{
			ttlPrbs++;
			int dv =doubleHash(compressedCode , stepCode , p);
			
			if  (N[dv]==null )	{

				N[dv] = key;
				numOfElements++;
				return;
				
			}
		
		}
			
	}
	
	 /**
     * Remove method.
     * Generate the hashcode from the key and compress it using MADcompression to get it's index in the HashTable.
     * If it hasn't been found then use double hashing to generate another hashkey + compress that key
     * in order to find the key.Once it's found assign the SENTINEL value in the cell to mark that it's been deleted.
     * 
     * Number of Elements will be decremented.
     * Probes and operations will be incremented.
     * 
     * @param String
     *            key. A key to be removed from the HashTable.
     * 
     */
    
	
	@Override
	public void remove(String key) throws DictionaryException	{
		ttlOps++;
		
	
		
		int hCode = hashcode.giveCode(key);
		int compressedCode = MADcompression(hCode);
		
		/**if not found throws DictionaryException  system.out.print(key not found);*/
	
		if ((find(key)==false)) { throw new DictionaryException ("The key you entered is not in the hash dictionary"); }
		if (N[compressedCode]==key)	{
			N[compressedCode] = SENTINEL;
			numOfElements--;
			return;
		
		}
		
		else { int stepCode = doubleCompression(hCode);
		
		for(int p = 0 ; p < N.length -1 ; p++)	{
			ttlPrbs++;
			int dv =doubleHash(compressedCode , stepCode , p);
			
			if(find(key)==false) {	throw new DictionaryException ("The key you entered is not in the hash dictionary"); 	}
			
			if (N[dv].equals(key))	{
				N[dv] = SENTINEL;
				numOfElements--;
				return;
				}
			}
		}
		
	}
	
	 /**
     * Find method.
     * 
     * Generate the hashcode from the key and compress it using MADcompression to get its index in the HashTable.
     * If it hasn't been found then use double hashing to generate another hashkey + compress that key
     * in order to find the key.Once it's found return true,else return false.
     * 
     * 
     * Probes and operations will be incremented.
     * 
     * @param String
     *            key. A key to be found from the HashTable.
     * 
     */
    
		
	@Override
	public boolean find(String key) {
		ttlOps++;
		
		int hCode = hashcode.giveCode(key);
		int compressedCode = MADcompression(hCode);
		
		if (N[compressedCode]==null) {  	return false; 	}
			
		if (N[compressedCode].equals(key)) {	return true; 	}
	
		//we use double hashing to find the same key 
		
		int stepCode = doubleCompression(hCode);
		
		for(int p =0 ; p <  N.length -1 ; p++)	{
			ttlPrbs++;
			int dv =doubleHash(MADcompression(hCode) , stepCode , p);
			
			if (N[dv]==null) {	return false; 		}
			
			if (N[dv].equals(key)) {	return true;  		}
		}	
	
		return false;

	}	
		
	
	 /**
     * Iterates through the HashTable.
     * 
     */
    
	@Override
	public Iterator<String> elements() {
				return new ArrayIterator<String>();
	
	}
	
	
	 /**
     * Calculates the average number of probes by dividing the total amount of probes and operation from the
     * insertion,remove and find methods.
     * 
     */
    
	
	public float averNumProbes()	{
		return (float) ttlPrbs/ttlOps;
		
	}
	
	 /**
     * Multiply, Add and Divide (MAD):
     * 
     * Takes in the hashCode multiplies it with a constant prime number + adds it with another prime value that's != 
     * zero. It must return a positive value so Math.abs was used ( otherwise I'd get ArrayIndex out of bounds errors)
     *  
     * @param int
     *            hashCode. Generates a compressed version of the given hashCode to be placed in the hash table.
     * 
     */
    
	
	private int MADcompression(int hashCode) {
		
	 	
		int test = Math.abs(((7 * hashCode) + 17) %N.length); 		// h2 (y) = (ay + b) mod N

		return test;
  }			
		
	
	
	 /**
     *
     * FloatLoad is n/M ,where n is the number of elements and M is the size of the array 
	 * if it is greater than the input load factor return true & rehash the array within the method
	 * it was called from.
     * 
     */
		
	private boolean floatOverload () {
		numOfElements=numOfElements+1;
		if(((float)numOfElements/(float)N.length) >= inputLoad) {
						
			return true;
			
		}
		
		return false;
		
	}

	
	 /**
     * Double Compression:
     * 
     * Takes in the stepCode ( i.e step distance from the original hash code) multiplies it with a constant prime number + adds it with another prime value that's != 
     * zero. 
     *  
     * @param int
     *            stepCode. Generates a compressed version of the given double hashCode to search for an empty cell
     *             in the hash table.
     * 
     */
	
	private int doubleCompression (int stepCode){
		int test = ( 	5 - ( stepCode % 	5));
		return test ;		//must be less than N 
		
	}
	
	 /**
     * Double Hash:
     * 
     * Takes in the stepCode ( i.e step distance from the original hash code) , hashCode and a constant p.
     *  
     * @param int
     *            stepCode. Generates a compressed version of the given double hashCode to search for an empty cell
     *             in the hash table.
     * 
     */
	
	private int doubleHash(int hashCode , int StepCode , int p)		// double hash= f(k) + p * g(k) % table size
		
		{	return (hashCode + ( p * StepCode)) % N.length; 		}

	
	
	 /**
     * Due to the hash working better if its size of a prime number we need to get the next largest prime number for the rehash
     * this will determine if the value passed is a prime number or not,instead of testing it all in 1 method that'll make it messy
     *
     * This method was based on the tutorial from Derek Banas. http://www.newthinktank.com/2013/03/java-hash-table-2/
     * @param int
     *           number.The number is the initial size of the array before it is rehashed.
     *           
     * 
     */
	
	
	private boolean isPrime(int number) {
			 	 
   // Eliminate the need to check versus even numbers
		if (number % 2 == 0)
            return false;
    // Check odd numbers returning if not prime.
    for (int i = 3; i * i <= number; i += 2)
            if (number % i == 0)
                    return false;

    return true;
       
	 }
	
	 /**
     * Rehash method:
     * 
     * This will resize the array,by declaring a new variable that will hold double the size of the intial size (N)
     * and then adding 1 to it to make it odd- if it is not a prime odd number then keep incrementing until it is.This
     * is done in order to minimize the number of collisions as much as possible and to spread the keys out.
     * 
     * The old values are then re-added by re-generating hashCodes for them using the insert method.
     *
     */
	
	
	 private void rehash() {
		 
		numOfElements=0;
		int placeholder= N.length;
		int newHashSize = (N.length * 2) +1; 	//new size will be double the amount
		while (!isPrime(newHashSize) )	{ newHashSize++;	}				//if new value is not prime increment until it is

		String tmpHash[] = new String [placeholder];	
		
		
		//tmpHash will hold original values of N
		
		for (int i = 0;i<placeholder;i++) {	
	 	    if((N[i]!=null) && (!N[i].equals(SENTINEL)) )
	 	    	tmpHash[i]=N[i];
       	  	
		 }
		
		
	 	N = new String [newHashSize];	//N is now bigger 
	 	
	 		for (int i = 0;i<placeholder;i++) {	
	 	    if(tmpHash[i]!=null)
        	insert(tmpHash[i]);
       	  	
		 }
	 		
	 		
	}

	 	/**
	     * Private Array Iterator class:
	     *
	     * This will iterate through the hashTable and tell the program whether or not there is a value after an element and return that value.
	     * Implemented methods:
	     * hasNext -> this will go through the array and return true if there is a value after an element.
	     * next - > will return the elements if hasNext returns true.
	     * 
	     * The remove method was not implemented as it is known to create bugs. (also it is useless as there already is a remove method implemented)
	     *
	     */
	  
	 private class ArrayIterator<E> implements Iterator<String> 
	 {	
	 	 int currentPos=0;
	
        @Override
        public boolean hasNext() {
        	
        	for (int nextPos=currentPos+1; nextPos < N.length;nextPos++) { 
        		        		if (N[nextPos] != null && !(N[nextPos].equals(SENTINEL))) {
        		 return true;
        		 
        		}

        	}
        	
			return false;
        }

      
        @Override
        public String next() {
                int nextPos = currentPos + 1;
                
                while (nextPos <= N.length - 1) {
                        if (N[nextPos] != null && !(N[nextPos].equals(SENTINEL))) {
                                currentPos = nextPos;
                                return N[currentPos];
                        }
                        nextPos++;
                }
                return null;
        }
	 	
	 	@Override
	 	public void remove() 	{  	}
	 	
	 	
	 }
	 	
	 	


}
	
	


