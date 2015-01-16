package Snapple;
//Interface your dictionary has to implement

import java.util.Iterator;

public interface Dictionary {

    // throws exception if key is alreadyPresent
    public void insert(String key) throws DictionaryException;
    
    // Removes the entry with the specified key from the dictionary. Throws 
    // DictionaryException if no entry with key in the dictionary          
    public void remove(String key)throws DictionaryException;

    /// Returns true if there is entry with specified key in the dictionary 
    public boolean find(String key);
    
   
    // Returns iterator object over all elements stored in the dictionary
    public Iterator elements();

}
