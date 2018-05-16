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
    public ArrayList<QuestionAnswer> search( Query query, QueryType queryType, RankingType rankingType, ElasticSearchInterface esi ) {
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
    
    public ArrayList<QuestionAnswer> score(ArrayList<QuestionAnswer> qaList, Query query) {
    	
    	
    	ArrayList<Pair> queryKgram = new ArrayList<Pair>();
    	ArrayList<String> queryString = new ArrayList<String>();
    	ArrayList<QuestionAnswer> output = new ArrayList<QuestionAnswer>();
    	ArrayList<Pair> answerKgram = new ArrayList<Pair>();
    	ArrayList<String> answerString = new ArrayList<String>();
    	
    	for(QueryTerm queryterm : query.queryterm) {
    		ArrayList<String> kGramTerm = kGram(queryterm.term);
    		for(String kgram : kGramTerm) {
    			if(!queryString.contains(kgram)) {
    				queryKgram.add(new Pair(kgram,queryterm.weight));
    				queryString.add(kgram);
    			}
    		}
 	   	}
    	for(QuestionAnswer qa : qaList) {
    		answerKgram.clear();
    		answerString.clear();
    		for(SearchEntry se: qa.answer) {
    			ArrayList<String> kGramTerm = kGram(se.text);
    			for(String kgram : kGramTerm) {
        			if(!queryString.contains(kgram)) {
        				answerKgram.add(new Pair(kgram,se.rank));
        				answerString.add(kgram);
        			}
        		}
    		}
    	}
    	
    	
		return output;
    	
    }
}
