/*
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 *
 *   Johan Boye, 2017
 */

package Engine;

import java.util.ArrayList;
import java.io.File;

/**
 *  This is the main class for the search engine.
 */
public class Engine {

   

    /** The searcher used to search the index. */
    Searcher searcher;

    /** The engine GUI. */
    SearchGUI gui;

    /**  Directories that should be indexed. */
    ArrayList<String> dirNames = new ArrayList<String>();

    /**  Lock to prevent simultaneous access to the index. */
    Object indexLock = new Object();

    /** The patterns matching non-standard words (e-mail addresses, etc.) */
    String patterns_file = null;

    /** The file containing the logo. */
    String pic_file = "";

    /** The file containing the pageranks. */
    String rank_file = "";
    ElasticSearchInterface esi;


    /* ----------------------------------------------- */


    /**
     *   Constructor.
     *   Indexes all chosen directories and files
     */
    public Engine( String[] args ) {
    		esi = new ElasticSearcher();
	    	Word2VecInterface word2vect = new w2vImporter();
	    	searcher = new Searcher(word2vect);
	    	gui = new SearchGUI( this, esi );
	    	gui.init();
		
    }


    /* ----------------------------------------------- */

    /**
     *   Decodes the command line arguments.
     */
    


    /* ----------------------------------------------- */


    public static void main( String[] args ) {
	Engine e = new Engine( args );
    }

}
