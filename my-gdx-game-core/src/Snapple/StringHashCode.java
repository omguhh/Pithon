/******************************************************************************
 * Author: Yasmine Fadel
 * 
 * Written: 2013-10-25
  ******************************************************************************/
package Snapple;


public class StringHashCode implements HashCode {
	
	 static int CONSTANT = 39; 
	

	public int giveCode(Object key) {
		// cast the object to a string
		return generateCode((String) key);
	}
	
	private int generateCode (String s){
		//set p as the second last character 
		int p = s.charAt(s.length()-1);
		int i =s.length()-2;
		
		while (i > 0)	{
			//calculate the values  of the remaining characters
			p = p * CONSTANT + s.charAt(i); //horner's rule where p=p*constantvalue + x[i]
			i--;
			
		}
		//if the value is negative so we use the absolute method to make sure a positive value is always returned
		if (p < 0){
			p =Math.abs(p);
		}
		//return the code
		return p;

			}


	}

