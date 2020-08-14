package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/

		if(docFile==null) {
			throw new FileNotFoundException("File not found");
		}
		
		HashMap<String, Occurrence> keysMap = new HashMap<String, Occurrence>(100, 2.0f);
		Scanner sc = new Scanner(new File(docFile));
		
		while(sc.hasNext()) 
		{
			String currWord = sc.next();
			currWord = this.getKeyword(currWord);
			if(currWord == null)
			{
				continue;
			}
			if(!noiseWords.contains(currWord) && !keysMap.containsKey(currWord))
			{
				Occurrence firstOccur = new Occurrence(docFile, 1);
				keysMap.put(currWord, firstOccur);
			}
			else if(keysMap.containsKey(currWord))
			{
				Occurrence increase = keysMap.get(currWord);
				increase.frequency++;
				keysMap.put(currWord, increase);
			}
		}
		return keysMap;
		
		
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
	
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	
	
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		
		Set<String> stringSet = kws.keySet();
		Iterator<String> thisIs = stringSet.iterator();
		
		
		while(thisIs.hasNext())
		{
			String currentKey = thisIs.next();
			Occurrence occur = kws.get(currentKey);
			ArrayList<Occurrence> occurList = keywordsIndex.get(currentKey);
			if(occurList == null)
			{
				occurList = new ArrayList<Occurrence>();
				keywordsIndex.put(currentKey, occurList);
			}
			
			occurList.add(occur);
			this.insertLastOccurrence(occurList);
		}
	}
	
	
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		
		word = word.trim();
		word = word.toLowerCase();
		
		
		while(word.startsWith(".") || word.startsWith(",") || word.startsWith("?") || word.startsWith("!") || word.startsWith(":") || word.startsWith(";")) 
		{
			word = word.substring(1);
		}
		
		
		while(word.endsWith(".") || word.endsWith(",") || word.endsWith("?") || word.endsWith("!") || word.endsWith(":") || word.endsWith(";")) 
		{
			word = word.substring(0, word.length() - 1);
		}
		
		
		for(int c = 0; c < word.length(); c++) 
		{
			if(!Character.isAlphabetic(word.charAt(c))) 
			{
				return null;
			}
		}
		return word;
		

	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		ArrayList<Integer> midpointList = new ArrayList<>();
        int frequencyIn = occs.get(occs.size()-1).frequency;

        int low = 0;
        int high = occs.size() - 2;
        int mid;
        int indexIn;
        
        while (true) {                                           
            mid = (high + low) / 2;
            midpointList.add(mid);
            //
            Occurrence occMidpoint = occs.get(mid);
            //
            if (occMidpoint.frequency == frequencyIn) {     
                indexIn = mid;
                break;
            }
            else if (occMidpoint.frequency < frequencyIn) {  
                high = mid - 1;                         
                if (low > high) {
                    indexIn = mid;                     
                    break;
                }
            }
            else {                                                
                low = mid + 1;                        
                if (low > high) {
                    indexIn = mid + 1;                
                    break;
                }
            }
        }
       
        if (indexIn != occs.size() - 1) {
            Occurrence temp = occs.get(occs.size()-1);              
            occs.remove(occs.size()-1);                             
            occs.add(indexIn, temp);              
        }
        //
        return midpointList;
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	
	
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		kw1 = kw1.toLowerCase();
        kw2 = kw2.toLowerCase();
        
        HashMap<String, String> returnDocument = new HashMap<>();          
        ArrayList<String> output = new ArrayList<>(); 
        ArrayList<Occurrence> occur1 = keywordsIndex.get(kw1);
        ArrayList<Occurrence> occur2 = keywordsIndex.get(kw2);
        int indexNum1 = (occur1 == null) ? -1 : 0;         
        int indexNum2 = (occur2 == null) ? -1 : 0;
        while ( (output.size() < 5) && (indexNum1 >= 0 || indexNum2 >= 0) ) { 
            Occurrence first  = (indexNum1 >= 0) ? occur1.get(indexNum1) : null;
            Occurrence second = (indexNum2 >= 0) ? occur2 != null ? occur2.get(indexNum2) : null : null;
            //
            Occurrence oneOcc;                                      
            if (first == null) {                                     
                oneOcc = second;                                      
                indexNum2++;                                            
            }
            else if (second == null) {                               
                oneOcc = first;
                indexNum1++;
            }
            else {                                                  
                if (first.frequency >= second.frequency) {         
                    oneOcc = first;
                    indexNum1++;
                }
                else {
                    oneOcc = second;
                    indexNum2++;
                }
            }
            if (oneOcc != null) {
                if (!returnDocument.containsKey(oneOcc.document)) {
                    output.add(oneOcc.document);          
                    returnDocument.put(oneOcc.document, oneOcc.document);
                } else {
                  
                }
            }
            if ((indexNum1 >= 0) && (indexNum1 == occur1.size())) { 
                indexNum1 = -1;
            }
            if (occur2 != null && (indexNum2 >= 0) && (indexNum2 == occur2.size())) {  
                indexNum2 = -1;
            }
        }
        return (output.size() == 0) ? null : output;  
}
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
	
	
}
