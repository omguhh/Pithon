package Snapple;

import java.io.*;
import java.util.Iterator;

import com.badlogic.gdx.utils.Array;

public class FindWord {
	Array<String> wordsFound;
	String wordFound;

	private static HashDictionary orginialD;
	private static HashDictionary alternativeD;

	public FindWord(String word) throws java.io.IOException {
		// to calculate the running time of the program

		String sub;
		Array<String> substrings = new Array<String> ();
		wordsFound =  new Array<String> ();
		int j, c, length, len;

		BufferedInputStream dict;

		try {
			// Initialize input streams and Dictionaries
			dict = new BufferedInputStream(new FileInputStream("D:/libGDX/core/src/dictionary.txt"));
			StringHashCode sHCode = new StringHashCode();

			orginialD = new HashDictionary(sHCode, (float) 0.55);
			int i = 0; // to count the words in the dictionary

			// Load all the words from dictionary into dictionary data structure
			FileWordRead reader = new FileWordRead(dict);
			while (reader.hasNextWord()) {
				orginialD.insert(reader.nextWord());
				i++;
			}
			// Read word for word from text file

			length = word.length();

			//System.out.println("Substrings of \"" + word + "\" are :-");

			for (c = 0; c < length; c++) {
				for (j = 1; j <= length - c; j++) {
					sub = word.substring(c, c + j);
					substrings.add(sub);
					//System.out.println(substrings);
				}
			}
			//check if any substrings are available in dictionary
			//if yes, print word on screen
			//len=substrings.size;
			//System.out.println("Words found in substring: ");
			for(String sub1 : substrings)
			{
				if(orginialD.find(sub1))
					wordFound=sub1; //first word found
					//System.out.println(sub1);
			}
			/*reader = new FileWordRead(file);
			while (reader.hasNextWord()) {
				String word = reader.nextWord();
				// Check if the words not in the dictionary and try to change it
				// to match dictionary words
				if (!orginialD.find(word)) {
					substitution(word);

				}
			}*/

		} catch (IOException e) { // Catch exceptions caused by file
									// input/output errors
			System.out.println("Check your file name");
			e.printStackTrace();
			System.exit(0);
		}
		// Iterates through all the elements in the words dictionary and prints
		// them out

		/*Iterator<String> it = alternativeD.elements();
		System.out.println("Alternative spelling of words:");
		while (it.hasNext()) {
			System.out.println(it.next() + ",");

		}*/
	}

	public String getWordFound()
	{
		return wordFound;
		
	}
}