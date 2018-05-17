package Engine;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.models.embeddings.loader.VectorsConfiguration;

import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import java.io.File;
import java.util.*;
/**
 *
 * @author Pontus
 */
public class w2vImporter implements Word2VecInterface {
    File vectors;
    File hs;
    File h_codes;
    File h_points;
    Word2Vec word2Vec;
    String JsonString = "{\"allowParallelTokenization\":true,\"batchSize\":512,\"elementsLearningAlgorithm\":null,\"epochs\":1,\"hugeModelExpected\":false,\"iterations\":1,\"layersSize\":100,\"learningRate\":0.025,\"learningRateDecayWords\":0,\"minLearningRate\":1.0E-4,\"minWordFrequency\":5,\"modelUtils\":\"org.deeplearning4j.models.embeddings.reader.impl.BasicModelUtils\",\"negative\":0.0,\"ngram\":0,\"preciseWeightInit\":false,\"sampling\":0.0,\"scavengerActivationThreshold\":2000000,\"scavengerRetentionDelay\":3,\"seed\":42,\"sequenceLearningAlgorithm\":null,\"stop\":\"STOP\",\"stopList\":[],\"tokenPreProcessor\":null,\"tokenizerFactory\":\"org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory\",\"trainElementsVectors\":true,\"trainSequenceVectors\":true,\"unk\":\"UNK\",\"useAdaGrad\":false,\"useHierarchicSoftmax\":true,\"useUnknown\":false,\"variableWindows\":null,\"vocabSize\":0,\"window\":5}";
    public w2vImporter(){
        // config this manually 
    	
	    String path = "/Users/yeramiankevin/Desktop/KTH/Search_Engines_and_Information_Retrieval_Systems/final-project/IR-project/w2v";
	    this.vectors = new File( path + "/syn0.txt");//text file with words and their wieghts, aka Syn0
	    this.hs = new File( path + "/syn1.txt");//text file HS layers, aka Syn1
	    this.h_codes = new File(path + "/codes.txt");// text file with Huffman tree codes
	    this.h_points = new File(path + "/huffman.txt");// text file with Huffman tree points
	    this.initw2v();
    }
    public void initw2v(){
        
   
    try{
        this.word2Vec =  WordVectorSerializer.readWord2VecFromText(vectors,hs,h_codes,h_points, new VectorsConfiguration().fromJson(JsonString));
                                           
        
	    }
	    catch(Exception e){
	    e.printStackTrace();
	    }
    
    }
    public String translate2W2v(String token){
        String toReturn = token.replaceAll( "å","aaa");
        toReturn = toReturn.replaceAll("ä","eae");
        toReturn = toReturn.replaceAll("ö","eoe");
        //System.out.println("2 w2v, from "+ token + " to " +toReturn );
        return toReturn;
    }
    
    public String translate2Query(String token){
        String toReturn = token.replaceAll("aaa", "å");
        toReturn = toReturn.replaceAll("eae", "ä");
        toReturn = toReturn.replaceAll("eoe", "ö");
        //System.out.println("2 query, from "+ token + " to " +toReturn );
        return toReturn;
    }
    public ArrayList<Pair> synonym(String token){
        double threshold = 0.6;//change if you want
        int numberOfWords = 5;//change if you want 
        //needs to deal with åäö
        String tokenBuffer = this.translate2W2v(token);
        
        
        Collection<String> words =  this.word2Vec.wordsNearest(tokenBuffer, numberOfWords);
        ArrayList<Pair> results = new ArrayList<Pair>();
        
        //debugging.
        boolean debugging = true;
        
        for(String word : words){
            double sim = this.word2Vec.similarity(word, tokenBuffer);
            if(sim >= threshold){
            	System.out.println("Synonym of " + token + " = " + word);
                //needs to deal with åäö
                String translated = translate2Query(word);
                results.add(new Pair(translated, sim / 2.0));
            }
            if(results.size() == numberOfWords) {
            	break;
            }
        }
        
        return results;
    }
    /*
    public static void main(String[] args) {
        //call these 2 functions to init. in this order
        w2vImporter n = new w2vImporter();
        //n.initw2v();
        
        //use this to get words closest to your token, returns a Collection<String>
        System.out.println(n.synonym("du"));
        System.out.println(n.synonym("dö"));
        System.out.println(n.synonym("räakna"));
        System.out.println(n.synonym("duå"));
  
    } */  
}
