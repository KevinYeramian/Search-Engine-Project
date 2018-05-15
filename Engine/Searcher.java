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
    public QuestionAnswer search( Query query, QueryType queryType, RankingType rankingType, ElasticSearchInterface esi ) {
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
    
    public QuestionAnswer score(ArrayList<QuestionAnswer> qa, Query query) {
    		
		return qa.get(0);
    	
    }
}
