/*
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 *
 *   Johan Boye, 2017
 */

package Engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.List;
import java.nio.charset.*;
import java.io.*;




/**
 *  A class for representing a query as a list of words, each of which has
 *  an associated weight.
 */
public class Query {
	public List<String> stopWords = Arrays.asList("och", "det", "att", "i", "en",
	        "jag", "hon", "som", "han", "på", "den", "med", "var", "sig",
	        "för", "så", "till", "är", "men", "ett", "om", "hade", "de", "av",
	        "icke", "mig", "du", "henne", "då", "sin", "nu", "har", "inte",
	        "hans", "honom", "skulle", "hennes", "där", "min", "man", "ej",
	        "vid", "kunde", "något", "från", "ut", "när", "efter", "upp", "vi",
	        "dem", "vara", "vad", "över", "än", "dig", "kan", "sina", "här",
	        "ha", "mot", "alla", "under", "någon", "eller", "allt", "mycket",
	        "sedan", "ju", "denna", "själv", "detta", "åt", "utan", "varit",
	        "hur", "ingen", "mitt", "ni", "bli", "blev", "oss", "din", "dessa",
	        "några", "deras", "blir", "mina", "samma", "vilken", "er", "sådan",
	        "vår", "blivit", "dess", "inom", "mellan", "sådant", "varför",
	        "varje", "vilka", "ditt", "vem", "vilket", "sitta", "sådana",
	        "vart", "dina", "vars", "vårt", "våra", "ert", "era", "vilkas");

    /**
     *  Help class to represent one query term, with its associated weight.
     */
    class QueryTerm {
	String term;
	double weight;
	QueryTerm( String t, double w ) {
	    term = t;
	    weight = w;
	}
    }

    /**
     *  Representation of the query as a list of terms with associated weights.
     *  In assignments 1 and 2, the weight of each term will always be 1.
     */
    public ArrayList<QueryTerm> queryterm = new ArrayList<QueryTerm>();

    /**
     *  Relevance feedback constant alpha (= weight of original query terms).
     *  Should be between 0 and 1.
     *  (only used in assignment 3).
     */
    double alpha = 0.2;

    /**
     *  Relevance feedback constant beta (= weight of query terms obtained by
     *  feedback from the user).
     *  (only used in assignment 3).
     */
    double beta = 1 - alpha;


    /**
     *  Creates a new empty Query
     */
    public Query() {
    }


    /**
     *  Creates a new Query from a string of words
     */
    public Query( String queryString  ) {
    		StringTokenizer tok = new StringTokenizer( queryString );
	
		while ( tok.hasMoreTokens()) {
			String token = tok.nextToken();
			
			if(!stopWords.contains(token)) {
				queryterm.add( new QueryTerm(token, 1.0) );
			}
		}
    }


    /**
     *  Returns the number of terms
     */
    public int size() {
	return queryterm.size();
    }
    

    /**
     *  Returns the Manhattan query length
     */
    public double length() {
	double len = 0;
	for ( QueryTerm t : queryterm ) {
	    len += t.weight;
	}
	return len;
    }


    /**
     *  Returns a copy of the Query
     */
    public Query copy() {
	Query queryCopy = new Query();
	for ( QueryTerm t : queryterm ) {
	    queryCopy.queryterm.add( new QueryTerm(t.term, t.weight) );
	}
	return queryCopy;
    }
    public void add(ArrayList<Pair> synonym) {
	    	for(Pair q : synonym) {
	    		if(!stopWords.contains(q.key)) {
	    			queryterm.add( new QueryTerm(q.key, q.value) );
	    		}
	    	}
    }
   
    /**
     *  Expands the Query using Relevance Feedback
     *
     *  @param results The results of the previous query.
     *  @param docIsRelevant A boolean array representing which query results the user deemed relevant.
     *  @param engine The search engine object
     */
    /*public void relevanceFeedback( PostingsList results, boolean[] docIsRelevant, Engine engine ) {
	//
	//  YOUR CODE HERE
	//
    }*/
}
