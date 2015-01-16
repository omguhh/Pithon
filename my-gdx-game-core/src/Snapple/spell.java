/****************************************************************************************
 * Author: Yasmine Fadel
 * Written: 2013-10-25
 *
 * The program reads all words from the dictionary files (d1-d6) and inserts them into
 * a dictionary data structure (hashTables/LinkedList). It will then read words from the second file and 
 * check if they are in the given dictionary.If the word is found in the dictionary then nothing will be changed. * 
 * Otherwise,if the word is not found in the dictionary,a series of suggestions will be generated and printed out.
 * I've only implemented one method for spellchecking : -
 * 
 * -Substitution:
 * It substitutes letters in the misspelled word with characters from A-Z
 * 
 *
 ****************************************************************************************/

package Snapple;

import java.io.*;
import java.util.Iterator;

public class spell {

        private static HashDictionary orginialD;
        private static HashDictionary alternativeD;

      public static void main(String[] args) throws java.io.IOException {
    	  //to calculate the running time of the program
    	  
        	final long startTime = System.currentTimeMillis();


                BufferedInputStream dict, file;

                try {
                        // Initialize input streams and Dictionaries
                        dict = new BufferedInputStream(new FileInputStream("dictionary.txt"));
                        file = new BufferedInputStream(new FileInputStream("checkTest.txt"));
                        StringHashCode sHCode = new StringHashCode();
                     
                        orginialD = new HashDictionary(sHCode, (float) 0.55);
                        alternativeD = new HashDictionary(sHCode, (float) 0.55);
                        int i = 0; //to count the words in the dictionary 
                        
                        // Load all the words from dictionary into dictionary data structure
                        FileWordRead reader = new FileWordRead(dict);
                        while (reader.hasNextWord()) {
                                orginialD.insert(reader.nextWord());
                               i++;
                               
                        }
                        
                        System.out.println("==================================================================");
                        System.out.println("Number of words in dictionary: " +i);
                        System.out.println("==================================================================");
                        // Read word for word from text file
                        
                        reader = new FileWordRead(file);
                        while (reader.hasNextWord()) {
                                String word = reader.nextWord();
                                // Check if the words not in the dictionary and try to change it
                                // to match dictionary words
                                if (!orginialD.find(word)) {
                                	substitution(word);

                                }
                        }

                } catch (IOException e) { // Catch exceptions caused by file
                                                                        // input/output errors
                        System.out.println("Check your file name");
                        e.printStackTrace();
                        System.exit(0);
                }
                // Iterates through all the elements in the words dictionary and prints
                // them out
               
                Iterator<String> it = alternativeD.elements();
                System.out.println("Alternative spelling of words:");
                while (it.hasNext()) {
                        System.out.println(it.next()+ ",");
                        
                }
                
                //Prints out the running time of the program
                
              System.out.println(" ");
              System.out.println("==================================================================");
              final long endTime = System.currentTimeMillis();
              System.out.println("RUNNING TIME: " + (endTime - startTime) + " MILLISECONDS");
              System.out.println("==================================================================");
        }

      /* 
       * This is the letter substitution method
       *   @param String
       *       word. The misspelled word to be substituted.
       */

        private static void substitution(String word) {
        	String newWord;
    		char [] chararr;
    		for (int i = 0;i<word.length();i++){

    			
    			//increment through every letter in the alphabet
    		for(char alphabet = 'a';alphabet <= 'z';alphabet++)	{
    			 
    			chararr=word.toCharArray();
    			chararr[i] = alphabet;
    			newWord=String.valueOf(chararr);
    			//if the word is still not found insert it into the alternative dictionary
    				if(!orginialD.find(newWord)){
    					
    				alternativeD.insert(newWord);

    				}

    			}
    
        }

        }
}