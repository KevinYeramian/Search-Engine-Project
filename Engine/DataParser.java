package Engine;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Dmitry Siniukov
 *
 * Launch the project with this as the main class to preprocess the dataset.
 * Arguments:
 * 	1. A path to the data folder
 * 	2. An integer number of the dataset's size: 1000, 10,000, or 100,000. (optional)
 * 
 * Result: a json-file of the correct format stored in the root folder of the project.
 */
public class DataParser {
	
	public static void main( String[] args ) {
		
		if (args.length == 0) {
			System.err.println("Pass the data set path as an argument.");
			return;
		}
		
		int sizeOption = 100000;
		if (args.length > 1) {
			sizeOption = Integer.parseInt(args[1]);
		}
		
		String path = args[0];
		
		File fResult = new File("data_" + sizeOption + ".json");
		if (fResult.exists()) {
			System.err.println("The file already exists.");
			return;
		}
		
		File fQuestions = new File(path, "questions_" + sizeOption + ".txt");
		File fAnswers = new File(path, "answers_" + sizeOption + ".txt");
		
		InputStreamReader questionsReader;
		InputStreamReader answersReader;
		OutputStreamWriter resultWriter;
		try {
			questionsReader = new InputStreamReader(new FileInputStream(fQuestions), StandardCharsets.UTF_8);
			answersReader = new InputStreamReader(new FileInputStream(fAnswers), StandardCharsets.UTF_8);
			resultWriter = new OutputStreamWriter(new FileOutputStream(fResult), StandardCharsets.UTF_8);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		BufferedReader questionsBufferedReader = new BufferedReader(questionsReader);
		BufferedReader answersBufferedReader = new BufferedReader(answersReader);
		BufferedWriter resultBufferedWriter = new BufferedWriter(resultWriter);
		
		try {
			String questionLine;
			String answerLine = answersBufferedReader.readLine();
			while ((questionLine = questionsBufferedReader.readLine()) != null) {
				
				JSONObject indexRow = new JSONObject();
				indexRow.put("index", new JSONObject()
							.put("_index", "chatbot_index")
							.put("_type", "question_answer"));
				resultBufferedWriter.write(indexRow.toString() + "\n");
				
				String[] questionLineParts = questionLine.split(" ");
				int questionNumber = Integer.parseInt(questionLineParts[0]);
				
				JSONObject row = new JSONObject();
				row.put("id", questionNumber);
				row.put("question", questionLine.substring(questionLineParts[0].length() + 1));
				
				JSONArray answersArray = new JSONArray();
				String[] answerLineParts;
				while (Integer.parseInt((answerLineParts = answerLine.split(" "))[0]) == questionNumber ) {
					answersArray.put(answerLine.substring(answerLineParts[0].length() + 1));
					answerLine = answersBufferedReader.readLine();
					if (answerLine == null) {
						break;
					}
				}
				
				row.put("answers", answersArray);
				
				resultBufferedWriter.write(row.toString() + "\n");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			questionsBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			answersBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			resultBufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		// Creating an index with a tokenizer
		try {
		    HttpPut request = new HttpPut("http://localhost:9200/chatbot_index");
		    request.addHeader("content-type", "application/json");
		    JSONObject body = new JSONObject()
		    	.put("settings", new JSONObject()
		    		.put("analysis", new JSONObject()
		    			.put("analyzer", new JSONObject()
		    				.put("my_analyzer", new JSONObject()
		    					.put("tokenizer", "url_email_tokenizer")))
		    			.put("tokenizer", new JSONObject()
		    				.put("url_email_tokenizer", new JSONObject()
		    					.put("type", "uax_url_email")))
		    		));
		    StringEntity params = new StringEntity(body.toString());
		    request.setEntity(params);
		    httpClient.execute(request);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		// Indexing
		try {

		    HttpPost request = new HttpPost("http://localhost:9200/_bulk");
		    request.addHeader("content-type", "application/json");
		    InputStream is = new FileInputStream(fResult);
		    BufferedHttpEntity params = new BufferedHttpEntity(new InputStreamEntity(new BufferedInputStream(is)));
		    request.setEntity(params);
		    
		    HttpResponse response = httpClient.execute(request);
		    HttpEntity entity = response.getEntity();
            if (entity != null) {
            	System.out.println(EntityUtils.toString(entity));
            }

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}

}
