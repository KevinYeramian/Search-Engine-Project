/*
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 *
 *   Johan Boye, 2017
 */

package Engine;
import java.util.ArrayList;
import java.util.*;
import Engine.Query.QueryTerm;
import scala.annotation.meta.param;


public class Searcher {
	
	final double SEARCH_ANSWERS_WEIGHT = 0.1;
	
	final int SEARCH_MAX_SIZE = 10;
	
    /** The index to be searched by this Searcher. */
	Word2VecInterface word2vect;

    /** Constructor */
    public Searcher(Word2VecInterface word2vect){
    		this.word2vect = word2vect;
    }

    /**
     *  Searches the index for postings matching the query.
     *  @return A postings list representing the result of the query.
     */
    public ArrayList<Pair> search( Query query, QueryType queryType, RankingType rankingType, ElasticSearchInterface esi ) {
    	   if(esi != null) {
	    	   Pair pair;
	    	   ArrayList<Pair> synonymList = new ArrayList<Pair>();
	    	   for(QueryTerm queryterm : query.queryterm) {
	    		   synonymList.addAll(word2vect.synonym(queryterm.term));
	    	   }
	    	   query.add(synonymList);
	    	   
	       return score(esi.search(query, SEARCH_ANSWERS_WEIGHT, SEARCH_MAX_SIZE),query);
    	   }
    	   return null;
    }
    public ArrayList<String> kGram(String s){
    	ArrayList<String> kGramTerm = new ArrayList<String>();
    	String aux;
    	for(int i = 0; i < s.length() -  3;i++) {
    		aux = s.substring(i,i + 3);
    		if(!kGramTerm.contains(aux)) {
    			kGramTerm.add(aux);
    		}
    	}
    	return kGramTerm;
    }
    
    public ArrayList<Pair> score(ArrayList<QuestionAnswer> qaList, Query query) {
    	if(qaList == null) {
    		return new ArrayList<Pair>();
    	}
    	HashMap<String,Integer> uniqueId = new HashMap<String,Integer>();
    	HashMap<String,Integer> string2N = new HashMap<String,Integer>();
    	HashMap<String,Integer> string2NQuery = new HashMap<String,Integer>();
    	HashMap<String,Integer> string2NQuestion = new HashMap<String,Integer>();
    	
    	int currentID = 0;
    	ArrayList<Pair> queryKgram = new ArrayList<Pair>();
    	ArrayList<String> queryString = new ArrayList<String>();
    	ArrayList<Pair> output = new ArrayList<Pair>();
    	ArrayList<Pair> answerKgram = new ArrayList<Pair>();
    	ArrayList<String> answerString = new ArrayList<String>();
    	
    	for(QueryTerm queryterm : query.queryterm) {
    		ArrayList<String> kGramTerm = kGram(queryterm.term);
    		string2N.clear();
    		for(String kgram : kGramTerm) {
    			if(!queryString.contains(kgram)) {
    				queryKgram.add(new Pair(kgram,queryterm.weight));
    				queryString.add(kgram);
    				Integer value = string2N.get(kgram);
    				if(value == null) {
    					string2N.put(kgram, 1);
    				}
    				
    			}
    			else {
    				int index = queryString.indexOf(kgram);
    				queryKgram.get(index).value = Math.max(queryKgram.get(index).value, queryterm.weight);
    				Integer value = string2N.get(kgram);
    				if(value == null) {
    					string2N.put(kgram, 1);
    				}
    			}
    		}
    		
    		for(String key : string2N.keySet()) {
    			Integer value = string2NQuery.get(key);
    			if(value == null) {
    				string2NQuery.put(key, 1);
    			}
    			else {
    				string2NQuery.put(key, 1 + value);
    			}
    		}
 	   	}
    	
    	for(QuestionAnswer qa : qaList) {
    		
    		
    		for(SearchEntry se: qa.answer) {
    			answerKgram.clear();
        		answerString.clear();
        		string2NQuestion.clear();
    			for(String term : se.text.split(" ")) {
    				
    				ArrayList<String> kGramTerm = kGram(term);
    				string2N.clear();
        			for(String kgram : kGramTerm) {
            			if(!answerString.contains(kgram)) {
            				answerKgram.add(new Pair(kgram,se.rank));
            				answerString.add(kgram);
            				Integer value = string2N.get(kgram);
            				if(value == null) {
            					string2N.put(kgram, 1);
            				}
            			}
            			else {
            				int index = answerString.indexOf(kgram);
            				answerKgram.get(index).value = Math.max(answerKgram.get(index).value, se.rank);
            				Integer value = string2N.get(kgram);
            				if(value == null) {
            					string2N.put(kgram, 1);
            				}
            			}
            		}
        			for(String key : string2N.keySet()) {
            			Integer value = string2NQuery.get(key);
            			if(value == null) {
            				string2NQuestion.put(key, 1);
            			}
            			else {
            				string2NQuestion.put(key, 1 + value);
            			}
            		}
        			
    			}
    			// End of term in text
    			
    			
    			double questionScore = 0.0;
        		for(Pair pair : answerKgram) {
        			int index = queryString.indexOf(pair.key);
            		
            		if(index != -1) {
            			System.out.println(string2NQuery.get(pair.key) + " " + queryKgram.get(index).value + " " + string2NQuestion.get(pair.key) + " " + pair.value);
            			questionScore += string2NQuery.get(pair.key) *  queryKgram.get(index).value;
            			questionScore += string2NQuestion.get(pair.key) * pair.value;
            		}
            		
            	}
        		double divised = 1.0;
        		/*for(Integer n : string2NQuery.values()) {
        			divised  += n;
        		}
        		for(Integer n : string2NQuestion.values()) {
        			divised  += n;
        		}*/
        		output.add(new Pair(se.text, questionScore / divised));
    		}
    		
    		
    	}
    	
    	
    	
    	output.sort(Comparator.comparing(p -> -p.value));
		return output;
    	
    }
}
